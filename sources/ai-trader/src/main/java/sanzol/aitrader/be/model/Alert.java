package sanzol.aitrader.be.model;

import java.math.BigDecimal;

import api.client.futures.model.enums.OrderSide;

public class Alert
{
	private Symbol symbol;
	private OrderSide side;
	private Price priceAlert;
	private Price priceLimit;
	private boolean isAlerted;

	public Alert()
	{
		//
	}

	public Alert(Symbol symbol, OrderSide side, BigDecimal priceAlert, BigDecimal priceLimit)
	{
		this.symbol = symbol;
		this.side = side;
		this.priceAlert = new Price(symbol, priceAlert);
		this.priceLimit = new Price(symbol, priceLimit);
		this.isAlerted = false;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public OrderSide getSide()
	{
		return side;
	}

	public void setSide(OrderSide side)
	{
		this.side = side;
	}

	public BigDecimal getPriceAlert()
	{
		return priceAlert.getValue();
	}

	public void setPriceAlert(BigDecimal priceAlert)
	{
		this.priceAlert = new Price(symbol, priceAlert);
	}

	public BigDecimal getPriceLimit()
	{
		return priceLimit.getValue();
	}

	public void setPriceLimit(BigDecimal priceLimit)
	{
		this.priceLimit = new Price(symbol, priceLimit);
	}

	public boolean isAlerted()
	{
		return isAlerted;
	}

	public void setAlerted(boolean isAlerted)
	{
		this.isAlerted = isAlerted;
	}

	@Override
	public String toString()
	{
		return "Alert [symbol=" + symbol + ", side=" + side + ", priceAlert=" + priceAlert + ", priceLimit=" + priceLimit + "]";
	}

}
