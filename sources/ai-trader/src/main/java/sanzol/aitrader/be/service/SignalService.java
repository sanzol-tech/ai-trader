package sanzol.aitrader.be.service;

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

import api.client.impl.config.ApiConfig;
import api.client.model.event.SymbolTickerEvent;
import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.enums.DepthMode;
import sanzol.aitrader.be.model.SignalPoint;
import sanzol.aitrader.be.model.Symbol;
import sanzol.aitrader.be.model.Signal;
import sanzol.aitrader.be.model.SymbolInfo;
import sanzol.util.log.LogService;
import sanzol.util.price.PriceUtil;

public final class SignalService
{
	private static final double MIN_SHLG_DIST = 0.8;
	private static final double MAX_SHLG_DIST = 12.0;
	private static final double MIN_RATIO = 1.8;

	private static boolean isStarted = false;

	private static boolean onlyFavorites = true;
	private static boolean onlyBetters = true;

	private static Map<String, SignalPoint> mapSignalPoints = new ConcurrentHashMap<String, SignalPoint>();
	private static List<Signal> lstShortSignals;
	private static List<Signal> lstLongSignals;

	private SignalService()
	{
		// Hide
	}

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

	public static List<SignalPoint> getLstSignalPoints()
	{
		return new ArrayList<SignalPoint>(mapSignalPoints.values());
	}

	// -----------------------------------------------------------------------

	private static Timer timer1;
	private static Timer timer2;

	public static boolean start()
	{
		try
		{
			if (!isStarted)
			{
				startTask1();
				startTask2();

				isStarted = true;
			}

			return true;
		}
		catch (Exception e)
		{
			LogService.error(e);
			return false;
		}
	}

