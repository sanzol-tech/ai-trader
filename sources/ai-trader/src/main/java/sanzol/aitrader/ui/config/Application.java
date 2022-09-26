package sanzol.aitrader.ui.config;

import java.lang.reflect.InvocationTargetException;

import api.client.impl.model.enums.MarketType;
import sanzol.aitrader.be.config.ServerApp;
import sanzol.aitrader.ui.forms.FrmConfig;
import sanzol.aitrader.ui.forms.FrmMain;
import sanzol.aitrader.ui.forms.FrmSplash;
import sanzol.util.log.LogService;

public final class Application
{
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

	private static void start(MarketType marketType) throws InvocationTargetException, InterruptedException
	{
		FrmSplash.launch();
		ServerApp.start(marketType, (e) -> { onServerAppStarted(e); });

		initializeUI();
		FrmMain.launch(false);
		FrmSplash.close();

		if (!ServerApp.isKeyLoadedOK())
		{
			FrmConfig.launch();
		}
	}

	private static void onServerAppStarted(String e)
	{
		LogService.info(e);
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException
	{
		MarketType marketType;
		if (args.length == 1 && args[0].equalsIgnoreCase("spot"))
			marketType = MarketType.spot;
		else
			marketType = MarketType.futures;

		start(marketType);
	}

}
