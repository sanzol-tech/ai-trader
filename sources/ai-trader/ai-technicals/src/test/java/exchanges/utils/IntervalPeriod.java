package exchanges.utils;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import exchanges.bybit.BybitIntervalType;

public class IntervalPeriod
{
	private String interval;
	private int limit;
	private long startAt;
	private long endAt;

	public long getStartAt()
	{
		return startAt;
	}

	public long getEndAt()
	{
		return endAt;
	}

	public IntervalPeriod(String interval, int limit)
	{
		this.interval = interval;
		this.limit = limit;
	}

	public static IntervalPeriod create(String interval, int limit)
	{
		return new IntervalPeriod(interval, limit);
	}

	public IntervalPeriod calc()
	{
		ZonedDateTime now;
		ZonedDateTime prev;

		if ("_1m".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusMinutes(1);
			prev = now.minusMinutes(limit);
		}
		else if ("_3m".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusMinutes(3);
			if (now.getMinute() % 3 != 0)
				now = now.minusMinutes(now.getMinute() % 3);
			prev = now.minusMinutes(limit * 3);
		}
		else if ("_5m".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusMinutes(5);
			if (now.getMinute() % 5 != 0)
				now = now.minusMinutes(now.getMinute() % 5);
			prev = now.minusMinutes(limit * 5);
		}
		else if ("_15m".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusMinutes(15);
			if (now.getMinute() % 25 != 0)
				now = now.minusMinutes(now.getMinute() % 15);
			prev = now.minusMinutes(limit * 15);
		}
		else if ("_30m".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusMinutes(30);
			if (now.getMinute() % 30 != 0)
				now = now.minusMinutes(now.getMinute() % 30);
			prev = now.minusMinutes(limit * 30);
		}
		else if ("_1h".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(1);
			prev = now.minusHours(limit);
		}
		else if ("_2h".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(2);
			if (now.getHour() % 2 != 0)
				now = now.minusHours(1);
			prev = now.minusHours(limit * 2);
		}
		else if ("_4h".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(4);
			if (now.getHour() % 4 != 0)
				now = now.minusHours(now.getHour() % 4);
			prev = now.minusHours(limit * 4);
		}
		else if ("_6h".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(6);
			if (now.getHour() % 6 != 0)
				now = now.minusHours(now.getHour() % 6);
			prev = now.minusHours(limit * 6);
		}
		else if ("_8h".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(8);
			if (now.getHour() % 8 != 0)
				now = now.minusHours(now.getHour() % 8);
			prev = now.minusHours(limit * 8);
		}
		else if ("_12h".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(12);
			if (now.getHour() % 12 != 0)
				now = now.minusHours(now.getHour() % 12);
			prev = now.minusHours(limit * 12);
		}
		else if ("_1d".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(1);
			prev = now.minusDays(limit);
		}
		else if ("_3d".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(3);
			if (now.getDayOfMonth() % 3 != 0)
				now = now.minusDays(now.getDayOfMonth() % 3);
			prev = now.minusDays(limit * 3);
		}
		else if ("_1w".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(7);
			if (now.getDayOfWeek().getValue() > 1)
				now = now.minusDays(now.getDayOfWeek().getValue() - 1);
			prev = now.minusDays(limit * 7);
		}
		else if ("_1M".equals(interval))
		{
			now = ZonedDateTime.now().truncatedTo(ChronoUnit.MONTHS).plusMonths(1);
			prev = now.minusMonths(limit);
		}
		else
		{
			throw new IllegalArgumentException("unknown interval");
		}

		// System.out.println(prev + " - " + now);

		endAt = now.toInstant().toEpochMilli() / 1000;
		startAt = prev.toInstant().toEpochMilli() / 1000;

		return this;
	}

	public static void main(String[] args) throws Exception
	{
		BybitIntervalType it = BybitIntervalType._1w;

		IntervalPeriod intervalPeriod = IntervalPeriod.create(it.name(), 2).calc();
		System.out.println(intervalPeriod.getStartAt());
		System.out.println(intervalPeriod.getEndAt());
	}

}
