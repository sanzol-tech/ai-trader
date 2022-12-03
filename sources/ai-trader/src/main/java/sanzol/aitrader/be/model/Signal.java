package sanzol.aitrader.be.model;

import java.math.BigDecimal;
import java.util.Locale;

import sanzol.util.price.PriceUtil;

public class Signal
{
	private String type;

	private Symbol symbol;
	private BigDecimal markPrice;
	private BigDecimal change24h;
	private BigDecimal volume;
	private BigDecimal stochastic;

	private BigDecimal inPrice;
	private BigDecimal distance;
	private BigDecimal takeProfit;
	private BigDecimal stopLoss;
	private BigDecimal ratio;

	public Signal()
	{
		//
	}

	public Signal(String type, Symbol symbol, BigDecimal markPrice, BigDecimal change24h, BigDecimal volume, BigDecimal stochastic,
				  BigDecimal inPrice, BigDecimal distance, BigDecimal takeProfit, BigDecimal stopLoss, BigDecimal ratio)
	{
		this.type = type;
		this.symbol = symbol;
		this.markPrice = markPrice;
		this.change24h = change24h;
		this.volume = volume;
		this.stochastic = stochastic;
		this.inPrice = inPrice;
		this.distance = distance;
		this.takeProfit = takeProfit;
		this.stopLoss = stopLoss;
		this.ratio = ratio;
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

	public BigDecimal getStochastic()
	{
		return stochastic;
	}

	public void setStochastic(BigDecimal stochastic)
	{
		this.stochastic = stochastic;
	}

	public BigDecimal getInPrice()
	{
		return inPrice;
	}

	public void setInPrice(BigDecimal inPrice)
	{
		this.inPrice = inPrice;
	}

	public BigDecimal getDistance()
	{
		return distance;
	}

	public void setDistance(BigDecimal distance)
	{
		this.distance = distance;
	}

	public BigDecimal getTakeProfit()
	{
		return takeProfit;
	}

	public void setTakeProfit(BigDecimal takeProfit)
	{
		this.takeProfit = takeProfit;
	}

	public BigDecimal getStopLoss()
	{
		return stopLoss;
	}

	public void setStopLoss(BigDecimal stopLoss)
	{
		this.stopLoss = stopLoss;
	}

	public BigDecimal getRatio()
	{
		return ratio;
	}

	public void setRatio(BigDecimal ratio)
	{
		this.ratio = ratio;
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------

	public String getStrMarkPrice()
	{
		return symbol.priceToStr(markPrice);
	}

	public String getStrTargetPrice()
	{
		return symbol.priceToStr(inPrice);
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------

	public String toString()
	{
		return String.format(Locale.US, "%-8s %6.2f%% %12s     RATIO 1:%-4.1f    VOL %4s    CHG %6.2f%%\n", symbol.getNameLeft(), distance, symbol.priceToStr(inPrice), ratio, PriceUtil.cashFormat(volume), change24h);
	}

}
