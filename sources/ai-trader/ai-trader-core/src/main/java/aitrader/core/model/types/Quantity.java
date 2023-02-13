package aitrader.core.model.types;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import aitrader.core.model.Symbol;

public class Quantity
{
	private Symbol symbol;
	private BigDecimal value;

	public Quantity(Symbol symbol, String value)
	{
		this.symbol = symbol;
		this.value = new BigDecimal(value);
	}

	public Quantity(Symbol symbol, Double value)
	{
		this.symbol = symbol;
		this.value = BigDecimal.valueOf(value);
	}

	public Quantity(Symbol symbol, BigDecimal value)
	{
		this.symbol = symbol;
		this.value = value;
	}

	public BigDecimal toBigDecimal()
	{
		return value;
	}

	public Double toDouble()
	{
		if (value == null)
			return null;

		return value.doubleValue();
	}

	@Override
	public String toString()
	{
		if (value == null)
			return "";

		return new DecimalFormat(symbol.getQtyPattern(), new DecimalFormatSymbols(Locale.ENGLISH)).format(value.doubleValue());
	}
}
