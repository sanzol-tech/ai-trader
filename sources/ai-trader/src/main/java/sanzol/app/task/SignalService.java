package sanzol.app.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.binance.client.exception.BinanceApiException;
import com.binance.client.model.event.SymbolTickerEvent;

import sanzol.app.config.Constants;
import sanzol.app.listener.SignalListener;
import sanzol.app.model.ShockPoint;
import sanzol.app.model.Signal;
import sanzol.app.model.SymbolInfo;
import sanzol.app.service.OBookService;
import sanzol.app.service.Symbol;
import sanzol.app.util.PriceUtil;

public final class SignalService
{
	private static boolean isStarted = false;

	private static boolean onlyFavorites = true;
	private static boolean onlyBetters = true;

	private static Map<String, ShockPoint> mapShockPoints = new ConcurrentHashMap<String, ShockPoint>();
	private static List<Signal> lstShortSignals;
	private static List<Signal> lstLongSignals;

	private static String modified = "n/a";

	public static boolean isOnlyFavorites()
	{
		return onlyFavorites;
	}

	public static void setOnlyFavorites(boolean onlyFavorites)
	{
		SignalService.onlyFavorites = onlyFavorites;
	}

	public static boolean isOnlyBetters()
	{
		return onlyBetters;
	}

	public static void setOnlyBetters(boolean onlyBetters)
	{
		SignalService.onlyBetters = onlyBetters;
	}

	public static List<ShockPoint> getLstShockPoints()
	{
		return new ArrayList<ShockPoint>(mapShockPoints.values());
	}

	public static String getModified()
	{
		return modified;
	}

	// -----------------------------------------------------------------------

	public static void start()
	{
		if (!isStarted)
		{
			Timer timer1 = new Timer("calcSignals");
			timer1.schedule(new MyTask1(), TimeUnit.SECONDS.toMillis(12), TimeUnit.SECONDS.toMillis(2));

			Timer timer2 = new Timer("searchShocks");
			timer2.schedule(new MyTask2(), TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS.toMillis(20));

			isStarted = true;
		}
	}

	public static class MyTask1 extends TimerTask
	{
		@Override
		public void run()
		{
			calcSignals();
			notifyAllLogObservers();
		}
	}

	public static class MyTask2 extends TimerTask
	{
		@Override
		public void run()
		{
			searchShocks();
		}
	}

	// -----------------------------------------------------------------------

	public static void restartShocks()
	{
		mapShockPoints = new ConcurrentHashMap<String, ShockPoint>();
		lstShortSignals = new ArrayList<Signal>();
		lstLongSignals = new ArrayList<Signal>();
	}

	public static void searchShocks()
	{
		try
		{
			List<SymbolInfo> lstSymbolsInfo = Symbol.getLstSymbolsInfo(onlyFavorites, onlyBetters);

			int count = 0;
			for (SymbolInfo symbolInfo : lstSymbolsInfo)
			{
				ShockPoint shockPoint = mapShockPoints.get(symbolInfo.getSymbol().getName());
				if (shockPoint != null)
				{
					Long expirationTime = shockPoint.getExpirationTime();
					if (expirationTime != null && expirationTime + TimeUnit.MINUTES.toMillis(2) > System.currentTimeMillis())
					{
						continue;
					}
				}

				searchShocks(symbolInfo.getSymbol());

				count++;
				if (count > 10)	{
					count = 0;
					Thread.sleep(7500);
				} else {
					Thread.sleep(750);
				}
			}
		}
		catch (Exception e)
		{
			LogService.error(e);
		}
	}

