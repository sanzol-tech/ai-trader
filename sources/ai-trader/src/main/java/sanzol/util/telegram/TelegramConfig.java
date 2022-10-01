package sanzol.util.telegram;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import api.client.impl.config.ApiConfig;
import sanzol.aitrader.be.config.Constants;
import sanzol.util.Properties;
import sanzol.util.log.LogService;

public class TelegramConfig
{
	public static String apiToken;
	public static String chatId;

	public static boolean isLoaded()
	{
		return apiToken != null && !apiToken.isEmpty() && chatId != null && !chatId.isEmpty();
	}
	
	public static String getApiToken()
	{
		return apiToken;
	}

	public static void setApiToken(String apiToken)
	{
		TelegramConfig.apiToken = apiToken;
	}

	public static String getChatId()
	{
		return chatId;
	}

	public static void setChatId(String chatId)
	{
		TelegramConfig.chatId = chatId;
	}

	public static boolean load() throws IOException
	{
		Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString(), Constants.TELEGRAM_FILENAME);
		if (!path.toFile().exists())
		{
			LogService.error("File " + Constants.TELEGRAM_FILENAME + " does not exist");
			return false;
		}

		Properties props = Properties.create(true).load(path);
		apiToken = props.getValue("telegramApiToken");
		chatId = props.getValue("telegramChatId");

		return true;
	}

	public static void save() throws IOException
	{
		Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString(), Constants.TELEGRAM_FILENAME);
		Properties.create(true)
			.put("telegramApiToken", apiToken)
			.put("telegramChatId", chatId)
			.save(path);
	}

	public static void main(String[] args)
	{
		/*
		try
		{
			telegramApiToken = "xxxx";
			telegramChatId = "yyyy";
			save();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		*/
	}

}
