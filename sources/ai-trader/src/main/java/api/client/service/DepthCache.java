package api.client.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import api.client.enums.DepthMode;
import sanzol.app.model.SymbolInfo;

public class DepthCache
{
	public static final int MAX_CACHE_SIZE = 20;
	public static final String[] CACHEABLE_SYMBOLS = new String[] { "BNB", "BTC", "DOT", "ETH", "SOL" };

	private static Map<String, DepthService> cache = new ConcurrentHashMap<String, DepthService>();

	public static boolean containsKey(String pair)
	{
		return cache.containsKey(pair);
	}

	public static DepthService get(String pair)
	{
		return cache.get(pair);
	}

	public static void put(String pair, DepthService depthClient)
	{
		cache.put(pair, depthClient);
	}

	public static void clean()
	{
		for (Map.Entry<String, DepthService> entry : cache.entrySet())
		{
			entry.getValue().close();
			cache.remove(entry.getKey());
		}
	}

	public static void start() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<SymbolInfo> topSymbols = PriceService.getLstSymbolsInfo(false, false);
		int maxSize = Math.min(topSymbols.size(), MAX_CACHE_SIZE);
		topSymbols = topSymbols.subList(0, maxSize);

		for (SymbolInfo symbolInfo : topSymbols)
		{
			String pair = symbolInfo.getSymbolName();

			if (!cache.containsKey(pair))
			{
				DepthService depthClient = DepthService.getInstance(pair, DepthMode.both_force, 0);
				cache.put(pair, depthClient);

				Thread.sleep(300);
			}
		}
	}

}
