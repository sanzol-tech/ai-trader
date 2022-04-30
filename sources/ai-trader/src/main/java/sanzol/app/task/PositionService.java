package sanzol.app.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import sanzol.app.config.Config;
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
													.thenComparing(Order::getUpdateTime);
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

	public static Map<String, GOrder> calc(Symbol coin, String posSide, BigDecimal posPrice, BigDecimal posQty, BigDecimal shootPrice, BigDecimal shootQty) throws Exception 
	{
		Map<String, GOrder> mapPosition = new HashMap<String, GOrder>();

		// --- POSITION -------------------------------------------------------
		PositionRisk positionRisk = getPositionRisk(coin.getName());
		boolean positionExists = (positionRisk != null && positionRisk.getPositionAmt().compareTo(BigDecimal.ZERO) != 0); 
		if (positionExists)
		{
			posSide = positionRisk.getPositionAmt().doubleValue() < 0 ? "SELL" : "BUY";
			posPrice = positionRisk.getEntryPrice();
			posQty = positionRisk.getPositionAmt().abs();
		}
		mapPosition.put("POS", new GOrder(posPrice, posQty));

		// --- SHOOT ----------------------------------------------------------
		mapPosition.put("SHOOT", new GOrder(shootPrice, shootQty));

		// --- RESULT ---------------------------------------------------------
		BigDecimal resultQty = posQty.add(shootQty);
		BigDecimal resultUSD = posPrice.multiply(posQty).add(shootPrice.multiply(shootQty));
		BigDecimal resultPrice = resultUSD.divide(resultQty, RoundingMode.HALF_UP);
		mapPosition.put("RESULT", new GOrder(resultPrice, resultQty, resultUSD));

		// --- OTHERS ---------------------------------------------------------
		BigDecimal othersPrice = null;
		BigDecimal othersQty = null;
		BigDecimal othersUSD = null;
		if (positionExists)
		{
			othersQty = resultQty;
			othersUSD = resultUSD;
			for (Order entry : getLstOpenOrders(coin.getName()))
			{
				if ("LIMIT".equals(entry.getType()) && posSide.equals(entry.getSide()))
				{
					othersQty = othersQty.add(entry.getOrigQty());
					othersUSD = entry.getOrigQty().multiply(entry.getPrice());
				}
			}
			othersPrice = othersUSD.divide(othersQty, RoundingMode.HALF_UP);
			mapPosition.put("OTHERS", new GOrder(othersPrice, othersQty, othersUSD));
		}

		// --- STOP LOSS ------------------------------------------------------
		BigDecimal slPrice;
		BigDecimal slAmt;
		if (positionExists)	{
			slPrice = othersPrice.multiply(BigDecimal.valueOf(1 - Config.getStoploss_increment()));
			slAmt = othersQty;
		} else {
			slPrice = resultPrice.multiply(BigDecimal.valueOf(1 - Config.getStoploss_increment()));
			slAmt = resultQty;
		}
		mapPosition.put("SL", new GOrder(slPrice, slAmt));

		// --- TAKE PROFIT ----------------------------------------------------
		BigDecimal tpAmt = posQty;
		BigDecimal tpPrice = resultPrice.multiply(BigDecimal.valueOf(1 + Config.getTakeprofit()));
		mapPosition.put("TP", new GOrder(tpPrice, tpAmt));

		// --- DISTANCES ------------------------------------------------------
		if ("BUY".equals(posSide))
		{
			BigDecimal shootDist = BigDecimal.ONE.subtract(posPrice.divide(shootPrice, RoundingMode.HALF_UP));
			mapPosition.get("SHOOT").setDist(shootDist);
			BigDecimal resultDist = BigDecimal.ONE.subtract(resultPrice.divide(shootPrice, RoundingMode.HALF_UP));
			mapPosition.get("RESULT").setDist(resultDist);
			BigDecimal tptDist = BigDecimal.ONE.subtract(tpPrice.divide(shootPrice, RoundingMode.HALF_UP));
			mapPosition.get("TP").setDist(tptDist);
		}
		else if ("SELL".equals(posSide))
		{
			BigDecimal shootDist = posPrice.divide(shootPrice, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
			mapPosition.get("SHOOT").setDist(shootDist);
			BigDecimal resultDist = resultPrice.divide(shootPrice, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
			mapPosition.get("RESULT").setDist(resultDist);
			BigDecimal tptDist = tpPrice.divide(shootPrice, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
			mapPosition.get("TP").setDist(tptDist);
		}		

		return mapPosition;
	}	

	// ------------------------------------------------------------------------

	public static String toStringOrders(String symbolName)
	{
		StringBuilder sb = new StringBuilder();

		for (Order entry : getLstOpenOrders(symbolName))
		{
			sb.append(String.format("%-22s %-6s %-13s %10s %14s %12s %14s\n", convertTime(entry.getUpdateTime()), entry.getSide(), entry.getType(), entry.getOrigQty(), entry.getPrice(), entry.getStopPrice(), entry.getReduceOnly() ? "R.Only" : ""));
		}

		return sb.toString();
	}

	public static String toStringPositions(boolean includeOrders)
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

				String side = entry.getPositionAmt().doubleValue() > 0 ? "LONG" : "SHORT";

				sbBody.append(String.format("%-22s %-20s %10s %14s %12s %14s\n",
											entry.getSymbol(),
											side + " " + entry.getMarginType() + " " + entry.getLeverage(),
											entry.getPositionAmt(),
											symbol.priceToStr(entry.getMarkPrice()),
											symbol.priceToStr(entry.getEntryPrice()), 
											Convert.usdToStr(entry.getUnrealizedProfit().doubleValue())));

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
	}

}
