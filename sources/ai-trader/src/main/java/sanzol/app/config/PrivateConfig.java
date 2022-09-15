package sanzol.app.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import api.client.config.ApiConfig;
import sanzol.app.service.LogService;
import sanzol.util.security.Cipher;

public class PrivateConfig
{
	public static String API_KEY = "";
	public static String SECRET_KEY = "";

	public static boolean loadKey()
	{
		try
		{
			File basepath = new File(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString());
			File fileConfig = new File(basepath, Constants.PRIVATEKEY_FILENAME);

			if (!fileConfig.exists())
			{
				LogService.error("File ai-trader.cfg does not exist");
				return false;
			}

			String text = FileUtils.readFileToString(fileConfig, StandardCharsets.UTF_8);
			String key = Cipher.decrypt(text);

			PrivateConfig.API_KEY = key.split("::")[0];
			PrivateConfig.SECRET_KEY = key.split("::")[1];
		}
		catch (Exception e)
		{
			LogService.error(e);
			return false;
		}
		return true;
	}

	public static void setKey(String apiKey, String secretKey) throws IOException
	{
		File basepath = new File(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString());
		File fileConfig = new File(basepath, Constants.PRIVATEKEY_FILENAME);

		String key = apiKey + "::" + secretKey;
		String text = Cipher.encrypt(key);

		FileUtils.writeStringToFile(fileConfig, text, StandardCharsets.UTF_8);

		// ----------------------------------
		PrivateConfig.API_KEY = apiKey;
		PrivateConfig.SECRET_KEY = secretKey;
	}

}
