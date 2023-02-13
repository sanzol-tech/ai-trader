package aitrader.core.service.position;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import aitrader.core.config.CoreConfig;
import aitrader.core.config.CoreLog;
import aitrader.core.config.PrivateConfig;
import aitrader.core.model.Position;
import aitrader.core.model.PositionName;
import aitrader.core.model.PositionOrder;
import aitrader.core.model.Symbol;
import aitrader.core.model.enums.OpType;
import aitrader.core.service.symbol.ExchangeInfoService;
import aitrader.core.service.symbol.SymbolInfoService;
import aitrader.core.service.symbol.SymbolTickerService;
import aitrader.util.observable.Handler;
import binance.futures.enums.PositionSide;
import binance.futures.impl.SignedClient;
import binance.futures.model.Order;
import binance.futures.model.PositionRisk;

public final class PositionService
{
	private static final long DEFAULT_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(10);

	private static boolean isStarted = false;

	private static Map<PositionName, Position> mapPositions = new HashMap<PositionName, Position>();	

	public static Map<PositionName, Position> getMapPositions()
	{
		return mapPositions;
	}

	public static List<Position> getPositions(String pair)
	{
		return mapPositions.values()
				.stream()
				.filter(e -> pair.equals(e.getSymbol().getPair()))
				.collect(Collectors.toList());
	}

	public static Position getPosition(String pair, String side)
	{
		return mapPositions.get(PositionName.from(pair, side));
	}

	public static boolean contains(String pair)
	{
		return mapPositions.values().stream().anyMatch(e -> e.getSymbol().getPair().equals(pair));
	}

 	public static int getShortCount()
	{
		int count = 0;
		for (Position entry : mapPositions.values())
		{
			if (entry.isOpen() && entry.getQuantity().doubleValue() < 0)
			{
				count++;
			}
		}
		return count;
	}

	public static int getLongCount()
	{
		int count = 0;
		for (Position entry : mapPositions.values())
		{
			if (entry.isOpen() && entry.getQuantity().doubleValue() > 0)
			{
				count++;
			}
		}
		return count;
	}

	public static PositionOrder getTpOrder(String pair, String side)
	{
		Position position = mapPositions.get(PositionName.from(pair, side));
		if (position != null && position.isOpen())
		{
			for (PositionOrder entry : position.getLstOrders())
			{
				if (entry.getOpType() == OpType.TAKE_PROFIT)
				{
					return entry;
				}
			}
		} 

		return null;
	}

	public static PositionOrder getSlOrder(String pair, String side)
	{
		Position position = mapPositions.get(PositionName.from(pair, side));
		if (position != null && position.isOpen())
		{
			for (PositionOrder entry : position.getLstOrders())
			{
				if (entry.getOpType() == OpType.STOP_LOSS)
				{
					return entry;
				}
			}
		}

		return null;
	}

	// --------------------------------------------------------------------

	public synchronized static void searchPositions()
	{
		try
		{
			SignedClient signedClient = SignedClient.create(PrivateConfig.getApiKey(), PrivateConfig.getSecretKey());
			
			List<PositionRisk> lstPositionRisk = signedClient.getPositionRisk();
			if (lstPositionRisk == null || lstPositionRisk.isEmpty())
			{
				mapPositions = new HashMap<PositionName, Position>();
				return;
			}
			mapPositions = toPositions(lstPositionRisk);

			// --------------------------------------------------------------------

			List<Order> lstOpenOrders = signedClient.getOpenOrders();

			Comparator<Order> orderComparator = Comparator
													.comparing(Order::getSymbol)
													.thenComparing(Order::getUpdateTime);
			Collections.sort(lstOpenOrders, orderComparator);
			
			// --------------------------------------------------------------------

			addPositionOrders(lstOpenOrders);
			
		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}

		try
		{
			PositionsBot.onPositionUpdate();
			notifyAllObservers();
		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}
	}

