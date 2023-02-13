package aitrader.core.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import aitrader.util.price.PriceUtil;

public class Signal
{
	private String symbol;
	private String orderSide;
	private BigDecimal limitPrice;
	private BigDecimal slPrice;
	private BigDecimal slDist;
	private BigDecimal tpPrice;
	private BigDecimal tpDist;
	private long createdAt;

	public Signal()
	{
		//
	}

	public Signal(String symbol, String orderSide, BigDecimal limitPrice, BigDecimal slPrice, BigDecimal tpPrice)
	{
		this.symbol = symbol;
		this.orderSide = orderSide;
		this.limitPrice = limitPrice;
		this.slPrice = slPrice;
		this.tpPrice = tpPrice;

		if ("SELL".equals(orderSide))
		{
			this.slDist = PriceUtil.priceDistUp(limitPrice, slPrice, true);
			this.tpDist = PriceUtil.priceDistDown(limitPrice, tpPrice, true);
		} 
		else
		{
			this.slDist = PriceUtil.priceDistDown(limitPrice, slPrice, true);
			this.tpDist = PriceUtil.priceDistUp(limitPrice, tpPrice, true);
		}

		this.createdAt = System.currentTimeMillis();
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

	public BigDecimal getLimitPrice()
	{
		return limitPrice;
	}

	public void setLimitPrice(BigDecimal limitPrice)
	{
		this.limitPrice = limitPrice;
	}

	public BigDecimal getSlPrice()
	{
		return slPrice;
	}

	public void setSlPrice(BigDecimal slPrice)
	{
		this.slPrice = slPrice;
	}

	public BigDecimal getSlDist()
	{
		return slDist;
	}

	public void setSlDist(BigDecimal slDist)
	{
		this.slDist = slDist;
	}

	public BigDecimal getTpPrice()
	{
		return tpPrice;
	}

	public void setTpPrice(BigDecimal tpPrice)
	{
		this.tpPrice = tpPrice;
	}

	public BigDecimal getTpDist()
	{
		return tpDist;
	}

	public void setTpDist(BigDecimal tpDist)
	{
		this.tpDist = tpDist;
	}

	public long getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(long createdAt)
	{
		this.createdAt = createdAt;
	}

	// ---- CALCULATED FIELDS ---------------------------------------------
	
	public BigDecimal getRatio()
	{
		return tpDist.divide(slDist, 1, RoundingMode.HALF_UP);
	}

	// ---- TO STRING -----------------------------------------------------

	@Override
	public String toString()
	{
		return "Signal [symbol=" + symbol + ", orderSide=" + orderSide + ", limitPrice=" + limitPrice + ", slPrice=" + slPrice + ", tpPrice=" + tpPrice + "]";
	}

}
