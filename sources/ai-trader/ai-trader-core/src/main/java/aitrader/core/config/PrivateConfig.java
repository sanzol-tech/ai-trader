package aitrader.core.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import aitrader.util.Properties;

public class PrivateConfig
{
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
		Path path = Paths.get(CoreConstants.DEFAULT_USER_FOLDER, CoreConstants.PRIVATEKEY_FILENAME);
		if (!path.toFile().exists())
		{
			CoreLog.error("File " + CoreConstants.PRIVATEKEY_FILENAME + " does not exist");
			return false;
		}

		Properties props = Properties.create(true).load(path);
		apiKey = props.getValue("apiKey");
		secretKey = props.getValue("secretKey");

		return true;
	}

	public static void save() throws IOException
	{
		Path path = Paths.get(CoreConstants.DEFAULT_USER_FOLDER, CoreConstants.PRIVATEKEY_FILENAME);
		Properties.create(true)
			.put("apiKey", apiKey)
			.put("secretKey", secretKey)
			.save(path);
	}	
	
}
