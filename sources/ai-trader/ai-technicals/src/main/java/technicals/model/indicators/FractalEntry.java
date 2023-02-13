package technicals.model.indicators;

import technicals.model.TechCandle;

public class FractalEntry extends TechCandle
{
	private boolean isHigh;
	private boolean isLow;

	public FractalEntry()
	{
		//
	}

	public FractalEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public boolean isHigh()
	{
		return isHigh;
	}

	public void setHigh(boolean isHigh)
	{
		this.isHigh = isHigh;
	}

	public boolean isLow()
	{
		return isLow;
	}

	public void setLow(boolean isLow)
	{
		this.isLow = isLow;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t%f\t%s\t%s", openTime, highPrice, lowPrice, isHigh ? "HIGH" : "", isLow ? "LOW" : "");
	}

}
