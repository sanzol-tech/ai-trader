package aitrader.core.service.symbol;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import aitrader.core.config.CoreLog;
import aitrader.core.model.SymbolInfo;
import binance.futures.enums.IntervalType;
import binance.futures.impl.UnsignedClient;
import binance.futures.model.Candle;

public class TechnicalService
{
	private static final long TIMER_DELAY_MILLIS = TimeUnit.MINUTES.toMillis(2);
	private static final long TIMER_PERIOD_MILLIS = TimeUnit.HOURS.toMillis(12);
	private static final long ITEM_DELAY = 200;

	// private static IntervalType intervalType = IntervalType._1d;
	// private static int periods = 14;
	
	private static boolean isStarted = false;
	private static boolean isExecuting = false;
	private static boolean isCancelRequested = false;
	private static LocalDateTime lastExecuted = null;

	private static Timer timer1;

	public static boolean isStarted()
	{
		return isStarted;
	}

	public static boolean isExecuting()
	{
		return isExecuting;
	}

	public static LocalDateTime getLastExecuted()
	{
		return lastExecuted;
	}

	public static void process(IntervalType intervalType, int periods)
	{
		try
		{
			CoreLog.info("TechnicalService - Begin process");
			isExecuting = true;

			List<SymbolInfo> lstSymbols = SymbolInfoService.getLstSymbolsInfo(false, false, false);
			for (SymbolInfo entry : lstSymbols)
			{
				if (isCancelRequested)
				{
					isCancelRequested = false;
					CoreLog.info("TechnicalService - Canceled");
					return;
				}
				
				List<Candle> lstCandles = UnsignedClient.getKlines(entry.getSymbol().getPair(), intervalType, periods);

				if (lstCandles != null && !lstCandles.isEmpty())
				{
					BigDecimal open = lstCandles.get(0).getOpenPrice();
					BigDecimal high = lstCandles.get(0).getHighPrice();
					BigDecimal low = lstCandles.get(0).getLowPrice();

					for (Candle candle : lstCandles)
					{
						if (candle.getHighPrice().doubleValue() > high.doubleValue())
						{
							high = candle.getHighPrice();
						}
						if (candle.getLowPrice().doubleValue() < low.doubleValue())
						{
							low = candle.getLowPrice();
						}
					}
					
					SymbolInfo symbolInfo = SymbolInfoService.getSymbolInfo(entry.getSymbol().getPair());
					symbolInfo.setOpen14d(open);
					symbolInfo.setHigh14d(high);
					symbolInfo.setLow14d(low);
				}

				Thread.sleep(ITEM_DELAY);
			}

			lastExecuted = LocalDateTime.now();

			CoreLog.info("TechnicalService - End process");
		} 
		catch (Exception e)
		{
			CoreLog.error(e);
		}
		finally 
		{
			isExecuting = false;
		}
	}

	public static void start()
	{
		if (isStarted)
		{
			return;
		}

		CoreLog.info("TechnicalService - Start");

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				process(IntervalType._1d, 14);
			}
		};
		timer1 = new Timer("technicalService");
		timer1.schedule(task, TIMER_DELAY_MILLIS, TIMER_PERIOD_MILLIS);

		isStarted = true;
	}

	public static void close()
	{
		if (isStarted && timer1 != null)
		{
			timer1.cancel();
			isStarted = false;
			isCancelRequested = true;
		}
	}

}
