package aitrader.core.service.position;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import aitrader.core.config.CoreLog;
import aitrader.core.config.PrivateConfig;
import binance.futures.impl.SignedClient;
import binance.futures.impl.async.WsUserData;
import binance.futures.model.user.UserDataUpdateEvent;

public class UserDataService
{
	private static final long DEFAULT_PERIOD_MILLIS = TimeUnit.MINUTES.toMillis(45);
	private static boolean timerStarted = false;
	
	private static SignedClient client;
	private static WsUserData wsUserData;

	// --------------------------------------------------------------------

	public static boolean start()
	{
		try
		{
			CoreLog.info("UserDataService - Start");

			client = SignedClient.create(PrivateConfig.getApiKey(), PrivateConfig.getSecretKey());
			String listenKey = client.startUserDataStream();

			WsUserData wsUserData = WsUserData.create(listenKey, (event) -> {
				onMessage(event);
			});
			wsUserData.connect();

			startTimer();
			
			return true;
		}
		catch (Exception e)
		{
			CoreLog.error(e);
			return false;
		}
	}

	public static void keepAlive()
	{
		try
		{
			String result = client.keepUserDataStream();
			CoreLog.info("UserDataService.keepUserDataStream " + result);
		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}
	}

	public static void close()
	{
		try
		{
			wsUserData.close();
	
			String result = client.closeUserDataStream();
			CoreLog.info("UserDataService.closeUserDataStream " + result);
			
			closeTimer();			
		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}
	}

	public static void onMessage(UserDataUpdateEvent event)
	{
		try
		{
			CoreLog.debug("UserDataService.onMessage");
			
			if ("ACCOUNT_UPDATE".equals(event.getEventType()))
			{
					BalanceService.checkBalance();
			}

			if ("ORDER_TRADE_UPDATE".equals(event.getEventType()))
			{
					PositionService.searchPositions();
			}
		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}
	}

	// --------------------------------------------------------------------

	private static Timer timer1;

	public static void startTimer()
	{
		if (timerStarted)
		{
			return;
		}

		CoreLog.info("UserDataService - Start timer");

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				keepAlive();
			}
		};
		timer1 = new Timer("UserDataService");
		timer1.schedule(task, DEFAULT_PERIOD_MILLIS, DEFAULT_PERIOD_MILLIS);

		timerStarted = true;
	}

	public static void closeTimer()
	{
		if (timerStarted && timer1 != null)
			timer1.cancel();
	}	
}
