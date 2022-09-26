package sanzol.aitrader.be.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import sanzol.util.log.LogService;

public class BalanceSpotService
{
	private static final long DEFAULT_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(30);

	private static boolean isStarted = false;
	// private static long timestamp = 0;

	// ------------------------------------------------------------------------

	private static Timer timer1;

	public static void start()
	{
		if (isStarted)
		{
			return;
		}

		LogService.info("BalanceService - Start");

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				checkBalances();
				notifyAllLogObservers();
			}
		};
		timer1 = new Timer("checkBalances");
		timer1.schedule(task, 0, DEFAULT_PERIOD_MILLIS);

		isStarted = true;
	}

	public static void close()
	{
		if (isStarted && timer1 != null)
			timer1.cancel();

		// ...

		notifyAllLogObservers();
	}

	private static void checkBalances()
	{
		try
		{

			// timestamp = System.currentTimeMillis();
		}
		catch (Exception e)
		{
			LogService.error(e);
		}

	}

	// ------------------------------------------------------------------------

	private static List<BalanceListener> observers = new ArrayList<BalanceListener>();

	public static void attachRefreshObserver(BalanceListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(BalanceListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers()
	{
		for (BalanceListener observer : observers)
		{
			observer.onBalanceUpdate();
		}
	}


}
