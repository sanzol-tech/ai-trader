package sanzol.app.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.decimal4j.util.DoubleRounder;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.market.ExchangeInfoEntry;
import com.binance.client.model.market.ExchangeInformation;

import sanzol.app.config.Constants;
import sanzol.app.config.PrivateConfig;

public class Symbol
{
	private String nameLeft;
	private int tickSize;
	private int quantityPrecision;

	public String getName()
	{
		return nameLeft + Constants.DEFAULT_SYMBOL_RIGHT;
	}

	public String getNameLeft()
	{
		return nameLeft;
	}

	public int getTickSize()
	{
		return tickSize;
	}

	public int getQuantityPrecision()
	{
		return quantityPrecision;
	}

	// ------------------------------------------------------------------------

	private static List<ExchangeInfoEntry> lstExInfEntries = null;

	private static void getExInfEntries()
	{
		if (lstExInfEntries == null)
		{
			RequestOptions options = new RequestOptions();
			SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);
			ExchangeInformation exInf = syncRequestClient.getExchangeInformation();
			lstExInfEntries = exInf.getSymbols();
		}
	}

	public static String getAll()
	{
		TreeSet<String> list = new TreeSet<String>();
		getExInfEntries();
		for (ExchangeInfoEntry entry : lstExInfEntries)
		{
			if (entry.getSymbol().endsWith(Constants.DEFAULT_SYMBOL_RIGHT))
			{
				String symbolName = entry.getSymbol().substring(0, entry.getSymbol().length() - Constants.DEFAULT_SYMBOL_RIGHT.length());
				list.add(symbolName);
			}
		}
		return StringUtils.join(list, ",");
	}

	// ------------------------------------------------------------------------

	public static Symbol getInstance(String symbolName)
	{
		if (!symbolName.endsWith(Constants.DEFAULT_SYMBOL_RIGHT))
		{
			return null;
		}

		Symbol coin = new Symbol();
		coin.nameLeft = symbolName.substring(0, symbolName.length() - Constants.DEFAULT_SYMBOL_RIGHT.length());
		getExInfEntries();
		for (ExchangeInfoEntry entry : lstExInfEntries)
		{
			if (entry.getSymbol().equals(symbolName))
			{
				ExchangeInfoEntry eInfoEntry = entry;

				String ts = eInfoEntry.getFilters().get(0).get(3).get("tickSize");
				coin.tickSize = (int) Math.log10(Double.valueOf(ts)) * -1;

				coin.quantityPrecision = eInfoEntry.getQuantityPrecision().intValue();
				
				return coin;
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------

	public String priceToStr(BigDecimal price)
	{
		String pattern = "#0";
		if (tickSize > 0)
		{
			pattern += "." + StringUtils.repeat("0", tickSize);
		}

		return new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(price.doubleValue());
	}

	public String priceToStr(double price)
	{
		String pattern = "#0";
		if (tickSize > 0)
		{
			pattern += "." + StringUtils.repeat("0", tickSize);
		}

		return new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(price);
	}

	public String coinsToStr(BigDecimal coins)
	{
		String pattern = "#0";
		if (quantityPrecision > 0)
		{
			pattern += "." + StringUtils.repeat("0", quantityPrecision);
		}

		return new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(coins.doubleValue());
	}

	public String coinsToStr(double coins)
	{
		String pattern = "#0";
		if (quantityPrecision > 0)
		{
			pattern += "." + StringUtils.repeat("0", quantityPrecision);
		}

		return new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(coins);
	}

	public double roundPrice(double price)
	{
		return DoubleRounder.round(price, tickSize);
	}

	public double roundCoins(double coins)
	{
		return DoubleRounder.round(coins, quantityPrecision);
	}
	
	public double addFewTicks(double price, int ticks)
	{
		return price + Math.pow(10, -tickSize) * ticks;
	}

	public double subFewTicks(double price, int ticks)
	{
		return price - Math.pow(10, -tickSize) * ticks;
	}

	public static String getFullSymbol(String symbolLeft)
	{
		return symbolLeft + Constants.DEFAULT_SYMBOL_RIGHT;
	}
	
	public static String getRightSymbol(String symbolName)
	{
		return symbolName.substring(0, symbolName.length() - Constants.DEFAULT_SYMBOL_RIGHT.length());
	}	

	// ------------------------------------------------------------------------

	public static void main(String[] args)
	{
		System.out.println(getAll());
	}	

}
