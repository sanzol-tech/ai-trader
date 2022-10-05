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

import api.client.exception.ApiException;
import api.client.impl.model.enums.MarketType;
import api.client.model.ExchangeInfoEntry;
import api.client.spot.impl.SyncSpotClient;
import api.client.spot.model.Account;
import api.client.spot.model.AssetBalance;
import api.client.spot.model.Order;
import api.client.spot.model.enums.OrderStatus;
import sanzol.aitrader.be.config.PrivateConfig;
import sanzol.aitrader.be.config.ServerApp;
import sanzol.util.Convert;
import sanzol.util.log.LogService;

public final class PositionSpotService
{
	private static final long DEFAULT_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(10);

	private static boolean isStarted = false;

	private static List<AssetBalance> lstBalances = new ArrayList<AssetBalance>();
	private static List<Order> lstOpenOrders = new ArrayList<Order>();

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

	private static Timer timer1;

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
		timer1 = new Timer("PositionService");
		timer1.schedule(task, 0, DEFAULT_PERIOD_MILLIS);

		isStarted = true;
	}

	public static void close()
	{
		if (isStarted && timer1 != null)
			timer1.cancel();

		lstBalances = new ArrayList<AssetBalance>();
		lstOpenOrders = new ArrayList<Order>();

		notifyAllLogObservers();
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

	public static String toStringOrders(String symbolName) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		StringBuilder sb = new StringBuilder();

		for (Order entry : getLstOpenOrders(symbolName))
		{
			sb.append(String.format("%-22s %-6s %-13s %14s %14s %14s\n", entry.getSymbol(), entry.getSide(), entry.getType(), entry.getPrice(), entry.getOrigQty(), entry.getStatus()));
		}

		return sb.toString();
	}

	public static String toStringAllOrders(String symbolLeft) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException
	{
		List<Order> lstAlldOrders = new ArrayList<Order>();

		try
		{
			List<ExchangeInfoEntry> lstExInfoEntries = ExchangeInfoService.getSymbolsSatrtWith(symbolLeft);
			for (ExchangeInfoEntry entry : lstExInfoEntries)
			{
				try
				{
					List<Order> lst = SyncSpotClient.getAllOrders(entry.getSymbol());
					lstAlldOrders.addAll(lst);
				}
				catch(ApiException ex)
				{
					if (!ex.getErrCode().equals("-1121"))
					{
						return ex.getMessage();
					}
				}
			}

			// Remove all no FILLED or NEW
			lstAlldOrders.removeIf((Order entry) -> entry.getStatus() != OrderStatus.FILLED && entry.getStatus() != OrderStatus.NEW);

			//Sort
			Collections.sort(lstAlldOrders, Comparator.comparing(Order::getStatus).reversed().thenComparing(Order::getUpdateTime).reversed());
		}
		catch(Exception ex)
		{
			return ex.getMessage();
		}

		StringBuilder sb = new StringBuilder();

		for (Order entry : lstAlldOrders)
		{
			sb.append(String.format("%-22s %-10s %-6s %-16s %14s %14s %14s\n", Convert.convertTime(entry.getUpdateTime()), entry.getSymbol(), entry.getSide(), entry.getType(), entry.getPrice(), entry.getOrigQty(), entry.getStatus()));
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

			sbBody.append(String.format("%-22s %-20s %14s %14s %14s %14s\n",
										entry.getAsset(),
										side,
										"",
										entry.getQuantity().toPlainString(),
										entry.getFree().toPlainString(),
										entry.getLocked().toPlainString()));

			if (includeOrders)
			{
				sbBody.append(StringUtils.repeat("-",110));
				sbBody.append("\n");
				sbBody.append(toStringOrders(entry.getAsset()));
				sbBody.append("\n\n");
			}

			sbBody.append(StringUtils.repeat("-",110));
			sbBody.append("\n");
			sbBody.append(toStringAllOrders(entry.getAsset()));
			sbBody.append("\n");
			sbBody.append(StringUtils.repeat("-",110));
			sbBody.append("\n");

		}

		StringBuilder sb  = new StringBuilder();
		sb.append(String.format("%-22s %-20s %14s %14s %14s %14s\n", "SYMBOL", "TYPE", "PRICE", "QTY", "FREE", "LOCKED"));
		sb.append(StringUtils.repeat("-", 110));
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
		ServerApp.start(MarketType.spot, (e) -> { System.out.println(e); });
		PrivateConfig.load();

		getPositions();
		//Thread.sleep(5000);
		System.out.println(toStringPositions(true));

		//System.out.println(toStringAllOrders("CELO"));
		
		System.exit(0);
	}

}
