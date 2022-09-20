package sanzol.aitrader.be.config;

import java.io.File;

public final class Constants
{
	public static final String APP_NAME = "AI Trader";

	public static final String DEFAULT_USER_FOLDER = System.getProperty("user.home") + File.separatorChar + "ai-trader";

	public static final String DEFAULT_LOG_FOLDER = "log";
	public static final String DEFAULT_EXPORT_FOLDER = "export";
	public static final String PRIVATEKEY_FILENAME = "ai-trader.cfg";
	public static final String PROPERTIES_FILENAME = "config.properties";
	public static final String ALERTS_FILENAME = "alerts.csv";
	public static final String SHOCKPOINTS_FILENAME = "shockpoints.csv";

}
