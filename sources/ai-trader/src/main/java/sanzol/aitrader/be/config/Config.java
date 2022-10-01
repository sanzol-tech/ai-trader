package sanzol.aitrader.be.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import api.client.impl.config.ApiConfig;
import sanzol.aitrader.be.enums.GridStrategy;
import sanzol.aitrader.be.enums.PriceIncrType;
import sanzol.aitrader.be.enums.QtyIncrType;
import sanzol.aitrader.be.enums.QuantityType;
import sanzol.util.Properties;
import sanzol.util.log.LogService;

public class Config
{
	private static final boolean IS_DARK_MODE = false;

	// Pair
	public static final String DEFAULT_SYMBOL_RIGHT = "USDT";

	private static final String[] FAVORITE_SYMBOLS = new String[]
		{"1000SHIB", "ADA", "ALGO", "ALICE", "ATOM", "AVAX", "BAT", "BCH",
		 "BNB", "BTC", "DASH", "DOGE", "DOT", "ETC", "ETH", "FTM", "GALA",
		 "KAVA", "LINK", "LTC", "MANA", "MATIC", "NEAR", "NEO", "OCEAN",
		 "QTUM", "ROSE", "RUNE", "SAND", "SOL", "SUSHI", "TRX", "UNI",
		 "VET", "XLM", "XMR", "XRP", "ZEC"};

	// Better symbols
	private static final long BETTER_SYMBOLS_MIN_VOLUME = 180 * 1000000;
	private static final double BETTER_SYMBOLS_MAX_CHANGE = 10;

	// Order book
	private static final int BLOCKS_TO_ANALYZE_BB = 8;
	private static final int BLOCKS_TO_ANALYZE_WA = 8;

	private static GridStrategy GRID_STRATEGY = GridStrategy.CUSTOM;

	// Default Grid Strategy
	private static final Integer ITERATIONS = 2;
	private static final Double PIP_BASE = null;
	private static final Double PIP_COEF = null;
	private static final PriceIncrType PRICE_INCR_TYPE = PriceIncrType.GEOMETRIC;
	private static final QtyIncrType QTY_INCR_TYPE = QtyIncrType.POSITION;
	private static final Double PRICE_INCR = 0.02;
	private static final Double QTY_INCR = 1.0;
	private static final Double STOP_LOSS = 0.02;
	private static final Double TAKE_PROFIT = 0.02;

	private static final QuantityType QUANTITY_TYPE = QuantityType.USD;
	private static final Double IN_QTY = 30.0;


	// Positions
	private static final int LEVERAGE = 10;
	private static final int POSITIONS_MAX = 5;
	private static final double BALANCE_MIN_AVAILABLE = 0.30;

	private static Boolean isDarkMode;

	private static String favoriteSymbols;

	private static Long betterSymbolsMinVolume;
	private static Double betterSymbolsMaxChange;

	private static Integer blocksToAnalizeBB;
	private static Integer blocksToAnalizeWA;

	private static GridStrategy gridStrategy;
	
	// Grid Strategy
	private static Integer iterations;
	private static Double pipBase;
	private static Double pipCoef;
	private static PriceIncrType priceIncrType;
	private static QtyIncrType qtyIncrType;
	private static Double priceIncr;
	private static Double qtyIncr;
	private static Double stopLoss;
	private static Double takeProfit;

	private static QuantityType quantityType;
	private static Double inQty;

	private static Integer leverage;
	private static Integer positionsMax;
	private static Double balanceMinAvailable;

	// -----------------------------------------------------------------------

	public static List<String> getLstFavSymbols()
	{
		if (favoriteSymbols == null || favoriteSymbols.isEmpty())
			return Arrays.asList(FAVORITE_SYMBOLS);
		else
			return Arrays.asList(favoriteSymbols.split("\\s*,\\s*"));
	}

	// -----------------------------------------------------------------------

	public static Boolean isDarkMode()
	{
		return isDarkMode != null ? isDarkMode : IS_DARK_MODE;
	}

	public static Integer getBlocksToAnalizeBB()
	{
		return blocksToAnalizeBB != null ? blocksToAnalizeBB : BLOCKS_TO_ANALYZE_BB;
	}

	public static Integer getBlocksToAnalizeWA()
	{
		return blocksToAnalizeWA != null ? blocksToAnalizeWA : BLOCKS_TO_ANALYZE_WA;
	}

	public static Long getBetterSymbolsMinVolume()
	{
		return betterSymbolsMinVolume != null ? betterSymbolsMinVolume : BETTER_SYMBOLS_MIN_VOLUME;
	}

	public static Double getBetterSymbolsMaxChange()
	{
		return betterSymbolsMaxChange != null ? betterSymbolsMaxChange : BETTER_SYMBOLS_MAX_CHANGE;
	}

