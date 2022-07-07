package sanzol.app.config;

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

public class Config
{
	private static final boolean IS_DARK_MODE = false;

	private static final String[] FAVORITE_SYMBOLS = new String[]
		{"1000SHIB", "ADA", "ALGO", "ALICE", "ATOM", "AVAX", "BAT", "BCH", 
		 "BNB", "BTC", "DASH", "DOGE", "DOT", "ETH", "FTM", "GALA", 
		 "KAVA", "LINK", "LTC", "MANA", "MATIC", "NEAR", "NEO", "OCEAN", 
		 "QTUM", "ROSE", "RUNE", "SAND", "SOL", "SUSHI", "TRX", "UNI", 
		 "VET", "XLM", "XMR", "XRP", "ZEC"};

	//Pair
	public static final String DEFAULT_SYMBOL_RIGHT = "USDT";

	//Order book
	private static final int BLOCKS_TO_ANALYZE = 6;
	
	//Better symbols
	private static final long BETTER_SYMBOLS_MIN_VOLUME = 100000000;
	private static final double BETTER_SYMBOLS_MAX_CHANGE = 10;

	private static final int LEVERAGE = 10;
	private static final int ITERATIONS = 4;
	private static final double PRICE_INCREMENT = 0.02;
	private static final double STOPLOSS_INCREMENT = 0.01;
	private static final double COINS_INCREMENT_1 = 0.05;
	private static final double COINS_INCREMENT = 1.0;
	private static final double TAKEPROFIT = 0.015;
	private static final int POSITIONS_MAX = 5;
	private static final double POSITION_START_QTY = 0.1;
	private static final double POSITION_START_QTY_MAX = 0.1;
	private static final double BALANCE_MIN_AVAILABLE = 0.30;

	private static Integer blocksToAnalize;
	private static Long betterSymbolsMinVolume;
	private static Double betterSymbolsMaxChange;
	private static Boolean isDarkMode;
	private static String favoriteSymbols;
	private static Integer leverage;
	private static Integer iterations;
	private static Double priceIncrement;
	private static Double stoplossIncrement;
	private static Double coinsIncrement1;
	private static Double coinsIncrement;
	private static Double takeprofit;
	private static Integer positionsMax;
	private static Double positionStartQty;
	private static Double positionStartQtyMax;
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

