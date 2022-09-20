package sanzol.aitrader.be.model;

import java.math.BigDecimal;

public class OrderBookElement
{
	private BigDecimal price;
	private BigDecimal qty;
	private BigDecimal sumQty;
	private BigDecimal sumPercent;
	private BigDecimal distance;

	public OrderBookElement()
	{
		//
	}

	public OrderBookElement(BigDecimal price, BigDecimal qty, BigDecimal sumQty, BigDecimal sumPercent, BigDecimal distance)
	{
		this.price = price;
		this.qty = qty;
		this.sumQty = sumQty;
		this.sumPercent = sumPercent;
		this.distance = distance;
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

	public BigDecimal getSumQty()
	{
		return sumQty;
	}

	public void setSumQty(BigDecimal sumQty)
	{
		this.sumQty = sumQty;
	}

	public BigDecimal getSumPercent()
	{
		return sumPercent;
	}

	public void setSumPercent(BigDecimal sumPercent)
	{
		this.sumPercent = sumPercent;
	}

	public BigDecimal getDistance()
	{
		return distance;
	}

	public void setDistance(BigDecimal distance)
	{
		this.distance = distance;
	}

	@Override
	public String toString()
	{
		return String.format("%f;%f;%f;%f;%f", price, qty, sumQty, sumPercent, distance);
	}

}
