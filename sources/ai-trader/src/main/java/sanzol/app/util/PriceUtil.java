package sanzol.app.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PriceUtil
{
	private PriceUtil()
	{
		// Hide
	}

	public static BigDecimal priceDist(BigDecimal a, BigDecimal b)
	{
		if (a.compareTo(b) < 0)
		{
			return b.subtract(a).divide(a, 4, RoundingMode.HALF_UP);
		}
		else if (a.compareTo(b) > 0)
		{
			return a.subtract(b).divide(a, 4, RoundingMode.HALF_UP);
		}
		else
		{
			return BigDecimal.ZERO;
		}
	}

	public static String cashFormat(double n, int iteration)
	{
		char[] suff = new char[] { 'K', 'M', 'B', 'T' };

		double d = ((long) n / 100) / 10.0;
		boolean isRound = (d * 10) % 10 == 0;
		return (d < 1000 ? ((d > 99.9 || isRound || (!isRound && d > 9.99) ? (int) d * 10 / 10 : d + "") + "" + suff[iteration]) : cashFormat(d, iteration + 1));

	}

}
