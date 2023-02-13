package aitrader.ui.config;

import java.lang.reflect.InvocationTargetException;

import aitrader.core.config.CoreApp;
import aitrader.core.config.PrivateConfig;
import aitrader.ui.forms.FrmConfigSecure;
import aitrader.ui.forms.FrmMain;
import aitrader.ui.forms.FrmSplash;
import aitrader.util.ExceptionUtils;

public final class UIApp
{
	private static void initializeUI()
	{
		try
		{
			UIConfig.load();
			
			Styles.applyStyle();
		}
		catch (Exception e)
		{
			System.err.println(ExceptionUtils.getMessage(e));
			System.exit(-1);
		}
	}

	private static void start() throws InvocationTargetException, InterruptedException
	{
		UILog.info(UIConstants.APP_NAME + " starting...");

		FrmSplash.launch();

		CoreApp.start((e) -> { onServerAppStarted(e); });

		initializeUI();

		FrmMain.launch(false);
		FrmSplash.close();

		if (!PrivateConfig.isLoaded())
		{
			FrmConfigSecure.launch();
		}
	}

	private static void onServerAppStarted(String e)
	{
		UILog.info(e);
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException
	{
		start();
	}

}
