package sanzol.app.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import api.client.model.async.SymbolTickerEvent;
import api.client.service.PriceService;
import sanzol.app.config.Config;
import sanzol.app.service.Symbol;
import sanzol.app.util.PriceUtil;

public class SymbolInfo
{
	public static final double MIN_USDT = 50;

	private Symbol symbol;

	private BigDecimal usdVolume;
	private BigDecimal priceChangePercent;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal avgPrice;
	private BigDecimal avgHigh;
	private BigDecimal avgLow;
	private BigDecimal minUsdQty;

	private BigDecimal lastPrice;
	private BigDecimal dist5m;

	private boolean isMinUsdQtyHigh = false;
	private boolean isLowVolume = false;
	private boolean isHighMove = false;
	private boolean isBestShort = false;
	private boolean isBestLong = false;
	private boolean isLowMove5m = false;

	private static final BigDecimal TWO = BigDecimal.valueOf(2);

	/*
	public SymbolInfo(SymbolTickerEvent symbolTickerEvent) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		symbol = Symbol.getInstance(symbolTickerEvent.getSymbol());
		if (symbol != null)
		{
			lastPrice = symbolTickerEvent.getLastPrice();
	
			usdVolume = symbolTickerEvent.getQuoteVolume();
			priceChangePercent = symbolTickerEvent.getPriceChangePercent();
			high = symbolTickerEvent.getHighPrice();
			low = symbolTickerEvent.getLowPrice();
			avgPrice = symbolTickerEvent.getWeightedAvgPrice();
	
			avgHigh = (avgPrice.add(high)).divide(TWO, symbol.getPricePrecision(), RoundingMode.HALF_UP);
			avgLow = (avgPrice.add(low)).divide(TWO, symbol.getPricePrecision(), RoundingMode.HALF_UP);
			minUsdQty = symbol.getMinQty().multiply(high);
	
			isMinUsdQtyHigh = (minUsdQty.doubleValue() > MIN_USDT);
			isLowVolume = (usdVolume.doubleValue() < Config.getBetterSymbolsMinVolume());
			isHighMove = (priceChangePercent.abs().doubleValue() > Config.getBetterSymbolsMaxChange());
			isBestShort = (lastPrice.doubleValue() > avgHigh.doubleValue());
			isBestLong = (lastPrice.doubleValue() < avgLow.doubleValue());
		}
	}
	*/
	
	public static SymbolInfo getInstance(SymbolTickerEvent symbolTickerEvent) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		Symbol _symbol = Symbol.getInstance(symbolTickerEvent.getSymbol());
		if (_symbol == null)
		{
			return null;
		}

		SymbolInfo si = new SymbolInfo();
		
		si.symbol = _symbol;

		si.lastPrice = symbolTickerEvent.getLastPrice();

		si.usdVolume = symbolTickerEvent.getQuoteVolume();
		si.priceChangePercent = symbolTickerEvent.getPriceChangePercent();
		si.high = symbolTickerEvent.getHighPrice();
		si.low = symbolTickerEvent.getLowPrice();
		si.avgPrice = symbolTickerEvent.getWeightedAvgPrice();

		si.avgHigh = (si.avgPrice.add(si.high)).divide(TWO, si.symbol.getPricePrecision(), RoundingMode.HALF_UP);
		si.avgLow = (si.avgPrice.add(si.low)).divide(TWO, si.symbol.getPricePrecision(), RoundingMode.HALF_UP);
		si.minUsdQty = si.symbol.getMinQty().multiply(si.high);

		si.isMinUsdQtyHigh = (si.minUsdQty.doubleValue() > MIN_USDT);
		si.isLowVolume = (si.usdVolume.doubleValue() < Config.getBetterSymbolsMinVolume());
		si.isHighMove = (si.priceChangePercent.abs().doubleValue() > Config.getBetterSymbolsMaxChange());
		si.isBestShort = (si.lastPrice.doubleValue() > si.avgHigh.doubleValue());
		si.isBestLong = (si.lastPrice.doubleValue() < si.avgLow.doubleValue());

