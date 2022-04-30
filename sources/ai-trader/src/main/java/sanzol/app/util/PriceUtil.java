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
}
