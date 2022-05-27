package sanzol.app.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.binance.client.model.event.SymbolTickerEvent;

import sanzol.app.config.Config;
import sanzol.app.service.Symbol;
import sanzol.app.task.PriceService;
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

	public SymbolInfo(SymbolTickerEvent symbolTickerEvent)
	{
		symbol = Symbol.getInstance(symbolTickerEvent.getSymbol());
		lastPrice = symbolTickerEvent.getLastPrice();

		usdVolume = symbolTickerEvent.getTotalTradedQuoteAssetVolume();
		priceChangePercent = symbolTickerEvent.getPriceChangePercent();
		high = symbolTickerEvent.getHigh();
		low = symbolTickerEvent.getLow();
		avgPrice = symbolTickerEvent.getWeightedAvgPrice();

		avgHigh = (avgPrice.add(high)).divide(TWO, symbol.getTickSize(), RoundingMode.HALF_UP);
		avgLow = (avgPrice.add(low)).divide(TWO, symbol.getTickSize(), RoundingMode.HALF_UP);
		minUsdQty = symbol.getMinQty().multiply(high);

		isMinUsdQtyHigh = (minUsdQty.doubleValue() > MIN_USDT);
		isLowVolume = (usdVolume.doubleValue() < Config.BETTER_SYMBOLS_MIN_VOLUME);
		isHighMove = (priceChangePercent.abs().doubleValue() > Config.BETTER_SYMBOLS_MAX_CHANGE);
		isBestShort = (lastPrice.doubleValue() > avgHigh.doubleValue());
		isBestLong = (lastPrice.doubleValue() < avgLow.doubleValue());

		/*
		if (!isMinUsdQtyHigh && !isLowVolume && !isHighMove)
		{
			dist5m = distance(lastCandlestick5m(symbol));
			isLowMove5m = dist5m.doubleValue() < 0.5;
		}
		*/
	}

	/*
	private static Candlestick lastCandlestick5m(Symbol symbol)
	{
		RequestOptions options = new RequestOptions();
		SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);
		Candlestick candlestick = syncRequestClient.getCandlestick(symbol.getName(), CandlestickInterval.FIVE_MINUTES, null, null, 1).get(0);
		return candlestick;
	}

	private static BigDecimal distance(Candlestick candlestick)
	{
		return (candlestick.getHigh().divide(candlestick.getLow(), 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)).multiply(BigDecimal.valueOf(100));
	}
	*/

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

	public static List<String> getBestSymbols()
	{
		List<String> lstSymbols = new ArrayList<String>();

		List<SymbolTickerEvent> lstSymbolTickerEvent = new ArrayList<SymbolTickerEvent>();
		lstSymbolTickerEvent.addAll(PriceService.getMapTickers().values());

		for (SymbolTickerEvent entry : lstSymbolTickerEvent)
		{
			SymbolInfo symbolInfo = new SymbolInfo(entry);
			if (!symbolInfo.isMinUsdQtyHigh() && !symbolInfo.isLowVolume() && !symbolInfo.isHighMove())
			{
				lstSymbols.add(symbolInfo.getSymbol().getNameLeft());
			}
		}

		Collections.sort(lstSymbols);

		return lstSymbols;
	}

	// ------------------------------------------------------------------------

	public String getSymbolName()
	{
		return symbol.getName();
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
