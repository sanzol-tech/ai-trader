package aitrader.core.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import aitrader.util.price.PriceUtil;
import binance.futures.enums.PositionSide;

public class Position
{
	private Symbol symbol;
	private boolean isOpen;
	private PositionSide positionSide;
	private String marginType;
	private BigDecimal leverage;
	private BigDecimal entryPrice;
	private BigDecimal markPrice;
	private BigDecimal liqPrice;
	private BigDecimal quantity;
	private BigDecimal pnl;

	private List<PositionOrder> lstOrders = new ArrayList<PositionOrder>();

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public boolean isOpen()
	{
		return isOpen;
	}

	public void setOpen(boolean isOpen)
	{
		this.isOpen = isOpen;
	}

	public PositionSide getPositionSide()
	{
		return positionSide;
	}

	public void setPositionSide(PositionSide positionSide)
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

	public BigDecimal getLeverage()
	{
		return leverage;
	}

	public void setLeverage(BigDecimal leverage)
	{
		this.leverage = leverage;
	}

	public BigDecimal getEntryPrice()
	{
		return entryPrice;
	}

	public void setEntryPrice(BigDecimal entryPrice)
	{
		this.entryPrice = entryPrice;
	}

	public BigDecimal getMarkPrice()
	{
		return markPrice;
	}

	public void setMarkPrice(BigDecimal markPrice)
	{
		this.markPrice = markPrice;
	}

	public BigDecimal getLiqPrice()
	{
		return liqPrice;
	}

	public void setLiqPrice(BigDecimal liqPrice)
	{
		this.liqPrice = liqPrice;
	}

	public BigDecimal getQuantity()
	{
		return quantity;
	}

	public void setQuantity(BigDecimal quantity)
	{
		this.quantity = quantity;
	}

	public BigDecimal getPnl()
	{
		return pnl;
	}

	public void setPnl(BigDecimal pnl)
	{
		this.pnl = pnl;
	}

	public List<PositionOrder> getLstOrders()
	{
		return lstOrders;
	}

	public void setLstOrders(List<PositionOrder> lstOrders)
	{
		this.lstOrders = lstOrders;
	}

	// ---- CALCULATED FIELDS ---------------------------------------------

	public BigDecimal getUsd()
	{
		return quantity.abs().multiply(entryPrice).setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getDistance()
	{
		if (entryPrice == null || markPrice == null) 
			return null;
		
		if (quantity.doubleValue() < 0)
			return PriceUtil.priceDistDown(markPrice, entryPrice, false);
		else
			return PriceUtil.priceDistUp(markPrice, entryPrice, false);
	}

}
