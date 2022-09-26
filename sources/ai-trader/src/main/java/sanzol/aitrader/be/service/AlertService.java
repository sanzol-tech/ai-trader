package sanzol.aitrader.be.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import sanzol.aitrader.be.model.Alert;
import sanzol.util.log.LogService;

public final class AlertService
{
	private static boolean isStarted = false;

	private static Map<String, Alert> mapAlerts = new ConcurrentHashMap<String, Alert>();

	private AlertService()
	{
		// Hide
	}

	public static Map<String, Alert> getMapAlerts()
	{
		return mapAlerts;
	}

	public static void add(Alert alert)
	{
		/*
		if (mapAlerts.containsKey(alert.getSymbol().getPair()))
		{
			throw new IllegalArgumentException("An alert already exists for the symbol");
		}
		*/

		mapAlerts.put(alert.getSymbol().getPair(), alert);
	}

	public static void remove(Alert alert)
	{
		mapAlerts.remove(alert.getSymbol().getPair());
	}

	private static void verifyAlerts()
	{
		if (observers.isEmpty())
		{
			return;
		}

		if (!mapAlerts.isEmpty())
		{
			for (Alert alert : mapAlerts.values())
			{

				BigDecimal lastPrice = PriceService.getLastPrice(alert.getSymbol());
				if (lastPrice.doubleValue() < 0)
				{
					continue;
				}

				// TimeOut
				if (alert.getTimeOut() < System.currentTimeMillis())
				{
					remove(alert);
					continue;
				}

				// Reached short limit price
				if (lastPrice.doubleValue() >= alert.getShortLimit().doubleValue() && alert.getAlertState() != AlertState.SHORT_LIMIT)
				{
					long timeOut = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15);
					changeAlertSatate(alert, AlertState.SHORT_LIMIT, timeOut);
					continue;
				}

				// Reached long limit price
				if (lastPrice.doubleValue() <= alert.getLongLimit().doubleValue() && alert.getAlertState() != AlertState.LONG_LIMIT)
				{
					long timeOut = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15);
					changeAlertSatate(alert, AlertState.LONG_LIMIT, timeOut);
					continue;
				}

				// Short alert price
				if (lastPrice.doubleValue() >= alert.getShortAlert().doubleValue() && alert.getAlertState() != AlertState.SHORT_ALERT && alert.getAlertState() != AlertState.SHORT_LIMIT)
				{
					changeAlertSatate(alert, AlertState.SHORT_ALERT, null);
					continue;
				}

				// Long alert price
				if (lastPrice.doubleValue() <= alert.getLongAlert().doubleValue() && alert.getAlertState() != AlertState.LONG_ALERT && alert.getAlertState() != AlertState.LONG_LIMIT)
				{
					changeAlertSatate(alert, AlertState.LONG_ALERT, null);
					continue;
				}

			}
		}
	}

	private static void changeAlertSatate(Alert alert, AlertState alertState, Long timeOut)
	{
		if (timeOut != null)
		{
			alert.setTimeOut(timeOut);
		}
		alert.setAlertState(alertState);

		LogService.debug(alertState.toString() + " / " + alert.toString());

		notifyAllLogObservers(alert);
	}

	// ------------------------------------------------------------------------

	private static Timer timer1;

	public static void start()
	{
		if (isStarted)
		{
			return;
		}

		LogService.info("AlertService - Start");

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				verifyAlerts();
				notifyAllLogObservers(null);
			}
		};
		timer1 = new Timer("verifyAlerts");
		timer1.schedule(task, TimeUnit.SECONDS.toMillis(15), TimeUnit.SECONDS.toMillis(5));

		isStarted = true;
	}

	public static void close()
	{
		if (timer1 != null)
			timer1.cancel();

		mapAlerts = new ConcurrentHashMap<String, Alert>();
	}

	// ------------------------------------------------------------------------

	private static List<AlertListener> observers = new ArrayList<AlertListener>();

	public static void attachRefreshObserver(AlertListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(AlertListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers(Alert alert)
	{
		for (AlertListener observer : observers)
		{
			if (alert == null)
				observer.onAlertsUptade();
			else
				observer.onAlert(alert);
		}
	}

}
