package api.client.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import api.client.config.ApiConfig;
import api.client.enums.MarketType;
import api.client.futures.sync.SyncFuturesClient;
import api.client.model.sync.ExchangeInfo;
import api.client.model.sync.ExchangeInfoEntry;
import api.client.spot.sync.SyncSpotClient;
import sanzol.app.config.Config;

public class ExchangeInfoService
{
	private static Map<String, ExchangeInfoEntry> mapExchangeInfo = new ConcurrentHashMap<String, ExchangeInfoEntry>();
	private static long time;

	public static void start() throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		ExchangeInfo exchangeInfo;
		if (ApiConfig.MARKET_TYPE == MarketType.futures)
			exchangeInfo = SyncFuturesClient.getExchangeInformation();
		else
			exchangeInfo = SyncSpotClient.getExchangeInformation();

		List<ExchangeInfoEntry> lstExchangeInfoEntries = exchangeInfo.getSymbols();
		time = System.currentTimeMillis();
		
		for (ExchangeInfoEntry entry : lstExchangeInfoEntries)
		{
			if (entry.getQuoteAsset().equalsIgnoreCase(Config.DEFAULT_SYMBOL_RIGHT))
			{
				mapExchangeInfo.put(entry.getSymbol(), entry);
			}
		}
	}

	public static ExchangeInfoEntry getExchangeInfo(String pair) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		if (!pair.endsWith(Config.DEFAULT_SYMBOL_RIGHT))
		{
			return null;
		}

		if (time + TimeUnit.HOURS.toMillis(4) < System.currentTimeMillis())
		{
			mapExchangeInfo = new ConcurrentHashMap<String, ExchangeInfoEntry>();
			start();
		}

		return mapExchangeInfo.get(pair);
	}	

}
