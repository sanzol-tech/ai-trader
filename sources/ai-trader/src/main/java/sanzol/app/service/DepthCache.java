package sanzol.app.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import api.client.futures.async.DepthClient;
import sanzol.app.model.SymbolInfo;

public class DepthCache
{
	public static final int MAX_CACHE_SIZE = 20;
	public static final String[] CACHEABLE_SYMBOLS = new String[] { "BNB", "BTC", "DOT", "ETH", "SOL" };

	private static Map<String, DepthClient> cache = new ConcurrentHashMap<String, DepthClient>();

	public static boolean containsKey(String pair)
	{
		return cache.containsKey(pair);
	}

	public static DepthClient get(String pair)
	{
		return cache.get(pair);
	}

	public static void put(String pair, DepthClient depthClient)
	{
		cache.put(pair, depthClient);
	}

	public static void clean()
	{
		for (Map.Entry<String, DepthClient> entry : cache.entrySet())
		{
			entry.getValue().close();
			cache.remove(entry.getKey());
		}
	}

	public static void start() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<SymbolInfo> topSymbols = Symbol.getLstSymbolsInfo(false, false);
		int maxSize = Math.min(topSymbols.size(), MAX_CACHE_SIZE);
		topSymbols = topSymbols.subList(0, maxSize);

		for (SymbolInfo symbolInfo : topSymbols)
		{
			String pair = symbolInfo.getSymbolName();

			if (!cache.containsKey(pair))
			{
				DepthClient depthClient = DepthClient.getInstance(pair, DepthClient.DepthMode.both_force, 0);
				cache.put(pair, depthClient);

				Thread.sleep(300);
			}
		}
	}

}
