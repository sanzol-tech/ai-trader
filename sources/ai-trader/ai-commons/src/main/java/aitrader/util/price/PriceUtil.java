package aitrader.util.price;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PriceUtil
{
	private PriceUtil()
	{
		// Hide
	}

	public static BigDecimal priceDistUp(BigDecimal a, BigDecimal b, boolean x100)
	{
		BigDecimal r = b.subtract(a).divide(a, 4, RoundingMode.HALF_UP);
		if (x100)
			return r.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN);
		else
			return r;
	}

	public static BigDecimal priceDistDown(BigDecimal a, BigDecimal b, boolean x100)
	{
		BigDecimal r = a.subtract(b).divide(a, 4, RoundingMode.HALF_UP);
		if (x100)
			return r.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN);
		else
			return r;
	}

	public static BigDecimal priceChange(BigDecimal a, BigDecimal b, boolean x100)
	{
		BigDecimal r = b.subtract(a).divide(a, 4, RoundingMode.HALF_UP);

		if (x100)
			return r.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN);
		else
			return r;
	}

	public static String cashFormat(double n)
	{
		return cashFormat(n, 0);
	}

	public static String cashFormat(BigDecimal n)
	{
		return cashFormat(n.doubleValue(), 0);
	}

	private static String cashFormat(double n, int iteration)
	{
		char[] suff = new char[] { 'K', 'M', 'B', 'T' };

		double d = ((long) n / 100) / 10.0;
		boolean isRound = (d * 10) % 10 == 0;
		return (d < 1000 ? ((d > 99.9 || isRound || (!isRound && d > 9.99) ? (int) d * 10 / 10 : d + "") + "" + suff[iteration]) : cashFormat(d, iteration + 1));
	}

}
