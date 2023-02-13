package technicals.indicators.depth;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import technicals.model.OrderBook;
import technicals.model.OrderBookEntry;

/**
 * Search for the grouped price blocks with the largest number of orders 
 */
public class DepthBlockPoints
{
	private OrderBook orderBook;

	private BigDecimal blockSize1;
	private List<OrderBookEntry> asksGrp1;
	private List<OrderBookEntry> bidsGrp1;

	private BigDecimal blockSize2;
	private List<OrderBookEntry> asksGrp2;
	private List<OrderBookEntry> bidsGrp2;

	private BigDecimal r1;
	private BigDecimal s1;
	private BigDecimal r2;
	private BigDecimal s2;

	public DepthBlockPoints(OrderBook orderBook)
	{
		this.orderBook = orderBook;
	}

	public BigDecimal getBlockSize1()
	{
		return blockSize1;
	}

	public List<OrderBookEntry> getAsksGrp1()
	{
		return asksGrp1;
	}

	public List<OrderBookEntry> getBidsGrp1()
	{
		return bidsGrp1;
	}

	public BigDecimal getBlockSize2()
	{
		return blockSize2;
	}

	public List<OrderBookEntry> getAsksGrp2()
	{
		return asksGrp2;
	}

	public List<OrderBookEntry> getBidsGrp2()
	{
		return bidsGrp2;
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

	public DepthBlockPoints calculate(int blocks)
	{
		List<OrderBookEntry> lstAsks = orderBook.getAsksList();
		List<OrderBookEntry> lstBids = orderBook.getBidsList(); 

		double firstAskPrice = orderBook.getAsks().firstEntry().getValue().getPrice().doubleValue();
		double firstBidPrice = orderBook.getBids().firstEntry().getValue().getPrice().doubleValue();
		BigDecimal pp = BigDecimal.valueOf((firstAskPrice + firstBidPrice) / 2);

		blockSize1 = getBlockSize(pp, BigDecimal.valueOf(1));
		blockSize2 = getBlockSize(pp, BigDecimal.valueOf(10));

		BigDecimal askPriceFrom1 = roundNearest(pp, blockSize1);
		BigDecimal bidPriceFrom1 = askPriceFrom1.add(blockSize1);
		BigDecimal askPriceFrom2 = roundNearest(pp, blockSize2);
		BigDecimal bidPriceFrom2 = askPriceFrom2.add(blockSize2);

		BigDecimal askPriceTo1 = askPriceFrom1.add(blockSize1.multiply(BigDecimal.valueOf(blocks)));
		BigDecimal bidPriceTo1 = bidPriceFrom1.subtract(blockSize1.multiply(BigDecimal.valueOf(blocks)));
		BigDecimal askPriceTo2 = askPriceFrom2.add(blockSize2.multiply(BigDecimal.valueOf(blocks)));
		BigDecimal bidPriceTo2 = bidPriceFrom2.subtract(blockSize2.multiply(BigDecimal.valueOf(blocks)));
		
		asksGrp1 = loadAsksGrp(lstAsks, blockSize1, askPriceFrom1);
		bidsGrp1 = loadBidsGrp(lstBids, blockSize1, bidPriceFrom1);
		asksGrp2 = loadAsksGrp(lstAsks, blockSize2, askPriceFrom2);
		bidsGrp2 = loadBidsGrp(lstBids, blockSize2, bidPriceFrom2);

		r1 = getBestBlockAsks(asksGrp1, askPriceFrom1, askPriceTo1);
		s1 = getBestBlockBids(bidsGrp1, bidPriceFrom1, bidPriceTo1);
		r2 = getBestBlockAsks(asksGrp2, askPriceFrom2, askPriceTo2);
		s2 = getBestBlockBids(bidsGrp2, bidPriceFrom2, bidPriceTo2);
		
		return this;
	}

	private static BigDecimal getBlockSize(BigDecimal price, BigDecimal size)
	{
		if (price.doubleValue() < 0.0001)
			return BigDecimal.valueOf(0.0000001).multiply(size);
		else if (price.doubleValue() < 0.001)
			return BigDecimal.valueOf(0.000001).multiply(size);
		else if (price.doubleValue() < 0.01)
			return BigDecimal.valueOf(0.00001).multiply(size);
		else if (price.doubleValue() < 0.1)
			return BigDecimal.valueOf(0.0001).multiply(size);
		else if (price.doubleValue() < 1)
			return BigDecimal.valueOf(0.001).multiply(size);
		else if (price.doubleValue() < 10)
			return BigDecimal.valueOf(0.01).multiply(size);
		else if (price.doubleValue() < 100)
			return BigDecimal.valueOf(0.1).multiply(size);
		else if (price.doubleValue() < 1000)
			return BigDecimal.valueOf(1).multiply(size);
		else if (price.doubleValue() < 10000)
			return BigDecimal.valueOf(10).multiply(size);
		else
			return BigDecimal.valueOf(100).multiply(size);
	}

	private static BigDecimal roundNearest(BigDecimal price, BigDecimal blockSize)
	{
		double _blockSize = blockSize.doubleValue();
		if (_blockSize < 1)
		{
			if (_blockSize == 0)
			{
				return price.setScale(0, RoundingMode.DOWN);
			}
			else
			{
				int precision = (int) Math.log10(_blockSize) * -1;
				return price.setScale(precision, RoundingMode.DOWN);
			}
		}
		else
		{
			return price.divide(blockSize, 0, RoundingMode.DOWN).multiply(blockSize);
		}
	}

	// --------------------------------------------------------------------

	private static List<OrderBookEntry> loadAsksGrp(List<OrderBookEntry> lstAsks, BigDecimal blockSize, BigDecimal priceFrom)
	{
		List<OrderBookEntry> asksGrp = new ArrayList<OrderBookEntry>();

		if (lstAsks == null || lstAsks.isEmpty())
		{
			return asksGrp;
		}

		BigDecimal priceBlock = priceFrom.add(blockSize);
		BigDecimal qty = BigDecimal.ZERO;

		for (int i = 1; i < lstAsks.size(); i++)
		{
			if (lstAsks.get(i).getPrice().doubleValue() <= priceBlock.doubleValue())
			{
				qty = qty.add(lstAsks.get(i).getQty());
			}
			else
			{
				OrderBookEntry prev = lstAsks.get(i - 1);
				OrderBookEntry newElement = new OrderBookEntry();
				newElement.setPrice(prev.getPrice());
				newElement.setQty(qty);
				asksGrp.add(newElement);

				priceBlock = priceBlock.add(blockSize);
				qty = lstAsks.get(i).getQty();
			}
		}
		OrderBookEntry prev = lstAsks.get(lstAsks.size() - 1);
		OrderBookEntry newElement = new OrderBookEntry();
		newElement.setPrice(prev.getPrice());
		newElement.setQty(qty);
		asksGrp.add(newElement);

		return asksGrp;
	}

	private static List<OrderBookEntry> loadBidsGrp(List<OrderBookEntry> lstBids, BigDecimal blockSize, BigDecimal priceFrom)
	{
		List<OrderBookEntry> bidsGrp = new ArrayList<OrderBookEntry>();

		if (lstBids == null || lstBids.isEmpty())
		{
			return bidsGrp;
		}

		BigDecimal priceBlock = priceFrom.subtract(blockSize);
		BigDecimal qty = BigDecimal.ZERO; // bids.get(0).getQty();

		for (int i = 1; i < lstBids.size(); i++)
		{
			if (lstBids.get(i).getPrice().doubleValue() >= priceBlock.doubleValue())
			{
				qty = qty.add(lstBids.get(i).getQty());
			}
			else
			{
				OrderBookEntry prev = lstBids.get(i - 1);
				OrderBookEntry newElement = new OrderBookEntry();
				newElement.setPrice(prev.getPrice());
				newElement.setQty(qty);
				bidsGrp.add(newElement);

				priceBlock = priceBlock.subtract(blockSize);
				qty = lstBids.get(i).getQty();
			}
		}
		OrderBookEntry prev = lstBids.get(lstBids.size() - 1);
		OrderBookEntry newElement = new OrderBookEntry();
		newElement.setPrice(prev.getPrice());
		newElement.setQty(qty);
		bidsGrp.add(newElement);

		return bidsGrp;
	}

	// --------------------------------------------------------------------

	private static BigDecimal getBestBlockAsks(List<OrderBookEntry> asksGrp, BigDecimal priceA, BigDecimal priceB)
	{
		if (asksGrp != null && !asksGrp.isEmpty())
		{
			OrderBookEntry eMax = null;
			for (OrderBookEntry e : asksGrp)
			{
				if (e.getPrice().doubleValue() <= priceA.doubleValue())
				{
					continue;
				}
				if (e.getPrice().doubleValue() > priceB.doubleValue())
				{
					break;
				}

				if ((eMax == null) || (eMax.getQty().compareTo(e.getQty()) == -1))
				{
					eMax = e;
				}
			}
			return eMax.getPrice();
		}
		return null;
	}

	private static BigDecimal getBestBlockBids(List<OrderBookEntry> bidsGrp, BigDecimal priceA, BigDecimal priceB)
	{
		if (bidsGrp != null && !bidsGrp.isEmpty())
		{
			OrderBookEntry eMax = null;
			for (OrderBookEntry e : bidsGrp)
			{
				if (e.getPrice().doubleValue() >= priceA.doubleValue())
				{
					continue;
				}
				if (e.getPrice().doubleValue() < priceB.doubleValue())
				{
					break;
				}

				if ((eMax == null) || (eMax.getQty().compareTo(e.getQty()) == -1))
				{
					eMax = e;
				}
			}
			return eMax.getPrice();
		}
		return null;
	}

}
