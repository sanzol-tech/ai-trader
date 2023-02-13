package technicals.model.oscillator;

import technicals.model.TechCandle;

public class StochRsiEntry extends TechCandle
{
	private double rsi;
	private double k1;
	private double k;
	private double d;

	public StochRsiEntry()
	{
		//
	}

	public StochRsiEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getRsi()
	{
		return rsi;
	}

	public void setRsi(double rsi)
	{
		this.rsi = rsi;
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
		return String.format("%s\t%f\t\t%f\t%f\t%f\t%f", openTime, closePrice, rsi, k1, k, d);
	}

}
