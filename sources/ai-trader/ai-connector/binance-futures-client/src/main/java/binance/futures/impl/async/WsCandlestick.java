package binance.futures.impl.async;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import aitrader.util.observable.Handler;
import binance.futures.config.ApiConstants;
import binance.futures.config.ApiLog;
import binance.futures.enums.IntervalType;
import binance.futures.model.event.CandlestickEvent;

public class WsCandlestick extends WebSocketClient
{
	private Handler<CandlestickEvent> observer;

	private String symbolPair;
	private IntervalType intervalType;

	// --------------------------------------------------------------------

	public WsCandlestick(URI serverUri, Draft draft)
	{
		super(serverUri, draft);
	}

	public WsCandlestick(URI serverURI)
	{
		super(serverURI);
	}

	public WsCandlestick(URI serverUri, Map<String, String> httpHeaders)
	{
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata)
	{
		ApiLog.info("WsCandlestick - opened connection - " + symbolPair + " - " + intervalType);
	}

	@Override
	public void onMessage(String message)
	{
		ApiLog.debug(message);

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try
		{
			CandlestickEvent event = mapper.readerFor(CandlestickEvent.class).readValue(message);

			observer.handle(event);
		}
		catch (IOException e)
		{
			ApiLog.error(e);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote)
	{
		ApiLog.info("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
	}

	@Override
	public void onError(Exception ex)
	{
		ApiLog.error(ex);
	}

	// --------------------------------------------------------------------

	public static WsCandlestick create(String symbolPair, IntervalType intervalType, Handler<CandlestickEvent> observer)
	{
		try
		{
			final String url = ApiConstants.WS_BASE_URL + "/" + symbolPair + "@kline_" + intervalType;

			WsCandlestick wsCandlestick = new WsCandlestick(new URI(url));
			wsCandlestick.observer = observer;
			wsCandlestick.symbolPair = symbolPair;
			wsCandlestick.intervalType = intervalType;
			return wsCandlestick;
		}
		catch (URISyntaxException e)
		{
			ApiLog.error(e);
			return null;
		}
	}

}