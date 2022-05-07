package sanzol.app.config;

import java.io.File;

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
