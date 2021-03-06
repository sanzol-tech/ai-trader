package sanzol.app.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.decimal4j.util.DoubleRounder;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.event.SymbolTickerEvent;
import com.binance.client.model.market.ExchangeInfoEntry;
import com.binance.client.model.market.ExchangeInformation;

import sanzol.app.config.Config;
import sanzol.app.model.SymbolInfo;
import sanzol.app.task.PriceService;
import sanzol.app.util.PriceUtil;

public class Symbol
{
	private String nameLeft;
	private String pricePattern;
	private String quantityPattern;
	private int tickSize;
	private int quantityPrecision;
	private BigDecimal minQty;

	public String getName()
	{
		return nameLeft + Config.DEFAULT_SYMBOL_RIGHT;
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

	public BigDecimal getMinQty()
	{
		return minQty;
	}

	// ------------------------------------------------------------------------

	private static List<ExchangeInfoEntry> lstExInfEntries = null;
	private static long exInfTime;

	private static void getExInfEntries()
	{
		if (lstExInfEntries == null || exInfTime + 1000 * 60 * 30 < System.currentTimeMillis())
		{
			SyncRequestClient syncRequestClient = SyncRequestClient.create();
			ExchangeInformation exInf = syncRequestClient.getExchangeInformation();
			lstExInfEntries = exInf.getSymbols();
			exInfTime = System.currentTimeMillis();
		}
	}

	public static List<String> getAll()
	{
		TreeSet<String> list = new TreeSet<String>();
		getExInfEntries();
		for (ExchangeInfoEntry entry : lstExInfEntries)
		{
			if (entry.getSymbol().endsWith(Config.DEFAULT_SYMBOL_RIGHT))
			{
				String symbolName = entry.getSymbol().substring(0, entry.getSymbol().length() - Config.DEFAULT_SYMBOL_RIGHT.length());
				list.add(symbolName);
			}
		}
		return new ArrayList<String>(list);
	}

	// ------------------------------------------------------------------------

	public static Symbol getInstance(String symbolName)
	{
		if (!symbolName.endsWith(Config.DEFAULT_SYMBOL_RIGHT))
		{
			return null;
		}

		Symbol symbol = new Symbol();
		symbol.nameLeft = symbolName.substring(0, symbolName.length() - Config.DEFAULT_SYMBOL_RIGHT.length());
		getExInfEntries();
		for (ExchangeInfoEntry entry : lstExInfEntries)
		{
			if (entry.getSymbol().equals(symbolName))
			{
				ExchangeInfoEntry eInfoEntry = entry;

				String ts = eInfoEntry.getFilters().get(0).get(3).get("tickSize");
				symbol.tickSize = (int) Math.log10(Double.valueOf(ts)) * -1;
				symbol.pricePattern = "#0";
				if (symbol.tickSize > 0)
				{
					symbol.pricePattern += "." + StringUtils.repeat("0", symbol.tickSize);
				}
				
				symbol.quantityPrecision = eInfoEntry.getQuantityPrecision().intValue();
				symbol.quantityPattern = "#0";
				if (symbol.quantityPrecision > 0)
				{
					symbol.quantityPattern += "." + StringUtils.repeat("0", symbol.quantityPrecision);
				}

				String mq = eInfoEntry.getFilters().get(1).get(3).get("minQty");
				symbol.minQty = new BigDecimal(mq);

				return symbol;
			}
		}
		return null;
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
		return new DecimalFormat(quantityPattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(coins.doubleValue());
	}

	public String qtyToStr(double coins)
	{
		return new DecimalFormat(quantityPattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(coins);
	}

	public double roundPrice(double price)
	{
		return DoubleRounder.round(price, tickSize);
	}

	public double roundQty(double coins)
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
		return symbolLeft + Config.DEFAULT_SYMBOL_RIGHT;
	}
	
	public static String getRightSymbol(String symbolName)
	{
		return symbolName.substring(0, symbolName.length() - Config.DEFAULT_SYMBOL_RIGHT.length());
	}	

	// ------------------------------------------------------------------------

	public static List<SymbolInfo> getLstSymbolsInfo(boolean onlyFavorites, boolean onlyBetters)
	{
		List<String> lstFavorites = Config.getLstFavSymbols();

		List<SymbolInfo> lstSymbolsInfo = new ArrayList<SymbolInfo>();

		List<SymbolTickerEvent> lstSymbolTickerEvent = new ArrayList<SymbolTickerEvent>();
		lstSymbolTickerEvent.addAll(PriceService.getMapTickers().values());

		for (SymbolTickerEvent entry : lstSymbolTickerEvent)
		{
			SymbolInfo symbolInfo = new SymbolInfo(entry);

			if (onlyFavorites && !lstFavorites.contains(symbolInfo.getSymbol().getNameLeft()))
			{
				continue;
			}

			if (onlyBetters && (symbolInfo.isLowVolume() || symbolInfo.isHighMove()))
			{
				continue;
			}

			lstSymbolsInfo.add(symbolInfo);
		}

		Comparator<SymbolInfo> orderComparator = Comparator.comparing(SymbolInfo::getUsdVolume).reversed();
		Collections.sort(lstSymbolsInfo, orderComparator);

		return lstSymbolsInfo;
	}

	public static List<String> getLstSymbolsMini(boolean onlyFavorites, boolean onlyBetters)
	{
		List<String> list = new ArrayList<String>();
		list.add(String.format("%-10s %11s %9s", "SYMBOL", "CHANGE", "VOLUME"));

		List<SymbolInfo> lstSymbolsInfo = getLstSymbolsInfo(onlyFavorites, onlyBetters);
		for (SymbolInfo entry : lstSymbolsInfo)
		{
			Symbol symbol = entry.getSymbol();
			list.add(String.format("%-10s %10.2f%% %9s", symbol.getNameLeft(), entry.getPriceChangePercent(), PriceUtil.cashFormat(entry.getUsdVolume())));
		}

		return list;
	}

	public static List<String> getLstSymbolNames(boolean onlyFavorites, boolean onlyBetters)
	{
		List<String> list = new ArrayList<String>();

		List<SymbolInfo> lstSymbolsInfo = getLstSymbolsInfo(onlyFavorites, onlyBetters);
		for (SymbolInfo entry : lstSymbolsInfo)
		{
			list.add(entry.getSymbol().getName());
		}
		
		return list;
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString()
	{
		return nameLeft;
	}

	// ------------------------------------------------------------------------
	
	public static void main(String[] args)
	{
		for(String symbol : getAll()) 
		{
			System.out.println(symbol);
		}
	}

}
