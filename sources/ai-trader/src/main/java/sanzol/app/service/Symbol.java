package sanzol.app.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.decimal4j.util.DoubleRounder;

import api.client.config.ApiConfig;
import api.client.model.sync.ExchangeInfoEntry;
import api.client.service.ExchangeInfoService;
import sanzol.app.config.Config;

public class Symbol
{
	private String pair;
	private String nameLeft;
	private String nameRight;

	private BigDecimal tickSizeEI;
	private int tickSize;
	private String pricePattern;

	private BigDecimal minQty;
	private int qtyPrecision;
	private String qtyPattern;

	public String getPair()
	{
		return pair;
	}

	public String getNameLeft()
	{
		return nameLeft;
	}

	public String getNameRight()
	{
		return nameRight;
	}

	public int getTickSize()
	{
		return tickSize;
	}

	public int getQtyPrecision()
	{
		return qtyPrecision;
	}

	public BigDecimal getMinQty()
	{
		return minQty;
	}

	// ------------------------------------------------------------------------

	private static Symbol toSymbol(ExchangeInfoEntry exchangeInfoEntry)
	{
		Symbol symbol = new Symbol();
		
		symbol.pair = exchangeInfoEntry.getSymbol();
		symbol.nameLeft = exchangeInfoEntry.getBaseAsset();
		symbol.nameRight = exchangeInfoEntry.getQuoteAsset();
		
		for (Map<String, String> entry : exchangeInfoEntry.getFilters())
		{
			if (entry.get("filterType").equals("PRICE_FILTER"))
			{
				String _tickSize = entry.get("tickSize");
				symbol.tickSizeEI = new BigDecimal(_tickSize);	

				symbol.tickSize = (int) Math.log10(Double.valueOf(_tickSize)) * -1;

				String _pricePattern = "#0";
				if (symbol.tickSize > 0)
				{
					_pricePattern += "." + StringUtils.repeat("0", symbol.tickSize);
				}
				symbol.pricePattern = _pricePattern;
			}

			if (entry.get("filterType").equals("LOT_SIZE"))
			{
				String _minQty = entry.get("minQty");
				symbol.minQty = new BigDecimal(_minQty);

				symbol.qtyPrecision = (int) Math.log10(Double.valueOf(_minQty)) * -1;
				
				String _qtyPattern = "#0";
				if (symbol.qtyPrecision > 0)
				{
					_qtyPattern += "." + StringUtils.repeat("0", symbol.qtyPrecision);
				}
				symbol.qtyPattern = _qtyPattern;
			}
		}
		
		return symbol;
	}

	public static Symbol getInstance(String pair) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		if (!pair.endsWith(Config.DEFAULT_SYMBOL_RIGHT))
		{
			return null;
		}

		ExchangeInfoEntry eInfoEntry = ExchangeInfoService.getExchangeInfo(pair);		
		if (eInfoEntry == null)
		{
			return null;
		}

		Symbol symbol = toSymbol(eInfoEntry);

		return symbol;
	}

	// ------------------------------------------------------------------------

	public String priceToStr(BigDecimal price)
	{
		return new DecimalFormat(pricePattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(price.doubleValue());
	}

	public String priceToStr(double price)
	{
		return new DecimalFormat(pricePattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(price);
	}

	public String qtyToStr(BigDecimal coins)
	{
		return new DecimalFormat(qtyPattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(coins.doubleValue());
	}

	public String qtyToStr(double coins)
	{
		return new DecimalFormat(qtyPattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(coins);
	}

	public double roundPrice(double price)
	{
		return DoubleRounder.round(price, tickSize);
	}

	public double roundQty(double coins)
	{
		return DoubleRounder.round(coins, qtyPrecision);
	}

	public BigDecimal addTicks(BigDecimal price, int ticks)
	{
		return price.add( tickSizeEI.multiply(BigDecimal.valueOf(ticks)) );
	}
	public double addTicks(double price, int ticks)
	{
		return price + Math.pow(10, -tickSize) * ticks;
	}

	public BigDecimal subTicks(BigDecimal price, int ticks)
	{
		return price.subtract( tickSizeEI.multiply(BigDecimal.valueOf(ticks)) );
	}
	public double subTicks(double price, int ticks)
	{
		return price - Math.pow(10, -tickSize) * ticks;
	}

	public static String getFullSymbol(String symbolLeft)
	{
		return symbolLeft + Config.DEFAULT_SYMBOL_RIGHT;
	}
	
	public static String getRightSymbol(String symbolName)
	{
		return symbolName.substring(0, symbolName.length() - Config.DEFAULT_SYMBOL_RIGHT.length());
	}	

	// ------------------------------------------------------------------------

	@Override
	public String toString()
	{
		return nameLeft;
	}

	// ------------------------------------------------------------------------
	
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		ApiConfig.setFutures();
		ExchangeInfoService.start();
		Symbol symbol = getInstance("BTCUSDT");
		System.out.println(symbol.getPair());
		System.out.println(symbol.getNameLeft());
		System.out.println(symbol.getNameRight());

		System.out.println(symbol.tickSizeEI);
		System.out.println(symbol.tickSize);
		System.out.println(symbol.pricePattern);

		System.out.println(symbol.minQty);
		System.out.println(symbol.qtyPrecision);
		System.out.println(symbol.qtyPattern);
	}

}
