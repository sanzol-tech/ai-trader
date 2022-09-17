package api.client.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.client.config.ApiConfig;
import api.client.enums.MarketType;
import api.client.futures.sync.SyncFuturesClient;
import api.client.model.async.SymbolTickerEvent;
import api.client.model.sync.SymbolTicker;
import api.client.spot.sync.SyncSpotClient;
import sanzol.app.config.Config;
import sanzol.app.model.SymbolInfo;
import sanzol.app.service.LogService;
import sanzol.app.service.Symbol;
import sanzol.app.util.PriceUtil;

public class PriceService extends WebSocketClient
{
	private static PriceService priceService;

	private static Map<String, SymbolTickerEvent> mapTickers = new ConcurrentHashMap<String, SymbolTickerEvent>();

	public static BigDecimal getLastPrice(Symbol symbol)
	{
		if (!mapTickers.containsKey(symbol.getPair()))
		{
			return BigDecimal.valueOf(-1);
		}

		return mapTickers.get(symbol.getPair()).getLastPrice();
	}

	public static SymbolTickerEvent getSymbolTickerEvent(Symbol symbol)
	{
		if (!mapTickers.containsKey(symbol.getPair()))
		{
			return null;
		}

		return mapTickers.get(symbol.getPair());
	}

	public static Map<String, SymbolTickerEvent> getMapTickers()
	{
		return mapTickers;
	}

	// ------------------------------------------------------------------------

	public static List<SymbolInfo> getLstSymbolsInfo(boolean onlyFavorites, boolean onlyBetters) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<String> lstFavorites = Config.getLstFavSymbols();

		List<SymbolInfo> lstSymbolsInfo = new ArrayList<SymbolInfo>();

		List<SymbolTickerEvent> lstSymbolTickerEvent = new ArrayList<SymbolTickerEvent>();
		lstSymbolTickerEvent.addAll(mapTickers.values());

		for (SymbolTickerEvent entry : lstSymbolTickerEvent)
		{
			SymbolInfo symbolInfo = SymbolInfo.getInstance(entry);
			
			if (symbolInfo == null)
			{
				continue;
			}

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

	public static List<SymbolInfo> getLstSymbolsInfo(boolean onlyFavorites, boolean onlyBetters, int limit) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		return getLstSymbolsInfo(onlyFavorites, onlyBetters).subList(0, limit - 1);
	}

	public static List<String> getLstSymbolsMini(boolean onlyFavorites, boolean onlyBetters) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<String> list = new ArrayList<String>();
		list.add(String.format("%-10s %12s %10s %10s", "SYMBOL", "PRICE", "VOLUME", "CHANGE"));

		List<SymbolInfo> lstSymbolsInfo = getLstSymbolsInfo(onlyFavorites, onlyBetters);
		for (SymbolInfo entry : lstSymbolsInfo)
		{
			Symbol symbol = entry.getSymbol();
			list.add(String.format("%-10s %12s %10s %8.2f %%", symbol.getNameLeft(), symbol.priceToStr(entry.getLastPrice()), PriceUtil.cashFormat(entry.getUsdVolume()), entry.getPriceChangePercent()));
		}

		return list;
	}

	public static List<String> getLstSymbolNames(boolean onlyFavorites, boolean onlyBetters) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<String> list = new ArrayList<String>();

		List<SymbolInfo> lstSymbolsInfo = getLstSymbolsInfo(onlyFavorites, onlyBetters);
		for (SymbolInfo entry : lstSymbolsInfo)
		{
			list.add(entry.getSymbol().getPair());
		}
		
		return list;
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
		LogService.debug(message);

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
	}

	// ------------------------------------------------------------------------

	public static void start() throws KeyManagementException, NoSuchAlgorithmException
	{
		try
		{
			// ---- GET SNAPSHOOT ---------------------------------------------
			List<SymbolTicker> lstSymbolTickers;
			if (ApiConfig.MARKET_TYPE == MarketType.futures)
				lstSymbolTickers = SyncFuturesClient.getSymbolTickers();
			else
				lstSymbolTickers = SyncSpotClient.getSymbolTickers();

			for (SymbolTicker entry : lstSymbolTickers)
			{
				if (entry.getSymbol().endsWith(Config.DEFAULT_SYMBOL_RIGHT))
				{
					mapTickers.put(entry.getSymbol(), entry.toSymbolTickerEvent());
				}
			}

			// ----------------------------------------------------------------
			URI uri = new URI(ApiConfig.WS_BASE_URL + "/" + "!ticker@arr");
			priceService = new PriceService(uri);
			priceService.connect();
		}
		catch (URISyntaxException e)
		{
			LogService.error(e);
		}
	}

	public static void main(String[] args) throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException
	{
		ApiConfig.setFutures();
		start();
	}

}