package technicals.model.oscillator;

import technicals.model.TechCandle;

public class MACDEntry extends TechCandle
{
	private double emaFast;
	private double emaSlow;
	private double macd;
	private double signal;
	private double histogram;

	public MACDEntry()
	{
		//
	}

	public MACDEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public MACDEntry(TechCandle candle, double emaFast, double emaSlow, double macd, double signal, double histogram)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();

		this.emaFast = emaFast;
		this.emaSlow = emaSlow;
		this.macd = macd;
		this.signal = signal;
		this.histogram = histogram;
	}

	public double getEmaFast()
	{
		return emaFast;
	}

	public void setEmaFast(double emaFast)
	{
		this.emaFast = emaFast;
	}

	public double getEmaSlow()
	{
		return emaSlow;
	}

	public void setEmaSlow(double emaSlow)
	{
		this.emaSlow = emaSlow;
	}

	public double getMacd()
	{
		return macd;
	}

	public void setMacd(double macd)
	{
		this.macd = macd;
	}

	public double getSignal()
	{
		return signal;
	}

	public void setSignal(double signal)
	{
		this.signal = signal;
	}

	public double getHistogram()
	{
		return histogram;
	}

	public void setHistogram(double histogram)
	{
		this.histogram = histogram;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t\t%f\t%f\t%f\t%f\t%f", openTime, closePrice, emaFast, emaSlow, macd, signal, histogram);
	}

}
