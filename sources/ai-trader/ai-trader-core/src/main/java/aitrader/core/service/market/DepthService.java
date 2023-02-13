package aitrader.core.service.market;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import aitrader.core.config.CoreLog;
import aitrader.core.model.enums.DepthMode;
import aitrader.core.service.symbol.SymbolInfoService;
import binance.futures.impl.UnsignedClient;
import binance.futures.impl.async.WsDepth;
import binance.futures.model.Depth;
import binance.futures.model.event.DepthEvent;
import technicals.model.OrderBook;
import technicals.model.OrderBookEntry;

public class DepthService
{
	private WsDepth wsDepth;

	private String symbolPair;
	private Map<BigDecimal, OrderBookEntry> mapAsks;
	private Map<BigDecimal, OrderBookEntry> mapBids;

	private Long connectTime;

	public String getSymbolPair()
	{
		return symbolPair;
	}

	public void setSymbolPair(String symbolPair)
	{
		this.symbolPair = symbolPair;
	}

	public Map<BigDecimal, OrderBookEntry> getMapAsks()
	{
		return mapAsks;
	}

	public void setMapAsks(Map<BigDecimal, OrderBookEntry> mapAsks)
	{
		this.mapAsks = mapAsks;
	}

	public Map<BigDecimal, OrderBookEntry> getMapBids()
	{
		return mapBids;
	}

	public void setMapBids(Map<BigDecimal, OrderBookEntry> mapBids)
	{
		this.mapBids = mapBids;
	}

	public Long getConnectTime()
	{
		return connectTime;
	}

	// --------------------------------------------------------------------

	public OrderBook toOrderBook()
	{
		TreeMap<BigDecimal, OrderBookEntry> asks = new TreeMap<BigDecimal, OrderBookEntry>();
		asks.putAll(mapAsks);

		TreeMap<BigDecimal, OrderBookEntry> bids = new TreeMap<BigDecimal, OrderBookEntry>(Collections.reverseOrder());
		bids.putAll(mapBids);

		OrderBook orderBook = new OrderBook(SymbolInfoService.getSymbol(symbolPair).getPricePrecision());
		orderBook.setAsks(asks);
		orderBook.setBids(bids);

		return orderBook;
	}

	// --------------------------------------------------------------------

	public void close()
	{
		wsDepth.close();
	}

	// --------------------------------------------------------------------

	public void onMessage(DepthEvent event)
	{
		for (List<BigDecimal> entry : event.getAsks())
		{
			OrderBookEntry orderBookEntry = new OrderBookEntry(entry.get(0), entry.get(1));

			if (orderBookEntry.getQty().doubleValue() == 0)
				mapAsks.remove(orderBookEntry.getPrice());
			else
				mapAsks.put(orderBookEntry.getPrice(), orderBookEntry);
		}
		for (List<BigDecimal> entry : event.getBids())
		{
			OrderBookEntry orderBookEntry = new OrderBookEntry(entry.get(0), entry.get(1));

			if (orderBookEntry.getQty().doubleValue() == 0)
				mapBids.remove(orderBookEntry.getPrice());
			else
				mapBids.put(orderBookEntry.getPrice(), orderBookEntry);
		}
	}

	// --------------------------------------------------------------------

	public void load(Depth depth)
	{
		mapAsks = new ConcurrentHashMap<BigDecimal, OrderBookEntry>();
		for (List<BigDecimal> entry : depth.getAsks())
		{
			OrderBookEntry orderBookEntry = new OrderBookEntry(entry.get(0), entry.get(1));
			mapAsks.put(orderBookEntry.getPrice(), orderBookEntry);
		}

		mapBids = new ConcurrentHashMap<BigDecimal, OrderBookEntry>();
		for (List<BigDecimal> entry : depth.getBids())
		{
			OrderBookEntry orderBookEntry = new OrderBookEntry(entry.get(0), entry.get(1));
			mapBids.put(orderBookEntry.getPrice(), orderBookEntry);
		}
	}	
	
	// --------------------------------------------------------------------

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

			// --------------------------------------------------------------------
			DepthService depthService = new DepthService();
			depthService.setSymbolPair(symbolPair);

			// --------------------------------------------------------------------
			if (mode == DepthMode.async_only)
			{
				depthService.mapAsks = new ConcurrentHashMap<BigDecimal, OrderBookEntry>();
				depthService.mapBids = new ConcurrentHashMap<BigDecimal, OrderBookEntry>();
			}
			else
			{
				// ---- GET SNAPSHOOT -----------------------------------------
				Depth depth = UnsignedClient.getDepth(symbolPair.toUpperCase());
				depthService.load(depth);
			}

			// --------------------------------------------------------------------
			if (mode != DepthMode.snapshot_only)
			{
				if (mode == DepthMode.both_force)
				{

					depthService.wsDepth = WsDepth.create(symbolPair, (event) -> {
						depthService.onMessage(event);
					});
					depthService.wsDepth.connect();

					depthService.connectTime = System.currentTimeMillis();

					// --------------------------------------------------------------------

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
			CoreLog.error(e);
			return null;
		}
	}

	public static void main(String[] args)
	{
		DepthService depthClient = getInstance("BTCUSDT");

		int i = 0;
		for (OrderBookEntry entry : depthClient.getMapAsks().values())
		{
			i++;
			System.out.println(i + " - " + entry.getPrice() + "  :  " + entry.getQty());
		}

		int j = 0;
		for (OrderBookEntry entry : depthClient.getMapBids().values())
		{
			j++;
			System.out.println(j + " - " + entry.getPrice() + "  :  " + entry.getQty());
		}

		System.out.println(i + " - " + j);
	}

}
