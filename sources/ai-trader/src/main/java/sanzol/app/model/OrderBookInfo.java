package sanzol.app.model;

import java.math.BigDecimal;
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
	}

	// ------------------------------------------------------------------------

	public BigDecimal getAskPriceBetween(double minPercent, double maxPercent)
	{
		if (asks != null && !asks.isEmpty())
		{
			double total = asks.get(asks.size() - 1).getSumQty().doubleValue();

			for (OrderBookElement entry : asks)
			{
				if (entry.getSumQty().doubleValue() > total * maxPercent)
				{
					return null;
				}

				if (entry.getSumQty().doubleValue() >= total * minPercent)
				{
					return entry.getPrice();
				}
			}
		}
		return null;
	}

	public BigDecimal getBidPriceBetween(double minPercent, double maxPercent)
	{
		if (bids != null && !bids.isEmpty())
		{
			double total = bids.get(bids.size() - 1).getSumQty().doubleValue();

			for (OrderBookElement entry : bids)
			{
				if (entry.getSumQty().doubleValue() > total * maxPercent)
				{
					return null;
				}

				if (entry.getSumQty().doubleValue() >= total * minPercent)
				{
					return entry.getPrice();
				}
			}
		}
		return null;
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
		fixShocks(BigDecimal.valueOf(0.2), BigDecimal.valueOf(0.5));
	}

	public void fixShocks(BigDecimal min, BigDecimal max)
	{
		BigDecimal askPrice30 = getAskPriceBetween(0.35, 0.45);
		BigDecimal bidPrice30 = getBidPriceBetween(0.35, 0.45);

		if (askPrice30 != null && askPrice30.doubleValue() > shShock.doubleValue())
			shShock = askPrice30;

		if (bidPrice30 != null && bidPrice30.doubleValue() < lgShock.doubleValue())
			lgShock = bidPrice30;
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

}
