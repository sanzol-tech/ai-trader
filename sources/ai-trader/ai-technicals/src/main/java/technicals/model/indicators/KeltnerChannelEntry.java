package technicals.model.indicators;

import technicals.model.TechCandle;

public class KeltnerChannelEntry extends TechCandle
{
	private double atr;
	private double upperBand;
	private double middleBand;
	private double lowerBand;

	public KeltnerChannelEntry()
	{
		//
	}

	public KeltnerChannelEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getAtr()
	{
		return atr;
	}

	public void setAtr(double atr)
	{
		this.atr = atr;
	}

	public double getUpperBand()
	{
		return upperBand;
	}

	public void setUpperBand(double upperBand)
	{
		this.upperBand = upperBand;
	}

	public double getMiddleBand()
	{
		return middleBand;
	}

	public void setMiddleBand(double middleBand)
	{
		this.middleBand = middleBand;
	}

	public double getLowerBand()
	{
		return lowerBand;
	}

	public void setLowerBand(double lowerBand)
	{
		this.lowerBand = lowerBand;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t\t%f\t%f\t%f\t%f", openTime, closePrice, atr, upperBand, middleBand, lowerBand);
	}

}
