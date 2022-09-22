package sanzol.aitrader.be.config;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import api.client.impl.config.ApiConfig;
import api.client.impl.model.enums.MarketType;
import sanzol.aitrader.be.service.AlertService;
import sanzol.aitrader.be.service.BalanceService;
import sanzol.aitrader.be.service.DepthCache;
import sanzol.aitrader.be.service.ExchangeInfoService;
import sanzol.aitrader.be.service.LastCandlestickService;
import sanzol.aitrader.be.service.PositionFuturesService;
import sanzol.aitrader.be.service.PositionSpotService;
import sanzol.aitrader.be.service.PriceService;
import sanzol.aitrader.be.service.SignalService;
import sanzol.util.log.LogService;

public final class ServerApp
{
	private static boolean keyLoadedOK = false;

	public static boolean isKeyLoadedOK()
	{
		return keyLoadedOK;
	}

	public static void initialize(MarketType marketType)
	{
		verifyFolders();

		keyLoadedOK = PrivateConfig.loadKey();

		try
		{
			Config.load();

			if (keyLoadedOK)
			{
				if (marketType == MarketType.spot)
				{
					// BalanceService.start();
					PositionSpotService.start();
				}
				else
				{
					BalanceService.start();
					PositionFuturesService.start();
				}
			}
		}
		catch (Exception e)
		{
			LogService.error(e);
		}
	}

	public static class InitializeTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				ExchangeInfoService.start();
				PriceService.start();
				LastCandlestickService.start("btcusdt");
				DepthCache.start();
				SignalService.start();
				AlertService.start();
			}
			catch (Exception e)
			{
				LogService.error(e);
			}
		}
	}

	private static void verifyFolders()
	{
		try
		{
			File basepath = new File(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString());

			if (!basepath.exists())
			{
				basepath.mkdirs();
			}
			File pathLog = new File(basepath, Constants.DEFAULT_LOG_FOLDER);
			if (!pathLog.exists())
			{
				pathLog.mkdirs();
			}
			File pathExport = new File(basepath, Constants.DEFAULT_EXPORT_FOLDER);
			if (!pathExport.exists())
			{
				pathExport.mkdirs();
			}
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

	@SuppressWarnings("resource")
	private static void assertNoOtherInstanceRunning(MarketType marketType)
	{
		new Thread(() -> {
			try
			{
				if (marketType == MarketType.spot)
					new ServerSocket(22723).accept();
				else
					new ServerSocket(22722).accept();
			}
			catch (IOException e)
			{
				System.err.println("another instance is running");
				System.exit(-1);
			}
		}).start();
	}

	public static void start(MarketType marketType)
	{
		ApiConfig.setMarketType(marketType);
		assertNoOtherInstanceRunning(marketType);
		
		initialize(marketType);

		Thread thread = new Thread(new InitializeTask(), "initializeTask");
		thread.start();
	}

	public static void main(String[] args)
	{
		start(MarketType.futures);
	}

}