	private static void searchShocks(Symbol symbol)
	{
		try
		{
			OBookService obService = OBookService.getInstance(symbol).request().calc();
			if (!obService.isSubscriptionTimeOK())
			{
				LogService.info("#$%&#$% FROM " + symbol.getNameLeft());
				return;
			}

			BigDecimal distShLg = PriceUtil.priceDistDown(obService.getShortPriceFixed(), obService.getLongPriceFixed(), true);

			if ((distShLg.doubleValue() < 1.2 || distShLg.doubleValue() > 6.0))
			{
				updateShockPoint(ShockPoint.NULL(symbol));
				LogService.info("DISCARD NEW SIGNALS FROM " + symbol.getNameLeft() + " - DISTANCE BETWEEN POINTS " + distShLg + " %");
			}
			else
			{
				updateShockPoint(new ShockPoint(symbol, obService.getShortPriceFixed(), obService.getLongPriceFixed()));
				LogService.info("CALC NEW SIGNALS FROM " + symbol.getNameLeft() + " AT " + obService.getShortPriceFixed() + " & " + obService.getLongPriceFixed());
			}
		}
		catch (BinanceApiException ex)
		{
			LogService.error("searchShocks : " + symbol.getName() + " : " +  ex.getMessage());
		}
	}

	private static void updateShockPoint(ShockPoint shockPoint)
	{
		if (mapShockPoints.containsKey(shockPoint.getSymbol().getName()))
		{
			mapShockPoints.replace(shockPoint.getSymbol().getName(), shockPoint);
		}
		else
		{
			mapShockPoints.put(shockPoint.getSymbol().getName(), shockPoint);
		}
	}

	// -----------------------------------------------------------------------

	public static List<Signal> getLstShortSignals()
	{
		if (lstShortSignals == null)
			return new ArrayList<Signal>();
		else
			return lstShortSignals;
	}

	public static List<Signal> getLstLongSignals()
	{
		if (lstLongSignals == null)
			return new ArrayList<Signal>();
		else
			return lstLongSignals;
	}

	private static void expireShocks(Symbol symbol, String reason)
	{
		mapShockPoints.get(symbol.getName()).setExpirationTime(System.currentTimeMillis());
		
		LogService.info("EXPIRE SIGNALS FROM " + symbol.getNameLeft() + " - " + reason);
	}

	private static void calcSignals()
	{
		if (mapShockPoints == null || mapShockPoints.isEmpty())
		{
			return;
		}
		List<Signal> lstShorts = new ArrayList<Signal>();
		List<Signal> lstLongs = new ArrayList<Signal>();

		try
		{
			List<String> lstSymbols = Symbol.getLstSymbolNames(onlyFavorites, onlyBetters);
			Set<String> setSymbols = new HashSet<>(lstSymbols);

			for (ShockPoint entry : mapShockPoints.values())
			{
				if (entry.getExpirationTime() < System.currentTimeMillis() || 
				   (entry.getShShock().doubleValue() == 0 && entry.getLgShock().doubleValue() == 0))
				{
					continue;
				}

				SymbolTickerEvent symbolTickerEvent = PriceService.getSymbolTickerEvent(entry.getSymbol());
				if (symbolTickerEvent == null)
				{
					continue;
				}

				BigDecimal lastPrice = symbolTickerEvent.getLastPrice();
				BigDecimal usdVolume = symbolTickerEvent.getTotalTradedQuoteAssetVolume();
				BigDecimal changePercent = symbolTickerEvent.getPriceChangePercent();

				BigDecimal distShort = PriceUtil.priceDistUp(lastPrice, entry.getShShock(), true);
				BigDecimal distLong = PriceUtil.priceDistDown(lastPrice, entry.getLgShock(), true);

				if (distShort.doubleValue() <= -0.03)
				{
					expireShocks(entry.getSymbol(), "REACHED THE SHORT POINT");
					continue;
				}
				if (distLong.doubleValue() <= -0.03)
				{
					expireShocks(entry.getSymbol(), "REACHED THE LONG POINT");
					continue;
				}

				if (!setSymbols.contains(entry.getSymbol().getName()))
				{
					continue;
				}

				BigDecimal high = symbolTickerEvent.getHigh();
				BigDecimal low = symbolTickerEvent.getLow();
				BigDecimal avgPrice = symbolTickerEvent.getWeightedAvgPrice();
				BigDecimal avgHigh = (avgPrice.add(high)).divide(BigDecimal.valueOf(2), entry.getSymbol().getTickSize(), RoundingMode.HALF_UP);
				BigDecimal avgLow = (avgPrice.add(low)).divide(BigDecimal.valueOf(2), entry.getSymbol().getTickSize(), RoundingMode.HALF_UP);
				boolean isBestShort = (lastPrice.doubleValue() > avgHigh.doubleValue());
				boolean isBestLong = (lastPrice.doubleValue() < avgLow.doubleValue());
				
				Signal shortSignal = new Signal("SHORT", entry.getSymbol(), lastPrice, entry.getShShock(), entry.getShortTProfit(), distShort);
				shortSignal.setChange24h(changePercent);
				shortSignal.setVolume(usdVolume);
				shortSignal.setBestSide(isBestShort ? "SHORT" : isBestLong ? "LONG" : "");
				lstShorts.add(shortSignal);

				Signal longSignal = new Signal("LONG", entry.getSymbol(), lastPrice, entry.getLgShock(), entry.getLongTProfit(), distLong);
				longSignal.setChange24h(changePercent);
				longSignal.setVolume(usdVolume);
				longSignal.setBestSide(isBestShort ? "SHORT" : isBestLong ? "LONG" : "");
				lstLongs.add(longSignal);
			}
		}
		catch (Exception e)
		{
			LogService.error(e);
		}

		Comparator<Signal> comparator = Comparator.comparing(Signal::getDistance);
		Collections.sort(lstShorts, comparator);
		Collections.sort(lstLongs, comparator);

		lstShortSignals = lstShorts;
		lstLongSignals = lstLongs;
	}	

