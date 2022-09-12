package api.client.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.client.config.ApiConfig;
import api.client.enums.DepthMode;
import api.client.enums.MarketType;
import api.client.futures.sync.SyncFuturesClient;
import api.client.model.async.DepthEvent;
import api.client.model.sync.Depth;
import api.client.model.sync.DepthEntry;
import api.client.spot.sync.SyncSpotClient;
import sanzol.app.service.LogService;
import sanzol.app.util.PriceUtil;

public class DepthService extends WebSocketClient
{
	private static final int MAX_DEPTH_LIMIT = 1000;
	private static final double MIN_DISTANCE = .3;

	private String symbol;
	private TreeMap<BigDecimal, DepthEntry> mapAsks;
	private TreeMap<BigDecimal, DepthEntry> mapBids;

	private Long connectTime;

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public TreeMap<BigDecimal, DepthEntry> getMapAsks()
	{
		return mapAsks;
	}

	public void setMapAsks(TreeMap<BigDecimal, DepthEntry> mapAsks)
	{
		this.mapAsks = mapAsks;
	}

	public TreeMap<BigDecimal, DepthEntry> getMapBids()
	{
		return mapBids;
	}

	public void setMapBids(TreeMap<BigDecimal, DepthEntry> mapBids)
	{
		this.mapBids = mapBids;
	}

	public Long getConnectTime()
	{
		return connectTime;
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	public DepthService(URI serverUri, Draft draft)
	{
		super(serverUri, draft);
	}

	public DepthService(URI serverURI)
	{
		super(serverURI);
	}

	public DepthService(URI serverUri, Map<String, String> httpHeaders)
	{
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata)
	{
		LogService.info("DepthClient - opened connection - " + symbol);
	}

	@Override
	public void onMessage(String message)
	{
		LogService.debug(message);

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try
		{
			DepthEvent event = mapper.readerFor(DepthEvent.class).readValue(message);

			for (List<BigDecimal> entry : event.getAsks())
			{
				DepthEntry depthEntry = new DepthEntry(entry.get(0), entry.get(1));
				
				if (depthEntry.getQty().doubleValue() == 0)
					mapAsks.remove(depthEntry.getPrice());
				else
					mapAsks.put(depthEntry.getPrice(), depthEntry);
			}
			for (List<BigDecimal> entry : event.getBids())
			{
				DepthEntry depthEntry = new DepthEntry(entry.get(0), entry.get(1));

				if (depthEntry.getQty().doubleValue() == 0)
					mapBids.remove(depthEntry.getPrice());
				else
					mapBids.put(depthEntry.getPrice(), depthEntry);
			}

		}
		catch (IOException e)
		{
			LogService.error(e);
		}

	}

	@Override
	public void onClose(int code, String reason, boolean remote)
	{
		LogService.info("DepthClient - Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
	}

	@Override
	public void onError(Exception ex)
	{
		LogService.error(ex);
	}

	// ------------------------------------------------------------------------

	private static boolean requiredMore(TreeMap<BigDecimal, DepthEntry> mapAsks, TreeMap<BigDecimal, DepthEntry> mapBids)
	{
		if (mapAsks.size() < MAX_DEPTH_LIMIT && mapBids.size() < MAX_DEPTH_LIMIT)
		{
			return false;
		}
		if (PriceUtil.priceDistUp(mapAsks.firstKey(), mapAsks.lastKey(), false).doubleValue() > MIN_DISTANCE && 
			PriceUtil.priceDistDown(mapBids.firstKey(), mapBids.lastKey(), false).doubleValue() > MIN_DISTANCE)
		{
			return false;
		}
		return true;
	}

	public static DepthService getInstance(String pair)
	{
		return getInstance(pair, DepthMode.both, TimeUnit.SECONDS.toMillis(10));
	}

	public static DepthService getInstance(String pair, DepthMode mode, long timeOut)
	{
		try
		{
			if (DepthCache.containsKey(pair))
			{
				return DepthCache.get(pair);
			}

			final String url = ApiConfig.WS_BASE_URL + "/" + pair.toLowerCase() + "@depth";

			DepthService depthClient = new DepthService(new URI(url));
			depthClient.symbol = pair;

			// ----------------------------------------------------------------
			if (mode == DepthMode.async_only)
			{
				depthClient.mapAsks = new TreeMap<BigDecimal, DepthEntry>();
				depthClient.mapBids = new TreeMap<BigDecimal, DepthEntry>(Collections.reverseOrder());
			}
			else
			{
				// ---- GET SNAPSHOOT -----------------------------------------
				Depth depth;
				if (ApiConfig.MARKET_TYPE == MarketType.futures)
					depth = SyncFuturesClient.getDepth(pair.toUpperCase(), null);
				else
					depth = SyncSpotClient.getDepth(pair.toUpperCase(), null);

				depthClient.mapAsks = depth.getMapAsks();
				depthClient.mapBids = depth.getMapBids();
			}

			// ----------------------------------------------------------------
			if (mode != DepthMode.snapshot_only)
			{
				if (mode == DepthMode.both_force || requiredMore(depthClient.mapAsks, depthClient.mapBids))
				{
					depthClient.connect();
					depthClient.connectTime = System.currentTimeMillis();
	
					if (timeOut > 0)
					{
						Thread.sleep(timeOut);
						depthClient.close();
						depthClient.connectTime = null;
					}
				}
			}

			return depthClient;
		}
		catch (Exception e) 
		{
			LogService.error(e);
			return null;
		}
	}

	public static void main(String[] args)
	{
		ApiConfig.setFutures();
		DepthService depthClient = getInstance("BTCUSDT");

		int i = 0;
		for (DepthEntry entry : depthClient.getMapAsks().values())
		{
			i++;
			System.out.println(i + " - "+ entry.getPrice() + "  :  " + entry.getQty());
		}

		int j = 0;
		for (DepthEntry entry : depthClient.getMapBids().values())
		{
			j++;
			System.out.println(j + " - "+ entry.getPrice() + "  :  " + entry.getQty());
		}

		System.out.println(i + " - " + j);
	}

}
