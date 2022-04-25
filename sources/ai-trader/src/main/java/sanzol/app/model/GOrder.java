package sanzol.app.model;

import java.math.BigDecimal;

public class GOrder
{
	private BigDecimal price;
	private BigDecimal coins;
	private BigDecimal usd;

	public GOrder(BigDecimal price, BigDecimal coins, BigDecimal usd)
	{
		this.price = price;
		this.coins = coins;
		this.usd = usd;
	}

	public GOrder(BigDecimal price, BigDecimal coins)
	{
		this.price = price;
		this.coins = coins;
		this.usd = coins.multiply(price);
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
		return coins;
	}

	public void setCoins(BigDecimal coins)
	{
		this.coins = coins;
	}

	public BigDecimal getUsd()
	{
		return usd;
	}

	public void setUsd(BigDecimal usd)
	{
		this.usd = usd;
	}

}
