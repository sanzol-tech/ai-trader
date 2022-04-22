package sanzol.app.config;

import java.io.File;

public class Constants
{
	public static final String APP_NAME = "AI Trader";

	// Constants
	public static final String DEFAULT_USER_FOLDER = System.getProperty("user.home") + File.separatorChar + "ai-trader";
	public static final String PRIVATEKEY_FILENAME = "ai-trader.cfg";
	public static final String PROPERTIES_FILENAME = "config.properties";
	public static final String SHOCKPOINTS_FILENAME = "shockpoints.csv";

	// Pair
	public static final String DEFAULT_SYMBOL_RIGHT = "USDT";

}
