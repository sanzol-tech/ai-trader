package sanzol.app.config;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

import sanzol.app.forms.FrmMain;
import sanzol.app.task.BalanceService;
import sanzol.app.task.PositionService;
import sanzol.app.task.PriceService;
import sanzol.app.task.SignalService;

public final class Application
{
	private static String error = "";

	public static String getError()
	{
		return error;
	}

	public static void initialize()
	{
		try
		{
			verifyFolders();
			PreventLaunchingMultiple();

			PrivateConfig.loadKey();
			Config.load();
			PriceService.start();
			BalanceService.start();
			PositionService.start();
			SignalService.start();
		}
		catch (Exception e)
		{
			error = "Application.initialize: " + e.getMessage();
			System.err.println(e.getMessage());
		}
	}

	private static void verifyFolders()
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

	public static void initializeUI()
	{
		try
		{
			Styles.setLight();
		}
		catch (Exception e)
		{
			error = "Application.initializeUI: " + e.getMessage();
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args)
	{
		initialize();
		initializeUI();
		FrmMain.launch();
	}

}
