package aitrader.core.config;

import java.io.File;

public final class CoreConstants
{
	public static final String APP_NAME = "ai-trader-core";

	public static final int RUNNING_PORT = 22822;
	
	public static final String DEFAULT_USER_FOLDER = System.getProperty("user.home") + File.separatorChar + "ai-trader-ng";

	public static final String DEFAULT_DATA_FOLDER = "data";
	public static final String DEFAULT_LOG_FOLDER = "log";
	public static final String DEFAULT_EXPORT_FOLDER = "export";

	public static final String PRIVATEKEY_FILENAME = "private.cfg";
	public static final String PROPERTIES_FILENAME = "ai-trader-core.cfg";
	public static final String TELEGRAM_FILENAME = "telegram.cfg";

}
