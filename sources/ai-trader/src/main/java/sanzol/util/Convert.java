package sanzol.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.decimal4j.util.DoubleRounder;

public final class Convert
{
	private Convert()
	{
		// Hide
	}

	// --- USD ---------------------------------------------------------------
	
	public static String usdToStr(BigDecimal usd)
	{
		String pattern = "#0.00";
		return new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(usd);
	}

	public static String usdToStr(double usd)
	{
		String pattern = "#0.00";
		return new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(usd);
	}

	// --- PERCENT -----------------------------------------------------------

	public static String toStrPercent(BigDecimal d)
	{
		if (d == null)
			return "";

		return d.multiply(BigDecimal.valueOf(100.0)).setScale(2, RoundingMode.HALF_UP).toString();
	}

	public static String toStrPercent(Double d)
	{
		if (d == null)
			return "";

		return String.valueOf(DoubleRounder.round(d * 100, 2));
	}

	public static BigDecimal strPercentToBigDecimal(String str)
	{
		if (str == null)
			return null;

		return (new BigDecimal(str)).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
	}

	public static Double strPercentToDbl(String str)
	{
		if (str == null)
			return null;

		return DoubleRounder.round(Double.valueOf(str) / 100, 4);
	}
	
	// --- TIME --------------------------------------------------------------
	
	public static String convertTime(long time)
	{
		Date date = new Date(time);
		Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

}
