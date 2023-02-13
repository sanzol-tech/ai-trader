package aitrader.core.model;

import java.beans.Transient;
import java.math.BigDecimal;

import aitrader.core.model.enums.QuantityType;

public class Alert
{
	private Symbol symbol;
	private String orderSide;
	private BigDecimal alertPrice;
	private BigDecimal limitPrice;
	private QuantityType quantityType;
	private BigDecimal quantity;
	private Long expirationAt;
	private boolean alerted;

	private BigDecimal alertDistance;
	private BigDecimal limitDistance;

	public Alert()
	{
		//
	}

	public Alert(Symbol symbol, String orderSide, BigDecimal alertPrice, BigDecimal limitPrice, QuantityType quantityType, BigDecimal quantity, Long expirationAt)
	{
		this.symbol = symbol;
		this.orderSide = orderSide;
		this.alertPrice = alertPrice;
		this.limitPrice = limitPrice;
		this.quantityType = quantityType;
		this.quantity = quantity;
		this.expirationAt = expirationAt != null ? System.currentTimeMillis() + expirationAt : null;
		this.alerted = false;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
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

	public BigDecimal getAlertPrice()
	{
		return alertPrice;
	}

	public void setAlertPrice(BigDecimal alertPrice)
	{
		this.alertPrice = alertPrice;
	}

	public BigDecimal getLimitPrice()
	{
		return limitPrice;
	}

	public void setLimitPrice(BigDecimal limitPrice)
	{
		this.limitPrice = limitPrice;
	}

	public Long getExpirationAt()
	{
		return expirationAt;
	}

	public QuantityType getQuantityType()
	{
		return quantityType;
	}

	public void setQuantityType(QuantityType quantityType)
	{
		this.quantityType = quantityType;
	}

	public BigDecimal getQuantity()
	{
		return quantity;
	}

	public void setQuantity(BigDecimal quantity)
	{
		this.quantity = quantity;
	}

	public void setExpirationAt(Long expirationAt)
	{
		this.expirationAt = expirationAt;
	}

	public boolean isAlerted()
	{
		return alerted;
	}

	public void setAlerted(boolean alerted)
	{
		this.alerted = alerted;
	}

	@Transient
	public BigDecimal getAlertDistance()
	{
		return alertDistance;
	}

	public void setAlertDistance(BigDecimal alertDistance)
	{
		this.alertDistance = alertDistance;
	}

	@Transient
	public BigDecimal getLimitDistance()
	{
		return limitDistance;
	}

	public void setLimitDistance(BigDecimal limitDistance)
	{
		this.limitDistance = limitDistance;
	}

	@Override
	public String toString()
	{
		return "Alert [symbol=" + symbol + ", orderSide=" + orderSide + ", limitPrice=" + limitPrice + ", quantityType=" + quantityType + ", quantity=" + quantity + "]";
	}

}
