package sanzol.aitrader.be.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import api.client.impl.config.ApiConfig;
import sanzol.aitrader.be.enums.PriceIncrType;
import sanzol.aitrader.be.enums.QtyIncrType;
import sanzol.aitrader.be.enums.QuantityType;

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
		return favoriteSymbols != null ? favoriteSymbols : String.join(",", FAVORITE_SYMBOLS);
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

	public static void setPriceIncrType(String code)
	{
		Config.priceIncrType = PriceIncrType.fromCode(code);
	}

	public static void setQtyIncrType(QtyIncrType qtyIncrType)
	{
		Config.qtyIncrType = qtyIncrType;
	}

	public static void setQtyIncrType(String code)
	{
		Config.qtyIncrType = QtyIncrType.fromCode(code);
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

	public static void setQuantityType(String code)
	{
		Config.quantityType = QuantityType.fromCode(code);
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

	public static void save() throws FileNotFoundException, IOException
	{
		File basepath = new File(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString());
		File file = new File(basepath, Constants.PROPERTIES_FILENAME);

		try (OutputStream output = new FileOutputStream(file))
		{
			Properties prop = new Properties();

			prop.setProperty("isDarkMode", String.valueOf(isDarkMode()));
			prop.setProperty("favoriteSymbols", getFavoriteSymbols());
			prop.setProperty("betterSymbolsMinVolume", String.valueOf(getBetterSymbolsMinVolume()));
			prop.setProperty("betterSymbolsMaxChange", String.valueOf(getBetterSymbolsMaxChange()));
			prop.setProperty("blocksToAnalizeBB", String.valueOf(getBlocksToAnalizeBB()));
			prop.setProperty("blocksToAnalizeWA", String.valueOf(getBlocksToAnalizeWA()));

			prop.setProperty("iterations", String.valueOf(getIterations()));
			prop.setProperty("pipBase", String.valueOf(getPipBase()));
			prop.setProperty("pipCoef", String.valueOf(getPipCoef()));
			prop.setProperty("priceIncrType", getPriceIncrType().getCode());
			prop.setProperty("qtyIncrType", getQtyIncrType().getCode());
			prop.setProperty("priceIncr", String.valueOf(getPriceIncr()));
			prop.setProperty("qtyIncr", String.valueOf(getQtyIncr()));
			prop.setProperty("stopLoss", String.valueOf(getStopLoss()));
			prop.setProperty("takeProfit", String.valueOf(getTakeProfit()));
			prop.setProperty("quantityType", getQuantityType().getCode());
			prop.setProperty("inQty", String.valueOf(getInQty()));

			prop.setProperty("leverage", String.valueOf(getLeverage()));
			prop.setProperty("positionsMax", String.valueOf(getPositionsMax()));
			prop.setProperty("balanceMinAvailable", String.valueOf(getBalanceMinAvailable()));

			prop.store(output, null);

			// prop.forEach((k, v) -> System.out.println("Key : " + k + ", Value : " + v));
		}
	}

	public static void load() throws FileNotFoundException, IOException
	{
		File basepath = new File(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString());
		File file = new File(basepath, Constants.PROPERTIES_FILENAME);

		if (file.exists())
		{
			try (InputStream input = new FileInputStream(file))
			{
				Properties prop = new Properties();

				prop.load(input);

				if (prop.containsKey("isDarkMode"))
					isDarkMode = "true".equalsIgnoreCase(prop.getProperty("isDarkMode"));
				if (prop.containsKey("favoriteSymbols"))
					favoriteSymbols = prop.getProperty("favoriteSymbols");
				if (prop.containsKey("betterSymbolsMinVolume"))
					betterSymbolsMinVolume = Long.valueOf(prop.getProperty("betterSymbolsMinVolume"));
				if (prop.containsKey("betterSymbolsMaxChange"))
					betterSymbolsMaxChange = Double.valueOf(prop.getProperty("betterSymbolsMaxChange"));
				if (prop.containsKey("blocksToAnalizeBB"))
					blocksToAnalizeBB = Integer.valueOf(prop.getProperty("blocksToAnalizeBB"));
				if (prop.containsKey("blocksToAnalizeWA"))
					blocksToAnalizeWA = Integer.valueOf(prop.getProperty("blocksToAnalizeWA"));

				if (prop.containsKey("iterations"))
					iterations = Integer.valueOf(prop.getProperty("iterations"));
				if (prop.containsKey("pipBase"))
					pipBase = Double.valueOf(prop.getProperty("pipBase"));
				if (prop.containsKey("pipCoef"))
					pipCoef = Double.valueOf(prop.getProperty("pipCoef"));
				if (prop.containsKey("priceIncrType"))
					priceIncrType = PriceIncrType.fromCode(prop.getProperty("priceIncrType"));
				if (prop.containsKey("qtyIncrType"))
					qtyIncrType = QtyIncrType.fromCode(prop.getProperty("qtyIncrType"));
				if (prop.containsKey("priceIncr"))
					priceIncr = Double.valueOf(prop.getProperty("priceIncr"));
				if (prop.containsKey("qtyIncr"))
					qtyIncr = Double.valueOf(prop.getProperty("qtyIncr"));
				if (prop.containsKey("stopLoss"))
					stopLoss = Double.valueOf(prop.getProperty("stopLoss"));
				if (prop.containsKey("takeProfit"))
					takeProfit = Double.valueOf(prop.getProperty("takeProfit"));
				if (prop.containsKey("quantityType"))
					quantityType = QuantityType.fromCode(prop.getProperty("quantityType"));
				if (prop.containsKey("inQty"))
					inQty = Double.valueOf(prop.getProperty("inQty"));

				if (prop.containsKey("leverage"))
					leverage = Integer.valueOf(prop.getProperty("leverage"));
				if (prop.containsKey("positionsMax"))
					positionsMax = Integer.valueOf(prop.getProperty("positionsMax"));
				if (prop.containsKey("balanceMinAvailable"))
					balanceMinAvailable = Double.valueOf(prop.getProperty("balanceMinAvailable"));

			}
		}
	}

}
