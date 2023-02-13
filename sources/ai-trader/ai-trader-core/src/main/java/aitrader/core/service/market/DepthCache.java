package aitrader.core.service.market;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import aitrader.core.config.CoreLog;
import aitrader.core.model.SymbolInfo;
import aitrader.core.model.enums.DepthMode;
import aitrader.core.service.symbol.SymbolInfoService;

public class DepthCache
{
	public static final int MAX_CACHE_SIZE = 20;

	private static Map<String, DepthService> cache = new ConcurrentHashMap<String, DepthService>();

	public static boolean containsKey(String symbolPair)
	{
		return cache.containsKey(symbolPair);
	}

	public static List<DepthService> getLstCache()
	{
		return new ArrayList<DepthService>(cache.values());
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

	public static boolean start()
	{
		try
		{
			List<String> lstSymbols = new ArrayList<String>();

			List<SymbolInfo> topSymbols = SymbolInfoService.getLstSymbolsInfo(false, false, false);
			int maxSize = Math.min(topSymbols.size(), MAX_CACHE_SIZE);
			topSymbols = topSymbols.subList(0, maxSize);

			for (SymbolInfo symbolInfo : topSymbols)
			{
				String symbolPair = symbolInfo.getSymbol().getPair();
				lstSymbols.add(symbolPair);
			}

			return start(lstSymbols);
		}
		catch (Exception e)
		{
			CoreLog.error(e);
			return false;
		}
	}

	public static boolean start(List<String> lstSymbols)
	{
		try
		{
			for (String symbolPair : lstSymbols)
			{
				if (add(symbolPair))
				{
					Thread.sleep(300);
				}
			}

			return true;
		}
		catch (Exception e)
		{
			CoreLog.error(e);
			return false;
		}
	}

}
