package sanzol.app.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import sanzol.util.security.Cipher;

public class PrivateConfig
{

	public static String API_KEY = "";
	public static String SECRET_KEY = "";


	public static String loadKey() throws IOException
	{
		File fileConfig = new File(Constants.DEFAULT_USER_FOLDER, Constants.PRIVATEKEY_FILENAME);
		String text = FileUtils.readFileToString(fileConfig, StandardCharsets.UTF_8);
		String key = Cipher.decrypt(text);

		PrivateConfig.API_KEY = key.split("::")[0];
		PrivateConfig.SECRET_KEY = key.split("::")[1];

		return key;
	}

	public static void setKey(String apiKey, String secretKey) throws IOException
	{
		File path = new File(Constants.DEFAULT_USER_FOLDER);
		if (!path.exists()) 
		{
			path.mkdirs();
		}

		File fileConfig = new File(path, Constants.PRIVATEKEY_FILENAME);

		String key = apiKey + "::" + secretKey;
		String text = Cipher.encrypt(key);

		FileUtils.writeStringToFile(fileConfig, text, StandardCharsets.UTF_8);

		// ----------------------------------
		PrivateConfig.API_KEY = apiKey;
		PrivateConfig.SECRET_KEY = secretKey;
	}

}
