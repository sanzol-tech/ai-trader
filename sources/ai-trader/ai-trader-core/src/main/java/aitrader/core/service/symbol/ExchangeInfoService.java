package aitrader.core.service.symbol;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import aitrader.core.config.CoreLog;
import aitrader.core.model.Symbol;
import binance.futures.impl.UnsignedClient;
import binance.futures.model.ExchangeInfo;
import binance.futures.model.ExchangeInfoEntry;

public class ExchangeInfoService
{
	private static final long TIME_TO_RELOAD = TimeUnit.HOURS.toMillis(8);
	
	private static Map<String, ExchangeInfoEntry> mapExchangeInfo = new ConcurrentHashMap<String, ExchangeInfoEntry>();
	private static long timestamp = 0;

	public static boolean getSnapshoot()
	{
		try
		{
			mapExchangeInfo = new ConcurrentHashMap<String, ExchangeInfoEntry>();
			
			ExchangeInfo exchangeInfo = UnsignedClient.getExchangeInformation();
			List<ExchangeInfoEntry> lstExchangeInfoEntries = exchangeInfo.getSymbols();

			timestamp = System.currentTimeMillis();

			for (ExchangeInfoEntry entry : lstExchangeInfoEntries)
			{
				// if (entry.getQuoteAsset().equalsIgnoreCase(Config.DEFAULT_SYMBOL_RIGHT) && "TRADING".equalsIgnoreCase(entry.getStatus()))
				if ("TRADING".equalsIgnoreCase(entry.getStatus()))
				{
					mapExchangeInfo.put(entry.getSymbol(), entry);
				}
			}

			return true;
		}
		catch (Exception e)
		{
			CoreLog.error(e);
			return false;
		}
	}

	public static void close()
	{
		mapExchangeInfo = new ConcurrentHashMap<String, ExchangeInfoEntry>();
		timestamp = 0;
	}

	public static Symbol toSymbol(String symbolPair)
	{
		ExchangeInfoEntry exchangeInfoEntry = mapExchangeInfo.get(symbolPair);
		if (exchangeInfoEntry == null)
		{
			return null;
		}

		String pair = exchangeInfoEntry.getSymbol();
		String nameLeft = exchangeInfoEntry.getBaseAsset();
		String nameRight = exchangeInfoEntry.getQuoteAsset();

		BigDecimal tickSize = null;
		int pricePrecision = 0;
		String pricePattern = null;

		BigDecimal minQty = null;
		int qtyPrecision = 0;
		String qtyPattern = null;

		for (Map<String, String> entry : exchangeInfoEntry.getFilters())
		{
			if (entry.get("filterType").equals("PRICE_FILTER"))
			{
				String _tickSize = entry.get("tickSize");
				tickSize = new BigDecimal(_tickSize);

				pricePrecision = (int) Math.log10(Double.valueOf(_tickSize)) * -1;

				String _pricePattern = "#0";
				if (pricePrecision > 0)
				{
					_pricePattern += "." + StringUtils.repeat("0", pricePrecision);
				}
				pricePattern = _pricePattern;
			}

			if (entry.get("filterType").equals("LOT_SIZE"))
			{
				String _minQty = entry.get("minQty");
				minQty = new BigDecimal(_minQty);

				qtyPrecision = (int) Math.log10(Double.valueOf(_minQty)) * -1;

				String _qtyPattern = "#0";
				if (qtyPrecision > 0)
				{
					_qtyPattern += "." + StringUtils.repeat("0", qtyPrecision);
				}
				qtyPattern = _qtyPattern;
			}
		}

		Symbol symbol = new Symbol(pair, nameLeft, nameRight, tickSize, pricePrecision, pricePattern, minQty, qtyPrecision, qtyPattern);

		return symbol;
	}
	
}
