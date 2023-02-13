package binance.futures.model.user;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderUpdate
{
	@JsonProperty("s")
	private String symbol;

	@JsonProperty("c")
	private String clientOrderId;

	@JsonProperty("S")
	private String side;

	@JsonProperty("o")
	private String type;

	@JsonProperty("f")
	private String timeInForce;

	@JsonProperty("q")
	private BigDecimal origQty;

	@JsonProperty("p")
	private BigDecimal price;

	@JsonProperty("ap")
	private BigDecimal avgPrice;

	@JsonProperty("sp")
	private BigDecimal stopPrice;

	@JsonProperty("x")
	private String executionType;

	@JsonProperty("X")
	private String orderStatus;

	@JsonProperty("i")
	private Long orderId;

	@JsonProperty("l")
	private BigDecimal lastFilledQty;

	@JsonProperty("z")
	private BigDecimal cumulativeFilledQty;

	@JsonProperty("L")
	private BigDecimal lastFilledPrice;

	@JsonProperty("N")
	private String commissionAsset;

	@JsonProperty("n")
	private BigDecimal commissionAmount;

	@JsonProperty("T")
	private Long orderTradeTime;

	@JsonProperty("t")
	private Long tradeID;

	@JsonProperty("b")
	private BigDecimal bidsNotional;

	@JsonProperty("a")
	private BigDecimal asksNotional;

	@JsonProperty("m")
	private Boolean isMarkerSide;

	@JsonProperty("R")
	private Boolean isReduceOnly;

	@JsonProperty("wt")
	private String workingType;

	@JsonProperty("ps")
	private String positionSide;

	@JsonProperty("AP")
	private BigDecimal activationPrice;

	@JsonProperty("cr")
	private BigDecimal callbackRate;

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getClientOrderId()
	{
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId)
	{
		this.clientOrderId = clientOrderId;
	}

	public String getSide()
	{
		return side;
	}

	public void setSide(String side)
	{
		this.side = side;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getTimeInForce()
	{
		return timeInForce;
	}

	public void setTimeInForce(String timeInForce)
	{
		this.timeInForce = timeInForce;
	}

	public BigDecimal getOrigQty()
	{
		return origQty;
	}

	public void setOrigQty(BigDecimal origQty)
	{
		this.origQty = origQty;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getAvgPrice()
	{
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice)
	{
		this.avgPrice = avgPrice;
	}

	public BigDecimal getStopPrice()
	{
		return stopPrice;
	}

	public void setStopPrice(BigDecimal stopPrice)
	{
		this.stopPrice = stopPrice;
	}

	public String getExecutionType()
	{
		return executionType;
	}

	public void setExecutionType(String executionType)
	{
		this.executionType = executionType;
	}

	public String getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public Long getOrderId()
	{
		return orderId;
	}

	public void setOrderId(Long orderId)
	{
		this.orderId = orderId;
	}

	public BigDecimal getLastFilledQty()
	{
		return lastFilledQty;
	}

	public void setLastFilledQty(BigDecimal lastFilledQty)
	{
		this.lastFilledQty = lastFilledQty;
	}

	public BigDecimal getCumulativeFilledQty()
	{
		return cumulativeFilledQty;
	}

	public void setCumulativeFilledQty(BigDecimal cumulativeFilledQty)
	{
		this.cumulativeFilledQty = cumulativeFilledQty;
	}

	public BigDecimal getLastFilledPrice()
	{
		return lastFilledPrice;
	}

	public void setLastFilledPrice(BigDecimal lastFilledPrice)
	{
		this.lastFilledPrice = lastFilledPrice;
	}

	public String getCommissionAsset()
	{
		return commissionAsset;
	}

	public void setCommissionAsset(String commissionAsset)
	{
		this.commissionAsset = commissionAsset;
	}

	public BigDecimal getCommissionAmount()
	{
		return commissionAmount;
	}

	public void setCommissionAmount(BigDecimal commissionAmount)
	{
		this.commissionAmount = commissionAmount;
	}

	public Long getOrderTradeTime()
	{
		return orderTradeTime;
	}

	public void setOrderTradeTime(Long orderTradeTime)
	{
		this.orderTradeTime = orderTradeTime;
	}

	public Long getTradeID()
	{
		return tradeID;
	}

	public void setTradeID(Long tradeID)
	{
		this.tradeID = tradeID;
	}

	public BigDecimal getBidsNotional()
	{
		return bidsNotional;
	}

	public void setBidsNotional(BigDecimal bidsNotional)
	{
		this.bidsNotional = bidsNotional;
	}

	public BigDecimal getAsksNotional()
	{
		return asksNotional;
	}

	public void setAsksNotional(BigDecimal asksNotional)
	{
		this.asksNotional = asksNotional;
	}

	public Boolean getIsMarkerSide()
	{
		return isMarkerSide;
	}

	public void setIsMarkerSide(Boolean isMarkerSide)
	{
		this.isMarkerSide = isMarkerSide;
	}

	public Boolean getIsReduceOnly()
	{
		return isReduceOnly;
	}

	public void setIsReduceOnly(Boolean isReduceOnly)
	{
		this.isReduceOnly = isReduceOnly;
	}

	public String getWorkingType()
	{
		return workingType;
	}

	public void setWorkingType(String workingType)
	{
		this.workingType = workingType;
	}

	public String getPositionSide()
	{
		return positionSide;
	}

	public void setPositionSide(String positionSide)
	{
		this.positionSide = positionSide;
	}

	public BigDecimal getActivationPrice()
	{
		return activationPrice;
	}

	public void setActivationPrice(BigDecimal activationPrice)
	{
		this.activationPrice = activationPrice;
	}

	public BigDecimal getCallbackRate()
	{
		return callbackRate;
	}

	public void setCallbackRate(BigDecimal callbackRate)
	{
		this.callbackRate = callbackRate;
	}

	@Override
	public String toString()
	{
		return "OrderUpdate [symbol=" + symbol + ", clientOrderId=" + clientOrderId + ", side=" + side + ", origQty=" + origQty + ", price=" + price + ", avgPrice=" + avgPrice + ", stopPrice="
				+ stopPrice + ", orderStatus=" + orderStatus + ", orderId=" + orderId + ", isReduceOnly=" + isReduceOnly + ", positionSide=" + positionSide + "]";
	}


}
