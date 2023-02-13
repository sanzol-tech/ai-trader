package technicals.indicators.depth;

import java.math.BigDecimal;
import java.math.RoundingMode;

import technicals.model.OrderBook;
import technicals.model.OrderBookEntry;

/**
 * Calculate the weighted average price of the union between ask and bid at a given depth
 */
public class DepthMiddlePrice
{
	private OrderBook orderBook;

	public DepthMiddlePrice(OrderBook orderBook)
	{
		this.orderBook = orderBook;
	}

	public BigDecimal calculate()
	{
		return calculate(0.15, false);
	}

	public BigDecimal calculate(double distance, boolean quoted)
	{
		double firstAskPrice = orderBook.getAsks().firstEntry().getValue().getPrice().doubleValue();
		double firstBidPrice = orderBook.getBids().firstEntry().getValue().getPrice().doubleValue();		
		double pp = (firstAskPrice + firstBidPrice) / 2;
		
		double maxPrice = pp * (1 + distance);
		double minPrice = pp * (1 - distance);

		double askSumProd = 0.0;
		double askSumQty = 0.0;
		double bidSumProd = 0.0;
		double bidSumQty = 0.0;

		for (OrderBookEntry entry : orderBook.getAsks().values())
		{
			if (entry.getPrice().doubleValue() > maxPrice)
			{
				break;
			}
			double qty = (quoted) ? entry.getUsd().doubleValue() : entry.getQty().doubleValue();
			askSumProd += entry.getPrice().doubleValue() * qty;
			askSumQty += qty;
		}

		for (OrderBookEntry entry : orderBook.getBids().values())
		{
			if (entry.getPrice().doubleValue() < minPrice)
			{
				break;
			}
			double qty = (quoted) ? entry.getUsd().doubleValue() : entry.getQty().doubleValue();
			bidSumProd += entry.getPrice().doubleValue() * qty;
			bidSumQty += qty;
		}

		double dmp = (askSumProd + bidSumProd) / (askSumQty + bidSumQty);

		return BigDecimal.valueOf(dmp).setScale(orderBook.getPricePrecision(), RoundingMode.HALF_UP);
	}

}
