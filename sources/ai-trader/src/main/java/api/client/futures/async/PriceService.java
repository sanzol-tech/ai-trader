package api.client.futures.async;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.client.constant.ApiClientConstants;
import api.client.futures.async.model.SymbolTickerEvent;
import api.client.futures.sync.SyncFuturesClient;
import api.client.futures.sync.model.SymbolTicker;
import sanzol.app.config.Config;
import sanzol.app.service.LogService;
import sanzol.app.service.Symbol;

public class PriceService extends WebSocketClient
{
	private static PriceService priceService;

	private static Map<String, SymbolTickerEvent> mapTickers = new ConcurrentHashMap<String, SymbolTickerEvent>();

	public static BigDecimal getLastPrice(Symbol coin)
	{
		if (!mapTickers.containsKey(coin.getName()))
		{
			return BigDecimal.valueOf(-1);
		}

		return mapTickers.get(coin.getName()).getLastPrice();
	}

	public static SymbolTickerEvent getSymbolTickerEvent(Symbol coin)
	{
		if (!mapTickers.containsKey(coin.getName()))
		{
			return null;
		}

		return mapTickers.get(coin.getName());
	}

	public static Map<String, SymbolTickerEvent> getMapTickers()
	{
		return mapTickers;
	}

	// ------------------------------------------------------------------------

	private static List<PriceListener> observers = new ArrayList<PriceListener>();

	public static void attachRefreshObserver(PriceListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(PriceListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers()
	{
		for (PriceListener observer : observers)
		{
			observer.onPriceUpdate();
		}
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	public PriceService(URI serverUri, Draft draft)
	{
		super(serverUri, draft);
	}

	public PriceService(URI serverURI)
	{
		super(serverURI);
	}

	public PriceService(URI serverUri, Map<String, String> httpHeaders)
	{
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata)
	{
		LogService.info("PriceService - opened connection");
	}

	@Override
	public void onMessage(String message)
	{
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try
		{
			SymbolTickerEvent[] event = mapper.readerFor(SymbolTickerEvent[].class).readValue(message);

			for (SymbolTickerEvent entry : event)
			{
				if (entry.getSymbol().endsWith(Config.DEFAULT_SYMBOL_RIGHT))
				{
					mapTickers.put(entry.getSymbol(), entry);
				}
			}

			/*
			for (SymbolTickerEvent entry : mapTickers.values())
			{
				System.out.println(entry.getSymbol() + " : " + entry.getLastPrice());
			}
			*/

			notifyAllLogObservers();

		}
		catch (IOException e)
		{
			LogService.error(e);
			e.printStackTrace();
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote)
	{
		LogService.info("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
	}

	@Override
	public void onError(Exception ex)
	{
		LogService.error(ex);
		ex.printStackTrace();
	}

	// ------------------------------------------------------------------------

	public static void start() throws KeyManagementException, NoSuchAlgorithmException
	{
		try
		{
			// ----------------------------------------------------------------
			List<SymbolTicker> lstSymbolTickers = SyncFuturesClient.getSymbolTickers();
			for (SymbolTicker entry : lstSymbolTickers)
			{
				if (entry.getSymbol().endsWith(Config.DEFAULT_SYMBOL_RIGHT))
				{
					mapTickers.put(entry.getSymbol(), entry.toSymbolTickerEvent());
				}
			}

			// ----------------------------------------------------------------
			URI uri = new URI(ApiClientConstants.FUTURES_WS_BASE_URL + "/" + "!ticker@arr");
			priceService = new PriceService(uri);
			priceService.connect();
		}
		catch (URISyntaxException e)
		{
			LogService.error(e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException
	{
		start();
	}

}