	private static Map<PositionName, Position> toPositions(List<PositionRisk> lstPositionRisk)
	{
		Map<PositionName, Position> mapPositions = new HashMap<PositionName, Position>();

		for (PositionRisk entry : lstPositionRisk)
		{
			Symbol symbol = SymbolInfoService.getSymbol(entry.getSymbol());
			if (symbol != null)
			{
				if (entry.getPositionAmt().abs().doubleValue() != 0)
				{
					Position position = new Position();
	
					position.setSymbol(symbol);
					position.setOpen(true);
					position.setPositionSide(PositionSide.valueOf(entry.getPositionSide()));
					position.setMarginType(entry.getMarginType());
					position.setLeverage(entry.getLeverage());
					position.setEntryPrice(entry.getEntryPrice());
					position.setMarkPrice(entry.getMarkPrice());
					position.setLiqPrice(entry.getLiquidationPrice());
					position.setQuantity(entry.getPositionAmt());
					position.setPnl(entry.getUnRealizedProfit());
	
					mapPositions.put(PositionName.from(entry.getSymbol(), entry.getPositionSide()), position);
				}
			}
		}

		return mapPositions;
	}

	private static OpType getOpType(Order order)
	{
		if ("LIMIT".equals(order.getType()) && order.getReduceOnly() && order.getStopPrice().doubleValue() == 0)
		{
			if ("SHORT".equals(order.getPositionSide()) && "BUY".equals(order.getSide()))
			{
				return OpType.TAKE_PROFIT;
			}
			else if ("LONG".equals(order.getPositionSide()) && "SELL".equals(order.getSide()))
			{
				return OpType.TAKE_PROFIT;
			}
		}

		if ("STOP_MARKET".equals(order.getType()) && order.getReduceOnly() && order.getStopPrice().doubleValue() != 0)
		{
			if ("SHORT".equals(order.getPositionSide()) && "BUY".equals(order.getSide()))
			{
				return OpType.STOP_LOSS;
			}
			else if ("LONG".equals(order.getPositionSide()) && "SELL".equals(order.getSide()))
			{
				return OpType.STOP_LOSS;
			}
		}

		return OpType.ORDER;
	}

	private static void addPositionOrders(List<Order> lstOrders)
	{
		for (Order entry : lstOrders)
		{
			Symbol symbol = SymbolInfoService.getSymbol(entry.getSymbol());
			if (symbol != null)
			{
				PositionOrder order = new PositionOrder();

				OpType opType = getOpType(entry);

				order.setOrderId(entry.getOrderId());
				order.setSymbol(symbol);
				order.setTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(entry.getUpdateTime()), ZoneOffset.UTC));
				order.setOrderSide(entry.getSide());
				order.setOpType(opType);
				order.setPrice(opType == OpType.STOP_LOSS ? entry.getStopPrice() : entry.getPrice());
				order.setQuantity(entry.getOrigQty());
				order.setReduceOnly(entry.getReduceOnly());
				order.setStatus(entry.getStatus());

				PositionName positionName = PositionName.from(entry.getSymbol(), entry.getPositionSide());
				Position position = mapPositions.get(positionName);
				if (position == null)
				{
					position = new Position();
					position.setSymbol(symbol);
					position.setPositionSide(PositionSide.valueOf(entry.getPositionSide()));
					position.setOpen(false);
					mapPositions.put(positionName, position);
				}

				position.getLstOrders().add(order);
			}
		}
	}
	
	// --------------------------------------------------------------------

	private static Timer timer1;

	public static void start()
	{
		if (isStarted)
		{
			return;
		}

		CoreLog.info("PoitionService - Start");
		
		TimerTask task = new TimerTask()
		{
			public void run()
			{
				searchPositions();
			}
		};
		timer1 = new Timer("PoitionService");
		timer1.schedule(task, 0, DEFAULT_PERIOD_MILLIS);

		isStarted = true;
	}

	public static void close()
	{
		if (isStarted && timer1 != null)
		{
			timer1.cancel();
			isStarted = false;
		}

		mapPositions = new HashMap<PositionName, Position>();

		notifyAllObservers();
	}

	// --------------------------------------------------------------------

	private static List<Handler<Void>> observers = new ArrayList<Handler<Void>>();

	public static void attachObserver(Handler<Void> observer)
	{
		observers.add(observer);
	}

	public static void deattachObserver(Handler<Void> observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllObservers()
	{
		for (Handler<Void> observer : observers)
		{
			observer.handle(null);
		}
	}
	
	// --------------------------------------------------------------------
	
	public static void main(String[] args) throws IOException
	{
		PrivateConfig.load();
		CoreConfig.load();
		
		ExchangeInfoService.getSnapshoot();
		SymbolTickerService.getSnapshoot();
		SymbolTickerService.openWebsocket();

		searchPositions();
	}

}
