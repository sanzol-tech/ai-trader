package aitrader.core.service.signals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import aitrader.core.config.CoreConfig;
import aitrader.core.config.CoreLog;
import aitrader.core.model.Signal;
import aitrader.core.model.Symbol;
import aitrader.core.model.SymbolInfo;
import aitrader.core.model.enums.DepthMode;
import aitrader.core.service.market.DepthSummary;
import aitrader.core.service.symbol.SymbolInfoService;
import aitrader.util.price.PriceUtil;
import binance.futures.enums.OrderSide;
import technicals.indicators.depth.DephMergedPoints;

public final class SignalGenerator
{
	private static long TIME_TO_LIVE = TimeUnit.HOURS.toMillis(4);

	private static Map<String, Signal> mapShortSignals = new ConcurrentHashMap<String, Signal>();
	private static Map<String, Signal> mapLongSignals = new ConcurrentHashMap<String, Signal>();

	private SignalGenerator()
	{
		// Hide
	}

	public static Map<String, Signal> getMapShortSignals()
	{
		return mapShortSignals;
	}

	public static Map<String, Signal> getMapLongSignals()
	{
		return mapLongSignals;
	}

	public static void clean()
	{
		mapShortSignals = new ConcurrentHashMap<String, Signal>();
		mapLongSignals = new ConcurrentHashMap<String, Signal>();
	}

	public static void generate()
	{
		try
		{
			List<SymbolInfo> lstSymbolsInfo = SymbolInfoService.getLstSymbolsInfo(CoreConfig.getFavoriteSymbols(), CoreConfig.getBetterSymbols(), false);

			// Search signals
			for (SymbolInfo symbolInfo : lstSymbolsInfo)
			{
				if (mapShortSignals.containsKey(symbolInfo.getSymbol().getPair()) || mapLongSignals.containsKey(symbolInfo.getSymbol().getPair()))
				{
					continue;
				}

				generateOrderBookSignals(symbolInfo);
			}

			// Purge signals
			mapShortSignals.values().removeIf(entry -> entry.getCreatedAt() + TIME_TO_LIVE < System.currentTimeMillis());
			mapLongSignals.values().removeIf(entry -> entry.getCreatedAt() + TIME_TO_LIVE < System.currentTimeMillis());

		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}
	}

 	private static void generateOrderBookSignals(SymbolInfo symbolInfo) throws Exception
	{
		Symbol symbol = symbolInfo.getSymbol();

		DepthSummary depthSummary = DepthSummary
				.getInstance(symbol, DepthMode.both, TimeUnit.SECONDS.toMillis(60))
				.calcDepthBlockPoints(CoreConfig.getBlocksToAnalizeBB())
				.calcDepthWeightedPoints(CoreConfig.getDist1ToAnalizeWA(), CoreConfig.getDist2ToAnalizeWA(), CoreConfig.getDist3ToAnalizeWA())
				.calcDephMergedPoints(CoreConfig.getMergeMode());

		DephMergedPoints dephMergedPoints = depthSummary.getDephMergedPoints();
		
 		if (!depthSummary.verifyConnectTime(TimeUnit.SECONDS.toMillis(180)))
 		{
			CoreLog.info("SKIP " + symbolInfo.getSymbol().getPair() + " - WAITING FOR MORE DATA");
			return;
 		}

		// Short
		BigDecimal shortPrice = dephMergedPoints.getR1().setScale(symbol.getPricePrecision(), RoundingMode.HALF_UP);
		BigDecimal shortSLoss = symbol.addTicksRound(dephMergedPoints.getR2(), 50);
		BigDecimal shortTProfit = symbol.addTicksRound(dephMergedPoints.getS1(), 50);

		// Long
		BigDecimal longPrice = dephMergedPoints.getS1().setScale(symbol.getPricePrecision(), RoundingMode.HALF_UP);
		BigDecimal longSLoss = symbol.subTicksRound(dephMergedPoints.getS2(), 50);
		BigDecimal longTProfit = symbol.subTicksRound(dephMergedPoints.getR1(), 50);

		if (isValidForShort(symbolInfo, shortPrice, shortSLoss, shortTProfit))
			addShort(symbolInfo, shortPrice, shortSLoss, shortTProfit);

		if (isValidForLong(symbolInfo, longPrice, longSLoss, longTProfit))
			addLong(symbolInfo, longPrice, longSLoss, longTProfit);		
	}

	private static boolean isValidForShort(SymbolInfo symbolInfo, BigDecimal shortPrice, BigDecimal shortSLoss, BigDecimal shortTProfit)
	{
		final double minTpDist = CoreConfig.getMinShortLongDist();
		final double maxTpDist = CoreConfig.getMaxShortLongDist();

		BigDecimal slDist = PriceUtil.priceDistUp(shortPrice, shortSLoss, true);
		BigDecimal tpDist = PriceUtil.priceDistDown(shortPrice, shortTProfit, true);

		return (slDist.doubleValue() < tpDist.doubleValue()) && ((tpDist.doubleValue() >= minTpDist && tpDist.doubleValue() <= maxTpDist));
	}

	private static boolean isValidForLong(SymbolInfo symbolInfo, BigDecimal longPrice, BigDecimal longSLoss, BigDecimal longTProfit)
	{
		final double minTpDist = CoreConfig.getMinShortLongDist();
		final double maxTpDist = CoreConfig.getMaxShortLongDist();

		BigDecimal slDist = PriceUtil.priceDistDown(longPrice, longSLoss, true);
		BigDecimal tpDist = PriceUtil.priceDistUp(longPrice, longTProfit, true);
		
		return (slDist.doubleValue() < tpDist.doubleValue()) && ((tpDist.doubleValue() >= minTpDist && tpDist.doubleValue() <= maxTpDist));
	}

	public static void addShort(SymbolInfo symbolInfo, BigDecimal shortPrice, BigDecimal shortSLoss, BigDecimal shortTProfit)
	{
		String pair = symbolInfo.getSymbol().getPair();
		Signal signal = new Signal(pair, OrderSide.SELL.name(), shortPrice, shortSLoss, shortTProfit);

		if (mapShortSignals.containsKey(pair))
			mapShortSignals.replace(pair, signal);
		else
			mapShortSignals.put(pair, signal);
		
		CoreLog.info("NEW SHORT SIGNAL - " + pair + " AT " + shortPrice);
	}

	public static void addLong(SymbolInfo symbolInfo, BigDecimal longPrice, BigDecimal longSLoss, BigDecimal longTProfit)
	{
		String pair = symbolInfo.getSymbol().getPair();
		Signal signal = new Signal(pair, OrderSide.BUY.name(), longPrice, longSLoss, longTProfit);

		if (mapLongSignals.containsKey(pair))
			mapLongSignals.replace(pair, signal);
		else
			mapLongSignals.put(pair, signal);
		
		CoreLog.info("NEW LONG SIGNAL - " + pair + " AT " + longPrice);
	}

	public static void removeSignals(String pair)
	{
		if (mapShortSignals.containsKey(pair))
			mapShortSignals.remove(pair);

		if (mapLongSignals.containsKey(pair))
			mapLongSignals.remove(pair);

	}

}
