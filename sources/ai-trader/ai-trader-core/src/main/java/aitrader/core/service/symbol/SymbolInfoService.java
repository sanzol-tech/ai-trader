package aitrader.core.service.symbol;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import aitrader.core.config.CoreConfig;
import aitrader.core.model.Symbol;
import aitrader.core.model.SymbolInfo;
import aitrader.util.observable.Handler;

public class SymbolInfoService
{
	private static Map<String, SymbolInfo> mapSymbolInfo = new ConcurrentHashMap<String, SymbolInfo>();

	public static Map<String, SymbolInfo> getMapSymbolInfo()
	{
		return mapSymbolInfo;
	}

	public static void clean()
	{
		mapSymbolInfo = new ConcurrentHashMap<String, SymbolInfo>();
	}

	public static SymbolInfo getSymbolInfo(String symbolPair)
	{
		return mapSymbolInfo.get(symbolPair);
	}

	public static void update(String symbolPair, SymbolInfo symbolInfo)
	{
		mapSymbolInfo.put(symbolPair, symbolInfo);
	}

	public static Symbol getSymbol(String symbolPair)
	{
		if (!mapSymbolInfo.containsKey(symbolPair))
		{
			return null;
		}

		return mapSymbolInfo.get(symbolPair).getSymbol();
	}

	public static BigDecimal getLastPrice(Symbol symbol)
	{
		if (!mapSymbolInfo.containsKey(symbol.getPair()))
		{
			return BigDecimal.valueOf(-1);
		}

		return mapSymbolInfo.get(symbol.getPair()).getLastPrice();
	}

	// --------------------------------------------------------------------

	public static List<SymbolInfo> getLstSymbolsInfo(boolean onlyFavorites, boolean onlyBetters, boolean onlyStochOk) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<String> lstWhiteList = new ArrayList<String>(Arrays.asList(CoreConfig.getWhiteList().split(",")));		
		List<String> lstBlackList = new ArrayList<String>(Arrays.asList(CoreConfig.getBlackList().split(",")));		

		List<SymbolInfo> lstSymbolsInfo = new ArrayList<SymbolInfo>();

		for (SymbolInfo entry : mapSymbolInfo.values())
		{
			if (onlyFavorites && !lstWhiteList.contains(entry.getSymbol().getNameLeft()))
			{
				continue;
			}

			if (onlyFavorites && lstBlackList.contains(entry.getSymbol().getNameLeft()))
			{
				continue;
			}

			if (onlyBetters)
			{
				boolean isVolumeOK = (entry.getQuoteVolume24h().doubleValue() >= CoreConfig.getMinVolume24h());
				boolean isChangeOK = (Math.abs(entry.getChange24h().doubleValue()) >= CoreConfig.getMinChange24h());

				if (!isVolumeOK || !isChangeOK)
				{
					continue;
				}
			}

			if (onlyStochOk)
			{
				boolean isStochOK = (entry.getStoch24h().doubleValue() >= CoreConfig.getHighStoch24h() || entry.getStoch24h().doubleValue() <= CoreConfig.getLowStoch24h());				

				if (!isStochOK)
				{
					continue;
				}
			}

			lstSymbolsInfo.add(entry);
		}

		Comparator<SymbolInfo> orderComparator = Comparator.comparing(SymbolInfo::getQuoteVolume24h).reversed();
		Collections.sort(lstSymbolsInfo, orderComparator);

		return lstSymbolsInfo;
	}

	public static Set<String> getSymbolNames(boolean onlyFavorites, boolean onlyBetters, boolean onlyStochOk) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		Set<String> list = new LinkedHashSet<String>();

		List<SymbolInfo> lstSymbolsInfo = getLstSymbolsInfo(onlyFavorites, onlyBetters, onlyStochOk);
		for (SymbolInfo entry : lstSymbolsInfo)
		{
			list.add(entry.getSymbol().getPair());
		}

		return list;
	}

	// --------------------------------------------------------------------

	private static List<Handler<Void>> observers = new ArrayList<Handler<Void>>();

	public static void attachObserver(Handler<Void> observer)
	{
		observers.add(observer);
	}

	public static void deattachObserver(Handler<Void> observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllObservers()
	{
		for (Handler<Void> observer : observers)
		{
			observer.handle(null);
		}
	}

}
