package aitrader.core.service.signals;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import aitrader.core.config.CoreConstants;
import aitrader.core.config.CoreLog;
import aitrader.core.config.TelegramConfig;
import aitrader.core.model.Alert;
import aitrader.core.service.symbol.SymbolInfoService;
import aitrader.core.service.trade.OrderHelper;
import aitrader.core.service.trade.QuantityHelper;
import aitrader.util.observable.Handler;
import aitrader.util.price.PriceUtil;
import aitrader.util.telegram.TelegramHelper;
import binance.futures.enums.OrderSide;
import binance.futures.enums.PositionSide;

public final class AlertService
{
	private static final long TIMER_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(15);
	private static final long TIMER_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(5);

	private static final String ALERTS_FILENAME = "alerts.json";

	private static boolean isStarted = false;

	private static List<Alert> lstAlerts = new ArrayList<Alert>();

	private AlertService()
	{
		// Hide
	}

	public static List<Alert> getAlerts(String orderSide)
	{
		List<Alert> list = new ArrayList<Alert>();

		for (Alert alert : lstAlerts)
		{
			BigDecimal lastPrice = SymbolInfoService.getLastPrice(alert.getSymbol());
			if (lastPrice == null)
			{
				continue;
			}

			if (alert.getExpirationAt() != null && alert.getExpirationAt() < System.currentTimeMillis())
			{
				continue;
			}

			if (alert.getOrderSide().equals(orderSide))
			{
				BigDecimal alertDist;
				BigDecimal limitDist;
				if ("SELL".equals(alert.getOrderSide()))
				{
					alertDist = PriceUtil.priceDistUp(lastPrice, alert.getAlertPrice(), true);
					limitDist = PriceUtil.priceDistUp(lastPrice, alert.getLimitPrice(), true);
				}
				else
				{
					alertDist = PriceUtil.priceDistDown(lastPrice, alert.getAlertPrice(), true);
					limitDist = PriceUtil.priceDistDown(lastPrice, alert.getLimitPrice(), true);
				}

				alert.setAlertDistance(alertDist);
				alert.setLimitDistance(limitDist);

				list.add(alert);
			}
		}

		Comparator<Alert> comparator = Comparator.comparing(Alert::getAlertDistance);
		Collections.sort(list, comparator);

		return list;
	}

	public static void clean()
	{
		lstAlerts = new ArrayList<Alert>();
	}

	public static void addAlert(Alert... alerts)
	{
		for (Alert alert : alerts)
		{
			boolean exists = lstAlerts.stream().anyMatch(a -> alert.getSymbol().getPair().equals(a.getSymbol().getPair()) && alert.getOrderSide().equals(a.getOrderSide()));
			if (exists)
			{
				throw new IllegalArgumentException("An alert already exists for the symbol");
			}

			lstAlerts.add(alert);
			update();
		}
	}

	// --------------------------------------------------------------------

	public static void update()
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(lstAlerts);
			Path path = Paths.get(CoreConstants.DEFAULT_USER_FOLDER, CoreConstants.DEFAULT_DATA_FOLDER, ALERTS_FILENAME);
			Files.writeString(path, jsonString, StandardOpenOption.CREATE);
		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}
	}

	public static void load()
	{
		try
		{
			Path path = Paths.get(CoreConstants.DEFAULT_USER_FOLDER, CoreConstants.DEFAULT_DATA_FOLDER, ALERTS_FILENAME);
			if (path.toFile().exists())
			{
				String jsonString = Files.readString(path);
				ObjectMapper mapper = new ObjectMapper();
				lstAlerts = mapper.readValue(jsonString, new TypeReference<List<Alert>>(){});
			}
		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}
	}

	// --------------------------------------------------------------------

	public static void checkAlerts()
	{
		if (observers.isEmpty())
		{
			return;
		}
		
		for (Alert alert : lstAlerts)
		{
			try
			{
				BigDecimal lastPrice = SymbolInfoService.getLastPrice(alert.getSymbol());
				if (lastPrice == null)
				{
					continue;
				}

				if (alert.getExpirationAt() != null && alert.getExpirationAt() < System.currentTimeMillis())
				{
					continue;
				}
				
				if ("SELL".equals(alert.getOrderSide()))
				{
					// Reached alert price
					if (!alert.isAlerted() && lastPrice.doubleValue() >= alert.getAlertPrice().doubleValue())
					{
						alert.setAlerted(true);
						alertTrigger(alert);
						continue;
					}
				}
				else if ("BUY".equals(alert.getOrderSide()))
				{
					// Reached alert price
					if (!alert.isAlerted() && lastPrice.doubleValue() <= alert.getAlertPrice().doubleValue())
					{
						alert.setAlerted(true);
						alertTrigger(alert);
						continue;
					}
				}

			}
			catch(Exception e)
			{
				CoreLog.error(e);				
			}
		}

		// Purge alerts
		lstAlerts.removeIf(entry -> entry.getExpirationAt() != null && entry.getExpirationAt() < System.currentTimeMillis());
	}

	private static void alertTrigger(Alert alert) throws Exception
	{
		PositionSide positionSide = "SELL".equals(alert.getOrderSide()) ? PositionSide.SHORT : PositionSide.LONG;
		OrderSide orderSide = "SELL".equals(alert.getOrderSide()) ? OrderSide.SELL : OrderSide.BUY;

		BigDecimal qty = QuantityHelper.create(alert.getSymbol(), alert.getLimitPrice()).from(alert.getQuantityType(), alert.getQuantity()).getQty();

		OrderHelper.postOrder(alert.getSymbol(), positionSide, orderSide, alert.getLimitPrice(), qty);

		String message = "alert " + alert.toString();
		TelegramHelper.sendMessage(TelegramConfig.getApiToken(), TelegramConfig.getChatId(), message);

		notifyAllObservers(alert);
	}

	// --------------------------------------------------------------------

	private static Timer timer1;

	public static void start()
	{
		if (isStarted)
		{
			return;
		}

		CoreLog.info("AlertService - Start");

		load();
		
		TimerTask task = new TimerTask()
		{
			public void run()
			{
				checkAlerts();
				notifyAllObservers(null);
			}
		};
		timer1 = new Timer("verifyAlerts");
		timer1.schedule(task, TIMER_DELAY_MILLIS, TIMER_PERIOD_MILLIS);

		isStarted = true;
	}

	public static void close()
	{
		if (isStarted && timer1 != null)
		{
			timer1.cancel();
			isStarted = false;
		}

		clean();
	}

	// --------------------------------------------------------------------

	private static List<Handler<Alert>> observers = new ArrayList<Handler<Alert>>();

	public static void attachObserver(Handler<Alert> observer)
	{
		observers.add(observer);
	}

	public static void deattachObserver(Handler<Alert> observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllObservers(Alert alert)
	{
		for (Handler<Alert> observer : observers)
		{
			observer.handle(alert);
		}
	}

}
