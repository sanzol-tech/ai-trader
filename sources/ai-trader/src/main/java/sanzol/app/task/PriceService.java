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
import sanzol.app.model.Symbol;

public class PriceService
{
	private static SubscriptionClient client = SubscriptionClient.create();

	private static Set<String> setFavorites = new LinkedHashSet<String>();
	private static Map<String, SymbolTickerEvent> mapTickers = new HashMap<String, SymbolTickerEvent>();

	public static BigDecimal getLastPrice(Symbol coin) throws Exception
	{
		if (!mapTickers.containsKey(coin.getName()))
		{
			return BigDecimal.valueOf(-1);
		}

		return mapTickers.get(coin.getName()).getLastPrice();
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
			
			String symbolName = Symbol.getRightSymbol(entry.getSymbol());
			list.add(String.format("%-8s %6.2f%%", symbolName, entry.getPriceChangePercent()));
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

	public static void main(String[] args) throws InterruptedException
	{
		start();

		Thread.sleep(3000);

		System.out.println(getSymbols(true));
	}

}
