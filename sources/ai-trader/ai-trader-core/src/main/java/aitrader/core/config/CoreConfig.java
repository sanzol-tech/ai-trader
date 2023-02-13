package aitrader.core.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import aitrader.core.model.enums.GridStrategy;
import aitrader.core.model.enums.PriceIncrType;
import aitrader.core.model.enums.QtyIncrType;
import aitrader.core.model.enums.QuantityType;
import aitrader.util.Properties;
import technicals.indicators.depth.DephMergedPoints.MergeMode;

public class CoreConfig
{
	private static String defaultSymbolRight = "USDT";

	// Market
	private static String whiteList = "BTC,ETH,DOGE,DOT,MATIC,TRX";
	private static String blackList = "";
	private static Long minVolume24h = 100000000l;
	private static Double minChange24h = 2d;
	private static Double highStoch24h = 80d;
	private static Double lowStoch24h = 20d;

	// Signals
	private static Boolean favoriteSymbols = false;
	private static Boolean betterSymbols = true;
	private static Integer blocksToAnalizeBB = 8;
	private static Double dist1ToAnalizeWA = 0.05;
	private static Double dist2ToAnalizeWA = 0.15;
	private static Double dist3ToAnalizeWA = 0.75;
	private static MergeMode mergeMode = MergeMode.MAX;
	private static Double minShortLongDist = 0.8;
	private static Double maxShortLongDist = 12d;
	private static Double minRatio = 0d;

	// Grid Strategy
	private static Integer iterations = 2;
	private static PriceIncrType priceIncrType = PriceIncrType.GEOMETRIC;
	private static QtyIncrType qtyIncrType = QtyIncrType.POSITION;
	private static Double priceIncr = 0.02;
	private static Double qtyIncr = 1d;
	private static Double stopLoss = 0.01;
	private static Double takeProfit = 0.02;
	private static QuantityType quantityType = QuantityType.BALANCE;
	private static Double inQty = 0.1;

	private static GridStrategy gridStrategy = GridStrategy.SIMPLE;

	// Balance & Positions
	private static Integer leverage = 10;
	private static Integer positionsMax = 2;
	private static Double balanceMinAvailable = 0.1;

	public static String getDefaultSymbolRight()
	{
		return defaultSymbolRight;
	}

	public static void setDefaultSymbolRight(String defaultSymbolRight)
	{
		CoreConfig.defaultSymbolRight = defaultSymbolRight;
	}

	public static String getWhiteList()
	{
		return whiteList;
	}

	public static void setWhiteList(String whiteList)
	{
		CoreConfig.whiteList = whiteList;
	}

	public static String getBlackList()
	{
		return blackList;
	}

	public static void setBlackList(String blackList)
	{
		CoreConfig.blackList = blackList;
	}

	public static Long getMinVolume24h()
	{
		return minVolume24h;
	}

	public static void setMinVolume24h(Long minVolume24h)
	{
		CoreConfig.minVolume24h = minVolume24h;
	}

	public static Double getMinChange24h()
	{
		return minChange24h;
	}

	public static void setMinChange24h(Double minChange24h)
	{
		CoreConfig.minChange24h = minChange24h;
	}

	public static Double getHighStoch24h()
	{
		return highStoch24h;
	}

	public static void setHighStoch24h(Double highStoch24h)
	{
		CoreConfig.highStoch24h = highStoch24h;
	}

	public static Double getLowStoch24h()
	{
		return lowStoch24h;
	}

	public static void setLowStoch24h(Double lowStoch24h)
	{
		CoreConfig.lowStoch24h = lowStoch24h;
	}

	public static Boolean getFavoriteSymbols()
	{
		return favoriteSymbols;
	}

	public static void setFavoriteSymbols(Boolean favoriteSymbols)
	{
		CoreConfig.favoriteSymbols = favoriteSymbols;
	}

	public static Boolean getBetterSymbols()
	{
		return betterSymbols;
	}

	public static void setBetterSymbols(Boolean betterSymbols)
	{
		CoreConfig.betterSymbols = betterSymbols;
	}

	public static Integer getBlocksToAnalizeBB()
	{
		return blocksToAnalizeBB;
	}

	public static void setBlocksToAnalizeBB(Integer blocksToAnalizeBB)
	{
		CoreConfig.blocksToAnalizeBB = blocksToAnalizeBB;
	}