	// -----------------------------------------------------------------------

	public static String toStringShocks()
	{
		if (mapShockPoints == null || mapShockPoints.isEmpty())
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (ShockPoint entry : mapShockPoints.values())
		{
			sb.append(entry.toString());
		}
		return sb.toString();
	}

	public static boolean saveShocks()
	{
		try
		{
			if (mapShockPoints != null & !mapShockPoints.isEmpty())
			{
				String text = "";
				for (ShockPoint entry : mapShockPoints.values())
				{
					text += entry.toString() + "\n";
				}

				Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, Constants.SHOCKPOINTS_FILENAME);
				Files.write(path, text.getBytes(StandardCharsets.UTF_8));

				return true;
			}
		}
		catch (Exception e)
		{
			LogService.error(e);
		}

		return false;
	}

	public static String toStringShorts()
	{
		if (lstShortSignals == null || lstShortSignals.isEmpty())
		{
			return "";
		}

		String text = String.format("\n%-8s %12s %9s %17s %10s\n", "SYMBOL", "TARGET", "DIST", "24h %", "VOLUME");
		text += StringUtils.repeat("-", 60) + "\n";

		for (Signal entry : lstShortSignals)
		{
			text += entry.toString();
		}

		return text;
	}

	public static String toStringLongs()
	{
		if (lstLongSignals == null || lstLongSignals.isEmpty())
		{
			return "";
		}

		String text = String.format("\n%-8s %12s %9s %17s %10s\n", "SYMBOL", "TARGET", "DIST", "24h %", "VOLUME");
		text += StringUtils.repeat("-", 60) + "\n";

		for (Signal entry : lstLongSignals)
		{
			text += entry.toString();
		}

		return text;
	}

	// ------------------------------------------------------------------------	

	private static List<SignalListener> observers = new ArrayList<SignalListener>();

	public static void attachRefreshObserver(SignalListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(SignalListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers()
	{
		for (SignalListener observer : observers)
		{
			observer.onSignalUpdate();
		}
	}

	// -----------------------------------------------------------------------

	public static void main(String[] args) throws Exception
	{
		PriceService.start();

		Thread.sleep(5000);

		searchShocks();
		System.out.println(""); 
		System.out.println(toStringShocks());
		saveShocks();

		calcSignals();
		System.out.println("");
		System.out.println(toStringShorts());
		System.out.println(toStringLongs());

		System.out.println("!");
	}

}
