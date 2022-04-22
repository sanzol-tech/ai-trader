package sanzol.app.model;

import java.math.BigDecimal;

public class OrderBookElement
{
	private BigDecimal price;
	private BigDecimal qty;
	private BigDecimal sumQty;
	private BigDecimal sumPercent;

	public OrderBookElement()
	{
		super();
	}

	public OrderBookElement(BigDecimal price, BigDecimal qty, BigDecimal sumQty)
	{
		this.price = price;
		this.qty = qty;
		this.sumQty = sumQty;
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

}
