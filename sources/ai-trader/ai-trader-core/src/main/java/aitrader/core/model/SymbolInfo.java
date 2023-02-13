package aitrader.core.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import aitrader.core.config.CoreLog;

public class SymbolInfo
{
	private Symbol symbol;

	private BigDecimal lastPrice;

	private BigDecimal high24h;
	private BigDecimal low24h;

	private BigDecimal quoteVolume24h;
	private BigDecimal change24h;

	private BigDecimal funding;

	private BigDecimal open14d;
	private BigDecimal high14d;
	private BigDecimal low14d;

	public SymbolInfo(Symbol symbol, BigDecimal lastPrice, BigDecimal high24h, BigDecimal low24h, BigDecimal quoteVolume24h, BigDecimal change24h)
	{
		this.symbol = symbol;
		this.lastPrice = lastPrice;
		this.high24h = high24h;
		this.low24h = low24h;
		this.quoteVolume24h = quoteVolume24h;
		this.change24h = change24h;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getLastPrice()
	{
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice)
	{
		this.lastPrice = lastPrice;
	}

	public BigDecimal getHigh24h()
	{
		return high24h;
	}

	public void setHigh24h(BigDecimal high24h)
	{
		this.high24h = high24h;
	}

	public BigDecimal getLow24h()
	{
		return low24h;
	}

	public void setLow24h(BigDecimal low24h)
	{
		this.low24h = low24h;
	}

	public BigDecimal getQuoteVolume24h()
	{
		return quoteVolume24h;
	}

	public void setQuoteVolume24h(BigDecimal quoteVolume24h)
	{
		this.quoteVolume24h = quoteVolume24h;
	}

	public BigDecimal getChange24h()
	{
		return change24h;
	}

	public void setChange24h(BigDecimal change24h)
	{
		this.change24h = change24h;
	}

	public BigDecimal getFunding()
	{
		return funding;
	}

	public void setFunding(BigDecimal funding)
	{
		this.funding = funding;
	}

	public void setOpen14d(BigDecimal open14d)
	{
		this.open14d = open14d;
	}

	public void setHigh14d(BigDecimal high14d)
	{
		this.high14d = high14d;
	}

	public void setLow14d(BigDecimal low14d)
	{
		this.low14d = low14d;
	}

	// ---- CALCULATED FIELDS ---------------------------------------------

	public BigDecimal getStoch24h()
	{
		try
		{
			return (lastPrice.subtract(low24h)).divide((high24h.subtract(low24h)), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
		}
		catch(ArithmeticException e)
		{
			CoreLog.error(e);
			return null;
		}
	}

	// --------------------------------------------------------------------

	public BigDecimal getHigh14d()
	{
		if (high14d == null) return null;
		
		return high24h.max(high14d);
	}

	public BigDecimal getLow14d()
	{
		if (low14d == null) return null;

		return low24h.min(low14d);
	}

	public BigDecimal getChange14d()
	{
		if (open14d == null) return null;

		return (lastPrice.subtract(open14d)).divide(open14d, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
	}
	
	public BigDecimal getStoch14d()
	{
		if (high14d == null || low14d == null) return null;

		BigDecimal high = high24h.max(high14d);
		BigDecimal low = low24h.min(low14d);

		return (lastPrice.subtract(low)).divide((high.subtract(low)), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
	}

	@Override
	public String toString()
	{
		return "SymbolInfo [symbol=" + symbol + ", lastPrice=" + lastPrice + ", high24h=" + high24h + ", low24h="
				+ low24h + ", quoteVolume24h=" + quoteVolume24h + ", change24h=" + change24h + ", funding=" + funding
				+ ", open14d=" + open14d + ", high14d=" + high14d + ", low14d=" + low14d + "]";
	}

}
