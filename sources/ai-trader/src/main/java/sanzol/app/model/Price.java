package sanzol.app.model;

import java.math.BigDecimal;

import sanzol.app.service.Symbol;

public class Price
{
	private Symbol symbol;
	private BigDecimal value;

	public Price(Symbol symbol, String value)
	{
		this.symbol = symbol;
		this.value = new BigDecimal(value);
	}

	public Price(Symbol symbol, Double value)
	{
		this.symbol = symbol;
		this.value = BigDecimal.valueOf(value);
	}

	public Price(Symbol symbol, BigDecimal value)
	{
		this.symbol = symbol;
		this.value = value;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getValue()
	{
		return value;
	}

	public void setValue(BigDecimal value)
	{
		this.value = value;
	}

	public Double toDouble()
	{
		if (value == null)
			return null;
		
		return value.doubleValue();
	}
	public BigDecimal toBigDecimal()
	{
		return value;
	}
	
	@Override
	public String toString()
	{
		if (value == null)
			return "";
		
		return symbol.priceToStr(value);
	}

}
