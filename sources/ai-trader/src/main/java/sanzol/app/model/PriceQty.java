package sanzol.app.model;

public class PriceQty
{
	private double priceDist;
	private double qtyIncr;

	public PriceQty(double priceDist, double qtyIncr)
	{
		this.priceDist = priceDist;
		this.qtyIncr = qtyIncr;
	}

	public double getPriceDist()
	{
		return priceDist;
	}

	public void setPriceDist(double priceDist)
	{
		this.priceDist = priceDist;
	}

	public double getQtyIncr()
	{
		return qtyIncr;
	}

	public void setQtyIncr(double qtyIncr)
	{
		this.qtyIncr = qtyIncr;
	}

}
