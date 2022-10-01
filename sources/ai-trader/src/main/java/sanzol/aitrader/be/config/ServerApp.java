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
import sanzol.aitrader.be.service.PositionService;
import sanzol.aitrader.be.service.PriceService;
import sanzol.aitrader.be.service.SignalService;
import sanzol.util.log.LogService;
import sanzol.util.observable.Handler;
import sanzol.util.telegram.TelegramConfig;

public final class ServerApp
{
	private static final int PORT_SPOT = 22723;
	private static final int PORT_FUTURES = 22722;

	@SuppressWarnings("resource")
	private static void checkAnotherInstanceRunning(MarketType marketType)
	{
		new Thread(() -> {
			try
			{
				if (marketType == MarketType.spot)
					new ServerSocket(PORT_SPOT).accept();
				else
					new ServerSocket(PORT_FUTURES).accept();
			}
			catch (IOException e)
			{
				System.err.println("another instance is running");
				System.exit(-1);
			}
		}, "checkAnotherInstanceRunning").start();
	}

	private static void verifyFolders()
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

	public static void start(MarketType marketType, Handler<String> observer)
	{
		try
		{
			checkAnotherInstanceRunning(marketType);
			verifyFolders();
			PrivateConfig.load();
			TelegramConfig.load();
			Config.load();
			servicesStart();
			observer.handle("ServerApp started");
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

	// --- Services ----------------------------------------------------------
	public static void servicesStart()
	{
		LogService.info("Starting services");

		Runnable runnable = () ->
		{
			try
			{
				ExchangeInfoService.start();

				PriceService.start();
				LastCandlestickService.start("btcusdt");
				DepthCache.start();
				SignalService.start();
				AlertService.start();
				if (PrivateConfig.isLoaded())
				{
					BalanceService.start();
					PositionService.start();
				}
			}
			catch (Exception e)
			{
				LogService.error(e);
			}
		};
		Thread thread = new Thread(runnable, "servicesStart");
		thread.start();
	}

	public static void servicesRestart(boolean prices, boolean last30mBtc, boolean depthCache, boolean signals, boolean alerts, boolean balance, boolean positions)
	{
		LogService.info("Restarting services");

		Runnable runnable = () ->
		{
			// --- CLOSE ------------------------------------------------------
			if (prices)
			{
				PriceService.close();
			}
			if (last30mBtc)
			{
				LastCandlestickService.close();
			}
			if (depthCache)
			{
				DepthCache.removeAll();
			}
			if (signals)
			{
				SignalService.close();
			}
			if (alerts)
			{
				AlertService.close();
			}
			if (PrivateConfig.isLoaded() && alerts)
			{
				BalanceService.close();
			}
			if (PrivateConfig.isLoaded() && alerts)
			{
				PositionService.close();
			}

			// --- START ------------------------------------------------------
			if (prices)
			{
				PriceService.start();
			}
			if (last30mBtc)
			{
				LastCandlestickService.start("btcusdt");
			}
			if (depthCache)
			{
				DepthCache.start();
			}
			if (signals)
			{
				SignalService.start();
			}
			if (alerts)
			{
				AlertService.start();
			}
			if (PrivateConfig.isLoaded() && alerts)
			{
				BalanceService.start();
			}
			if (PrivateConfig.isLoaded() && alerts)
			{
				PositionService.start();
			}

		};
		Thread thread = new Thread(runnable, "servicesRestart");
		thread.start();
	}

	// --- -------- ----------------------------------------------------------

	public static void main(String[] args)
	{
		start(MarketType.futures, (e) -> { System.out.println(e); });
	}

}
