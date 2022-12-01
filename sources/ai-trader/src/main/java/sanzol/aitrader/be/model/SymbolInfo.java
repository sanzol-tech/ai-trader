package sanzol.aitrader.be.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import api.client.model.event.SymbolTickerEvent;
import sanzol.aitrader.be.config.Config;
import sanzol.aitrader.be.service.PriceService;

public class SymbolInfo
{
	private Symbol symbol;

	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal lastPrice;

	private BigDecimal usdVolume;
	private BigDecimal priceChangePercent;
	
	private BigDecimal highLow;
	private BigDecimal stochastic;

	private boolean isLowVolume = false;
	private boolean isHighChange = false;

	public static SymbolInfo getInstance(SymbolTickerEvent symbolTickerEvent) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		Symbol _symbol = Symbol.fromPair(symbolTickerEvent.getSymbol());
		if (_symbol == null)
		{
			return null;
		}

		SymbolInfo si = new SymbolInfo();

		si.symbol = _symbol;

		si.open = symbolTickerEvent.getOpenPrice();
		si.high = symbolTickerEvent.getHighPrice();
		si.low = symbolTickerEvent.getLowPrice();
		si.lastPrice = symbolTickerEvent.getLastPrice();

		si.usdVolume = symbolTickerEvent.getQuoteVolume();
		si.priceChangePercent = symbolTickerEvent.getPriceChangePercent();

		si.highLow = si.high.subtract(si.low).divide(si.low, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
		si.stochastic = (si.lastPrice.subtract(si.low)).divide((si.high.subtract(si.low)), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

		si.isLowVolume = (si.usdVolume.doubleValue() < Config.getBetterSymbolsMinVolume());
		si.isHighChange = (si.priceChangePercent.abs().doubleValue() > Config.getBetterSymbolsMaxChange());

		return si;
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
			if (symbolInfo != null && !symbolInfo.isLowVolume() && !symbolInfo.isHighChange())
			{
				lstSymbols.add(symbolInfo.getSymbol().getNameLeft());
			}
		}

		Collections.sort(lstSymbols);

		return lstSymbols;
	}

	// ------------------------------------------------------------------------
	
	public Symbol getSymbol()
	{
		return symbol;
	}

	public BigDecimal getOpen()
	{
		return open;
	}

	public BigDecimal getHigh()
	{
		return high;
	}

	public BigDecimal getLow()
	{
		return low;
	}

	public BigDecimal getLastPrice()
	{
		return lastPrice;
	}

	public BigDecimal getUsdVolume()
	{
		return usdVolume;
	}

	public BigDecimal getPriceChangePercent()
	{
		return priceChangePercent;
	}

	public BigDecimal getHighLow()
	{
		return highLow;
	}

	public BigDecimal getStochastic()
	{
		return stochastic;
	}

	public boolean isLowVolume()
	{
		return isLowVolume;
	}

	public boolean isHighChange()
	{
		return isHighChange;
	}

	// ------------------------------------------------------------------------

	public String getSymbolPair()
	{
		return symbol.getPair();
	}

}
