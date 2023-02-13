package technicals.indicators.depth;

import technicals.model.OrderBook;
import technicals.model.OrderBookEntry;

/**
 * Calculate delta between ask and bid at a given depth
 */
public class DepthDelta
{
	private OrderBook orderBook;

	private double askSumQty;
	private double askSumQuoted;
	private double bidSumQty;
	private double bidSumQuoted;
	private double delta;
	private double deltaPercent;
	private double deltaQuoted;
	private double deltaQuotedPercent;

	public DepthDelta(OrderBook orderBook)
	{
		this.orderBook = orderBook;
	}

	public double getAskSumQty()
	{
		return askSumQty;
	}

	public double getAskSumQuoted()
	{
		return askSumQuoted;
	}

	public double getBidSumQty()
	{
		return bidSumQty;
	}

	public double getBidSumQuoted()
	{
		return bidSumQuoted;
	}

	public double getDelta()
	{
		return delta;
	}

	public double getDeltaPercent()
	{
		return deltaPercent;
	}

	public double getDeltaQuoted()
	{
		return deltaQuoted;
	}

	public double getDeltaQuotedPercent()
	{
		return deltaQuotedPercent;
	}

	public DepthDelta calculate(Double distance)
	{
		double firstAskPrice = orderBook.getAsks().firstEntry().getValue().getPrice().doubleValue();
		double firstBidPrice = orderBook.getBids().firstEntry().getValue().getPrice().doubleValue();
		double pp = (firstAskPrice + firstBidPrice) / 2;

		double maxPrice = pp * (distance != null ? (1 + distance) : 5);
		double minPrice = pp * (distance != null ? (1 - distance) : 0.2);

		askSumQty = 0.0;
		askSumQuoted = 0.0;
		bidSumQty = 0.0;
		bidSumQuoted = 0.0;

		for (OrderBookEntry entry : orderBook.getAsks().values())
		{
			if (entry.getPrice().doubleValue() > maxPrice)
			{
				break;
			}
			askSumQty += entry.getQty().doubleValue();
			askSumQuoted += entry.getUsd().doubleValue();
		}

		for (OrderBookEntry entry : orderBook.getBids().values())
		{
			if (entry.getPrice().doubleValue() < minPrice)
			{
				break;
			}
			bidSumQty += entry.getQty().doubleValue();
			bidSumQuoted += entry.getUsd().doubleValue();
		}

		delta = askSumQty - bidSumQty;
		deltaPercent = ((bidSumQty - askSumQty) / (askSumQty + bidSumQty)) * 100;
		deltaQuoted = askSumQuoted - bidSumQuoted;
		deltaQuotedPercent = ((bidSumQuoted - askSumQuoted) / (askSumQuoted + bidSumQuoted)) * 100;
		
		return this;
	}

}
