package sanzol.util.telegram;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import sanzol.util.log.LogService;

public final class TelegramHelper
{
	private static final String TELEGRAM_BASE_URL = "https://api.telegram.org";

	private TelegramHelper()
	{
		// Hide
	}

	public static synchronized String getUpdates(String apiToken)
	{
		String urlString = String.format(TELEGRAM_BASE_URL + "/bot%s/getUpdates", apiToken);

		try
		{
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			InputStream is = new BufferedInputStream(conn.getInputStream());
			String result = IOUtils.toString(is, StandardCharsets.UTF_8);
			return result;
		}
		catch (Exception e)
		{
			LogService.error(e);
			return e.getMessage();
		}
	}

	public static synchronized String sendMessage(String message)
	{
		if (TelegramConfig.isLoaded())
			return sendMessage(TelegramConfig.getApiToken(), TelegramConfig.getChatId(), message);
		else
			return null;
	}

	public static synchronized String sendMessage(String apiToken, String chatId, String message)
	{
		String urlString = String.format(TELEGRAM_BASE_URL + "/bot%s/sendMessage?chat_id=%s&text=%s", apiToken, chatId, message);

		try
		{
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			InputStream is = new BufferedInputStream(conn.getInputStream());
			String result = IOUtils.toString(is, StandardCharsets.UTF_8);
			return result;
		}
		catch (Exception e)
		{
			LogService.error(e);
			return e.getMessage();
		}
	}

	public static void main(String[] args) throws IOException
	{
		TelegramConfig.load();
		String result = sendMessage("hello");
		System.out.println(result);
	}

}
