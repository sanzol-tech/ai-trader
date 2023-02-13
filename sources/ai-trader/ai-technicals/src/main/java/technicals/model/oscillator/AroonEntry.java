package technicals.model.oscillator;

import technicals.model.TechCandle;

public class AroonEntry extends TechCandle
{
	private double aroonUp;
	private double aroonDown;
	private double oscillator;

	public AroonEntry()
	{
		//
	}

	public AroonEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
	}

	public double getAroonUp()
	{
		return aroonUp;
	}

	public void setAroonUp(double aroonUp)
	{
		this.aroonUp = aroonUp;
	}

	public double getAroonDown()
	{
		return aroonDown;
	}

	public void setAroonDown(double aroonDown)
	{
		this.aroonDown = aroonDown;
	}

	public double getOscillator()
	{
		return oscillator;
	}

	public void setOscillator(double oscillator)
	{
		this.oscillator = oscillator;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t%f\t\t%f\t%f\t%f", openTime, highPrice, lowPrice, aroonUp, aroonDown, oscillator);
	}

}
