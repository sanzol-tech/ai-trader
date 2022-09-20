package sanzol.aitrader.be.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sanzol.aitrader.be.enums.DepthMode;
import sanzol.aitrader.be.model.SymbolInfo;

public class DepthCache
{
	public static final int MAX_CACHE_SIZE = 20;

	private static Map<String, DepthService> cache = new ConcurrentHashMap<String, DepthService>();

	public static boolean containsKey(String symbolPair)
	{
		return cache.containsKey(symbolPair);
	}

	public static DepthService get(String symbolPair)
	{
		return cache.get(symbolPair);
	}

	public static boolean add(String symbolPair)
	{
		if (!cache.containsKey(symbolPair))
		{
			DepthService depthService = DepthService.getInstance(symbolPair, DepthMode.both_force, 0);
			cache.put(symbolPair, depthService);
			return true;
		}
		return false;
	}

	public static boolean remove(String symbolPair)
	{
		if (cache.containsKey(symbolPair))
		{
			DepthService depthService = cache.get(symbolPair);
			depthService.close();
			cache.remove(symbolPair);
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
			String symbolPair = symbolInfo.getSymbolPair();

			if (add(symbolPair))
			{
				Thread.sleep(300);
			}
		}
	}

}
