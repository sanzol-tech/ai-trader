package technicals.indicators.depth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import technicals.model.OrderBook;
import technicals.model.OrderBookEntry;

/**
 * Search for the n-prices with the largest number of orders 
 */
public class DepthSuperPrices 
{
	private OrderBook orderBook;

	private List<OrderBookEntry> asks;
	private List<OrderBookEntry> bids;

	public DepthSuperPrices(OrderBook orderBook)
	{
		this.orderBook = orderBook;
	}

	public List<OrderBookEntry> getAsks()
	{
		return asks;
	}

	public List<OrderBookEntry> getBids()
	{
		return bids;
	}

	public DepthSuperPrices searchSuperAskPrices(int maxSize)
	{
		double firstPrice = orderBook.getAsks().firstEntry().getValue().getPrice().doubleValue();
		double maxPrice = firstPrice * 5;

		asks = new ArrayList<OrderBookEntry>();

		for (OrderBookEntry e : orderBook.getAsks().values())
		{
			if (e.getPrice().doubleValue() > maxPrice)
			{
				break;
			}
			asks.add(new OrderBookEntry(e.getPrice(), e.getQty()));
		}

		Collections.sort(asks, Comparator.comparing(OrderBookEntry::getQty).reversed());
		asks = asks.stream().limit(maxSize).collect(Collectors.toList());
		Collections.sort(asks, Comparator.comparing(OrderBookEntry::getPrice));

		return this;
	}

	public DepthSuperPrices searchSuperBidPrices(long maxSize)
	{
		double firstPrice = orderBook.getAsks().firstEntry().getValue().getPrice().doubleValue();
		double minPrice = firstPrice * 0.2;

		bids = new ArrayList<OrderBookEntry>();

		for (OrderBookEntry e : orderBook.getBids().values())
		{
			if (e.getPrice().doubleValue() < minPrice)
			{
				break;
			}
			bids.add(new OrderBookEntry(e.getPrice(), e.getQty()));
		}

		Collections.sort(bids, Comparator.comparing(OrderBookEntry::getQty).reversed());
		bids = bids.stream().limit(maxSize).collect(Collectors.toList());
		Collections.sort(bids, Comparator.comparing(OrderBookEntry::getPrice).reversed());

		return this;
	}

}
