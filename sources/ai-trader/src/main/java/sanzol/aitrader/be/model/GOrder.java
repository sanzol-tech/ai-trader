package sanzol.aitrader.be.model;

import java.math.BigDecimal;

public class GOrder
{
	private BigDecimal price;
	private BigDecimal qty;
	private BigDecimal usd;
	private BigDecimal dist;

	public GOrder(BigDecimal price, BigDecimal qty, BigDecimal usd)
	{
		this.price = price;
		this.qty = qty;
		this.usd = usd;
	}

	public GOrder(BigDecimal price, BigDecimal qty)
	{
		this.price = price;
		this.qty = qty;
		this.usd = qty.multiply(price);
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getCoins()
	{
		return qty;
	}

	public void setCoins(BigDecimal qty)
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

	public BigDecimal getDist()
	{
		return dist;
	}

	public void setDist(BigDecimal dist)
	{
		this.dist = dist;
	}

}
