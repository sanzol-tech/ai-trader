package sanzol.aitrader.be.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import api.client.futures.impl.SyncFuturesClient;
import api.client.futures.model.Order;
import api.client.futures.model.PositionRisk;
import api.client.impl.model.enums.MarketType;
import sanzol.aitrader.be.config.ServerApp;
import sanzol.aitrader.be.model.Symbol;
import sanzol.util.log.LogService;
import sanzol.util.price.Convert;

public final class PositionService
{
	private static final long DEFAULT_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(10);

	private static boolean isStarted = false;

	private static List<PositionRisk> lstPositionRisk;
	private static List<Order> lstOpenOrders;

	// ------------------------------------------------------------------------

	public static List<PositionRisk> getLstPositionRisk()
	{
		return lstPositionRisk;
	}

	public static int getShortCount()
	{
		int count = 0;
		if (lstPositionRisk != null && !lstPositionRisk.isEmpty())
		{
			for (PositionRisk entry : lstPositionRisk)
			{
				if (entry.getPositionAmt().doubleValue() < 0)
				{
					count++;
				}
			}
		}
		return count;
	}

	public static int getLongCount()
	{
		int count = 0;
		if (lstPositionRisk != null && !lstPositionRisk.isEmpty())
		{
			for (PositionRisk entry : lstPositionRisk)
			{
				if (entry.getPositionAmt().doubleValue() > 0)
				{
					count++;
				}
			}
		}
		return count;
	}

	public static PositionRisk getPositionRisk(String symbolName)
	{
		if (lstPositionRisk != null && !lstPositionRisk.isEmpty())
		{
			for (PositionRisk entry : lstPositionRisk)
			{
				if (entry.getSymbol().equals(symbolName))
				{
					return entry;
				}
			}
		}
		return null;
	}

	public static List<Order> getLstOpenOrders()
	{
		return lstOpenOrders;
	}

	public static List<Order> getLstOpenOrders(String symbolName)
	{
		List<Order> list = new ArrayList<Order>();
		if (lstOpenOrders != null && !lstOpenOrders.isEmpty())
		{
			for (Order entry : lstOpenOrders)
			{
				if (entry.getSymbol().equals(symbolName) && "NEW".equals(entry.getStatus()))
				{
					list.add(entry);
				}
			}
		}
		return list;
	}
	
	public static boolean existsPosition(String symbolName)
	{
		if (lstPositionRisk != null && !lstPositionRisk.isEmpty())
		{
			for (PositionRisk entry : lstPositionRisk)
			{
				if (entry.getSymbol().equals(symbolName) && entry.getPositionAmt().compareTo(BigDecimal.ZERO) != 0)
				{
					return true;
				}
			}
		}
		if (lstOpenOrders != null && !lstOpenOrders.isEmpty())
		{
			for (Order entry : lstOpenOrders)
			{
				if (entry.getSymbol().equals(symbolName) && "NEW".equals(entry.getStatus()))
				{
					return true;
				}
			}
		}
		return false;
	}

	public static Order getTpOrder(String symbolName, String side)
	{
		for (Order entry : getLstOpenOrders(symbolName))
		{
			if ("LIMIT".equals(entry.getType()) && entry.getReduceOnly() && entry.getStopPrice().doubleValue() == 0)
			{
				if ("SHORT".equals(side) && "BUY".equals(entry.getSide()))
				{
					return entry;
				}
				else if ("LONG".equals(side) && "SELL".equals(entry.getSide()) && entry.getReduceOnly() && entry.getStopPrice().doubleValue() == 0)
				{
					return entry;
				}
			}
		}
		return null;
	}

	public static Order getSlOrder(String symbolName, String side)
	{
		for (Order entry : getLstOpenOrders(symbolName))
		{
			if ("STOP_MARKET".equals(entry.getType()) && entry.getReduceOnly() && entry.getStopPrice().doubleValue() != 0)
			{
				if ("SHORT".equals(side) && "BUY".equals(entry.getSide()))
				{
					return entry;
				}
				else if ("LONG".equals(side) && "SELL".equals(entry.getSide()))
				{
					return entry;
				}
			}
		}
		return null;
	}
	
