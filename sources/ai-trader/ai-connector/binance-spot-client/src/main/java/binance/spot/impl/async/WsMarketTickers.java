package binance.spot.impl.async;

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
import binance.spot.config.ApiConstants;
import binance.spot.config.ApiLog;
import binance.spot.model.event.SymbolTickerEvent;

public class WsMarketTickers extends WebSocketClient
{
	private Handler<SymbolTickerEvent[]> observer;

	// --------------------------------------------------------------------

	public WsMarketTickers(URI serverUri, Draft draft)
	{
		super(serverUri, draft);
	}

	public WsMarketTickers(URI serverURI)
	{
		super(serverURI);
	}

	public WsMarketTickers(URI serverUri, Map<String, String> httpHeaders)
	{
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata)
	{
		ApiLog.info("WsSymbolTicker - opened connection");
	}

	@Override
	public void onMessage(String message)
	{
		ApiLog.debug(message);

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try
		{
			SymbolTickerEvent[] event = mapper.readerFor(SymbolTickerEvent[].class).readValue(message);

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

	public static WsMarketTickers create(Handler<SymbolTickerEvent[]> observer)
	{
		try
		{
			final String url = ApiConstants.WS_BASE_URL + "/" + "!ticker@arr";

			WsMarketTickers wsMarketTickers = new WsMarketTickers(new URI(url));
			wsMarketTickers.observer = observer;
			return wsMarketTickers;
		}
		catch (URISyntaxException e)
		{
			ApiLog.error(e);
			return null;
		}
	}

}