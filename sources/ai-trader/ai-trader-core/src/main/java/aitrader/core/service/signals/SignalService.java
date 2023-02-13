package aitrader.core.service.signals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import aitrader.core.config.CoreLog;
import aitrader.core.model.Signal;
import aitrader.core.model.SignalShow;
import aitrader.core.model.SymbolInfo;
import aitrader.core.service.symbol.SymbolInfoService;
import aitrader.util.observable.Handler;
import aitrader.util.price.PriceUtil;

public class SignalService
{
	private static List<SignalShow> lstShortSignalShow = new ArrayList<SignalShow>();
	private static List<SignalShow> lstLongSignalShow = new ArrayList<SignalShow>();

	public static List<SignalShow> getLstShortSignalShow()
	{
		return lstShortSignalShow;
	}

	public static List<SignalShow> getLstLongSignalShow()
	{
		return lstLongSignalShow;
	}

	// --------------------------------------------------------------------

	private static final long GENERATE_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(200);
	private static final long GENERATE_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(60);
	private static final long CHECK_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(240);
	private static final long CHECK_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(2);

	private static boolean isStarted = false;

	private static Timer timer1;
	private static Timer timer2;

	public static boolean start()
	{
		try
		{
			if (!isStarted)
			{
				startTask1();
				startTask2();

				isStarted = true;
			}

			return true;
		}
		catch (Exception e)
		{
			CoreLog.error(e);
			return false;
		}
	}
	
	public static void startTask1()
	{
		CoreLog.info("SignalService - generateSignals - Start");

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				SignalGenerator.generate();
				notifyAllObservers();
			}
		};
		timer2 = new Timer("generateSignals");
		timer2.schedule(task, GENERATE_DELAY_MILLIS, GENERATE_PERIOD_MILLIS);
	}

	public static void startTask2()
	{
		CoreLog.info("SignalService - checkSignals - Start");

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				checkSignals();
				notifyAllObservers();
			}
		};
		timer1 = new Timer("checkSignals");
		timer1.schedule(task, CHECK_DELAY_MILLIS, CHECK_PERIOD_MILLIS);
	}

	public static void close()
	{
		if (isStarted)
		{
			if (timer1 != null)
				timer1.cancel();

			if (timer2 != null)
				timer2.cancel();

			SignalGenerator.clean();
			
			lstShortSignalShow = new ArrayList<SignalShow>();
			lstLongSignalShow = new ArrayList<SignalShow>();

			isStarted = false;

			notifyAllObservers();
		}
	}

	// --------------------------------------------------------------------

	public static void checkSignals()
	{
		lstShortSignalShow = new ArrayList<SignalShow>();
		for (Signal signal : SignalGenerator.getMapShortSignals().values())
		{
			SymbolInfo symbolInfo = SymbolInfoService.getSymbolInfo(signal.getSymbol());
			BigDecimal distance = PriceUtil.priceDistUp(symbolInfo.getLastPrice(), signal.getLimitPrice(), true);

			if (distance.doubleValue() <= -0.01)
			{
				SignalGenerator.removeSignals(signal.getSymbol());
				CoreLog.info(signal.getSymbol() + " - REACHED THE SHORT POINT");
				continue;
			}

			SignalShow entry = new SignalShow(signal, symbolInfo, distance);
			lstShortSignalShow.add(entry);
		}

		lstLongSignalShow = new ArrayList<SignalShow>();
		for (Signal signal : SignalGenerator.getMapLongSignals().values())
		{
			SymbolInfo symbolInfo = SymbolInfoService.getSymbolInfo(signal.getSymbol());
			BigDecimal distance = PriceUtil.priceDistDown(symbolInfo.getLastPrice(), signal.getLimitPrice(), true);

			if (distance.doubleValue() <= -0.01)
			{
				SignalGenerator.removeSignals(signal.getSymbol());
				CoreLog.info(signal.getSymbol() + " - REACHED THE LONG POINT");
				continue;
			}

			SignalShow entry = new SignalShow(signal, symbolInfo, distance);
			lstLongSignalShow.add(entry);
		}

		Comparator<SignalShow> comparator = Comparator.comparing(SignalShow::getDistance);
		Collections.sort(lstShortSignalShow, comparator);
		Collections.sort(lstLongSignalShow, comparator);
	}
	
	// --------------------------------------------------------------------
	
	private static List<Handler<Void>> observers = new ArrayList<Handler<Void>>();

	public static void attachObserver(Handler<Void> observer)
	{
		observers.add(observer);
	}

	public static void deattachObserver(Handler<Void> observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllObservers()
	{
		for (Handler<Void> observer : observers)
		{
			observer.handle(null);
		}
	}

}
