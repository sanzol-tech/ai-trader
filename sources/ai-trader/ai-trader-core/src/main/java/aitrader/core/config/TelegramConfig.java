package aitrader.core.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import aitrader.util.Properties;

public class TelegramConfig
{
	private static String apiToken;
	private static String chatId;

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
		Path path = Paths.get(CoreConstants.DEFAULT_USER_FOLDER, CoreConstants.TELEGRAM_FILENAME);
		if (!path.toFile().exists())
		{
			CoreLog.error("File " + CoreConstants.TELEGRAM_FILENAME + " does not exist");
			return false;
		}

		Properties props = Properties.create(true).load(path);
		apiToken = props.getValue("telegramApiToken");
		chatId = props.getValue("telegramChatId");

		return true;
	}

	public static void save() throws IOException
	{
		Path path = Paths.get(CoreConstants.DEFAULT_USER_FOLDER, CoreConstants.TELEGRAM_FILENAME);
		Properties.create(true)
			.put("telegramApiToken", apiToken)
			.put("telegramChatId", chatId)
			.save(path);
	}

}
