package api.client.futures.model.sync;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order
{
	private Long orderId;
	private String clientOrderId;
	private String symbol;
	private BigDecimal cumQty;
	private BigDecimal cumQuote;
	private BigDecimal executedQty;
	private BigDecimal avgPrice;
	private BigDecimal origQty;
	private BigDecimal price;
	private Boolean reduceOnly;
	private String side;
	private String positionSide;
	private String status;
	private BigDecimal stopPrice;
	private String closePosition;
	private String timeInForce;
	private String type;
	private String origType;
	private BigDecimal activationPrice;
	private BigDecimal priceRate;
	private Long updateTime;
	private String workingType;
	private Boolean priceProtect;

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

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getCumQty()
	{
		return cumQty;
	}

	public void setCumQty(BigDecimal cumQty)
	{
		this.cumQty = cumQty;
	}

	public BigDecimal getCumQuote()
	{
		return cumQuote;
	}

	public void setCumQuote(BigDecimal cumQuote)
	{
		this.cumQuote = cumQuote;
	}

	public BigDecimal getExecutedQty()
	{
		return executedQty;
	}

	public void setExecutedQty(BigDecimal executedQty)
	{
		this.executedQty = executedQty;
	}

	public BigDecimal getAvgPrice()
	{
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice)
	{
		this.avgPrice = avgPrice;
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

	public Boolean getReduceOnly()
	{
		return reduceOnly;
	}

	public void setReduceOnly(Boolean reduceOnly)
	{
		this.reduceOnly = reduceOnly;
	}

	public String getSide()
	{
		return side;
	}

	public void setSide(String side)
	{
		this.side = side;
	}

	public String getPositionSide()
	{
		return positionSide;
	}

	public void setPositionSide(String positionSide)
	{
		this.positionSide = positionSide;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public BigDecimal getStopPrice()
	{
		return stopPrice;
	}

	public void setStopPrice(BigDecimal stopPrice)
	{
		this.stopPrice = stopPrice;
	}

	public String getClosePosition()
	{
		return closePosition;
	}

	public void setClosePosition(String closePosition)
	{
		this.closePosition = closePosition;
	}

	public String getTimeInForce()
	{
		return timeInForce;
	}

	public void setTimeInForce(String timeInForce)
	{
		this.timeInForce = timeInForce;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getOrigType()
	{
		return origType;
	}

	public void setOrigType(String origType)
	{
		this.origType = origType;
	}

	public BigDecimal getActivationPrice()
	{
		return activationPrice;
	}

	public void setActivationPrice(BigDecimal activationPrice)
	{
		this.activationPrice = activationPrice;
	}

	public BigDecimal getPriceRate()
	{
		return priceRate;
	}

	public void setPriceRate(BigDecimal priceRate)
	{
		this.priceRate = priceRate;
	}

	public Long getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Long updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getWorkingType()
	{
		return workingType;
	}

	public void setWorkingType(String workingType)
	{
		this.workingType = workingType;
	}

	public Boolean getPriceProtect()
	{
		return priceProtect;
	}

	public void setPriceProtect(Boolean priceProtect)
	{
		this.priceProtect = priceProtect;
	}

}
