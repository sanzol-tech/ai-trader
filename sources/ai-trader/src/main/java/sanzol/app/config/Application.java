package sanzol.app.config;

import javax.swing.UIManager;

import sanzol.app.forms.FrmMain;
import sanzol.app.task.BalanceService;
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
			PrivateConfig.loadKey();
			PriceService.start();
			Config.load();
			BalanceService.start();
			SignalService.start();
		}
		catch (Exception e)
		{
			error = "Application.initialize: " + e.getMessage();
			System.err.println(e.getMessage());
		}
	}

	public static void initializeUI()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
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
