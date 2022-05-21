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
		{"1000SHIB", "ADA", "ALGO", "ALICE", "ALPHA", "ANC", "APE", "ATOM", 
		 "AUDIO", "AVAX", "BAT", "BCH", "BLZ", "BNB", "BTC", "BTS", "CELO", 
		 "DASH", "DOGE", "DOT", "EGLD", "EOS", "ETH", "FIL", "FTM", "GALA", 
		 "GMT", "HNT", "IOTA", "KAVA", "KNC", "LINA", "LINK", "LTC", "LUNA", 
		 "MANA", "MATIC", "NEAR", "NEO", "OCEAN", "OGN", "OMG", "ONE", "PEOPLE", 
		 "QTUM", "ROSE", "RUNE", "SAND", "SOL", "STORJ", "SUSHI", "THETA", "TRX", 
		 "UNI", "VET", "WAVES", "XLM", "XMR", "XRP", "XTZ", "ZEC", "ZIL"};

	private static final Integer LEVERAGE = 10;
	private static final Integer ITERATIONS = 5;
	private static final Double PRICE_INCREMENT = 0.02;
	private static final Double STOPLOSS_INCREMENT = 0.1;
	private static final Double COINS_INCREMENT = 0.50;
	private static final Double TAKEPROFIT = 0.01;
	private static final Integer POSITIONS_MAX = 5;
	private static final Double POSITION_START_QTY = 0.03;
	private static final Double POSITION_START_QTY_MAX = 0.035;
	private static final Double BALANCE_MIN_AVAILABLE = 0.30;

	private static Boolean isDarkMode;
	private static String favoriteSymbols;
	private static Integer leverage;
	private static Integer iterations;
	private static Double priceIncrement;
	private static Double stoplossIncrement;
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

	public static String getFavorite_symbols()
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

	public static Double getPrice_increment()
	{
		return priceIncrement != null ? priceIncrement : PRICE_INCREMENT;
	}

	public static Double getStoploss_increment()
	{
		return stoplossIncrement != null ? stoplossIncrement : STOPLOSS_INCREMENT;
	}

	public static Double getCoins_increment()
	{
		return coinsIncrement != null ? coinsIncrement : COINS_INCREMENT;
	}

	public static Double getTakeprofit()
	{
		return takeprofit != null ? takeprofit : TAKEPROFIT;
	}

	public static Integer getPositions_max()
	{
		return positionsMax != null ? positionsMax : POSITIONS_MAX;
	}

	public static Double getPosition_start_qty()
	{
		return positionStartQty != null ? positionStartQty : POSITION_START_QTY;
	}

	public static Double getPosition_start_qty_max()
	{
		return positionStartQtyMax != null ? positionStartQtyMax : POSITION_START_QTY_MAX;
	}

	public static Double getBalance_min_available()
	{
		return balanceMinAvailable != null ? balanceMinAvailable : BALANCE_MIN_AVAILABLE;
	}

	// -----------------------------------------------------------------------

	public static void setFavoriteSymbols(String favoriteSymbols)
	{
		Config.favoriteSymbols = favoriteSymbols;
	}

	public static void setDarkMode(boolean isDarkMode)
	{
		Config.isDarkMode = isDarkMode;
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

			prop.setProperty("is_dark_mode", String.valueOf(isDarkMode));
			prop.setProperty("favorite_symbols", favoriteSymbols);
			prop.setProperty("leverage", String.valueOf(leverage));
			prop.setProperty("iterations", String.valueOf(iterations));
			prop.setProperty("price_increment", String.valueOf(priceIncrement));
			prop.setProperty("stoploss_increment", String.valueOf(stoplossIncrement));
			prop.setProperty("coins_increment", String.valueOf(coinsIncrement));
			prop.setProperty("takeprofit", String.valueOf(takeprofit));
			prop.setProperty("positions_max", String.valueOf(positionsMax));
			prop.setProperty("position_start_qty", String.valueOf(positionStartQty));
			prop.setProperty("position_start_qty_max", String.valueOf(positionStartQtyMax));
			prop.setProperty("balance_min_available", String.valueOf(balanceMinAvailable));
			
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
