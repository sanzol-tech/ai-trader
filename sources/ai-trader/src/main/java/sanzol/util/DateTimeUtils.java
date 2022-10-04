package sanzol.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class DateTimeUtils
{
	private static final String TXT_DAYS = "days";
	private static final String TXT_HOURS = "hours";
	private static final String TXT_MIN = "mins";
	private static final String TXT_SEC = "secs";
	private static final String TXT_MSECS = "mills";

	private static final long ONE_MILL = 1;
	private static final long ONE_SECOND = ONE_MILL * 1000;
	private static final long ONE_MINUTE = ONE_SECOND * 60;
	private static final long ONE_HOUR = ONE_MINUTE * 60;
	private static final long ONE_DAY = ONE_HOUR * 24;

	public static String readableDateDiff(Temporal startDate, Temporal endDate)
	{
		if (startDate == null || endDate == null)
		{
			return null;
		}

		return millisToReadable(ChronoUnit.MILLIS.between(startDate, endDate));
	}

	public static String readableDateDiff(long startDate, long endDate)
	{
		return millisToReadable(endDate - startDate);
	}

	public static String millisToReadable(long millis)
	{
		long days = millis / ONE_DAY;
		long rest = millis % ONE_DAY;

		long hours = rest / ONE_HOUR;
		rest = rest % ONE_HOUR;

		long minutes = rest / ONE_MINUTE;
		rest = rest % ONE_MINUTE;

		long seconds = rest / ONE_SECOND;
		rest = rest % ONE_SECOND;

		int l = 0;
		String text = "";
		if (days > 0 && l < 2)
		{
			text += (text.isEmpty() ? "" : ", ") + days + " " + TXT_DAYS;
			l++;
		}
		if (hours > 0 && l < 2)
		{
			text += (text.isEmpty() ? "" : ", ") + hours + " " + TXT_HOURS;
			l++;
		}
		if (minutes > 0 && l < 2)
		{
			text += (text.isEmpty() ? "" : ", ") + minutes + " " + TXT_MIN;
			l++;
		}
		if (seconds > 0 && l < 2)
		{
			text += (text.isEmpty() ? "" : ", ") + seconds + " " + TXT_SEC;
			l++;
		}
		if (rest > 0 && l < 2)
		{
			text += (text.isEmpty() ? "" : ", ") + rest + " " + TXT_MSECS;
			l++;
		}

		return text;
	}

	public static LocalDateTime toLocalDateTime(long value)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault());
	}

}
