package aitrader.core.model.types;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Percentage
{
	private int precision = 2;
	private BigDecimal value;

	public Percentage(BigDecimal value)
	{
		this.value = value;
	}

	public int getPrecision()
	{
		return precision;
	}

	public Percentage withPrecision(int precision)
	{
		this.precision = precision;
		return this;
	}

	public Percentage valueOf(BigDecimal value)
	{
		return new Percentage(value);
	}

	public Percentage valueOf(double value)
	{
		return new Percentage(BigDecimal.valueOf(value).setScale(precision, RoundingMode.HALF_UP));
	}

	public Percentage valueOf(String value)
	{
		return new Percentage((new BigDecimal(value)).setScale(precision, RoundingMode.HALF_UP));
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

		return value.toPlainString();
	}

}
