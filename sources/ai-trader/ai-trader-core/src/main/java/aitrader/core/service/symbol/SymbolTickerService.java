package aitrader.core.service.symbol;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import aitrader.core.config.CoreConfig;
import aitrader.core.config.CoreLog;
import aitrader.core.model.Symbol;
import aitrader.core.model.SymbolInfo;
import binance.futures.impl.UnsignedClient;
import binance.futures.impl.async.WsMarketTickers;
import binance.futures.model.SymbolTicker;
import binance.futures.model.event.SymbolTickerEvent;

public class SymbolTickerService
{
	private static WsMarketTickers wsSymbolTicker;

	// --------------------------------------------------------------------

	public static void getSnapshoot()
	{
		try
		{
			SymbolInfoService.clean();

			List<SymbolTicker> lstSymbolTickers = UnsignedClient.getSymbolTickers();

			for (SymbolTicker entry : lstSymbolTickers)
			{
				if (entry.getSymbol().endsWith(CoreConfig.getDefaultSymbolRight()))
				{
					addSymbolInfo(entry);
				}
			}
		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}
	}

	public static boolean openWebsocket()
	{
		try
		{
			// ---- GET SNAPSHOOT ---------------------------------------------
			List<SymbolTicker> lstSymbolTickers = UnsignedClient.getSymbolTickers();

			for (SymbolTicker entry : lstSymbolTickers)
			{
				if (entry.getSymbol().endsWith(CoreConfig.getDefaultSymbolRight()))
				{
					addSymbolInfo(entry);
				}
			}

			// --------------------------------------------------------------------

			wsSymbolTicker = WsMarketTickers.create((event) -> {
				onMessage(event);
			});
			wsSymbolTicker.connect();

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
		wsSymbolTicker.close();

		SymbolInfoService.clean();

		SymbolInfoService.notifyAllObservers();
	}

	public static void onMessage(SymbolTickerEvent[] event)
	{
		try
		{
			for (SymbolTickerEvent entry : event)
			{
				if (entry.getSymbol().endsWith(CoreConfig.getDefaultSymbolRight()))
				{
					updateSymbolInfo(entry);
				}
			}

			SymbolInfoService.notifyAllObservers();
		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}
	}

	public static void addSymbolInfo(SymbolTicker ticker) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		Symbol symbol = ExchangeInfoService.toSymbol(ticker.getSymbol());
		if (symbol != null)
		{
			SymbolInfo symbolInfo = new SymbolInfo(symbol, ticker.getLastPrice(), ticker.getHighPrice(), ticker.getLowPrice(), ticker.getQuoteVolume(), ticker.getPriceChangePercent());
			SymbolInfoService.update(ticker.getSymbol(), symbolInfo);
		}
	}

	public static void updateSymbolInfo(SymbolTickerEvent event) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		Symbol symbol = ExchangeInfoService.toSymbol(event.getSymbol());
		if (symbol != null)
		{
			SymbolInfo symbolInfo = SymbolInfoService.getSymbolInfo(symbol.getPair());

			symbolInfo.setLastPrice(event.getLastPrice());
			symbolInfo.setHigh24h(event.getHighPrice());
			symbolInfo.setLow24h(event.getLowPrice());
			symbolInfo.setQuoteVolume24h(event.getQuoteVolume());
			symbolInfo.setChange24h(event.getPriceChangePercent());
		}
	}

}