	public static String getFavoriteSymbols()
	{
		return favoriteSymbols != null ? favoriteSymbols : String.join(", ", FAVORITE_SYMBOLS);
	}

	public static Integer getLeverage()
	{
		return leverage != null ? leverage : LEVERAGE;
	}

	public static Integer getPositionsMax()
	{
		return positionsMax != null ? positionsMax : POSITIONS_MAX;
	}

	public static Double getBalanceMinAvailable()
	{
		return balanceMinAvailable != null ? balanceMinAvailable : BALANCE_MIN_AVAILABLE;
	}

	public static GridStrategy getGridStrategy()
	{
		return gridStrategy != null ? gridStrategy : GRID_STRATEGY;
	}

	public static Integer getIterations()
	{
		return iterations != null ? iterations : ITERATIONS;
	}

	public static Double getPipBase()
	{
		return pipBase != null ? pipBase : PIP_BASE;
	}

	public static Double getPipCoef()
	{
		return pipCoef != null ? pipCoef : PIP_COEF;
	}

	public static PriceIncrType getPriceIncrType()
	{
		return priceIncrType != null ? priceIncrType : PRICE_INCR_TYPE;
	}

	public static QtyIncrType getQtyIncrType()
	{
		return qtyIncrType != null ? qtyIncrType : QTY_INCR_TYPE;
	}

	public static Double getPriceIncr()
	{
		return priceIncr != null ? priceIncr : PRICE_INCR;
	}

	public static Double getQtyIncr()
	{
		return qtyIncr != null ? qtyIncr : QTY_INCR;
	}

	public static Double getStopLoss()
	{
		return stopLoss != null ? stopLoss : STOP_LOSS;
	}

	public static Double getTakeProfit()
	{
		return takeProfit != null ? takeProfit : TAKE_PROFIT;
	}

	public static QuantityType getQuantityType()
	{
		return quantityType != null ? quantityType : QUANTITY_TYPE;
	}

	public static Double getInQty()
	{
		return inQty != null ? inQty : IN_QTY;
	}

	// -----------------------------------------------------------------------

	public static void setIsDarkMode(Boolean isDarkMode)
	{
		Config.isDarkMode = isDarkMode;
	}

	public static void setFavoriteSymbols(String favoriteSymbols)
	{
		Config.favoriteSymbols = favoriteSymbols;
	}

	public static void setBetterSymbolsMinVolume(Long betterSymbolsMinVolume)
	{
		Config.betterSymbolsMinVolume = betterSymbolsMinVolume;
	}

	public static void setBetterSymbolsMinVolume(String betterSymbolsMinVolume)
	{
		Config.betterSymbolsMinVolume = Long.valueOf(betterSymbolsMinVolume);
	}

	public static void setBetterSymbolsMaxChange(Double betterSymbolsMaxChange)
	{
		Config.betterSymbolsMaxChange = betterSymbolsMaxChange;
	}

	public static void setBetterSymbolsMaxChange(String betterSymbolsMaxChange)
	{
		Config.betterSymbolsMaxChange = Double.valueOf(betterSymbolsMaxChange);
	}

	public static void setBlocksToAnalizeBB(Integer blocksToAnalizeBB)
	{
		Config.blocksToAnalizeBB = blocksToAnalizeBB;
	}

	public static void setBlocksToAnalizeBB(String blocksToAnalizeBB)
	{
		Config.blocksToAnalizeBB = Integer.valueOf(blocksToAnalizeBB);
	}

	public static void setBlocksToAnalizeWA(Integer blocksToAnalizeWA)
	{
		Config.blocksToAnalizeWA = blocksToAnalizeWA;
	}

	public static void setBlocksToAnalizeWA(String blocksToAnalizeWA)
	{
		Config.blocksToAnalizeWA = Integer.valueOf(blocksToAnalizeWA);
	}

	public static void setPositionsMax(Integer positionsMax)
	{
		Config.positionsMax = positionsMax;
	}

	public static void setPositionsMax(String positionsMax)
	{
		Config.positionsMax = Integer.valueOf(positionsMax);
	}

	public static void setBalanceMinAvailable(Double balanceMinAvailable)
	{
		Config.balanceMinAvailable = balanceMinAvailable;
	}

	public static void setLeverage(Integer leverage)
	{
		Config.leverage = leverage;
	}

	public static void setLeverage(String leverage)
	{
		Config.leverage = Integer.valueOf(leverage);
	}

	public static void setGridStrategy(GridStrategy gridStrategy)
	{
		Config.gridStrategy = gridStrategy;
	}

	public static void setIterations(Integer iterations)
	{
		Config.iterations = iterations;
	}

	public static void setIterations(String iterations)
	{
		Config.iterations = Integer.valueOf(iterations);;
	}

	public static void setPipBase(Double pipBase)
	{
		Config.pipBase = pipBase;
	}

