package aitrader.core.model;

import aitrader.core.model.enums.OpType;
import binance.futures.enums.OrderSide;

public class GridOrder
{
	private int number;
	private OrderSide orderSide;
	private OpType opType;
	private double distance;
	private double price;
	private double quantity;
	private double usd;
	private double sumCoins;
	private double sumUsd;
	private double lost;
	private double newPrice;
	private double takeProfit;
	private double profit;
	private double recoveryNeeded;
	private String status;
	private String result;

	public GridOrder()
	{
		//
	}

	public GridOrder(int number, OrderSide orderSide, OpType opType, double distance, double price, double quantity,
			double usd, double sumCoins, double sumUsd, double lost, double newPrice, double takeProfit, double profit,
			double recoveryNeeded)
	{
		this.number = number;
		this.orderSide = orderSide;
		this.opType = opType;
		this.distance = distance;
		this.price = price;
		this.quantity = quantity;
		this.usd = usd;
		this.sumCoins = sumCoins;
		this.sumUsd = sumUsd;
		this.lost = lost;
		this.newPrice = newPrice;
		this.takeProfit = takeProfit;
		this.profit = profit;
		this.recoveryNeeded = recoveryNeeded;
		this.status = null;
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public OrderSide getOrderSide()
	{
		return orderSide;
	}

	public void setOrderSide(OrderSide orderSide)
	{
		this.orderSide = orderSide;
	}

	public OpType getOpType()
	{
		return opType;
	}

	public void setOpType(OpType opType)
	{
		this.opType = opType;
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

	public double getQuantity()
	{
		return quantity;
	}

	public void setQuantity(double quantity)
	{
		this.quantity = quantity;
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

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
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
