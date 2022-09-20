package sanzol.aitrader.be.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import api.client.futures.impl.SyncFuturesClient;
import api.client.impl.async.WsDepth;
import api.client.impl.config.ApiConfig;
import api.client.impl.model.enums.MarketType;
import api.client.model.Depth;
import api.client.model.DepthEntry;
import api.client.model.event.DepthEvent;
import api.client.spot.impl.SyncSpotClient;
import sanzol.aitrader.be.enums.DepthMode;
import sanzol.util.log.LogService;
import sanzol.util.price.PriceUtil;

public class DepthService
{
	private static final int MAX_DEPTH_LIMIT = 1000;
	private static final double MIN_DISTANCE = .3;

	private WsDepth wsDepth;
	
	private String symbolPair;
	private TreeMap<BigDecimal, DepthEntry> mapAsks;
	private TreeMap<BigDecimal, DepthEntry> mapBids;

	private Long connectTime;

	public String getSymbolPair()
	{
		return symbolPair;
	}

	public void setSymbolPair(String symbolPair)
	{
		this.symbolPair = symbolPair;
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

	public void close()
	{
		wsDepth.close();
	}

	// ------------------------------------------------------------------------

	public void onMessage(DepthEvent event)
	{
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

	// ------------------------------------------------------------------------

	private static boolean requiredMore(TreeMap<BigDecimal, DepthEntry> mapAsks, TreeMap<BigDecimal, DepthEntry> mapBids)
	{
		if (mapAsks.size() < MAX_DEPTH_LIMIT && mapBids.size() < MAX_DEPTH_LIMIT)
		{
			return false;
		}
		if (PriceUtil.priceDistUp(mapAsks.firstKey(), mapAsks.lastKey(), false).doubleValue() > MIN_DISTANCE
				&& PriceUtil.priceDistDown(mapBids.firstKey(), mapBids.lastKey(), false).doubleValue() > MIN_DISTANCE)
		{
			return false;
		}
		return true;
	}

	public static DepthService getInstance(String pair)
	{
		return getInstance(pair, DepthMode.both, TimeUnit.SECONDS.toMillis(10));
	}

	public static DepthService getInstance(String symbolPair, DepthMode mode, long timeOut)
	{
		try
		{
			if (DepthCache.containsKey(symbolPair))
			{
				return DepthCache.get(symbolPair);
			}

			// ----------------------------------------------------------------
			DepthService depthService = new DepthService();
			
			// ----------------------------------------------------------------
			if (mode == DepthMode.async_only)
			{
				depthService.mapAsks = new TreeMap<BigDecimal, DepthEntry>();
				depthService.mapBids = new TreeMap<BigDecimal, DepthEntry>(Collections.reverseOrder());
			}
			else
			{
				// ---- GET SNAPSHOOT -----------------------------------------
				Depth depth;
				if (ApiConfig.MARKET_TYPE == MarketType.futures)
					depth = SyncFuturesClient.getDepth(symbolPair.toUpperCase(), null);
				else
					depth = SyncSpotClient.getDepth(symbolPair.toUpperCase(), null);

				depthService.mapAsks = depth.getMapAsks();
				depthService.mapBids = depth.getMapBids();
			}

			// ----------------------------------------------------------------
			if (mode != DepthMode.snapshot_only)
			{
				if (mode == DepthMode.both_force || requiredMore(depthService.mapAsks, depthService.mapBids))
				{

					depthService.wsDepth = WsDepth.create(symbolPair, (event) -> {
						depthService.onMessage(event);
					});
					depthService.wsDepth.connect();

					depthService.connectTime = System.currentTimeMillis();

					// ----------------------------------------------------------------

					if (timeOut > 0)
					{
						Thread.sleep(timeOut);
						depthService.close();
						depthService.connectTime = null;
					}
				}
			}

			return depthService;
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
			System.out.println(i + " - " + entry.getPrice() + "  :  " + entry.getQty());
		}

		int j = 0;
		for (DepthEntry entry : depthClient.getMapBids().values())
		{
			j++;
			System.out.println(j + " - " + entry.getPrice() + "  :  " + entry.getQty());
		}

		System.out.println(i + " - " + j);
	}

}
