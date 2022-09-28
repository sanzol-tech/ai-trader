package sanzol.aitrader.be.model;

import sanzol.aitrader.be.enums.GridOrderStatus;
import sanzol.aitrader.be.enums.GridOrderType;

public class GridOrder
{
	private int number;
	private GridOrderType type;
	private double distance;
	private double price;
	private double coins;
	private double usd;
	private double sumCoins;
	private double sumUsd;
	private double lost;
	private double newPrice;
	private double takeProfit;
	private double profit;
	private double recoveryNeeded;
	private GridOrderStatus status;
	private String result;

	public GridOrder()
	{
		//
	}

	public GridOrder(int number, GridOrderType type, double distance, double price, double coins, double usd, double sumCoins, double sumUsd, double lost, double newPrice, double takeProfit, double profit, double recoveryNeeded)
	{
		this.number = number;
		this.type = type;
		this.distance = distance;
		this.price = price;
		this.coins = coins;
		this.usd = usd;
		this.sumCoins = sumCoins;
		this.sumUsd = sumUsd;
		this.lost = lost;
		this.newPrice = newPrice;
		this.takeProfit = takeProfit;
		this.profit = profit;
		this.recoveryNeeded = recoveryNeeded;
		this.status = GridOrderStatus.NEW;
	}

	public int getNumber()
	{
		return number;
	}

	public GridOrderType getType()
	{
		return type;
	}

	public void setType(GridOrderType type)
	{
		this.type = type;
	}

	public double getDistance()
	{
		return distance;
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
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

	public double getSumCoins()
	{
		return sumCoins;
	}

	public void setSumCoins(double sumCoins)
	{
		this.sumCoins = sumCoins;
	}

	public double getSumUsd()
	{
		return sumUsd;
	}

	public void setSumUsd(double sumUsd)
	{
		this.sumUsd = sumUsd;
	}

	public double getLost()
	{
		return lost;
	}

	public void setLost(double lost)
	{
		this.lost = lost;
	}

	public double getNewPrice()
	{
		return newPrice;
	}

	public void setNewPrice(double newPrice)
	{
		this.newPrice = newPrice;
	}

	public double getTakeProfit()
	{
		return takeProfit;
	}

	public void setTakeProfit(double takeProfit)
	{
		this.takeProfit = takeProfit;
	}

	public double getProfit()
	{
		return profit;
	}

	public void setProfit(double profit)
	{
		this.profit = profit;
	}

	public double getRecoveryNeeded()
	{
		return recoveryNeeded;
	}

	public void setRecoveryNeeded(double recoveryNeeded)
	{
		this.recoveryNeeded = recoveryNeeded;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public GridOrderStatus getStatus()
	{
		return status;
	}

	public void setStatus(GridOrderStatus status)
	{
		this.status = status;
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

}
