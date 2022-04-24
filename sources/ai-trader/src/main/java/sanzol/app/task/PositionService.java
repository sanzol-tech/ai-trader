package sanzol.app.task;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.trade.Order;
import com.binance.client.model.trade.PositionRisk;

import sanzol.app.config.Application;
import sanzol.app.config.PrivateConfig;
import sanzol.app.model.GOrder;
import sanzol.app.model.Symbol;
import sanzol.app.util.Convert;

public final class PositionService
{
	private static final long DEFAULT_PERIOD_MILLIS = 5000;

	private static boolean isStarted = false;

	private static Map<String, BigDecimal> mapAmountsPrev = new HashMap<String, BigDecimal>();
	private static Set<String> setAmountChange = new HashSet<String>();
	private static List<PositionRisk> lstPositionRisk;
	private static List<Order> lstOpenOrders;

	private static String errorMessage;

	public static List<PositionRisk> getLstPositionRisk()
	{
		return lstPositionRisk;
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


	public static String getErrorMessage()
	{
		return errorMessage;
	}


	public static boolean wasChange()
	{
		if (setAmountChange != null && !setAmountChange.isEmpty())
		{
			setAmountChange.clear();
			return true;
		}
		return false;
	}

	
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


	private static void getPositions()
	{
		try
		{
			RequestOptions options = new RequestOptions();
			SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

			lstPositionRisk = syncRequestClient.getPositionRisk();

			if (lstPositionRisk == null || lstPositionRisk.isEmpty())
			{
				lstOpenOrders.clear();
				mapAmountsPrev.clear();
				setAmountChange.clear();
				return;
			}

			verifyAmountChange();

			lstOpenOrders = syncRequestClient.getOpenOrders(null);

			Comparator<Order> orderComparator = Comparator
													.comparing(Order::getSymbol)
													.thenComparing(Order::getPrice)
													.thenComparing(Order::getStopPrice);
			Collections.sort(lstOpenOrders, orderComparator);
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
			System.err.println("BalanceService.runGetBalances: " + e.getMessage());
		}
	}

	
	private static void verifyAmountChange()
	{
		if (lstPositionRisk != null && lstPositionRisk.isEmpty())
		{
			for (PositionRisk entry : lstPositionRisk)
			{
				if (!mapAmountsPrev.get(entry.getSymbol()).equals(entry.getPositionAmt()))
				{
					mapAmountsPrev.put(entry.getSymbol(), entry.getPositionAmt());
					setAmountChange.add(entry.getSymbol());
				}
			}
		}
	}	

	// ------------------------------------------------------------------------

	public static double calcFinalPriceAvg(Symbol symbol, List<GOrder> lstGrid)
	{
		double sumCoins = 0;
		double sumUsd = 0;
		for (GOrder entry : lstGrid)
		{
			sumCoins += entry.getCoins();
			sumUsd += entry.getUsd();
		}
		return sumUsd / sumCoins;
	}

/* TODO
	public static String recalcSL(String symbolName, BigDecimal price, BigDecimal coins)
	{
		List<GOrder> lstGrid = new ArrayList<GOrder>();
		
		lstGrid.add(new GOrder(price.doubleValue(), coins.doubleValue(), price.doubleValue() * coins.doubleValue()));
	}
*/

	public static String recalcSL(String symbolName, BigDecimal price, BigDecimal coins)
	{
		String side = null;
		double sumCoins = 0;
		double sumUsd = 0;

		if (lstPositionRisk != null && !lstPositionRisk.isEmpty())
		{
			for (PositionRisk entry : lstPositionRisk)
			{
				if (entry.getSymbol().equals(symbolName))
				{
					side = entry.getPositionAmt().doubleValue() < 0 ? "SELL" : "BUY";
					sumCoins = Math.abs(entry.getPositionAmt().doubleValue());
					sumUsd = Math.abs(entry.getPositionAmt().doubleValue()) * entry.getEntryPrice().doubleValue();
				}
			}
		}

		if (side == null)
			return null;

		// -------------------------------------------------------------------
		sumCoins += coins.doubleValue();
		sumUsd += coins.doubleValue() * price.doubleValue();

		// -------------------------------------------------------------------
		for (Order entry : getLstOpenOrders())
		{
			if ("LIMIT".equals(entry.getType()) && side.equals(entry.getSide()))
			{
				sumCoins += entry.getOrigQty().doubleValue();
				sumUsd += entry.getOrigQty().doubleValue() * entry.getPrice().doubleValue();
			}
		}
		double slPrice0 = sumUsd / sumCoins;
		
		return "" + slPrice0;
	}
	
	// ------------------------------------------------------------------------

	public static String toStringOrders(String symbolName)
	{
		StringBuilder sb = new StringBuilder();

		for (Order entry : getLstOpenOrders())
		{
			sb.append(String.format("%-22s %-6s %-13s %10s %14s %12s %14s\n", convertTime(entry.getUpdateTime()), entry.getSide(), entry.getType(), entry.getOrigQty(), entry.getPrice(), entry.getStopPrice(), entry.getReduceOnly() ? "R.Only" : ""));
		}

		return sb.toString();
	}

	public static String toStringPositions(boolean includeOrders)
	{
		if (lstPositionRisk == null || lstPositionRisk.isEmpty())
		{
			return "NO RECORDS";
		}

		StringBuilder sb = new StringBuilder();

		sb.append(String.format("%-22s %-20s %10s %14s %12s %14s\n", "SYMBOL", "TYPE", "AMOUNT", "PRICE", "AVG PRICE", "PNL"));
		sb.append(StringUtils.repeat("-", 97));
		sb.append("\n");

		for (PositionRisk entry : lstPositionRisk)
		{
			if (entry.getPositionAmt().doubleValue() != 0.0)
			{
				Symbol symbol = Symbol.getInstance(entry.getSymbol());

				String side = entry.getPositionAmt().doubleValue() > 0 ? "LONG" : "SHORT";

				sb.append(String.format("%-22s %-20s %10s %14s %12s %14s\n",
										entry.getSymbol(),
										side + " " + entry.getMarginType() + " " + entry.getLeverage(),
										entry.getPositionAmt(),
										symbol.priceToStr(entry.getMarkPrice()),
										symbol.priceToStr(entry.getEntryPrice()), 
										Convert.usdToStr(entry.getUnrealizedProfit().doubleValue())));

				if (includeOrders)
				{
					sb.append(StringUtils.repeat("-",97));
					sb.append("\n");
					sb.append(toStringOrders(entry.getSymbol()));
					sb.append(StringUtils.repeat("-",97));
					sb.append("\n");
				}
			}
		}

		return sb.toString();
	}

	public static String convertTime(long time)
	{
		Date date = new Date(time);
		Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static void main(String[] args) throws InterruptedException
	{
		Application.initialize();

		Thread.sleep(2000);

		System.out.println(toStringPositions(true));

		recalcSL("BTCUSDT", BigDecimal.valueOf(40000), BigDecimal.valueOf(0.018));

	}

}
