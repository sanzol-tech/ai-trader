package sanzol.app.model;

import java.math.BigDecimal;

import sanzol.app.service.Symbol;

public class Alert
{
	private Symbol symbol;
	private String side;
	private BigDecimal price;

	public Alert()
	{
		//
	}

	public Alert(Symbol symbol, String side, BigDecimal price)
	{
		this.symbol = symbol;
		this.side = side;
		this.price = price;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public String getSide()
	{
		return side;
	}

	public void setSide(String side)
	{
		this.side = side;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

}
