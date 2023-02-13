package technicals.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Precision
{

	public static double round(double value, Integer precision)
	{
		if (precision == null)
			return value;
		else
			return BigDecimal.valueOf(value).setScale(precision, RoundingMode.HALF_UP).doubleValue();
	}

}
