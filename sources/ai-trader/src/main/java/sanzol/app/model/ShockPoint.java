package sanzol.app.model;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import sanzol.app.service.Symbol;
import sanzol.app.util.PriceUtil;

public class ShockPoint
{
	public static final Long EXPIRATION_MILLIS = TimeUnit.MINUTES.toMillis(120);

	private Symbol symbol;
	private BigDecimal shShock;
	private BigDecimal lgShock;
	private BigDecimal shortTProfit;
	private BigDecimal longTProfit;
	private Long expirationTime;

	public ShockPoint()
	{
		//
	}

	public static ShockPoint NULL(Symbol symbol)
	{
		return new ShockPoint(symbol, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, System.currentTimeMillis() + EXPIRATION_MILLIS);
	}

	public ShockPoint(Symbol symbol, BigDecimal shShock, BigDecimal lgShock)
	{
		this.symbol = symbol;
		this.shShock = shShock;
		this.lgShock = lgShock;
		this.shortTProfit =  PriceUtil.priceDistDown(shShock, lgShock, true);
		this.longTProfit =  PriceUtil.priceDistUp(lgShock, shShock, true);
		this.expirationTime = System.currentTimeMillis() + EXPIRATION_MILLIS;
	}

	public ShockPoint(Symbol symbol, BigDecimal shShock, BigDecimal lgShock, BigDecimal shTarget, BigDecimal lgTarget, Long expirationTime)
	{
		this.symbol = symbol;
		this.shShock = shShock;
		this.lgShock = lgShock;
		this.shortTProfit = shTarget;
		this.longTProfit = lgTarget;
		this.expirationTime = expirationTime;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getShShock()
	{
		return shShock;
	}

	public void setShShock(BigDecimal shShock)
	{
		this.shShock = shShock;
	}

	public BigDecimal getLgShock()
	{
		return lgShock;
	}

	public void setLgShock(BigDecimal lgShock)
	{
		this.lgShock = lgShock;
	}

	public BigDecimal getShortTProfit()
	{
		return shortTProfit;
	}

	public void setShortTProfit(BigDecimal shortTProfit)
	{
		this.shortTProfit = shortTProfit;
	}

	public BigDecimal getLongTProfit()
	{
		return longTProfit;
	}

	public void setLongTProfit(BigDecimal longTProfit)
	{
		this.longTProfit = longTProfit;
	}

	public Long getExpirationTime()
	{
		return expirationTime;
	}

	public void setExpirationTime(Long expirationTime)
	{
		this.expirationTime = expirationTime;
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------

	public String getStrShort()
	{
		return symbol.priceToStr(shShock);
	}

	public String getStrLong()
	{
		return symbol.priceToStr(lgShock);
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------

	@Override
	public String toString()
	{
		return String.format("%s;%s;%s;%s", symbol.getNameLeft(), symbol.priceToStr(shShock), symbol.priceToStr(lgShock), expirationTime == null ? "" : expirationTime.toString());
	}

}
