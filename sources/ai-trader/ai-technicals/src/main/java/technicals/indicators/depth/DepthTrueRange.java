package technicals.indicators.depth;

import java.math.BigDecimal;
import java.math.RoundingMode;

import technicals.model.OrderBook;
import technicals.model.OrderBookEntry;

/**
 * Depth True Range Volatility indicator calculated based on the order book
 */
public class DepthTrueRange
{
	private OrderBook orderBook;

	public DepthTrueRange(OrderBook orderBook)
	{
		this.orderBook = orderBook;
	}

	public BigDecimal calculate()
	{
		return calculate(0.15, false);
	}

	public BigDecimal calculate(double distance, boolean inPercentage)
	{
		double firstAskPrice = orderBook.getAsks().firstEntry().getValue().getPrice().doubleValue();
		double firstBidPrice = orderBook.getBids().firstEntry().getValue().getPrice().doubleValue();
		double pp = (firstAskPrice + firstBidPrice) / 2;

		double maxPrice = pp * (1 + distance);
		double minPrice = pp * (1 - distance);

		double awa = weightedAverageAsks(maxPrice);
		double awb = weightedAverageBids(minPrice);

		if (inPercentage)
		{
			double dtr = ((awa / awb) - 1) * 100;
			return BigDecimal.valueOf(dtr).setScale(2, RoundingMode.HALF_UP);
		}
		else
		{
			double dtr = awa - awb;
			return BigDecimal.valueOf(dtr).setScale(orderBook.getPricePrecision(), RoundingMode.HALF_UP);
		}
	}

	private double weightedAverageAsks(double maxPrice)
	{
		double sumProd = 0.0;
		double sumQty = 0.0;

		for (OrderBookEntry entry : orderBook.getAsks().values())
		{
			if (entry.getPrice().doubleValue() > maxPrice)
			{
				break;
			}
			sumProd += entry.getPrice().doubleValue() * entry.getQty().doubleValue();
			sumQty += entry.getQty().doubleValue();
		}

		return sumProd / sumQty;
	}

	private double weightedAverageBids(double minPrice)
	{
		double sumProd = 0.0;
		double sumQty = 0.0;

		for (OrderBookEntry entry : orderBook.getBids().values())
		{
			if (entry.getPrice().doubleValue() < minPrice)
			{
				break;
			}
			sumProd += entry.getPrice().doubleValue() * entry.getQty().doubleValue();
			sumQty += entry.getQty().doubleValue();
		}

		return sumProd / sumQty;
	}

}
