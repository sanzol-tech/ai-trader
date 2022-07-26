package sanzol.app.config;

import com.binance.client.SubscriptionClient;

import sanzol.app.service.OBookCache;
import sanzol.app.task.CandlestickCache;
import sanzol.app.task.LogService;
import sanzol.app.task.PriceService;

public class WsClient
{
	private static SubscriptionClient client;

	public static void initialize()
	{
		client = SubscriptionClient.create();

		PriceService.start(client);
		CandlestickCache.start(client);
		OBookCache.start(client);
	}

	private static boolean isRestarting = false;
	public static void restarByError(Exception e)
	{
		LogService.error("WsClient.restarByError " + e.getMessage());
		if (isRestarting)
		{
			LogService.error("skip restart");
			return;
		}

		try
		{
			isRestarting = true;

			LogService.error("WsClient.restarByError " + e.getMessage());
			client.unsubscribeAll();
			initialize();
	
			try { Thread.sleep(1500); }	catch (InterruptedException e1) { }
		}
		finally
		{
			isRestarting = false;	
		}
		
	}

}
