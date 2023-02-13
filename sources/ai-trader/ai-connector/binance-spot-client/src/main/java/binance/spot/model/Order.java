package binance.spot.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import binance.spot.enums.OrderSide;
import binance.spot.enums.OrderStatus;
import binance.spot.enums.OrderType;
import binance.spot.enums.TimeInForce;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order
{

	private String symbol;
	private Long orderId;
	private String clientOrderId;
	private String orderListId;
	private BigDecimal price;
	private BigDecimal origQty;
	private String executedQty;
	private OrderStatus status;
	private TimeInForce timeInForce;
	private OrderType type;
	private OrderSide side;
	private BigDecimal stopPrice;
	private String icebergQty;
	private long time;
	private String cummulativeQuoteQty;
	private long updateTime;
	private boolean isWorking;
	private String origQuoteOrderQty;

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public Long getOrderId()
	{
		return orderId;
	}

	public void setOrderId(Long orderId)
	{
		this.orderId = orderId;
	}

	public String getClientOrderId()
	{
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId)
	{
		this.clientOrderId = clientOrderId;
	}

	public String getOrderListId()
	{
		return orderListId;
	}

	public void setOrderListId(String orderListId)
	{
		this.orderListId = orderListId;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getOrigQty()
	{
		return origQty;
	}

	public void setOrigQty(BigDecimal origQty)
	{
		this.origQty = origQty;
	}

	public String getExecutedQty()
	{
		return executedQty;
	}

	public void setExecutedQty(String executedQty)
	{
		this.executedQty = executedQty;
	}

	public OrderStatus getStatus()
	{
		return status;
	}

	public void setStatus(OrderStatus status)
	{
		this.status = status;
	}

	public TimeInForce getTimeInForce()
	{
		return timeInForce;
	}

	public void setTimeInForce(TimeInForce timeInForce)
	{
		this.timeInForce = timeInForce;
	}

	public OrderType getType()
	{
		return type;
	}

	public void setType(OrderType type)
	{
		this.type = type;
	}

	public OrderSide getSide()
	{
		return side;
	}

	public void setSide(OrderSide side)
	{
		this.side = side;
	}

	public BigDecimal getStopPrice()
	{
		return stopPrice;
	}

	public void setStopPrice(BigDecimal stopPrice)
	{
		this.stopPrice = stopPrice;
	}

	public String getIcebergQty()
	{
		return icebergQty;
	}

	public void setIcebergQty(String icebergQty)
	{
		this.icebergQty = icebergQty;
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public String getCummulativeQuoteQty()
	{
		return cummulativeQuoteQty;
	}

	public void setCummulativeQuoteQty(String cummulativeQuoteQty)
	{
		this.cummulativeQuoteQty = cummulativeQuoteQty;
	}

	public long getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(long updateTime)
	{
		this.updateTime = updateTime;
	}

	public boolean isWorking()
	{
		return isWorking;
	}

	public void setWorking(boolean isWorking)
	{
		this.isWorking = isWorking;
	}

	public String getOrigQuoteOrderQty()
	{
		return origQuoteOrderQty;
	}

	public void setOrigQuoteOrderQty(String origQuoteOrderQty)
	{
		this.origQuoteOrderQty = origQuoteOrderQty;
	}

	@Override
	public String toString()
	{
		return "Order [symbol=" + symbol + ", orderId=" + orderId + ", clientOrderId=" + clientOrderId + ", orderListId=" + orderListId + ", price=" + price + ", origQty=" + origQty + ", executedQty="
				+ executedQty + ", status=" + status + ", timeInForce=" + timeInForce + ", type=" + type + ", side=" + side + ", stopPrice=" + stopPrice + ", icebergQty=" + icebergQty + ", time="
				+ time + ", cummulativeQuoteQty=" + cummulativeQuoteQty + ", updateTime=" + updateTime + ", isWorking=" + isWorking + ", origQuoteOrderQty=" + origQuoteOrderQty + "]";
	}

}
