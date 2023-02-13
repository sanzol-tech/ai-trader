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
import binance.spot.model.event.DepthEvent;

public class WsDepth extends WebSocketClient
{
	private Handler<DepthEvent> observer;

	private String symbolPair;

	// --------------------------------------------------------------------

	public WsDepth(URI serverUri, Draft draft)
	{
		super(serverUri, draft);
	}

	public WsDepth(URI serverURI)
	{
		super(serverURI);
	}

	public WsDepth(URI serverUri, Map<String, String> httpHeaders)
	{
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata)
	{
		ApiLog.info("WsDepth - opened connection - " + symbolPair);
	}

	@Override
	public void onMessage(String message)
	{
		ApiLog.debug(message);

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try
		{
			DepthEvent event = mapper.readerFor(DepthEvent.class).readValue(message);

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

	public static WsDepth create(String symbolPair, Handler<DepthEvent> observer)
	{
		try
		{
			final String url = ApiConstants.WS_BASE_URL + "/" + symbolPair.toLowerCase() + "@depth";

			WsDepth wsDepth = new WsDepth(new URI(url));
			wsDepth.observer = observer;
			wsDepth.symbolPair = symbolPair;
			return wsDepth;
		}
		catch (URISyntaxException e)
		{
			ApiLog.error(e);
			return null;
		}
	}

}