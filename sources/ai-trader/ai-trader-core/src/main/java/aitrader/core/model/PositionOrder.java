package aitrader.core.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;

import aitrader.core.model.enums.OpType;

public class PositionOrder
{
	private Long orderId;
	private Symbol symbol;
	private ZonedDateTime time;
	private String  orderSide;
	private OpType opType;
	private BigDecimal price;
	private BigDecimal quantity;
	private Boolean reduceOnly;
	private String status;

	public Long getOrderId()
	{
		return orderId;
	}

	public void setOrderId(Long orderId)
	{
		this.orderId = orderId;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public ZonedDateTime getTime()
	{
		return time;
	}

	public void setTime(ZonedDateTime time)
	{
		this.time = time;
	}

	public String getOrderSide()
	{
		return orderSide;
	}

	public void setOrderSide(String orderSide)
	{
		this.orderSide = orderSide;
	}

	public OpType getOpType()
	{
		return opType;
	}

	public void setOpType(OpType opType)
	{
		this.opType = opType;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getQuantity()
	{
		return quantity;
	}

	public void setQuantity(BigDecimal quantity)
	{
		this.quantity = quantity;
	}

	public Boolean getReduceOnly()
	{
		return reduceOnly;
	}

	public void setReduceOnly(Boolean reduceOnly)
	{
		this.reduceOnly = reduceOnly;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	// ---- CALCULATED FIELDS ---------------------------------------------

	public BigDecimal getUsd()
	{
		return quantity.multiply(price).setScale(2, RoundingMode.HALF_UP);
	}

}