	public static void startTask1()
	{
		LogService.info("SignalService - calcSignals - Start");

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				calcSignals();
				notifyAllLogObservers();
			}
		};
		timer1 = new Timer("calcSignals");
		timer1.schedule(task, TimeUnit.SECONDS.toMillis(240), TimeUnit.SECONDS.toMillis(2));
	}

	public static void startTask2()
	{
		LogService.info("SignalService - searchPoints - Start");

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				searchPoints();
				notifyAllLogObservers();
			}
		};
		timer2 = new Timer("searchPoints");
		timer2.schedule(task, TimeUnit.SECONDS.toMillis(200), TimeUnit.SECONDS.toMillis(60));
	}

	public static void close()
	{
		if (isStarted)
		{
			if (timer1 != null)
				timer1.cancel();

			if (timer2 != null)
				timer2.cancel();

			mapSignalPoints = new ConcurrentHashMap<String, SignalPoint>();
			lstShortSignals = new ArrayList<Signal>();
			lstLongSignals = new ArrayList<Signal>();

			isStarted = false;

			notifyAllLogObservers();
		}
	}

	// -----------------------------------------------------------------------

	public static void searchPoints()
	{
		try
		{
			List<SymbolInfo> lstSymbolsInfo = PriceService.getLstSymbolsInfo(onlyFavorites, onlyBetters);

			int count = 0;
			for (SymbolInfo symbolInfo : lstSymbolsInfo)
			{
				SignalPoint signalPoint = mapSignalPoints.get(symbolInfo.getSymbol().getPair());
				if (signalPoint != null)
				{
					Long expirationTime = signalPoint.getExpirationTime();
					if (expirationTime != null && expirationTime + TimeUnit.MINUTES.toMillis(2) > System.currentTimeMillis())
					{
						continue;
					}
				}

				searchPoints(symbolInfo.getSymbol());

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

	private static void searchPoints(Symbol symbol) throws KeyManagementException, NoSuchAlgorithmException
	{
		try
		{
			OrderBookService depth = OrderBookService.getInstance(symbol).request(DepthMode.both, TimeUnit.SECONDS.toMillis(60));

			if (!depth.verifyConnectTime(TimeUnit.SECONDS.toMillis(180)))
			{
				LogService.info("SKIP " + symbol.getNameLeft() + " - WAITING FOR MORE DATA");
				return;
			}

			depth.calc();

			BigDecimal distShLg = PriceUtil.priceDistDown(depth.getAskFixedPoint1(), depth.getBidFixedPoint1(), true);

			if ((distShLg.doubleValue() < MIN_SHLG_DIST || distShLg.doubleValue() > MAX_SHLG_DIST))
			{
				updatePoint(SignalPoint.NULL(symbol));
				LogService.info("DISCARD SIGNALPOINTS - " + symbol.getNameLeft() + " - DISTANCE BETWEEN POINTS " + distShLg + " %");
			}
			else
			{
				updatePoint(new SignalPoint(symbol, depth.getAskFixedPoint1(), depth.getBidFixedPoint1(), depth.getAskFixedPoint2(), depth.getBidFixedPoint2()));
				LogService.info("CALC SIGNALPOINTS - " + symbol.getNameLeft() + " AT " + depth.getAskFixedPoint1() + " & " + depth.getBidFixedPoint1());
			}
		}
		catch (Exception ex)
		{
			LogService.error("searchShocks : " + symbol.getNameLeft() + " : " +  ex.getMessage());
		}
	}

	private static void updatePoint(SignalPoint signalPoint)
	{
		if (mapSignalPoints.containsKey(signalPoint.getSymbol().getPair()))
		{
			mapSignalPoints.replace(signalPoint.getSymbol().getPair(), signalPoint);
		}
		else
		{
			mapSignalPoints.put(signalPoint.getSymbol().getPair(), signalPoint);
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
		mapSignalPoints.get(symbol.getPair()).setExpirationTime(System.currentTimeMillis());

		LogService.info("EXPIRE SIGNALS FROM " + symbol.getNameLeft() + " - " + reason);
	}

	private static void calcSignals()
	{
		if (mapSignalPoints == null || mapSignalPoints.isEmpty())
		{
			return;
		}
		List<Signal> lstShorts = new ArrayList<Signal>();
		List<Signal> lstLongs = new ArrayList<Signal>();

		try
		{
			List<String> lstSymbols = PriceService.getLstSymbolNames(onlyFavorites, onlyBetters);
			Set<String> setSymbols = new HashSet<>(lstSymbols);

			for (SignalPoint entry : mapSignalPoints.values())
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

				if (!setSymbols.contains(entry.getSymbol().getPair()))
				{
					continue;
				}

				BigDecimal high = symbolTickerEvent.getHighPrice();
				BigDecimal low = symbolTickerEvent.getLowPrice();
				BigDecimal avgPrice = symbolTickerEvent.getWeightedAvgPrice();
				BigDecimal avgHigh = (avgPrice.add(high)).divide(BigDecimal.valueOf(2), entry.getSymbol().getPricePrecision(), RoundingMode.HALF_UP);
				BigDecimal avgLow = (avgPrice.add(low)).divide(BigDecimal.valueOf(2), entry.getSymbol().getPricePrecision(), RoundingMode.HALF_UP);
				boolean isBestShort = (markPrice.doubleValue() > avgHigh.doubleValue());
				boolean isBestLong = (markPrice.doubleValue() < avgLow.doubleValue());
				String bestSide = isBestShort ? "24H HIGH" : isBestLong ? "24H LOW" : "";

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
		if (mapSignalPoints == null || mapSignalPoints.isEmpty())
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (SignalPoint entry : mapSignalPoints.values())
		{
			sb.append(entry.toString());
		}
		return sb.toString();
	}

	public static boolean saveShocks()
	{
		try
		{
			if (mapSignalPoints != null & !mapSignalPoints.isEmpty())
			{
				String text = "";
				for (SignalPoint entry : mapSignalPoints.values())
				{
					text += entry.toString() + "\n";
				}

				Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString(), Constants.SHOCKPOINTS_FILENAME);
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

		String text = "SHORTS\n";
		text += StringUtils.repeat("-", 70) + "\n";

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

		String text = "LONGS\n";
		text += StringUtils.repeat("-", 70) + "\n";

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

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, InterruptedException
	{
		PriceService.start();

		Thread.sleep(5000);

		searchPoints();
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
