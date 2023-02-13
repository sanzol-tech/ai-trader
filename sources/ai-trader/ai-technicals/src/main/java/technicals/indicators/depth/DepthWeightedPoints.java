package technicals.indicators.depth;

import java.math.BigDecimal;
import java.math.RoundingMode;

import technicals.model.OrderBook;
import technicals.model.OrderBookEntry;

/**
 * Calculate the weighted average price at a given distance for asks and bids
 */
public class DepthWeightedPoints
{
	private OrderBook orderBook;

	private BigDecimal r1;
	private BigDecimal s1;
	private BigDecimal r2;
	private BigDecimal s2;
	private BigDecimal r3;
	private BigDecimal s3;

	public DepthWeightedPoints(OrderBook orderBook)
	{
		this.orderBook = orderBook;
	}

	public BigDecimal getR1()
	{
		return r1;
	}

	public BigDecimal getS1()
	{
		return s1;
	}

	public BigDecimal getR2()
	{
		return r2;
	}

	public BigDecimal getS2()
	{
		return s2;
	}

	public BigDecimal getR3()
	{
		return r3;
	}

	public BigDecimal getS3()
	{
		return s3;
	}

	public DepthWeightedPoints calculate(double distance1, double distance2, double distance3)
	{
		double firstAskPrice = orderBook.getAsks().firstEntry().getValue().getPrice().doubleValue();
		double firstBidPrice = orderBook.getBids().firstEntry().getValue().getPrice().doubleValue();
		double pp = (firstAskPrice + firstBidPrice) / 2;

		double askPriceTo1 = pp * (1 + distance1);
		double bidPriceTo1 = pp * (1 - distance1);
		r1 = weightedAverageAsks(pp, askPriceTo1);
		s1 = weightedAverageBids(pp, bidPriceTo1);

		double askPriceTo2 = pp * (1 + distance2);
		double bidPriceTo2 = pp * (1 - distance2);
		r2 = weightedAverageAsks(pp, askPriceTo2);
		s2 = weightedAverageBids(pp, bidPriceTo2);

		double askPriceTo3 = pp * (1 + distance3);
		double bidPriceTo3 = pp * (1 - distance3);
		r3 = weightedAverageAsks(pp, askPriceTo3);
		s3 = weightedAverageBids(pp, bidPriceTo3);

		return this;
	}

	private BigDecimal weightedAverageAsks(double priceA, double priceB)
	{
		double sumProd = 0.0;
		double sumQty = 0.0;

		for (OrderBookEntry entry : orderBook.getAsks().values())
		{
			if (entry.getPrice().doubleValue() <= priceA)
			{
				continue;
			}
			if (entry.getPrice().doubleValue() > priceB)
			{
				break;
			}
			sumProd += entry.getPrice().doubleValue() * entry.getQty().doubleValue();
			sumQty += entry.getQty().doubleValue();
		}

		double avg = sumProd / sumQty;
		return BigDecimal.valueOf(avg).setScale(orderBook.getPricePrecision(), RoundingMode.HALF_UP);
	}

	private BigDecimal weightedAverageBids(double priceA, double priceB)
	{
		double sumProd = 0.0;
		double sumQty = 0.0;

		for (OrderBookEntry entry : orderBook.getBids().values())
		{
			if (entry.getPrice().doubleValue() >= priceA)
			{
				continue;
			}
			if (entry.getPrice().doubleValue() < priceB)
			{
				break;
			}
			sumProd += entry.getPrice().doubleValue() * entry.getQty().doubleValue();
			sumQty += entry.getQty().doubleValue();
		}

		double avg = sumProd / sumQty;
		return BigDecimal.valueOf(avg).setScale(orderBook.getPricePrecision(), RoundingMode.HALF_UP);
	}

}
