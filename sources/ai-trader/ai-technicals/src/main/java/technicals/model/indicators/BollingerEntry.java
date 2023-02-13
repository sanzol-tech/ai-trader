package technicals.model.indicators;

import technicals.model.TechCandle;

public class BollingerEntry extends TechCandle
{
	private double standardDeviation;
	private double upperBand;
	private double middleBand;
	private double lowerBand;

	public BollingerEntry()
	{
		//
	}

	public BollingerEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getStandardDeviation()
	{
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation)
	{
		this.standardDeviation = standardDeviation;
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
		return String.format("%s\t%f\t\t%f\t%f\t%f\t%f", openTime, closePrice, standardDeviation, upperBand, middleBand, lowerBand);
	}

}
