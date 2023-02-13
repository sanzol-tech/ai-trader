package technicals.model;

import java.time.ZonedDateTime;

public class TechCandle
{
	protected ZonedDateTime openTime;
	protected double openPrice;
	protected double highPrice;
	protected double lowPrice;
	protected double closePrice;
	protected double volume;
	protected double quoteVolume;
	protected long count;

	public TechCandle()
	{
		//
	}

	public TechCandle(ZonedDateTime openTime, double openPrice, double highPrice, double lowPrice, double closePrice, double volume)
	{
		this.openTime = openTime;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.volume = volume;
	}

	public ZonedDateTime getOpenTime()
	{
		return openTime;
	}

	public void setOpenTime(ZonedDateTime openTime)
	{
		this.openTime = openTime;
	}

	public double getOpenPrice()
	{
		return openPrice;
	}

	public void setOpenPrice(double openPrice)
	{
		this.openPrice = openPrice;
	}

	public double getHighPrice()
	{
		return highPrice;
	}

	public void setHighPrice(double highPrice)
	{
		this.highPrice = highPrice;
	}

	public double getLowPrice()
	{
		return lowPrice;
	}

	public void setLowPrice(double lowPrice)
	{
		this.lowPrice = lowPrice;
	}

	public double getClosePrice()
	{
		return closePrice;
	}

	public void setClosePrice(double closePrice)
	{
		this.closePrice = closePrice;
	}

	public double getVolume()
	{
		return volume;
	}

	public void setVolume(double volume)
	{
		this.volume = volume;
	}

	public double getQuoteVolume()
	{
		return quoteVolume;
	}

	public void setQuoteVolume(double quoteVolume)
	{
		this.quoteVolume = quoteVolume;
	}

	public long getCount()
	{
		return count;
	}

	public void setCount(long count)
	{
		this.count = count;
	}

	// ---- CALCULATED FIELDS -------------------------------------------------

	public double getHL2()
	{
		return (highPrice + lowPrice) / 2;
	}

	public double getHLC3()
	{
		return (highPrice + lowPrice + closePrice) / 3;
	}

	public double getOHLC4()
	{
		return (openPrice + highPrice + lowPrice + closePrice) / 4;
	}

	// ---- DEFAULT VALUE -----------------------------------------------------

	public double getDefaultPrice()
	{
		return closePrice;
	}

	// ---- TO STRING ---------------------------------------------------------

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t%f\t%f\t%f\t%f", openTime, openPrice, highPrice, lowPrice, closePrice, volume);
	}

}