	// ------------------------------------------------------------------------

	public static void start()
	{
		if (isStarted)
		{
			return;
		}

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				getPositions();
			}
		};
		Timer timer = new Timer("BalanceService");
		timer.schedule(task, 0, DEFAULT_PERIOD_MILLIS);

		isStarted = true;
	}

	private synchronized static void getPositions()
	{
		try
		{
			lstPositionRisk = SyncFuturesClient.getPositionRisk();

			if (lstPositionRisk == null || lstPositionRisk.isEmpty())
			{
				lstOpenOrders.clear();
				return;
			}

			lstOpenOrders = SyncFuturesClient.getOpenOrders();

			Comparator<Order> orderComparator = Comparator
													.comparing(Order::getSymbol)
													.thenComparing(Order::getUpdateTime);
			Collections.sort(lstOpenOrders, orderComparator);
		}
		catch (Exception e)
		{
			LogService.error(e);
		}

		try
		{
			BotService.onPositionUpdate();
			notifyAllLogObservers();
		}
		catch (Exception e)
		{
			LogService.error(e);
		}
	}

	// ------------------------------------------------------------------------

	public static String toStringOrders(String symbolName)
	{
		StringBuilder sb = new StringBuilder();

		for (Order entry : getLstOpenOrders(symbolName))
		{
			sb.append(String.format("%-22s %-6s %-13s %10s %14s %12s %14s\n", Convert.convertTime(entry.getUpdateTime()), entry.getSide(), entry.getType(), entry.getOrigQty(), entry.getPrice(), entry.getStopPrice(), entry.getReduceOnly() ? "R.Only" : ""));
		}

		return sb.toString();
	}

	public static String toStringPositions(boolean includeOrders) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		if (lstPositionRisk == null || lstPositionRisk.isEmpty())
		{
			return "No open positions found";
		}

		StringBuilder sbBody = new StringBuilder();
		for (PositionRisk entry : lstPositionRisk)
		{
			if (entry.getPositionAmt().compareTo(BigDecimal.ZERO) != 0)
			{
				Symbol symbol = Symbol.getInstance(entry.getSymbol());
				if (symbol != null)
				{
					String side = entry.getPositionAmt().doubleValue() > 0 ? "LONG" : "SHORT";

					sbBody.append(String.format("%-22s %-20s %10s %14s %12s %14s\n",
												entry.getSymbol(),
												side + " " + entry.getMarginType() + " " + entry.getLeverage(),
												entry.getPositionAmt(),
												symbol.priceToStr(entry.getMarkPrice()),
												symbol.priceToStr(entry.getEntryPrice()), 
												Convert.usdToStr(entry.getUnRealizedProfit().doubleValue())));

					if (includeOrders)
					{
						sbBody.append(StringUtils.repeat("-",97));
						sbBody.append("\n");
						sbBody.append(toStringOrders(entry.getSymbol()));
						sbBody.append(StringUtils.repeat("-",97));
						sbBody.append("\n");
					}
				}
			}
		}

		if (sbBody.length() == 0)
		{
			return "No open positions found";
		}

		StringBuilder sb  = new StringBuilder();
		sb.append(String.format("%-22s %-20s %10s %14s %12s %14s\n", "SYMBOL", "TYPE", "AMOUNT", "PRICE", "AVG PRICE", "PNL"));
		sb.append(StringUtils.repeat("-", 97));
		sb.append("\n");
		sb.append(sbBody);
		
		return sb.toString();
	}

	// ------------------------------------------------------------------------

	private static List<PositionListener> observers = new ArrayList<PositionListener>();
	
	public static void attachRefreshObserver(PositionListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(PositionListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers()
	{
		for (PositionListener observer : observers)
		{
			observer.onPositionUpdate();
		}
	}

	// ------------------------------------------------------------------------

	public static void main(String[] args) throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException
	{
		ServerApp.start(MarketType.futures);

		Thread.sleep(5000);
		System.out.println(toStringPositions(true));
	}

}
