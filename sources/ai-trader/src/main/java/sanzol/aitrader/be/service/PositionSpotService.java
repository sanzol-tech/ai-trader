package sanzol.aitrader.be.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import api.client.spot.impl.SyncSpotClient;
import api.client.spot.model.Account;
import api.client.spot.model.AssetBalance;
import api.client.spot.model.Order;
import api.client.spot.model.enums.OrderStatus;
import sanzol.aitrader.be.config.PrivateConfig;
import sanzol.util.log.LogService;
import sanzol.util.price.Convert;

public final class PositionSpotService
{
	private static final long DEFAULT_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(10);

	private static boolean isStarted = false;

	private static List<AssetBalance> lstBalances;
	private static List<Order> lstOpenOrders;
	private static List<Order> lstFilledOrders;

	// ------------------------------------------------------------------------

	public static int getLongCount()
	{
		Set<String> setSymbols = new HashSet<String>();
		if (lstOpenOrders != null && !lstOpenOrders.isEmpty())
		{
			for (Order entry : lstOpenOrders)
			{
				setSymbols.add(entry.getSymbol());
			}
		}
		return setSymbols.size();
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
				if (entry.getSymbol().startsWith(symbolName) && entry.getStatus() == OrderStatus.NEW)
				{
					list.add(entry);
				}
			}
		}
		return list;
	}

	public static boolean existsPosition(String symbolName)
	{
		return false; // TODO
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
		Timer timer = new Timer("PositionService");
		timer.schedule(task, 0, DEFAULT_PERIOD_MILLIS);

		isStarted = true;
	}

	private synchronized static void getPositions()
	{
		try
		{
			Account account = SyncSpotClient.getAccountInformation();
			if (account != null)
			{
				lstBalances = account.getBalances();
			}
			Collections.sort(lstBalances, Comparator.comparing(AssetBalance::getQuantity).reversed());

			lstOpenOrders = SyncSpotClient.getOpenOrders();
			Collections.sort(lstOpenOrders, Comparator.comparing(Order::getSymbol).thenComparing(Order::getUpdateTime));
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
			sb.append(String.format("%-22s %-6s %-13s %14s %14s %14s\n", Convert.convertTime(entry.getUpdateTime()), entry.getSide(), entry.getType(), entry.getOrigQty(), entry.getPrice(), entry.getStopPrice()));
		}

		return sb.toString();
	}

	public static String toStringFilledOrders(String symbolLeft) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException
	{
		try
		{
			lstFilledOrders = SyncSpotClient.getFilledOrders(symbolLeft + "BUSD");
			Collections.sort(lstFilledOrders, Comparator.comparing(Order::getUpdateTime));
		}
		catch(Exception ex)
		{

		}

		StringBuilder sb = new StringBuilder();

		for (Order entry : lstFilledOrders)
		{
			sb.append(String.format("%-22s %-10s %-6s %-10s %14s %14s %14s\n", Convert.convertTime(entry.getUpdateTime()), entry.getSymbol(), entry.getSide(), entry.getType(), entry.getOrigQty(), entry.getPrice(), entry.getStatus()));
		}

		return sb.toString();
	}
	
	public static String toStringPositions(boolean includeOrders) throws KeyManagementException, NoSuchAlgorithmException, IOException, InvalidKeyException
	{
		if (lstBalances == null || lstBalances.isEmpty())
		{
			return "No positions / balances";
		}

		StringBuilder sbBody = new StringBuilder();
		for (AssetBalance entry : lstBalances)
		{
			String side = "LONG";

			sbBody.append(String.format("%-22s %-20s %14s %14s %14s\n",
										entry.getAsset(),
										side,
										entry.getQuantity().toPlainString(),
										entry.getFree().toPlainString(),
										entry.getLocked().toPlainString()));

			if (includeOrders)
			{
				sbBody.append(StringUtils.repeat("-",97));
				sbBody.append("\n");
				sbBody.append(toStringOrders(entry.getAsset()));
				sbBody.append(StringUtils.repeat("-",97));
				sbBody.append("\n");
			}
/*
			sbBody.append(StringUtils.repeat("-",97));
			sbBody.append("\n");
			sbBody.append(toStringFilledOrders(entry.getAsset()));
			sbBody.append(StringUtils.repeat("-",97));
			sbBody.append("\n");
*/			
		}

		StringBuilder sb  = new StringBuilder();
		sb.append(String.format("%-22s %-20s %14s %14s %14s\n", "SYMBOL", "TYPE", "QTY", "FREE", "LOCKED", "AVG PRICE"));
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

	public static void main(String[] args) throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException, InvalidKeyException
	{
		//ServerApp.start(MarketType.spot);
		PrivateConfig.loadKey();
		getPositions();
		//Thread.sleep(5000);
		System.out.println(toStringPositions(true));
		//System.out.println(toStringFilledOrders("FILBUSD"));
		//System.out.println(toStringFilledOrders("ANKRBUSD"));
	}

}
