package sanzol.app.model;

import java.math.BigDecimal;

import sanzol.app.service.Symbol;
import sanzol.app.util.PriceUtil;

public class Signal
{
	private String type;
	private Symbol symbol;
	private BigDecimal markPrice;
	private BigDecimal targetPrice;
	private BigDecimal takeProfit;
	private BigDecimal distance;
	private BigDecimal change24h;
	private BigDecimal volume;

	public Signal()
	{
		//
	}

	public Signal(String type, Symbol symbol, BigDecimal markPrice, BigDecimal targetPrice, BigDecimal takeProfit, BigDecimal distance)
	{
		this.type = type;
		this.symbol = symbol;
		this.markPrice = markPrice;
		this.targetPrice = targetPrice;
		this.takeProfit = takeProfit;
		this.distance = distance;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getMarkPrice()
	{
		return markPrice;
	}

	public void setMarkPrice(BigDecimal markPrice)
	{
		this.markPrice = markPrice;
	}

	public BigDecimal getTargetPrice()
	{
		return targetPrice;
	}

	public void setTargetPrice(BigDecimal targetPrice)
	{
		this.targetPrice = targetPrice;
	}

	public BigDecimal getTakeProfit()
	{
		return takeProfit;
	}

	public void setTakeProfit(BigDecimal takeProfit)
	{
		this.takeProfit = takeProfit;
	}

	public BigDecimal getDistance()
	{
		return distance;
	}

	public void setDistance(BigDecimal distance)
	{
		this.distance = distance;
	}

	public BigDecimal getChange24h()
	{
		return change24h;
	}

	public void setChange24h(BigDecimal change24h)
	{
		this.change24h = change24h;
	}

	public BigDecimal getVolume()
	{
		return volume;
	}

	public void setVolume(BigDecimal volume)
	{
		this.volume = volume;
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------

	public String getStrMarkPrice()
	{
		return symbol.priceToStr(markPrice);
	}

	public String getStrTargetPrice()
	{
		return symbol.priceToStr(targetPrice);
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------

	public String toString()
	{
		return String.format("%-8s %6.2f%% %12s   TP:%6.2f%%    24h:%6.2f%%    vol:%5s\n", symbol.getNameLeft(), distance, symbol.priceToStr(targetPrice), takeProfit, change24h, PriceUtil.cashFormat(volume));
	}

}
