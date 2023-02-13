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
import binance.futures.model.user.UserDataUpdateEvent;

public class WsUserData extends WebSocketClient
{
	private Handler<UserDataUpdateEvent> observer;

	// --------------------------------------------------------------------

	public WsUserData(URI serverUri, Draft draft)
	{
		super(serverUri, draft);
	}

	public WsUserData(URI serverURI)
	{
		super(serverURI);
	}

	public WsUserData(URI serverUri, Map<String, String> httpHeaders)
	{
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata)
	{
		ApiLog.info("WsUserData - opened connection");
	}

	@Override
	public void onMessage(String message)
	{
		ApiLog.debug(message);

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try
		{
			UserDataUpdateEvent event = mapper.readerFor(UserDataUpdateEvent.class).readValue(message);

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

	public static WsUserData create(String listenKey, Handler<UserDataUpdateEvent> observer)
	{
		try
		{
			final String url = ApiConstants.WS_BASE_URL + "/" + listenKey;

			WsUserData wsUserData = new WsUserData(new URI(url));
			wsUserData.observer = observer;
			return wsUserData;
		}
		catch (URISyntaxException e)
		{
			ApiLog.error(e);
			return null;
		}
	}

}
