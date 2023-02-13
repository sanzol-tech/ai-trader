package exchanges.binance;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceCandle
{

	private long openTime;
	private BigDecimal openPrice;
	private BigDecimal highPrice;
	private BigDecimal lowPrice;
	private BigDecimal closePrice;
	private BigDecimal volume;
	private BigDecimal quoteVolume;
	private Long count;

	public BinanceCandle()
	{
		//
	}

	public BinanceCandle(long openTime, BigDecimal openPrice, BigDecimal highPrice, BigDecimal lowPrice, BigDecimal closePrice, BigDecimal volume, BigDecimal quoteVolume, Long count)
	{
		this.openPrice = openPrice;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.volume = volume;
		this.quoteVolume = quoteVolume;
		this.count = count;
	}

	public long getOpenTime()
	{
		return openTime;
	}

	public void setOpenTime(long openTime)
	{
		this.openTime = openTime;
	}

	public BigDecimal getOpenPrice()
	{
		return openPrice;
	}

	public void setOpenPrice(BigDecimal openPrice)
	{
		this.openPrice = openPrice;
	}

	public BigDecimal getHighPrice()
	{
		return highPrice;
	}

	public void setHighPrice(BigDecimal highPrice)
	{
		this.highPrice = highPrice;
	}

	public BigDecimal getLowPrice()
	{
		return lowPrice;
	}

	public void setLowPrice(BigDecimal lowPrice)
	{
		this.lowPrice = lowPrice;
	}

	public BigDecimal getClosePrice()
	{
		return closePrice;
	}

	public void setClosePrice(BigDecimal closePrice)
	{
		this.closePrice = closePrice;
	}

	public BigDecimal getVolume()
	{
		return volume;
	}

	public void setVolume(BigDecimal volume)
	{
		this.volume = volume;
	}

	public BigDecimal getQuoteVolume()
	{
		return quoteVolume;
	}

	public void setQuoteVolume(BigDecimal quoteVolume)
	{
		this.quoteVolume = quoteVolume;
	}

	public Long getCount()
	{
		return count;
	}

	public void setCount(Long count)
	{
		this.count = count;
	}

	// ---- CALCULATED FIELDS -------------------------------------------------

	public ZonedDateTime getOpenTimeZoned()
	{
		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(openTime), ZoneOffset.UTC);
	}

	// ---- TO STRING ---------------------------------------------------------
	
	@Override
	public String toString()
	{
		return "BinanceCandle [openTime=" + getOpenTimeZoned() + ", openPrice=" + openPrice + ", highPrice=" + highPrice + ", lowPrice=" + lowPrice + ", closePrice=" + closePrice + ", volume=" + volume + ", quoteVolume=" + quoteVolume + ", count=" + count + "]";
	}

}
