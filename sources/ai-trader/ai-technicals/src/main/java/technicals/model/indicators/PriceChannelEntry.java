package technicals.model.indicators;

import technicals.model.TechCandle;

public class PriceChannelEntry extends TechCandle
{
	private double upperChannel;
	private double centerLine;
	private double lowerChannel;

	public PriceChannelEntry()
	{
		//
	}

	public PriceChannelEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getUpperChannel()
	{
		return upperChannel;
	}

	public void setUpperChannel(double upperChannel)
	{
		this.upperChannel = upperChannel;
	}

	public double getCenterLine()
	{
		return centerLine;
	}

	public void setCenterLine(double centerLine)
	{
		this.centerLine = centerLine;
	}

	public double getLowerChannel()
	{
		return lowerChannel;
	}

	public void setLowerChannel(double lowerChannel)
	{
		this.lowerChannel = lowerChannel;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t\t%f\t%f\t%f", openTime, closePrice, upperChannel, centerLine, lowerChannel);
	}

}
