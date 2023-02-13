package technicals.model.oscillator;

import technicals.model.TechCandle;

public class AdxEntry extends TechCandle
{
	private double tr1;
	private double posDm1;
	private double negDm1;
	private double trX;
	private double posDmX;
	private double negDmX;
	private double posDI;
	private double negDI;
	private double diffDI;
	private double sumDI;
	private double dx;
	private double adx;
	
	public AdxEntry()
	{
		//
	}

	public AdxEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getTr1()
	{
		return tr1;
	}

	public void setTr1(double tr1)
	{
		this.tr1 = tr1;
	}

	public double getPosDm1()
	{
		return posDm1;
	}

	public void setPosDm1(double posDm1)
	{
		this.posDm1 = posDm1;
	}

	public double getNegDm1()
	{
		return negDm1;
	}

	public void setNegDm1(double negDm1)
	{
		this.negDm1 = negDm1;
	}

	public double getTrX()
	{
		return trX;
	}

	public void setTrX(double trX)
	{
		this.trX = trX;
	}

	public double getPosDmX()
	{
		return posDmX;
	}

	public void setPosDmX(double posDmX)
	{
		this.posDmX = posDmX;
	}

	public double getNegDmX()
	{
		return negDmX;
	}

	public void setNegDmX(double negDmX)
	{
		this.negDmX = negDmX;
	}

	public double getPosDI()
	{
		return posDI;
	}

	public void setPosDI(double posDI)
	{
		this.posDI = posDI;
	}

	public double getNegDI()
	{
		return negDI;
	}

	public void setNegDI(double negDI)
	{
		this.negDI = negDI;
	}

	public double getDiffDI()
	{
		return diffDI;
	}

	public void setDiffDI(double diffDI)
	{
		this.diffDI = diffDI;
	}

	public double getSumDI()
	{
		return sumDI;
	}

	public void setSumDI(double sumDI)
	{
		this.sumDI = sumDI;
	}

	public double getDx()
	{
		return dx;
	}

	public void setDx(double dx)
	{
		this.dx = dx;
	}

	public double getAdx()
	{
		return adx;
	}

	public void setAdx(double adx)
	{
		this.adx = adx;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t\t%f", openTime, closePrice, adx);
	}

	public String toStringDetail()
	{
		return String.format("%s\t%f\t\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f", 
				openTime, closePrice, tr1 , posDm1 , negDm1 , trX , posDmX , negDmX , posDI , negDI , diffDI , sumDI , dx , adx);
	}

}
