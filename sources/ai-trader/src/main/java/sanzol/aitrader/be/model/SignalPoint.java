package sanzol.aitrader.be.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import sanzol.util.price.PriceUtil;

public class SignalPoint
{
	public static final Long EXPIRATION_MILLIS = TimeUnit.MINUTES.toMillis(120);

	private Symbol symbol;

	private BigDecimal shortPrice;
	private BigDecimal longPrice;
	
	private BigDecimal shortTProfit;
	private BigDecimal longTProfit;

	private BigDecimal shortSLoss;
	private BigDecimal longTSLoss;

	private BigDecimal shortRatio;
	private BigDecimal longRatio;

	private Long expirationTime;

	public SignalPoint()
	{
		//
	}

	public static SignalPoint NULL(Symbol symbol)
	{
		return new SignalPoint(symbol, System.currentTimeMillis() + EXPIRATION_MILLIS);
	}

	public SignalPoint(Symbol symbol, Long expirationTime)
	{
		this.symbol = symbol;

		this.shortPrice = BigDecimal.ZERO;
		this.longPrice = BigDecimal.ZERO;

		this.shortTProfit = BigDecimal.ZERO;
		this.longTProfit = BigDecimal.ZERO;

		this.shortSLoss = BigDecimal.ZERO;
		this.longTSLoss = BigDecimal.ZERO;

		this.shortRatio = BigDecimal.ZERO;
		this.longRatio = BigDecimal.ZERO;

		this.expirationTime = expirationTime;
	}

	public SignalPoint(Symbol symbol, BigDecimal shortPrice, BigDecimal longPrice, BigDecimal shShock2, BigDecimal lgShock2)
	{
		this.symbol = symbol;

		this.shortPrice = shortPrice;
		this.longPrice = longPrice;

		this.shortTProfit = PriceUtil.priceDistDown(shortPrice, longPrice, true);
		this.longTProfit = PriceUtil.priceDistUp(longPrice, shortPrice, true);

		this.shortSLoss = PriceUtil.priceDistDown(shortPrice, shShock2, true);
		this.longTSLoss = PriceUtil.priceDistUp(longPrice, lgShock2, true);
		
		if (shortSLoss.compareTo(BigDecimal.ZERO) == 0)
			this.shortRatio = BigDecimal.ZERO;
		else
			this.shortRatio = shortTProfit.divide(shortSLoss, 1, RoundingMode.HALF_UP).abs();

		if (longTSLoss.compareTo(BigDecimal.ZERO) == 0)
			this.longRatio = BigDecimal.ZERO;
		else
			this.longRatio = longTProfit.divide(longTSLoss, 1, RoundingMode.HALF_UP).abs();

		this.expirationTime = System.currentTimeMillis() + EXPIRATION_MILLIS;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getShortPrice()
	{
		return shortPrice;
	}

	public void setShortPrice(BigDecimal shortPrice)
	{
		this.shortPrice = shortPrice;
	}

	public BigDecimal getLongPrice()
	{
		return longPrice;
	}

	public void setLongPrice(BigDecimal longPrice)
	{
		this.longPrice = longPrice;
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

	public BigDecimal getShortSLoss()
	{
		return shortSLoss;
	}

	public void setShortSLoss(BigDecimal shortSLoss)
	{
		this.shortSLoss = shortSLoss;
	}

	public BigDecimal getLongTSLoss()
	{
		return longTSLoss;
	}

	public void setLongTSLoss(BigDecimal longTSLoss)
	{
		this.longTSLoss = longTSLoss;
	}

	public BigDecimal getShortRatio()
	{
		return shortRatio;
	}

	public void setShortRatio(BigDecimal shortRatio)
	{
		this.shortRatio = shortRatio;
	}

	public BigDecimal getLongRatio()
	{
		return longRatio;
	}

	public void setLongRatio(BigDecimal longRatio)
	{
		this.longRatio = longRatio;
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
		return symbol.priceToStr(shortPrice);
	}

	public String getStrLong()
	{
		return symbol.priceToStr(longPrice);
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------

	@Override
	public String toString()
	{
		return String.format("%s;%s;%s;%s", symbol.getNameLeft(), symbol.priceToStr(shortPrice), symbol.priceToStr(longPrice), expirationTime == null ? "" : expirationTime.toString());
	}

}
