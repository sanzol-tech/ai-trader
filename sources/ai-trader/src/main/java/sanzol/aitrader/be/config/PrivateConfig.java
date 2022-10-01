package sanzol.aitrader.be.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import api.client.impl.config.ApiConfig;
import sanzol.util.Properties;
import sanzol.util.log.LogService;

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
		Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString(), Constants.PRIVATEKEY_FILENAME);
		if (!path.toFile().exists())
		{
			LogService.error("File " + Constants.PRIVATEKEY_FILENAME + " does not exist");
			return false;
		}

		Properties props = Properties.create(true).load(path);
		apiKey = props.getValue("apiKey");
		secretKey = props.getValue("secretKey");

		return true;
	}

	public static void save() throws IOException
	{
		Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString(), Constants.PRIVATEKEY_FILENAME);
		Properties.create(true)
			.put("apiKey", apiKey)
			.put("secretKey", secretKey)
			.save(path);
	}	

	public static void main(String[] args)
	{
		/*
		try
		{
			apiKey = "xxxx";
			secretKey = "yyyy";
			save();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		*/
	}	
}
