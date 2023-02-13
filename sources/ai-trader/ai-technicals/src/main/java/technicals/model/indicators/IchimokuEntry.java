package technicals.model.indicators;

import technicals.model.TechCandle;

public class IchimokuEntry extends TechCandle
{
	private double conversionLine;
	private double baseLine;
	private double leadingSpanA;
	private double leadingSpanB;

	public IchimokuEntry()
	{
		//
	}

	public IchimokuEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getConversionLine()
	{
		return conversionLine;
	}

	public void setConversionLine(double conversionLine)
	{
		this.conversionLine = conversionLine;
	}

	public double getBaseLine()
	{
		return baseLine;
	}

	public void setBaseLine(double baseLine)
	{
		this.baseLine = baseLine;
	}

	public double getLeadingSpanA()
	{
		return leadingSpanA;
	}

	public void setLeadingSpanA(double leadingSpanA)
	{
		this.leadingSpanA = leadingSpanA;
	}

	public double getLeadingSpanB()
	{
		return leadingSpanB;
	}

	public void setLeadingSpanB(double leadingSpanB)
	{
		this.leadingSpanB = leadingSpanB;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t\t%f\t%f\t%f\t%f", openTime, closePrice, conversionLine, baseLine, leadingSpanA, leadingSpanB);
	}

}
