package sanzol.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import api.client.futures.model.sync.AccountBalance;
import api.client.futures.sync.SyncFuturesClient;
import sanzol.app.config.Config;

public final class BalanceService
{
	private static final long DEFAULT_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(30);

	private static boolean isStarted = false;
	private static long timestamp = 0;

	private static AccountBalance accountBalance = null;

	public static AccountBalance getAccountBalance()
	{
		return accountBalance;
	}

	public static AccountBalance getAccountBalanceNow()
	{
		if (timestamp < System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(1))
		{
			runGetBalances();
		}

		return accountBalance;
	}

	public static void start()
	{
		if (isStarted)
		{
			return;
		}

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				runGetBalances();
				notifyAllLogObservers();
			}
		};
		Timer timer = new Timer("BalanceService");
		timer.schedule(task, 0, DEFAULT_PERIOD_MILLIS);

		isStarted = true;
	}

	private static void runGetBalances()
	{
		try
		{
			List<AccountBalance> lst = SyncFuturesClient.getBalance();

			for (AccountBalance e : lst)
			{
				if (e.getAsset().equals(Config.DEFAULT_SYMBOL_RIGHT))
				{
					accountBalance = e;
				}
			}

			timestamp = System.currentTimeMillis();
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
