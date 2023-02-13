package examples;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import aitrader.util.Properties;

public class PrivateConfig
{
	public static final String DEFAULT_USER_FOLDER = System.getProperty("user.home") + File.separatorChar + "ai-trader" + File.separatorChar + "futures";
	public static final String PRIVATEKEY_FILENAME = "ai-trader.cfg";

	private static String apiKey;
	private static String secretKey;

	public static boolean isLoaded()
	{
		return apiKey != null && !apiKey.isEmpty() && secretKey != null && !secretKey.isEmpty();
	}

	public static String getApiKey()
	{
		return apiKey;
	}

	public static void setApiKey(String apiKey)
	{
		PrivateConfig.apiKey = apiKey;
	}

	public static String getSecretKey()
	{
		return secretKey;
	}

	public static void setSecretKey(String secretKey)
	{
		PrivateConfig.secretKey = secretKey;
	}

	public static boolean load() throws IOException
	{
		Path path = Paths.get(DEFAULT_USER_FOLDER, PRIVATEKEY_FILENAME);
		if (!path.toFile().exists())
		{
			System.err.println("File " + PRIVATEKEY_FILENAME + " does not exist");
			return false;
		}

		Properties props = Properties.create(true).load(path);
		apiKey = props.getValue("apiKey");
		secretKey = props.getValue("secretKey");

		return true;
	}

}
