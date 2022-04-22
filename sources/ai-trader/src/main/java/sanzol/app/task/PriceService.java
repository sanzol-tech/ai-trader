package sanzol.app.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.binance.client.SubscriptionClient;
import com.binance.client.model.event.SymbolTickerEvent;

import sanzol.app.config.Constants;
import sanzol.app.model.Symbol;

public class PriceService
{
	private static SubscriptionClient client = SubscriptionClient.create();

	private static Map<String, SymbolTickerEvent> mapTickers;

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

}
