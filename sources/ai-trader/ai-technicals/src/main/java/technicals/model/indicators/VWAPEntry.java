package technicals.model.indicators;

import technicals.model.TechCandle;

public class VWAPEntry extends TechCandle
{
	private double tpv;
	private double vwap;

	public VWAPEntry()
	{
		//
	}

	public VWAPEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getTpv()
	{
		return tpv;
	}

	public void setTpv(double tpv)
	{
		this.tpv = tpv;
	}

	public double getVwap()
	{
		return vwap;
	}

	public void setVwap(double vwap)
	{
		this.vwap = vwap;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t\t%f\t%f", openTime, closePrice, tpv, vwap);
	}

}
