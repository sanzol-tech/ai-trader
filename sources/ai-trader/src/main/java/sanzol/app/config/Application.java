package sanzol.app.config;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

import api.client.config.ApiConfig;
import api.client.service.DepthCache;
import api.client.service.ExchangeInfoService;
import api.client.service.LastCandlestickService;
import api.client.service.PriceService;
import sanzol.app.forms.FrmConfig;
import sanzol.app.forms.FrmMain;
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
		PreventLaunchingMultiple();
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
		
		Thread thread = new Thread(new InitializeTask(), "initializeTask");
		thread.start();

		initializeUI();
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

	private static void verifyFolders()
	{
		try
		{
			File path = new File(Constants.DEFAULT_USER_FOLDER);
			if (!path.exists())
			{
				path.mkdirs();
			}
			File pathLog = new File(Constants.DEFAULT_LOG_FOLDER);
			if (!pathLog.exists())
			{
				pathLog.mkdirs();
			}
			File pathExport = new File(Constants.DEFAULT_EXPORT_FOLDER);
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

	private static void PreventLaunchingMultiple()
	{
		File file = new File(Constants.DEFAULT_USER_FOLDER, "ai-trader.lock");
		try
		{
			FileChannel fc = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			FileLock lock = fc.tryLock();
			if (lock == null)
			{
				System.out.println("another instance is running");
				System.exit(0);
			}
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
			System.exit(-1);
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

	public static void main(String[] args)
	{
		if (args.length == 1 && args[0].equalsIgnoreCase("spot"))
			ApiConfig.setSpot();
		else
			ApiConfig.setFutures();

		initialize();

		FrmMain.launch();

		if (!keyLoadedOK)
		{
			FrmConfig.launch();
		}
	}

}