	public static Double getDist1ToAnalizeWA()
	{
		return dist1ToAnalizeWA;
	}

	public static void setDist1ToAnalizeWA(Double dist1ToAnalizeWA)
	{
		CoreConfig.dist1ToAnalizeWA = dist1ToAnalizeWA;
	}

	public static Double getDist2ToAnalizeWA()
	{
		return dist2ToAnalizeWA;
	}

	public static void setDist2ToAnalizeWA(Double dist2ToAnalizeWA)
	{
		CoreConfig.dist2ToAnalizeWA = dist2ToAnalizeWA;
	}

	public static Double getDist3ToAnalizeWA()
	{
		return dist3ToAnalizeWA;
	}

	public static void setDist3ToAnalizeWA(Double dist3ToAnalizeWA)
	{
		CoreConfig.dist3ToAnalizeWA = dist3ToAnalizeWA;
	}

	public static MergeMode getMergeMode()
	{
		return mergeMode;
	}

	public static void setMergeMode(MergeMode mergeMode)
	{
		CoreConfig.mergeMode = mergeMode;
	}

	public static Double getMinShortLongDist()
	{
		return minShortLongDist;
	}

	public static void setMinShortLongDist(Double minShortLongDist)
	{
		CoreConfig.minShortLongDist = minShortLongDist;
	}

	public static Double getMaxShortLongDist()
	{
		return maxShortLongDist;
	}

	public static void setMaxShortLongDist(Double maxShortLongDist)
	{
		CoreConfig.maxShortLongDist = maxShortLongDist;
	}

	public static Double getMinRatio()
	{
		return minRatio;
	}

	public static void setMinRatio(Double minRatio)
	{
		CoreConfig.minRatio = minRatio;
	}

	public static Integer getIterations()
	{
		return iterations;
	}

	public static void setIterations(Integer iterations)
	{
		CoreConfig.iterations = iterations;
	}

	public static PriceIncrType getPriceIncrType()
	{
		return priceIncrType;
	}

	public static void setPriceIncrType(PriceIncrType priceIncrType)
	{
		CoreConfig.priceIncrType = priceIncrType;
	}

	public static QtyIncrType getQtyIncrType()
	{
		return qtyIncrType;
	}

	public static void setQtyIncrType(QtyIncrType qtyIncrType)
	{
		CoreConfig.qtyIncrType = qtyIncrType;
	}

	public static Double getPriceIncr()
	{
		return priceIncr;
	}

	public static void setPriceIncr(Double priceIncr)
	{
		CoreConfig.priceIncr = priceIncr;
	}

	public static Double getQtyIncr()
	{
		return qtyIncr;
	}

	public static void setQtyIncr(Double qtyIncr)
	{
		CoreConfig.qtyIncr = qtyIncr;
	}

	public static Double getStopLoss()
	{
		return stopLoss;
	}

	public static void setStopLoss(Double stopLoss)
	{
		CoreConfig.stopLoss = stopLoss;
	}

	public static Double getTakeProfit()
	{
		return takeProfit;
	}

	public static void setTakeProfit(Double takeProfit)
	{
		CoreConfig.takeProfit = takeProfit;
	}

	public static QuantityType getQuantityType()
	{
		return quantityType;
	}

	public static void setQuantityType(QuantityType quantityType)
	{
		CoreConfig.quantityType = quantityType;
	}

	public static Double getInQty()
	{
		return inQty;
	}

	public static void setInQty(Double inQty)
	{
		CoreConfig.inQty = inQty;
	}

	public static GridStrategy getGridStrategy()
	{
		return gridStrategy;
	}

	public static void setGridStrategy(GridStrategy gridStrategy)
	{
		CoreConfig.gridStrategy = gridStrategy;
	}

	public static Integer getLeverage()
	{
		return leverage;
	}

	public static void setLeverage(Integer leverage)
	{
		CoreConfig.leverage = leverage;
	}

	public static Integer getPositionsMax()
	{
		return positionsMax;
	}

	public static void setPositionsMax(Integer positionsMax)
	{
		CoreConfig.positionsMax = positionsMax;
	}

	public static Double getBalanceMinAvailable()
	{
		return balanceMinAvailable;
	}

	public static void setBalanceMinAvailable(Double balanceMinAvailable)
	{
		CoreConfig.balanceMinAvailable = balanceMinAvailable;
	}