	public static void setPipBase(String pipBase)
	{
		Config.pipBase = Double.valueOf(pipBase);
	}

	public static void setPipCoef(Double pipCoef)
	{
		Config.pipCoef = pipCoef;
	}

	public static void setPipCoef(String pipCoef)
	{
		Config.pipCoef = Double.valueOf(pipCoef);
	}

	public static void setPriceIncrType(PriceIncrType priceIncrType)
	{
		Config.priceIncrType = priceIncrType;
	}

	public static void setQtyIncrType(QtyIncrType qtyIncrType)
	{
		Config.qtyIncrType = qtyIncrType;
	}

	public static void setPriceIncr(Double priceIncr)
	{
		Config.priceIncr = priceIncr;
	}

	public static void setPriceIncr(String priceIncr)
	{
		Config.priceIncr = Double.valueOf(priceIncr);
	}

	public static void setQtyIncr(Double qtyIncr)
	{
		Config.qtyIncr = qtyIncr;
	}

	public static void setQtyIncr(String qtyIncr)
	{
		Config.qtyIncr = Double.valueOf(qtyIncr);
	}

	public static void setStopLoss(Double stopLoss)
	{
		Config.stopLoss = stopLoss;
	}

	public static void setStopLoss(String stopLoss)
	{
		Config.stopLoss = Double.valueOf(stopLoss);
	}

	public static void setTakeProfit(Double takeProfit)
	{
		Config.takeProfit = takeProfit;
	}

	public static void setTakeProfit(String takeProfit)
	{
		Config.takeProfit = Double.valueOf(takeProfit);
	}

	public static void setQuantityType(QuantityType quantityType)
	{
		Config.quantityType = quantityType;
	}

	public static void setInQty(Double inQty)
	{
		Config.inQty = inQty;
	}

	public static void setInQty(String inQty)
	{
		Config.inQty = Double.valueOf(inQty);
	}

	// -----------------------------------------------------------------------

	public static boolean load() throws IOException
	{
		Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString(), Constants.PROPERTIES_FILENAME);
		if (!path.toFile().exists())
		{
			LogService.error("File " + Constants.PRIVATEKEY_FILENAME + " does not exist");
			return false;
		}

		Properties props = Properties.create(false).load(path);
		isDarkMode = props.getBoolean("isDarkMode");
		favoriteSymbols = props.getValue("favoriteSymbols");
		betterSymbolsMinVolume = props.getLong("betterSymbolsMinVolume");
		betterSymbolsMaxChange = props.getDouble("betterSymbolsMaxChange");
		blocksToAnalizeBB = props.getInteger("blocksToAnalizeBB");
		blocksToAnalizeWA = props.getInteger("blocksToAnalizeWA");
		gridStrategy = GridStrategy.fromName(props.getValue("gridStrategy"));
		iterations = props.getInteger("iterations");
		pipBase = props.getDouble("pipBase");
		pipCoef = props.getDouble("pipCoef");
		priceIncrType = PriceIncrType.fromName(props.getValue("priceIncrType"));
		qtyIncrType = QtyIncrType.fromName(props.getValue("qtyIncrType"));
		priceIncr = props.getDouble("priceIncr");
		qtyIncr = props.getDouble("qtyIncr");
		stopLoss = props.getDouble("stopLoss");
		takeProfit = props.getDouble("takeProfit");
		quantityType = QuantityType.fromName(props.getValue("quantityType"));
		inQty = props.getDouble("inQty");
		leverage = props.getInteger("leverage");
		positionsMax = props.getInteger("positionsMax");
		balanceMinAvailable = props.getDouble("balanceMinAvailable");

		return true;
	}

	public static void save() throws IOException
	{
		Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString(), Constants.PROPERTIES_FILENAME);
		Properties.create(false)
			.put("isDarkMode", isDarkMode)
			.put("favoriteSymbols", favoriteSymbols)
			.put("betterSymbolsMinVolume", betterSymbolsMinVolume)
			.put("betterSymbolsMaxChange", betterSymbolsMaxChange)
			.put("blocksToAnalizeBB", blocksToAnalizeBB)
			.put("blocksToAnalizeWA", blocksToAnalizeWA)
			.put("gridStrategy", gridStrategy.name())
			.put("iterations", iterations)
			.put("pipBase", pipBase)
			.put("pipCoef", pipCoef)
			.put("priceIncrType", priceIncrType.name())
			.put("qtyIncrType", qtyIncrType.name())
			.put("priceIncr", priceIncr)
			.put("qtyIncr", qtyIncr)
			.put("stopLoss", stopLoss)
			.put("takeProfit", takeProfit)
			.put("quantityType", quantityType.name())
			.put("inQty", inQty)
			.put("leverage", leverage)
			.put("positionsMax", positionsMax)
			.put("balanceMinAvailable", balanceMinAvailable)
			.save(path);
	}

}
