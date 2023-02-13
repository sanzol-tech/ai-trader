package exchanges.binance;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import technicals.model.OrderBook;
import technicals.model.OrderBookEntry;
import technicals.model.TechCandle;

public class BinanceUtils
{

	public static TechCandle[] toCandleArray(List<BinanceCandle> lstCandles)
	{
		TechCandle[] candle = new TechCandle[lstCandles.size()];
		for (int i = 0; i < lstCandles.size(); i++)
		{
			candle[i] = new technicals.model.TechCandle();
			candle[i].setOpenTime(lstCandles.get(i).getOpenTimeZoned());
			candle[i].setOpenPrice(lstCandles.get(i).getOpenPrice().doubleValue());
			candle[i].setClosePrice(lstCandles.get(i).getClosePrice().doubleValue());
			candle[i].setHighPrice(lstCandles.get(i).getHighPrice().doubleValue());
			candle[i].setLowPrice(lstCandles.get(i).getLowPrice().doubleValue());
			candle[i].setVolume(lstCandles.get(i).getVolume().doubleValue());
			candle[i].setQuoteVolume(lstCandles.get(i).getQuoteVolume().doubleValue());
			candle[i].setCount(lstCandles.get(i).getCount());
		}
		return candle;
	}
	
	public static OrderBook toOrderBook(Depth depth, int precision)
	{
		OrderBook orderBook = new OrderBook(precision);
		orderBook.setAsks(toMapAsks(depth.getAsks()));
		orderBook.setBids(toMapBids(depth.getBids()));
		return orderBook;
	}

	private static TreeMap<BigDecimal, OrderBookEntry> toMapAsks(List<List<BigDecimal>> asks)
	{
		TreeMap<BigDecimal, OrderBookEntry> map = new TreeMap<BigDecimal, OrderBookEntry>();
		for (List<BigDecimal> entry : asks)
		{
			BigDecimal price = entry.get(0).stripTrailingZeros();
			BigDecimal qty = entry.get(1).stripTrailingZeros();

			map.put(price, new OrderBookEntry(price, qty));
		}
		return map;
	}

	private static TreeMap<BigDecimal, OrderBookEntry> toMapBids(List<List<BigDecimal>> bids)
	{
		TreeMap<BigDecimal, OrderBookEntry> map = new TreeMap<BigDecimal, OrderBookEntry>(Collections.reverseOrder());
		for (List<BigDecimal> entry : bids)
		{
			BigDecimal price = entry.get(0).stripTrailingZeros();
			BigDecimal qty = entry.get(1).stripTrailingZeros();
			map.put(price, new OrderBookEntry(price, qty));
		}
		return map;
	}

}
