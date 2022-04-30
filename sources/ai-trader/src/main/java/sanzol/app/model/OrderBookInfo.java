package sanzol.app.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class OrderBookInfo
{
	private Symbol coin;

	private List<OrderBookElement> asks;
	private List<OrderBookElement> asksGrp;

	private List<OrderBookElement> bids;
	private List<OrderBookElement> bidsGrp;

	private BigDecimal shShock;
	private BigDecimal lgShock;

	private BigDecimal shShockWAvg;
	private BigDecimal lgShockWAvg;
	
	private BigDecimal shShockFixed;
	private BigDecimal lgShockFixed;
	
	public Symbol getCoin()
	{
		return coin;
	}

	public void setCoin(Symbol coin)
	{
		this.coin = coin;
	}

	public List<OrderBookElement> getAsks()
	{
		return asks;
	}

	public List<OrderBookElement> getAsksGrp()
	{
		return asksGrp;
	}

	public List<OrderBookElement> getBids()
	{
		return bids;
	}

	public List<OrderBookElement> getBidsGrp()
	{
		return bidsGrp;
	}

	public BigDecimal getShShock()
	{
		return shShock;
	}

	public BigDecimal getLgShock()
	{
		return lgShock;
	}

	public BigDecimal getShShockWAvg()
	{
		return shShockWAvg;
	}

	public BigDecimal getLgShockWAvg()
	{
		return lgShockWAvg;
	}

	public BigDecimal getShShockFixed()
	{
		return shShockFixed;
	}

	public BigDecimal getLgShockFixed()
	{
		return lgShockFixed;
	}

	// -------------------------------------------------------------------------

	public OrderBookInfo()
	{
		//
	}

	public OrderBookInfo(Symbol coin, List<OrderBookElement> asks, List<OrderBookElement> asksGrp, List<OrderBookElement> bids, List<OrderBookElement> bidsGrp)
	{
		this.coin = coin;

		this.asks = asks;
		this.bids = bids;

		this.asksGrp = asksGrp;
		this.bidsGrp = bidsGrp;

		this.shShock = getMaxAsksGrp();
		this.lgShock = getMaxBidsGrp();
		
		this.shShockWAvg = weightedAverage(asks);
		this.lgShockWAvg = weightedAverage(bids);
		
		fixShocks();
	}

	// ------------------------------------------------------------------------

	private BigDecimal getMaxAsksGrp()
	{
		if (asksGrp != null && !asksGrp.isEmpty())
		{
			OrderBookElement eMax = null;
			for (OrderBookElement e : asksGrp)
			{
				if ((eMax == null) || (eMax.getQty().compareTo(e.getQty()) == -1))
					eMax = e;
			}
			return eMax.getPrice();
		}
		return null;
	}

	private BigDecimal getMaxBidsGrp()
	{
		if (bidsGrp != null && !bidsGrp.isEmpty())
		{
			OrderBookElement eMax = null;
			for (OrderBookElement e : bidsGrp)
			{
				if ((eMax == null) || (eMax.getQty().compareTo(e.getQty()) == -1))
					eMax = e;
			}
			return eMax.getPrice();
		}
		return null;
	}

	// ------------------------------------------------------------------------

	public void fixShocks()
	{
		BigDecimal shortWAvg = weightedAverage(asks);
		BigDecimal longWAvg = weightedAverage(bids);

		if (shortWAvg != null && shortWAvg.doubleValue() > shShock.doubleValue())
			shShockFixed = shortWAvg;
		else
			shShockFixed = shShock;

		if (longWAvg != null && longWAvg.doubleValue() < lgShock.doubleValue())
			lgShockFixed = longWAvg;
		else
			lgShockFixed = lgShock;
	}

	public static BigDecimal weightedAverage(List<OrderBookElement> lst)
	{
		return weightedAverage(lst, 0.5, 0.2);
	}
	
	public static BigDecimal weightedAverage(List<OrderBookElement> lst, double maxAccumPercent, double maxDist)
	{
		BigDecimal sumProd = BigDecimal.ZERO;
		BigDecimal sumQty = BigDecimal.ZERO;

		for (OrderBookElement entry : lst)
		{
			if (entry.getSumPercent().doubleValue() > maxAccumPercent || entry.getDistance().doubleValue() > maxDist)
			{
				break;
			}
			sumProd = sumProd.add(entry.getPrice().multiply(entry.getQty()));
			sumQty = sumQty.add(entry.getQty());
		}

		return sumProd.divide(sumQty, RoundingMode.HALF_UP);
	}

	// ------------------------------------------------------------------------

	public String getStrShShock()
	{
		return coin.priceToStr(shShock);
	}

	public String getStrLgShock()
	{
		return coin.priceToStr(lgShock);
	}

	public String getStrShShockWAvg()
	{
		return coin.priceToStr(shShockWAvg);
	}

	public String getStrLgShockWAvg()
	{
		return coin.priceToStr(lgShockWAvg);
	}

	public String getStrShShockFixed()
	{
		return coin.priceToStr(shShockFixed);
	}

	public String getStrLgShockFixed()
	{
		return coin.priceToStr(lgShockFixed);
	}
	
}
