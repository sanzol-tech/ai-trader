package sanzol.app.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

import api.client.futures.async.PriceService;
import api.client.futures.async.DepthClient.DepthMode;
import api.client.futures.async.model.SymbolTickerEvent;
import sanzol.app.config.Constants;
import sanzol.app.model.ShockPoint;
import sanzol.app.model.Signal;
import sanzol.app.model.SymbolInfo;
import sanzol.app.util.PriceUtil;

public final class SignalService
{
	private static final double MIN_RATIO = 1.8;
	
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
			LogService.info("SignalService - Start");

			Timer timer1 = new Timer("calcSignals");
			timer1.schedule(new MyTask1(), TimeUnit.SECONDS.toMillis(240), TimeUnit.SECONDS.toMillis(2));

			Timer timer2 = new Timer("searchShocks");
			timer2.schedule(new MyTask2(), TimeUnit.SECONDS.toMillis(200), TimeUnit.SECONDS.toMillis(60));

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

	public static void restartShocks() throws KeyManagementException, NoSuchAlgorithmException, InterruptedException, IOException
	{
		mapShockPoints = new ConcurrentHashMap<String, ShockPoint>();
		lstShortSignals = new ArrayList<Signal>();
		lstLongSignals = new ArrayList<Signal>();

		DepthCache.clean();
		DepthCache.start();
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

	private static void searchShocks(Symbol symbol) throws KeyManagementException, NoSuchAlgorithmException
	{
		try
		{
			DepthService depth = DepthService.getInstance(symbol).request(DepthMode.both, TimeUnit.SECONDS.toMillis(60));

			if (!depth.verifyConnectTime(TimeUnit.SECONDS.toMillis(180)))
			{
				LogService.info("SKIP " + symbol.getNameLeft() + " - WAITING FOR MORE DATA");
				return;
			}

			depth.calc();

			BigDecimal distShLg = PriceUtil.priceDistDown(depth.getAskFixedPoint1(), depth.getBidFixedPoint1(), true);

			if ((distShLg.doubleValue() < 0.8 || distShLg.doubleValue() > 10.0))
			{
				updateShockPoint(ShockPoint.NULL(symbol));
				LogService.info("DISCARD SHOCKPOINTS - " + symbol.getNameLeft() + " - DISTANCE BETWEEN POINTS " + distShLg + " %");
			}
			else
			{
				updateShockPoint(new ShockPoint(symbol, depth.getAskFixedPoint1(), depth.getBidFixedPoint1(), depth.getAskFixedPoint2(), depth.getBidFixedPoint2()));
				LogService.info("CALC SHOCKPOINTS - " + symbol.getNameLeft() + " AT " + depth.getAskFixedPoint1() + " & " + depth.getBidFixedPoint1());
			}
		}
		catch (Exception ex)
		{
			LogService.error("searchShocks : " + symbol.getNameLeft() + " : " +  ex.getMessage());
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
				   (entry.getShortPrice().doubleValue() == 0 && entry.getLongPrice().doubleValue() == 0))
				{
					continue;
				}

				SymbolTickerEvent symbolTickerEvent = PriceService.getSymbolTickerEvent(entry.getSymbol());
				if (symbolTickerEvent == null)
				{
					continue;
				}

				BigDecimal markPrice = symbolTickerEvent.getLastPrice();
				BigDecimal volume = symbolTickerEvent.getQuoteVolume();
				BigDecimal change24h = symbolTickerEvent.getPriceChangePercent();

				BigDecimal distShort = PriceUtil.priceDistUp(markPrice, entry.getShortPrice(), true);
				BigDecimal distLong = PriceUtil.priceDistDown(markPrice, entry.getLongPrice(), true);

				if (distShort.doubleValue() <= -0.01)
				{
					expireShocks(entry.getSymbol(), "REACHED THE SHORT POINT");
					continue;
				}
				if (distLong.doubleValue() <= -0.01)
				{
					expireShocks(entry.getSymbol(), "REACHED THE LONG POINT");
					continue;
				}

				if (!setSymbols.contains(entry.getSymbol().getName()))
				{
					continue;
				}

				BigDecimal high = symbolTickerEvent.getHighPrice();
				BigDecimal low = symbolTickerEvent.getLowPrice();
				BigDecimal avgPrice = symbolTickerEvent.getWeightedAvgPrice();
				BigDecimal avgHigh = (avgPrice.add(high)).divide(BigDecimal.valueOf(2), entry.getSymbol().getTickSize(), RoundingMode.HALF_UP);
				BigDecimal avgLow = (avgPrice.add(low)).divide(BigDecimal.valueOf(2), entry.getSymbol().getTickSize(), RoundingMode.HALF_UP);
				boolean isBestShort = (markPrice.doubleValue() > avgHigh.doubleValue());
				boolean isBestLong = (markPrice.doubleValue() < avgLow.doubleValue());
				String bestSide = isBestShort ? "WAVG HIGH" : isBestLong ? "WAVG LOW" : "";

				if (entry.getShortRatio().doubleValue() > MIN_RATIO)
				{
					Signal shortSignal = new Signal("SHORT", entry.getSymbol(), markPrice, change24h, volume, bestSide, entry.getShortPrice(), distShort, entry.getShortTProfit(), entry.getShortSLoss(), entry.getShortRatio());
					lstShorts.add(shortSignal);
				}
				//else
				//{
				//	LogService.info("DISCARD SHORT - " + entry.getSymbol().getNameLeft() + " - LOW RATIO 1:" + entry.getShortRatio().doubleValue());
				//}

				if (entry.getLongRatio().doubleValue() > MIN_RATIO)
				{
					Signal longSignal = new Signal("LONG", entry.getSymbol(), markPrice, change24h, volume, bestSide, entry.getLongPrice(), distLong, entry.getLongTProfit(), entry.getLongTSLoss(), entry.getLongRatio());
					lstLongs.add(longSignal);
				}
				//else
				//{
				//	LogService.info("DISCARD LONG - " + entry.getSymbol().getNameLeft() + " - LOW RATIO 1:" + entry.getLongRatio().doubleValue());
				//}

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
