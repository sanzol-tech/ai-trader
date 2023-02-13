package technicals.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class OrderBook
{
	private int pricePrecision;
	private int qtyPrecision;

	private TreeMap<BigDecimal, OrderBookEntry> asks;
	private TreeMap<BigDecimal, OrderBookEntry> bids;

	public OrderBook(int pricePrecision)
	{
		this.pricePrecision = pricePrecision;
	}

	public int getPricePrecision()
	{
		return pricePrecision;
	}

	public void setPricePrecision(int pricePrecision)
	{
		this.pricePrecision = pricePrecision;
	}

	public int getQtyPrecision()
	{
		return qtyPrecision;
	}

	public void setQtyPrecision(int qtyPrecision)
	{
		this.qtyPrecision = qtyPrecision;
	}

	public TreeMap<BigDecimal, OrderBookEntry> getAsks()
	{
		return asks;
	}

	public void setAsks(TreeMap<BigDecimal, OrderBookEntry> asks)
	{
		this.asks = asks;
	}

	public TreeMap<BigDecimal, OrderBookEntry> getBids()
	{
		return bids;
	}

	public void setBids(TreeMap<BigDecimal, OrderBookEntry> bids)
	{
		this.bids = bids;
	}

	// ---- CALCULATED FIELDS -------------------------------------------------

	public List<OrderBookEntry> getAsksList()
	{
		return new ArrayList<OrderBookEntry>(asks.values());
	}

	public List<OrderBookEntry> getBidsList()
	{
		return new ArrayList<OrderBookEntry>(bids.values());
	}

}
