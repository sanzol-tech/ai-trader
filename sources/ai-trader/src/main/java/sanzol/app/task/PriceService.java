package sanzol.app.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.binance.client.SubscriptionClient;
import com.binance.client.model.event.SymbolTickerEvent;

import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.listener.PriceListener;
import sanzol.app.service.Symbol;

public final class PriceService
{
	private static SubscriptionClient client = SubscriptionClient.create();

	private static Set<String> setFavorites = new LinkedHashSet<String>();
	private static Map<String, SymbolTickerEvent> mapTickers = new HashMap<String, SymbolTickerEvent>();

	public static String btcLabel() throws Exception
	{
		String symbolName = "BTC" + Constants.DEFAULT_SYMBOL_RIGHT;
		Symbol coin = Symbol.getInstance(symbolName);

		if (!mapTickers.containsKey(coin.getName()))
		{
			return "n/a";
		}

		BigDecimal price = mapTickers.get(symbolName).getLastPrice();
		BigDecimal priceChange = mapTickers.get(symbolName).getPriceChangePercent();
		String label = String.format("BTC  %s  %.2f %%", coin.priceToStr(price), priceChange);
		return label;
	}

	public static BigDecimal getLastPrice(Symbol coin)
	{
		if (!mapTickers.containsKey(coin.getName()))
		{
			return BigDecimal.valueOf(-1);
		}

		return mapTickers.get(coin.getName()).getLastPrice();
	}

	public static SymbolTickerEvent getSymbolTickerEvent(Symbol coin)
	{
		if (!mapTickers.containsKey(coin.getName()))
		{
			return null;
		}

		return mapTickers.get(coin.getName());
	}

	public static Map<String, SymbolTickerEvent> getMapTickers()
	{
		return mapTickers;
	}

	public static void start()
	{
		loadFavorites();

		mapTickers = new HashMap<String, SymbolTickerEvent>();

		client.subscribeAllTickerEvent(((event) -> {
			List<SymbolTickerEvent> lstSymbolTickerEvent = event;
			for (SymbolTickerEvent entry : lstSymbolTickerEvent)
			{
				if (entry.getSymbol().endsWith(Constants.DEFAULT_SYMBOL_RIGHT))
				{
					mapTickers.put(entry.getSymbol(), entry);
				}
			}

			notifyAllLogObservers();

		}), null);
	}

	public static void stop()
	{
		client.unsubscribeAll();
	}

	public static List<String> getSymbols(boolean onlyFavorites)
	{
		List<String> list = new ArrayList<String>();
		for (SymbolTickerEvent entry : mapTickers.values())
		{
			if (onlyFavorites && !setFavorites.contains(Symbol.getRightSymbol(entry.getSymbol())))
			{
				continue;
			}
			if (!entry.getSymbol().endsWith(Constants.DEFAULT_SYMBOL_RIGHT))
			{
				continue;
			}

			Symbol coin = Symbol.getInstance(entry.getSymbol());
			list.add(String.format("%-8s %10s %8.2f%%", coin.getNameLeft(), coin.priceToStr(entry.getLastPrice()), entry.getPriceChangePercent()));
		}
		Collections.sort(list);
		return list;
	}

	private static void loadFavorites()
	{
		setFavorites = new LinkedHashSet<String>();
		for (String item : Config.getLstFavSymbols())
		{
			setFavorites.add(item);
		}
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

	public static void main(String[] args) throws InterruptedException
	{
		start();

		Thread.sleep(3000);

		System.out.println(getSymbols(true));
	}

}
