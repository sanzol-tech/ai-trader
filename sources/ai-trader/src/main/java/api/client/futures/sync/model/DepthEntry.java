package api.client.futures.sync.model;

import java.math.BigDecimal;

public class DepthEntry
{
	private BigDecimal price;
	private BigDecimal qty;

	public DepthEntry()
	{
		//
	}

	public DepthEntry(BigDecimal price, BigDecimal qty)
	{
		this.price = price;
		this.qty = qty;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	@Override
	public String toString()
	{
		return String.format("%f;%f", price, qty);
	}

}
