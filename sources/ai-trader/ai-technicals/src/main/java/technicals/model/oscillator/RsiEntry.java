package technicals.model.oscillator;

import technicals.model.TechCandle;

public class RsiEntry extends TechCandle
{
	private double change;
	private double gain;
	private double loss;
	private double avgGain;
	private double avgLoss;
	private double rs;
	private double rsi;

	public RsiEntry()
	{
		//
	}

	public RsiEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getChange()
	{
		return change;
	}

	public void setChange(double change)
	{
		this.change = change;
	}

	public double getGain()
	{
		return gain;
	}

	public void setGain(double gain)
	{
		this.gain = gain;
	}

	public double getLoss()
	{
		return loss;
	}

	public void setLoss(double loss)
	{
		this.loss = loss;
	}

	public double getAvgGain()
	{
		return avgGain;
	}

	public void setAvgGain(double avgGain)
	{
		this.avgGain = avgGain;
	}

	public double getAvgLoss()
	{
		return avgLoss;
	}

	public void setAvgLoss(double avgLoss)
	{
		this.avgLoss = avgLoss;
	}

	public double getRs()
	{
		return rs;
	}

	public void setRs(double rs)
	{
		this.rs = rs;
	}

	public double getRsi()
	{
		return rsi;
	}

	public void setRsi(double rsi)
	{
		this.rsi = rsi;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f", openTime, closePrice, change, gain, loss, avgGain, avgLoss, rs, rsi);
		// return String.format("%s\t%f\t%f", openTime, closePrice, rsi);
	}

}