	// --------------------------------------------------------------------

	public static boolean load() throws IOException
	{
		Path path = Paths.get(CoreConstants.DEFAULT_USER_FOLDER, CoreConstants.PROPERTIES_FILENAME);
		if (!path.toFile().exists())
		{
			CoreLog.error("File " + CoreConstants.PROPERTIES_FILENAME + " does not exist");
			return false;
		}

		Properties props = Properties.create(false).load(path);
		defaultSymbolRight = props.getValue("defaultSymbolRight");
		whiteList = props.getValue("whiteList");
		blackList = props.getValue("blackList");
		minVolume24h = props.getLong("minVolume24h");
		minChange24h = props.getDouble("minChange24h");
		highStoch24h = props.getDouble("highStoch24h");
		lowStoch24h = props.getDouble("lowStoch24h");
		favoriteSymbols = props.getBoolean("favoriteSymbols");
		betterSymbols = props.getBoolean("betterSymbols");
		blocksToAnalizeBB = props.getInteger("blocksToAnalizeBB");
		dist1ToAnalizeWA = props.getDouble("dist1ToAnalizeWA");
		dist2ToAnalizeWA = props.getDouble("dist2ToAnalizeWA");
		dist3ToAnalizeWA = props.getDouble("dist3ToAnalizeWA");
		mergeMode = MergeMode.fromName(props.getValue("mergeMode"));
		minShortLongDist = props.getDouble("minShortLongDist");
		maxShortLongDist = props.getDouble("maxShortLongDist");
		minRatio = props.getDouble("minRatio");
		iterations = props.getInteger("iterations");
		priceIncrType = PriceIncrType.fromName(props.getValue("priceIncrType"));
		qtyIncrType = QtyIncrType.fromName(props.getValue("qtyIncrType"));
		priceIncr = props.getDouble("priceIncr");
		qtyIncr = props.getDouble("qtyIncr");
		stopLoss = props.getDouble("stopLoss");
		takeProfit = props.getDouble("takeProfit");
		quantityType = QuantityType.fromName(props.getValue("quantityType"));
		inQty = props.getDouble("inQty");
		gridStrategy = GridStrategy.fromName(props.getValue("gridStrategy"));
		leverage = props.getInteger("leverage");
		positionsMax = props.getInteger("positionsMax");
		balanceMinAvailable = props.getDouble("balanceMinAvailable");

		return true;
	}

	public static void save() throws IOException
	{
		Path path = Paths.get(CoreConstants.DEFAULT_USER_FOLDER, CoreConstants.PROPERTIES_FILENAME);
		Properties.create(false)
			.put("defaultSymbolRight", defaultSymbolRight)
			.put("whiteList", whiteList)
			.put("blackList", blackList)
			.put("minVolume24h", minVolume24h)
			.put("minChange24h", minChange24h)
			.put("highStoch24h", highStoch24h)
			.put("lowStoch24h", lowStoch24h)
			.put("favoriteSymbols", favoriteSymbols)
			.put("betterSymbols", betterSymbols)
			.put("blocksToAnalizeBB", blocksToAnalizeBB)
			.put("dist1ToAnalizeWA", dist1ToAnalizeWA)
			.put("dist2ToAnalizeWA", dist2ToAnalizeWA)
			.put("dist3ToAnalizeWA", dist3ToAnalizeWA)
			.put("mergeMode", mergeMode != null ? mergeMode.name() : "")
			.put("minShortLongDist", minShortLongDist)
			.put("maxShortLongDist", maxShortLongDist)
			.put("minRatio", minRatio)
			.put("iterations", iterations)
			.put("priceIncrType", priceIncrType != null ? priceIncrType.name() : "")
			.put("qtyIncrType", qtyIncrType != null ? qtyIncrType.name() : "")
			.put("priceIncr", priceIncr)
			.put("qtyIncr", qtyIncr)
			.put("stopLoss", stopLoss)
			.put("takeProfit", takeProfit)
			.put("quantityType", quantityType != null ? quantityType.name() : "")
			.put("inQty", inQty)
			.put("gridStrategy", gridStrategy != null ? gridStrategy.name() : "")
			.put("leverage", leverage)
			.put("positionsMax", positionsMax)
			.put("balanceMinAvailable", balanceMinAvailable)
			.save(path);
	}

}
