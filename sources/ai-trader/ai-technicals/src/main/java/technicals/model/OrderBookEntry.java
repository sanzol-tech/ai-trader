package technicals.model;

import java.math.BigDecimal;

public class OrderBookEntry
{
	private BigDecimal price;
	private BigDecimal qty;
	private BigDecimal usd;

	public OrderBookEntry()
	{
		//
	}

	public OrderBookEntry(BigDecimal price, BigDecimal qty)
	{
		this.price = price;
		this.qty = qty;
		this.usd = price.multiply(qty);
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public BigDecimal getUsd()
	{
		return usd;
	}

	public void setUsd(BigDecimal usd)
	{
		this.usd = usd;
	}

	@Override
	public String toString()
	{
		return String.format("%f;%f;%f", price, qty, usd);
	}

}
