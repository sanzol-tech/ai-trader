package sanzol.app.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.binance.client.SubscriptionClient;
import com.binance.client.model.event.SymbolTickerEvent;

import sanzol.app.config.Config;
import sanzol.app.listener.PriceListener;
import sanzol.app.service.Symbol;

public final class PriceService
{
	private static SubscriptionClient client = SubscriptionClient.create();

	private static Set<String> setFavorites = new LinkedHashSet<String>();
	private static Map<String, SymbolTickerEvent> mapTickers = new ConcurrentHashMap<String, SymbolTickerEvent>();

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

		mapTickers = new ConcurrentHashMap<String, SymbolTickerEvent>();

		client.subscribeAllTickerEvent(((event) -> {
			List<SymbolTickerEvent> lstSymbolTickerEvent = event;
			for (SymbolTickerEvent entry : lstSymbolTickerEvent)
			{
				if (entry.getSymbol().endsWith(Config.DEFAULT_SYMBOL_RIGHT))
				{
					mapTickers.put(entry.getSymbol(), entry);
				}
			}

			notifyAllLogObservers();

		}), System.err::println);
	}

	public static void stop()
	{
		client.unsubscribeAll();
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
		System.out.println("...");
	}

}
