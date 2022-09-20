package api.client.futures.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionRisk
{
	private BigDecimal entryPrice;
	private BigDecimal leverage;
	private Double maxNotionalValue;
	private BigDecimal liquidationPrice;
	private BigDecimal markPrice;
	private BigDecimal positionAmt;
	private String symbol;
	private String isolatedMargin;
	private String positionSide;
	private String marginType;
	private BigDecimal unRealizedProfit;

	public BigDecimal getEntryPrice()
	{
		return entryPrice;
	}

	public void setEntryPrice(BigDecimal entryPrice)
	{
		this.entryPrice = entryPrice;
	}

	public BigDecimal getLeverage()
	{
		return leverage;
	}

	public void setLeverage(BigDecimal leverage)
	{
		this.leverage = leverage;
	}

	public Double getMaxNotionalValue()
	{
		return maxNotionalValue;
	}

	public void setMaxNotionalValue(Double maxNotionalValue)
	{
		this.maxNotionalValue = maxNotionalValue;
	}

	public BigDecimal getLiquidationPrice()
	{
		return liquidationPrice;
	}

	public void setLiquidationPrice(BigDecimal liquidationPrice)
	{
		this.liquidationPrice = liquidationPrice;
	}

	public BigDecimal getMarkPrice()
	{
		return markPrice;
	}

	public void setMarkPrice(BigDecimal markPrice)
	{
		this.markPrice = markPrice;
	}

	public BigDecimal getPositionAmt()
	{
		return positionAmt;
	}

	public void setPositionAmt(BigDecimal positionAmt)
	{
		this.positionAmt = positionAmt;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getIsolatedMargin()
	{
		return isolatedMargin;
	}

	public void setIsolatedMargin(String isolatedMargin)
	{
		this.isolatedMargin = isolatedMargin;
	}

	public String getPositionSide()
	{
		return positionSide;
	}

	public void setPositionSide(String positionSide)
	{
		this.positionSide = positionSide;
	}

	public String getMarginType()
	{
		return marginType;
	}

	public void setMarginType(String marginType)
	{
		this.marginType = marginType;
	}

	public BigDecimal getUnRealizedProfit()
	{
		return unRealizedProfit;
	}

	public void setUnRealizedProfit(BigDecimal unRealizedProfit)
	{
		this.unRealizedProfit = unRealizedProfit;
	}

}
