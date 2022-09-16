package api.client.sync;

import java.util.List;

import api.client.enums.MarketType;
import api.client.futures.enums.IntervalType;
import api.client.model.sync.Depth;
import api.client.model.sync.ExchangeInfo;
import api.client.model.sync.Kline;
import api.client.model.sync.SymbolTicker;

public interface SyncRequestClient
{

	public static SyncRequestClient getInstance(MarketType marketType)
	{
		if (marketType == MarketType.futures)
			return null;
		else
			return null;
	}

	ExchangeInfo getExchangeInformation();

	List<SymbolTicker> getSymbolTickers();

	Depth getDepth(String symbol, Integer limit);

	List<Kline> getKlines(String symbol, IntervalType interval, int limit);

}