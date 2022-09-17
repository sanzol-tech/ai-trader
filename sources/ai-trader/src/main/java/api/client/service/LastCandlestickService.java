package api.client.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.client.config.ApiConfig;
import api.client.futures.enums.IntervalType;
import api.client.model.async.CandlestickEvent;
import sanzol.app.service.LogService;
import sanzol.app.util.PriceUtil;

public class LastCandlestickService extends WebSocketClient
{
	private static LastCandlestickService lastCandlestickService;
	private static BigDecimal priceChangePercent = BigDecimal.ZERO;
	private static BigDecimal priceMove = BigDecimal.ZERO;

	public static BigDecimal getPriceChangePercent()
	{
		return priceChangePercent;
	}

	public static BigDecimal getPriceMove()
	{
		return priceMove;
	}

	// ------------------------------------------------------------------------
	
	private static List<LastCandlestickListener> observers = new ArrayList<LastCandlestickListener>();

	public static void attachRefreshObserver(LastCandlestickListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(LastCandlestickListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers()
	{
		for (LastCandlestickListener observer : observers)
		{
			observer.onBtcChangeUpdate();
		}
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	public LastCandlestickService(URI serverUri, Draft draft)
	{
		super(serverUri, draft);
	}

	public LastCandlestickService(URI serverURI)
	{
		super(serverURI);
	}

	public LastCandlestickService(URI serverUri, Map<String, String> httpHeaders)
	{
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata)
	{
		LogService.info("LastCandlestickService - opened connection");
	}

	@Override
	public void onMessage(String message)
	{
		LogService.debug(message);

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try
		{
			CandlestickEvent event = mapper.readerFor(CandlestickEvent.class).readValue(message);

			BigDecimal openPrice = event.getKline().getOpen();
			BigDecimal highPrice = event.getKline().getHigh();
			BigDecimal lowPrice = event.getKline().getLow();
			BigDecimal closePrice = event.getKline().getClose();

			priceChangePercent = PriceUtil.priceChange(openPrice, closePrice, true);
			priceMove = PriceUtil.priceChange(lowPrice, highPrice, true);

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

	public static void start(String symbol)
	{
		start(symbol, IntervalType._30m);
	}

	public static void start(String symbol, IntervalType intervalType)
	{
		try
		{
			URI uri = new URI(ApiConfig.WS_BASE_URL + "/" + symbol + "@kline_" + intervalType.getCode());

			lastCandlestickService = new LastCandlestickService(uri);
			lastCandlestickService.connect();
		}
		catch (URISyntaxException e)
		{
			LogService.error(e);
		}
	}

	public static void main(String[] args) throws URISyntaxException
	{
		ApiConfig.setFutures();
		start("btcusdt", IntervalType._30m);
	}

}
