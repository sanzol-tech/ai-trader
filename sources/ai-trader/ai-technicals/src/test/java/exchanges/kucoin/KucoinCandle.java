package exchanges.kucoin;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KucoinCandle
{
	private long openTime;
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private BigDecimal volume;

	public KucoinCandle()
	{
		//
	}

	public KucoinCandle(long openTime, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, BigDecimal volume)
	{
		this.openTime = openTime;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}

	public long getOpenTime()
	{
		return openTime;
	}

	public void setOpenTime(long openTime)
	{
		this.openTime = openTime;
	}

	public BigDecimal getOpen()
	{
		return open;
	}

	public void setOpen(BigDecimal open)
	{
		this.open = open;
	}

	public BigDecimal getHigh()
	{
		return high;
	}

	public void setHigh(BigDecimal high)
	{
		this.high = high;
	}

	public BigDecimal getLow()
	{
		return low;
	}

	public void setLow(BigDecimal low)
	{
		this.low = low;
	}

	public BigDecimal getClose()
	{
		return close;
	}

	public void setClose(BigDecimal close)
	{
		this.close = close;
	}

	public BigDecimal getVolume()
	{
		return volume;
	}

	public void setVolume(BigDecimal volume)
	{
		this.volume = volume;
	}

	// ---- CALCULATED FIELDS -------------------------------------------------

	public ZonedDateTime getOpenTimeZoned()
	{
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(openTime), ZoneOffset.UTC);
	}

	// ---- TO STRING ---------------------------------------------------------

	@Override
	public String toString()
	{
		return "KucoinCandle [openTime=" + getOpenTimeZoned() + ", open=" + open + ", high=" + high + ", low=" + low + ", close=" + close + ", volume=" + volume + "]";
	}

}