		return si;
	}	
	// ------------------------------------------------------------------------

	public String toString()
	{
		String flags 
			= (isBestShort() ? "BestShort " : "") 
			+ (isBestLong() ? "BestLong " : "") 
			+ (isMinUsdQtyHigh() ? "MinUsdQtyHigh " : "")
			+ (isHighMove() ? "HighMove " : "") 
			+ (isLowVolume() ? "LowVolume " : "") 
			+ (isLowMove5m() ? "LowMove5m " : "");

		return String.format("%-10s  vol %5s    24Hs %6.2f %%    5m %6.2f %%    %s", symbol.getNameLeft(), PriceUtil.cashFormat(usdVolume), priceChangePercent, dist5m, flags);
	}

	// ------------------------------------------------------------------------

	public static List<String> getBestSymbols() throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<String> lstSymbols = new ArrayList<String>();

		List<SymbolTickerEvent> lstSymbolTickerEvent = new ArrayList<SymbolTickerEvent>();
		lstSymbolTickerEvent.addAll(PriceService.getMapTickers().values());

		for (SymbolTickerEvent entry : lstSymbolTickerEvent)
		{
			SymbolInfo symbolInfo = getInstance(entry);
			if (symbolInfo != null && !symbolInfo.isMinUsdQtyHigh() && !symbolInfo.isLowVolume() && !symbolInfo.isHighMove())
			{
				lstSymbols.add(symbolInfo.getSymbol().getNameLeft());
			}
		}

		Collections.sort(lstSymbols);

		return lstSymbols;
	}

	// ------------------------------------------------------------------------

	public String getPair()
	{
		return symbol.getPair();
	}

	// ------------------------------------------------------------------------

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getUsdVolume()
	{
		return usdVolume;
	}

	public void setUsdVolume(BigDecimal usdVolume)
	{
		this.usdVolume = usdVolume;
	}

	public BigDecimal getPriceChangePercent()
	{
		return priceChangePercent;
	}

	public void setPriceChangePercent(BigDecimal priceChangePercent)
	{
		this.priceChangePercent = priceChangePercent;
	}

	public BigDecimal getHigh()
	{
		return high;
	}

	public void setHigh(BigDecimal high)
	{
		this.high = high;
	}

	public BigDecimal getLow()
	{
		return low;
	}

	public void setLow(BigDecimal low)
	{
		this.low = low;
	}

	public BigDecimal getAvgPrice()
	{
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice)
	{
		this.avgPrice = avgPrice;
	}

	public BigDecimal getAvgHigh()
	{
		return avgHigh;
	}

	public void setAvgHigh(BigDecimal avgHigh)
	{
		this.avgHigh = avgHigh;
	}

	public BigDecimal getAvgLow()
	{
		return avgLow;
	}

	public void setAvgLow(BigDecimal avgLow)
	{
		this.avgLow = avgLow;
	}

	public BigDecimal getMinUsdQty()
	{
		return minUsdQty;
	}

	public void setMinUsdQty(BigDecimal minUsdQty)
	{
		this.minUsdQty = minUsdQty;
	}

	public BigDecimal getLastPrice()
	{
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice)
	{
		this.lastPrice = lastPrice;
	}

	public BigDecimal getDist5m()
	{
		return dist5m;
	}

	public void setDist5m(BigDecimal dist5m)
	{
		this.dist5m = dist5m;
	}

	public boolean isMinUsdQtyHigh()
	{
		return isMinUsdQtyHigh;
	}

	public void setMinUsdQtyHigh(boolean isMinUsdQtyHigh)
	{
		this.isMinUsdQtyHigh = isMinUsdQtyHigh;
	}

	public boolean isLowVolume()
	{
		return isLowVolume;
	}

	public void setLowVolume(boolean isLowVolume)
	{
		this.isLowVolume = isLowVolume;
	}

	public boolean isHighMove()
	{
		return isHighMove;
	}

	public void setHighMove(boolean isHighMove)
	{
		this.isHighMove = isHighMove;
	}

	public boolean isBestShort()
	{
		return isBestShort;
	}

	public void setBestShort(boolean isBestShort)
	{
		this.isBestShort = isBestShort;
	}

	public boolean isBestLong()
	{
		return isBestLong;
	}

	public void setBestLong(boolean isBestLong)
	{
		this.isBestLong = isBestLong;
	}

	public boolean isLowMove5m()
	{
		return isLowMove5m;
	}

	public void setLowMove5m(boolean isLowMove5m)
	{
		this.isLowMove5m = isLowMove5m;
	}

}
