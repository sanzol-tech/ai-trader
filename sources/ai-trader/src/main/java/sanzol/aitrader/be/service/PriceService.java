package sanzol.aitrader.be.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import api.client.futures.impl.SyncFuturesClient;
import api.client.impl.async.WsMarketTickers;
import api.client.impl.config.ApiConfig;
import api.client.impl.model.enums.MarketType;
import api.client.model.SymbolTicker;
import api.client.model.event.SymbolTickerEvent;
import api.client.spot.impl.SyncSpotClient;
import sanzol.aitrader.be.config.Config;
import sanzol.aitrader.be.model.Symbol;
import sanzol.aitrader.be.model.SymbolInfo;
import sanzol.util.log.LogService;
import sanzol.util.price.PriceUtil;

public class PriceService
{
	private static WsMarketTickers wsSymbolTicker;

	private static Map<String, SymbolTickerEvent> mapTickers = new ConcurrentHashMap<String, SymbolTickerEvent>();

	public static BigDecimal getLastPrice(Symbol symbol)
	{
		if (!mapTickers.containsKey(symbol.getPair()))
		{
			return BigDecimal.valueOf(-1);
		}

		return mapTickers.get(symbol.getPair()).getLastPrice();
	}

	public static SymbolTickerEvent getSymbolTickerEvent(Symbol symbol)
	{
		if (!mapTickers.containsKey(symbol.getPair()))
		{
			return null;
		}

		return mapTickers.get(symbol.getPair());
	}

	public static Map<String, SymbolTickerEvent> getMapTickers()
	{
		return mapTickers;
	}

	// ------------------------------------------------------------------------

	public static List<SymbolInfo> getLstSymbolsInfo(boolean onlyFavorites, boolean onlyBetters) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<String> lstFavorites = Config.getLstFavSymbols();

		List<SymbolInfo> lstSymbolsInfo = new ArrayList<SymbolInfo>();

		List<SymbolTickerEvent> lstSymbolTickerEvent = new ArrayList<SymbolTickerEvent>();
		lstSymbolTickerEvent.addAll(mapTickers.values());

		for (SymbolTickerEvent entry : lstSymbolTickerEvent)
		{
			SymbolInfo symbolInfo = SymbolInfo.getInstance(entry);

			if (symbolInfo == null)
			{
				continue;
			}

			if (onlyFavorites && !lstFavorites.contains(symbolInfo.getSymbol().getNameLeft()))
			{
				continue;
			}

			if (onlyBetters && (symbolInfo.isLowVolume() || symbolInfo.isHighMove()))
			{
				continue;
			}

			lstSymbolsInfo.add(symbolInfo);
		}

		Comparator<SymbolInfo> orderComparator = Comparator.comparing(SymbolInfo::getUsdVolume).reversed();
		Collections.sort(lstSymbolsInfo, orderComparator);

		return lstSymbolsInfo;
	}

	public static List<SymbolInfo> getLstSymbolsInfo(boolean onlyFavorites, boolean onlyBetters, int limit) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		return getLstSymbolsInfo(onlyFavorites, onlyBetters).subList(0, limit - 1);
	}

	public static List<String> getLstSymbolsMini(boolean onlyFavorites, boolean onlyBetters) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<String> list = new ArrayList<String>();
		list.add(String.format("%-10s %12s %10s %10s", "SYMBOL", "PRICE", "VOLUME", "CHANGE"));

		List<SymbolInfo> lstSymbolsInfo = getLstSymbolsInfo(onlyFavorites, onlyBetters);
		for (SymbolInfo entry : lstSymbolsInfo)
		{
			Symbol symbol = entry.getSymbol();
			list.add(String.format("%-10s %12s %10s %8.2f %%", symbol.getNameLeft(), symbol.priceToStr(entry.getLastPrice()), PriceUtil.cashFormat(entry.getUsdVolume()), entry.getPriceChangePercent()));
		}

		return list;
	}

	public static List<String> getLstSymbolNames(boolean onlyFavorites, boolean onlyBetters) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<String> list = new ArrayList<String>();

		List<SymbolInfo> lstSymbolsInfo = getLstSymbolsInfo(onlyFavorites, onlyBetters);
		for (SymbolInfo entry : lstSymbolsInfo)
		{
			list.add(entry.getSymbol().getPair());
		}

		return list;
	}

	// ------------------------------------------------------------------------

	private static List<PriceListener> observers = new ArrayList<PriceListener>();

	public static void attachRefreshObserver(PriceListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(PriceListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers()
	{
		for (PriceListener observer : observers)
		{
			observer.onPriceUpdate();
		}
	}

	// ------------------------------------------------------------------------

	public static void onMessage(SymbolTickerEvent[] event)
	{
		for (SymbolTickerEvent entry : event)
		{
			if (entry.getSymbol().endsWith(Config.DEFAULT_SYMBOL_RIGHT))
			{
				mapTickers.put(entry.getSymbol(), entry);
			}
		}

		/*
		for (SymbolTickerEvent entry : mapTickers.values())
		{
			System.out.println(entry.getSymbol() + " : " + entry.getLastPrice());
		}
		*/

		notifyAllLogObservers();
	}

	// ------------------------------------------------------------------------

	public static boolean start()
	{
		try
		{
			// ---- GET SNAPSHOOT ---------------------------------------------
			List<SymbolTicker> lstSymbolTickers;
			if (ApiConfig.MARKET_TYPE == MarketType.futures)
				lstSymbolTickers = SyncFuturesClient.getSymbolTickers();
			else
				lstSymbolTickers = SyncSpotClient.getSymbolTickers();

			for (SymbolTicker entry : lstSymbolTickers)
			{
				if (entry.getSymbol().endsWith(Config.DEFAULT_SYMBOL_RIGHT))
				{
					mapTickers.put(entry.getSymbol(), entry.toSymbolTickerEvent());
				}
			}

			// ----------------------------------------------------------------

			wsSymbolTicker = WsMarketTickers.create((event) -> {
				onMessage(event);
			});
			wsSymbolTicker.connect();

			return true;
		}
		catch (Exception e)
		{
			LogService.error(e);
			return false;
		}
	}

	public static void close()
	{
		wsSymbolTicker.close();

		mapTickers = new ConcurrentHashMap<String, SymbolTickerEvent>();

		notifyAllLogObservers();
	}

	public static void main(String[] args) throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException
	{
		ApiConfig.setFutures();
		start();
	}

}