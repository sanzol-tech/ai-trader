package sanzol.aitrader.ui.config;

import api.client.impl.model.enums.MarketType;
import sanzol.aitrader.be.config.ServerApp;
import sanzol.aitrader.ui.forms.FrmConfig;
import sanzol.aitrader.ui.forms.FrmMain;
import sanzol.aitrader.ui.forms.FrmSplash;

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

	private static void start(MarketType marketType)
	{
		FrmSplash.launch();

		// -------------------------------------------------------------------
		ServerApp.start(marketType);
		// -------------------------------------------------------------------

		initializeUI();

		FrmMain.launch();

		if (!ServerApp.isKeyLoadedOK())
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

		start(marketType);
	}

}
