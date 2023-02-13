package aitrader.core.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class VirtualOrder
{
	private String symbol;
	private String orderSide;
	private BigDecimal activationPrice;
	private BigDecimal limitPrice;
	private BigDecimal qty;
	private ZonedDateTime createdAt;
	private ZonedDateTime expirationAt;

	public VirtualOrder()
	{
		//
	}

	public VirtualOrder(String symbol, String orderSide, BigDecimal activationPrice, BigDecimal limitPrice, BigDecimal qty)
	{
		this.symbol = symbol;
		this.orderSide = orderSide;
		this.activationPrice = activationPrice;
		this.limitPrice = limitPrice;
		this.qty = qty;
		this.createdAt = ZonedDateTime.now();
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getOrderSide()
	{
		return orderSide;
	}

	public void setOrderSide(String orderSide)
	{
		this.orderSide = orderSide;
	}

	public BigDecimal getActivationPrice()
	{
		return activationPrice;
	}

	public void setActivationPrice(BigDecimal activationPrice)
	{
		this.activationPrice = activationPrice;
	}

	public BigDecimal getLimitPrice()
	{
		return limitPrice;
	}

	public void setLimitPrice(BigDecimal limitPrice)
	{
		this.limitPrice = limitPrice;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public ZonedDateTime getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(ZonedDateTime createdAt)
	{
		this.createdAt = createdAt;
	}

	public ZonedDateTime getExpirationAt()
	{
		return expirationAt;
	}

	public void setExpirationAt(ZonedDateTime expirationAt)
	{
		this.expirationAt = expirationAt;
	}

	@Override
	public String toString()
	{
		return "VirtualOrder [symbol=" + symbol + ", orderSide=" + orderSide + ", activationPrice=" + activationPrice + ", limitPrice=" + limitPrice + ", qty=" + qty + "]";
	}

}
