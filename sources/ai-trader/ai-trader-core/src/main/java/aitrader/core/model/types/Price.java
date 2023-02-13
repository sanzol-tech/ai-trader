package aitrader.core.model.types;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import aitrader.core.model.Symbol;

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

		return new DecimalFormat(symbol.getPricePattern(), new DecimalFormatSymbols(Locale.ENGLISH)).format(value.doubleValue());
	}

}
