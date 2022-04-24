package sanzol.app.model;

public class GOrder
{
	private double price;
	private double coins;
	private double usd;

	public GOrder(double price, double coins, double usd)
	{
		this.price = price;
		this.coins = coins;
		this.usd = usd;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public double getCoins()
	{
		return coins;
	}

	public void setCoins(double coins)
	{
		this.coins = coins;
	}

	public double getUsd()
	{
		return usd;
	}

	public void setUsd(double usd)
	{
		this.usd = usd;
	}

}
