package technicals.model.oscillator;

import technicals.model.TechCandle;

public class WilliamsREntry extends TechCandle
{
	private double highestHigh;
	private double lowestLow;
	private double r;

	public WilliamsREntry()
	{
		//
	}

	public WilliamsREntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getHighestHigh()
	{
		return highestHigh;
	}

	public void setHighestHigh(double highestHigh)
	{
		this.highestHigh = highestHigh;
	}

	public double getLowestLow()
	{
		return lowestLow;
	}

	public void setLowestLow(double lowestLow)
	{
		this.lowestLow = lowestLow;
	}

	public double getR()
	{
		return r;
	}

	public void setR(double r)
	{
		this.r = r;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t\t%f\t%f\t%f", openTime, closePrice, highestHigh, lowestLow, r);
	}

}
