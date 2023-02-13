package technicals.model.oscillator;

import technicals.model.TechCandle;

public class StochasticEntry extends TechCandle
{
	private double highestHigh;
	private double lowestLow;
	private double k1;
	private double k;
	private double d;

	public StochasticEntry()
	{
		//
	}

	public StochasticEntry(TechCandle candle)
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

	public double getK1()
	{
		return k1;
	}

	public void setK1(double k1)
	{
		this.k1 = k1;
	}

	public double getK()
	{
		return k;
	}

	public void setK(double k)
	{
		this.k = k;
	}

	public double getD()
	{
		return d;
	}

	public void setD(double d)
	{
		this.d = d;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t\t%f\t%f\t%f\t%f\t%f", openTime, closePrice, highestHigh, lowestLow, k1, k, d);
	}

}
