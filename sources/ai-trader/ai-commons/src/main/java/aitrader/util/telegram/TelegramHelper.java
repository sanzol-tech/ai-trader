package aitrader.util.telegram;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aitrader.util.ExceptionUtils;

public final class TelegramHelper
{
	private static final Logger LOG = LoggerFactory.getLogger(TelegramHelper.class);

	private static final String TELEGRAM_BASE_URL = "https://api.telegram.org";

	private TelegramHelper()
	{
		// Hide
	}

	public static synchronized String getUpdates(String apiToken)
	{
		try
		{
			URI uri = URI.create(String.format(TELEGRAM_BASE_URL + "/bot%s/getUpdates", apiToken)); 

			HttpClient httpClient = HttpClient.newBuilder().build();
			HttpRequest request = HttpRequest.newBuilder()
	            .GET()
	            .uri(uri)
	            .build();

			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
			
			if (response.statusCode() != 200)
			{
				throw new Exception(response.body()); 
			}

			return response.body();
		}
		catch (Exception e)
		{
			LOG.error("getUpdates failed", e);
			return ExceptionUtils.getMessage(e);
		}
	}

	public static synchronized String sendMessage(String apiToken, String chatId, String message)
	{
		try
		{
			URI uri = URI.create(String.format(TELEGRAM_BASE_URL + "/bot%s/sendMessage?chat_id=%s&text=%s", apiToken, chatId, URLEncoder.encode(message, "UTF-8"))); 

			HttpClient httpClient = HttpClient.newBuilder().build();
			HttpRequest request = HttpRequest.newBuilder()
	            .GET()
	            .uri(uri)
	            .build();

			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
			
			if (response.statusCode() != 200)
			{
				throw new Exception(response.body()); 
			}

			return response.body();
		}
		catch (Exception e)
		{
			LOG.error("sendMessage failed", e);
			return ExceptionUtils.getMessage(e);
		}
	}

}
