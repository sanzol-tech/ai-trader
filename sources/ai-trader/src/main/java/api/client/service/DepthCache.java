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

	private static Map<String, DepthService> cache = new ConcurrentHashMap<String, DepthService>();

	public static boolean containsKey(String pair)
	{
		return cache.containsKey(pair);
	}

	public static DepthService get(String pair)
	{
		return cache.get(pair);
	}

	public static boolean add(String pair)
	{
		if (!cache.containsKey(pair))
		{
			DepthService depthService = DepthService.getInstance(pair, DepthMode.both_force, 0);
			cache.put(pair, depthService);
			return true;
		}
		return false;
	}

	public static boolean remove(String pair)
	{
		if (cache.containsKey(pair))
		{
			DepthService depthService = cache.get(pair);
			depthService.close();
			cache.remove(pair);
			return true;
		}
		return false;
	}

	public static void removeAll()
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
			String pair = symbolInfo.getPair();

			if (add(pair))
			{
				Thread.sleep(300);
			}
		}
	}

}