	public static Integer getBlocksToAnalize()
	{
		return blocksToAnalize != null ? blocksToAnalize : BLOCKS_TO_ANALYZE;
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

	public static Integer getIterations()
	{
		return iterations != null ? iterations : ITERATIONS;
	}

	public static Double getPriceIncrement()
	{
		return priceIncrement != null ? priceIncrement : PRICE_INCREMENT;
	}

	public static Double getStoplossIncrement()
	{
		return stoplossIncrement != null ? stoplossIncrement : STOPLOSS_INCREMENT;
	}

	public static Double getCoinsIncrement1()
	{
		return coinsIncrement1 != null ? coinsIncrement1 : COINS_INCREMENT_1;
	}

	public static Double getCoinsIncrement()
	{
		return coinsIncrement != null ? coinsIncrement : COINS_INCREMENT;
	}

	public static Double getTakeprofit()
	{
		return takeprofit != null ? takeprofit : TAKEPROFIT;
	}

	public static Integer getPositionsMax()
	{
		return positionsMax != null ? positionsMax : POSITIONS_MAX;
	}

	public static Double getPositionStartQty()
	{
		return positionStartQty != null ? positionStartQty : POSITION_START_QTY;
	}

	public static Double getPositionStartQtyMax()
	{
		return positionStartQtyMax != null ? positionStartQtyMax : POSITION_START_QTY_MAX;
	}

	public static Double getBalanceMinAvailable()
	{
		return balanceMinAvailable != null ? balanceMinAvailable : BALANCE_MIN_AVAILABLE;
	}

	// -----------------------------------------------------------------------

	public static void setDarkMode(boolean isDarkMode)
	{
		Config.isDarkMode = isDarkMode;
	}

	public static void setBlocksToAnalize(Integer blocksToAnalize)
	{
		Config.blocksToAnalize = blocksToAnalize;
	}
	
	public static void setBlocksToAnalize(String blocksToAnalize)
	{
		Config.blocksToAnalize = Integer.valueOf(blocksToAnalize);
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

	public static void setFavoriteSymbols(String favoriteSymbols)
	{
		Config.favoriteSymbols = favoriteSymbols;
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
		Config.iterations = Integer.valueOf(iterations);
	}

	public static void setPriceIncrement(Double priceIncrement)
	{
		Config.priceIncrement = priceIncrement;
	}

	public static void setPriceIncrement(String price_increment)
	{
		Config.priceIncrement = Double.valueOf(price_increment);
	}

	public static void setStoplossIncrement(Double stoplossIncrement)
	{
		Config.stoplossIncrement = stoplossIncrement;
	}

	public static void setStoplossIncrement(String stoplossIncrement)
	{
		Config.stoplossIncrement = Double.valueOf(stoplossIncrement);
	}

	public static void setCoinsIncrement1(Double coinsIncrement1)
	{
		Config.coinsIncrement1 = coinsIncrement1;
	}

	public static void setCoinsIncrement1(String coinsIncrement1)
	{
		Config.coinsIncrement1 = Double.valueOf(coinsIncrement1);
	}

	public static void setCoinsIncrement(Double coinsIncrement)
	{
		Config.coinsIncrement = coinsIncrement;
	}

	public static void setCoinsIncrement(String coinsIncrement)
	{
		Config.coinsIncrement = Double.valueOf(coinsIncrement);
	}

	public static void setTakeprofit(Double takeprofit)
	{
		Config.takeprofit = takeprofit;
	}

	public static void setTakeprofit(String takeprofit)
	{
		Config.takeprofit = Double.valueOf(takeprofit);
	}

	public static void setPositionsMax(Integer positionsMax)
	{
		Config.positionsMax = positionsMax;
	}

	public static void setPositionsMax(String positionsMax)
	{
		Config.positionsMax = Integer.valueOf(positionsMax);
	}

	public static void setPositionStartQty(Double positionStartQty)
	{
		Config.positionStartQty = positionStartQty;
	}

	public static void setPositionStartQtyMax(Double positionStartQtyMax)
	{
		Config.positionStartQtyMax = positionStartQtyMax;
	}

	public static void setBalanceMinAvailable(Double balanceMinAvailable)
	{
		Config.balanceMinAvailable = balanceMinAvailable;
	}

	// -----------------------------------------------------------------------

	public static void save() throws FileNotFoundException, IOException
	{
		File file = new File(Constants.DEFAULT_USER_FOLDER, Constants.PROPERTIES_FILENAME);
		try (OutputStream output = new FileOutputStream(file))
		{
			Properties prop = new Properties();

			prop.setProperty("is_dark_mode", String.valueOf(isDarkMode()));
			prop.setProperty("better_symbols_min_volume", String.valueOf(getBetterSymbolsMinVolume()));
			prop.setProperty("better_symbols_max_change", String.valueOf(getBetterSymbolsMaxChange()));
			prop.setProperty("blocks_to_analize", String.valueOf(getBlocksToAnalize()));
			prop.setProperty("favorite_symbols", getFavoriteSymbols());
			prop.setProperty("leverage", String.valueOf(getLeverage()));
			prop.setProperty("iterations", String.valueOf(getIterations()));
			prop.setProperty("price_increment", String.valueOf(getPriceIncrement()));
			prop.setProperty("stoploss_increment", String.valueOf(getStoplossIncrement()));
			prop.setProperty("coins_increment1", String.valueOf(getCoinsIncrement1()));
			prop.setProperty("coins_increment", String.valueOf(getCoinsIncrement()));
			prop.setProperty("takeprofit", String.valueOf(getTakeprofit()));
			prop.setProperty("positions_max", String.valueOf(getPositionsMax()));
			prop.setProperty("position_start_qty", String.valueOf(getPositionStartQty()));
			prop.setProperty("position_start_qty_max", String.valueOf(getPositionStartQtyMax()));
			prop.setProperty("balance_min_available", String.valueOf(getBalanceMinAvailable()));
			
			prop.store(output, null);

			//prop.forEach((k, v) -> System.out.println("Key : " + k + ", Value : " + v));
		}
	}

	public static void load() throws FileNotFoundException, IOException
	{
		File file = new File(Constants.DEFAULT_USER_FOLDER, Constants.PROPERTIES_FILENAME);
		if (file.exists())
		{
			try (InputStream input = new FileInputStream(file))
			{
				Properties prop = new Properties();

				prop.load(input);

				if (prop.containsKey("better_symbols_min_volume"))
					betterSymbolsMinVolume = Long.valueOf(prop.getProperty("better_symbols_min_volume"));
				if (prop.containsKey("better_symbols_max_change"))
					betterSymbolsMaxChange = Double.valueOf(prop.getProperty("better_symbols_max_change"));
				if (prop.containsKey("blocks_to_analize"))
					blocksToAnalize = Integer.valueOf(prop.getProperty("blocks_to_analize"));
				if (prop.containsKey("favorite_symbols"))
					favoriteSymbols = prop.getProperty("favorite_symbols");
				if (prop.containsKey("is_dark_mode"))
					isDarkMode =  "true".equalsIgnoreCase(prop.getProperty("is_dark_mode"));
				if (prop.containsKey("leverage"))
					leverage = Integer.valueOf(prop.getProperty("leverage"));
				if (prop.containsKey("iterations"))
					iterations = Integer.valueOf(prop.getProperty("iterations"));
				if (prop.containsKey("price_increment"))
					priceIncrement = Double.valueOf(prop.getProperty("price_increment"));
				if (prop.containsKey("stoploss_increment"))
					stoplossIncrement = Double.valueOf(prop.getProperty("stoploss_increment"));
				if (prop.containsKey("coins_increment1"))
					coinsIncrement1 = Double.valueOf(prop.getProperty("coins_increment1"));
				if (prop.containsKey("coins_increment"))
					coinsIncrement = Double.valueOf(prop.getProperty("coins_increment"));
				if (prop.containsKey("takeprofit"))
					takeprofit = Double.valueOf(prop.getProperty("takeprofit"));
				if (prop.containsKey("positions_max"))
					positionsMax = Integer.valueOf(prop.getProperty("positions_max"));
				if (prop.containsKey("position_start_qty"))
					positionStartQty = Double.valueOf(prop.getProperty("position_start_qty"));
				if (prop.containsKey("position_start_qty_max"))
					positionStartQtyMax = Double.valueOf(prop.getProperty("position_start_qty_max"));
				if (prop.containsKey("balance_min_available"))
					balanceMinAvailable = Double.valueOf(prop.getProperty("balance_min_available"));

			}
		}
	}

}
