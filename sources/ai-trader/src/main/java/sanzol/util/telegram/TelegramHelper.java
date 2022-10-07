package sanzol.util.telegram;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.ws.rs.core.UriBuilder;

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
		try
		{
			URI uri = UriBuilder.fromUri(String.format(TELEGRAM_BASE_URL + "/bot%s/getUpdates", apiToken))
				.build();

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
		try
		{
			URI uri = UriBuilder.fromUri(String.format(TELEGRAM_BASE_URL + "/bot%s/sendMessage", apiToken))
				.queryParam("chat_id", chatId)
				.queryParam("text", message)
				.build();

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
			LogService.error(e);
			return e.getMessage();
		}
	}

	public static void main(String[] args) throws IOException
	{
		TelegramConfig.load();
		//String result = sendMessage("hello !!!");
		String result = getUpdates(TelegramConfig.getApiToken());
		System.out.println(result);
	}

}
