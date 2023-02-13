package aitrader.core.model.types;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Usd
{
	private String pattern = "#0.00";

	private BigDecimal value;

	public Usd(BigDecimal value)
	{
		this.value = value;
	}

	public Usd valueOf(BigDecimal value)
	{
		return new Usd(value);
	}

	public Percentage valueOf(double value)
	{
		return new Percentage(BigDecimal.valueOf(value));
	}

	public Percentage valueOf(String value)
	{
		return new Percentage(new BigDecimal(value));
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

		return new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(value);
	}

}
