package sanzol.app.task;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.trade.AccountBalance;

import sanzol.app.config.Constants;
import sanzol.app.config.PrivateConfig;

public final class BalanceService
{
	private static final long DEFAULT_PERIOD_MILLIS = 1000 * 20;

	private static boolean isStarted = false;
	private static long timestamp = 0;

	private static AccountBalance accountBalance = null;

	private static String errorMessage;

	public static AccountBalance getAccountBalance()
	{
		return accountBalance;
	}

	public static AccountBalance getAccountBalanceNow()
	{
		if (timestamp < System.currentTimeMillis() - 1000)
		{
			runGetBalances();
		}

		return accountBalance;
	}

	public static String getErrorMessage()
	{
		return errorMessage;
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
			RequestOptions options = new RequestOptions();
			SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

			List<AccountBalance> lst = syncRequestClient.getBalance();
			for (AccountBalance e : lst)
			{
				if (e.getAsset().equals(Constants.DEFAULT_SYMBOL_RIGHT))
				{
					accountBalance = e;
				}
			}

			timestamp = System.currentTimeMillis();
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
			System.err.println("BalanceService.runGetBalances: " + e.getMessage());
		}

	}

}
