package aitrader.core.config;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import aitrader.core.service.market.DepthCache;
import aitrader.core.service.position.BalanceService;
import aitrader.core.service.position.PositionService;
import aitrader.core.service.position.UserDataService;
import aitrader.core.service.signals.AlertService;
import aitrader.core.service.signals.SignalService;
import aitrader.core.service.symbol.ExchangeInfoService;
import aitrader.core.service.symbol.SymbolTickerService;
import aitrader.core.service.symbol.TechnicalService;
import aitrader.core.service.trade.PositionModeService;
import aitrader.util.ExceptionUtils;
import aitrader.util.observable.Handler;

public final class CoreApp
{
	@SuppressWarnings("resource")
	private static void checkAnotherInstanceRunning()
	{
		new Thread(() -> {
			try
			{
				new ServerSocket(CoreConstants.RUNNING_PORT).accept();
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
		File basepath = new File(CoreConstants.DEFAULT_USER_FOLDER);

		if (!basepath.exists())
		{
			basepath.mkdirs();
		}
		File pathData = new File(basepath, CoreConstants.DEFAULT_DATA_FOLDER);
		if (!pathData.exists())
		{
			pathData.mkdirs();
		}
		File pathLog = new File(basepath, CoreConstants.DEFAULT_LOG_FOLDER);
		if (!pathLog.exists())
		{
			pathLog.mkdirs();
		}
		File pathExport = new File(basepath, CoreConstants.DEFAULT_EXPORT_FOLDER);
		if (!pathExport.exists())
		{
			pathExport.mkdirs();
		}
	}

	public static void start(Handler<String> observer)
	{
		try
		{
			CoreLog.info(CoreConstants.APP_NAME + " starting...");
			checkAnotherInstanceRunning();
			verifyFolders();
			PrivateConfig.load();
			TelegramConfig.load();
			CoreConfig.load();
			servicesStart();
			observer.handle(CoreConstants.APP_NAME + " started");
		}
		catch (Exception e)
		{
			System.err.println(ExceptionUtils.getMessage(e));
			System.exit(-1);
		}
	}

	// --- Services ----------------------------------------------------------
	public static void servicesStart()
	{
		CoreLog.info(CoreConstants.APP_NAME + " Starting services...");

		Runnable runnable = () ->
		{
			try
			{
				ExchangeInfoService.getSnapshoot();
				SymbolTickerService.getSnapshoot();
				SymbolTickerService.openWebsocket();
				TechnicalService.start();

				DepthCache.start();

				SignalService.start();
				AlertService.start();

				if (PrivateConfig.isLoaded())
				{
					PositionModeService.loadPositionMode();
					
					// BalanceService.start();
					// PositionService.start();
					BalanceService.checkBalance();
					PositionService.searchPositions();
					UserDataService.start();
				}
			}
			catch (Exception e)
			{
				CoreLog.error(e);
			}
		};
		Thread thread = new Thread(runnable, "servicesStart");
		thread.start();
	}

	public static void servicesRestart(boolean symbolInfo, boolean depthCache, boolean signals, boolean alerts, boolean userDate)
	{
		CoreLog.info(CoreConstants.APP_NAME + " Restarting services...");

		Runnable runnable = () ->
		{
			// --- CLOSE ------------------------------------------------------
			if (symbolInfo)
			{
				ExchangeInfoService.close();
				SymbolTickerService.close();
				TechnicalService.close();
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
			if (PrivateConfig.isLoaded() && userDate)
			{
				BalanceService.close();
				PositionService.close();
				UserDataService.close();
			}

			// --- START ------------------------------------------------------
			if (symbolInfo)
			{
				ExchangeInfoService.getSnapshoot();
				SymbolTickerService.getSnapshoot();
				SymbolTickerService.openWebsocket();
				TechnicalService.close();
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
			if (PrivateConfig.isLoaded() && userDate)
			{
				BalanceService.checkBalance();
				PositionService.searchPositions();
				UserDataService.start();
			}

		};
		Thread thread = new Thread(runnable, "servicesRestart");
		thread.start();
	}

	// --- -------- ----------------------------------------------------------

	public static void main(String[] args)
	{
		start((e) -> { System.out.println(e); });
	}

}
