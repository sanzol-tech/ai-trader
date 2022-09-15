package sanzol.app.config;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import api.client.config.ApiConfig;
import api.client.enums.MarketType;
import api.client.service.DepthCache;
import api.client.service.ExchangeInfoService;
import api.client.service.LastCandlestickService;
import api.client.service.PriceService;
import sanzol.app.forms.FrmConfig;
import sanzol.app.forms.FrmMain;
import sanzol.app.forms.FrmSplash;
import sanzol.app.service.BalanceService;
import sanzol.app.service.LogService;
import sanzol.app.service.PositionService;
import sanzol.app.service.SignalService;

public final class Application
{
	private static boolean keyLoadedOK = false;

	public static void initialize()
	{
		verifyFolders();

		keyLoadedOK = PrivateConfig.loadKey();

		try
		{
			Config.load();

			if (keyLoadedOK)
			{
				BalanceService.start();
				PositionService.start();
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
			}
			catch (Exception e)
			{
				LogService.error(e);
			}
		}
	}

	private static void initializeUI()
	{
		try
		{
			Styles.applyStyle();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			System.exit(-1);
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

	private static void start(MarketType marketType)
	{
		FrmSplash.launch();

		initialize();

		Thread thread = new Thread(new InitializeTask(), "initializeTask");
		thread.start();

		initializeUI();

		FrmMain.launch();

		if (!keyLoadedOK)
		{
			FrmConfig.launch();
		}
	}

	public static void main(String[] args)
	{
		MarketType marketType;
		if (args.length == 1 && args[0].equalsIgnoreCase("spot"))
			marketType = MarketType.spot;
		else
			marketType = MarketType.futures;

		ApiConfig.setMarketType(marketType);

		assertNoOtherInstanceRunning(marketType);

		start(marketType);
	}

}
