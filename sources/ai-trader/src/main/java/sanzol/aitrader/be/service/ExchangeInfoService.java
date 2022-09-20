package sanzol.aitrader.be.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import api.client.futures.impl.SyncFuturesClient;
import api.client.impl.config.ApiConfig;
import api.client.impl.model.enums.MarketType;
import api.client.model.ExchangeInfo;
import api.client.model.ExchangeInfoEntry;
import api.client.spot.impl.SyncSpotClient;
import sanzol.aitrader.be.config.Config;

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
			if (entry.getQuoteAsset().equalsIgnoreCase(Config.DEFAULT_SYMBOL_RIGHT) && "TRADING".equalsIgnoreCase(entry.getStatus()))
			{
				mapExchangeInfo.put(entry.getSymbol(), entry);
			}
		}
	}

	public static ExchangeInfoEntry getExchangeInfo(String symbolPair) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		if (!symbolPair.endsWith(Config.DEFAULT_SYMBOL_RIGHT))
		{
			return null;
		}

		if (time + TimeUnit.HOURS.toMillis(4) < System.currentTimeMillis())
		{
			mapExchangeInfo = new ConcurrentHashMap<String, ExchangeInfoEntry>();
			start();
		}

		return mapExchangeInfo.get(symbolPair);
	}

}
