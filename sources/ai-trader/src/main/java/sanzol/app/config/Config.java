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
	private static final String[] FAVORITE_SYMBOLS = new String[]
		{"1000SHIB", "ADA", "ALGO", "ALICE", "ALPHA", "ANC", "APE", "ATOM", 
		 "AUDIO", "AVAX", "BAT", "BCH", "BLZ", "BNB", "BTC", "BTS", "CELO", 
		 "DASH", "DOGE", "DOT", "EGLD", "EOS", "ETH", "FIL", "FTM", "GALA", 
		 "GMT", "HNT", "IOTA", "KAVA", "KNC", "LINA", "LINK", "LTC", "LUNA", 
		 "MANA", "MATIC", "NEAR", "NEO", "OCEAN", "OGN", "OMG", "ONE", "PEOPLE", 
		 "ROSE", "RUNE", "SAND", "SOL", "STORJ", "SUSHI", "THETA", "TRX", "UNI", 
		 "VET", "WAVES", "XLM", "XMR", "XRP", "XTZ", "ZEC", "ZIL"};

	private static final Integer LEVERAGE = 10;
	private static final Integer ITERATIONS = 7;
	private static final Double PRICE_INCREMENT = 0.02;
	private static final Double STOPLOSS_INCREMENT = 0.03;
	private static final Double COINS_INCREMENT = 0.40;
	private static final Double TAKEPROFIT = 0.01;
	private static final Double BALANCE_START_POSITION = 0.04;
	private static final Double BALANCE_MIN_AVAILABLE = 0.15;

	private static String favorite_symbols;
	private static Integer leverage;
	private static Integer iterations;
	private static Double price_increment;
	private static Double stoploss_increment;
	private static Double coins_increment;
	private static Double takeprofit;
	private static Double balance_start_position;
	private static Double balance_min_available;

	// -----------------------------------------------------------------------

	public static List<String> getLstFavSymbols()
	{
		if (favorite_symbols == null || favorite_symbols.isEmpty())
			return Arrays.asList(FAVORITE_SYMBOLS);
		else
			return Arrays.asList(favorite_symbols.split("\\s*,\\s*"));
	}

	// -----------------------------------------------------------------------

	public static String getFavorite_symbols()
	{
		return favorite_symbols != null ? favorite_symbols : String.join(",", FAVORITE_SYMBOLS);
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
		return price_increment != null ? price_increment : PRICE_INCREMENT;
	}

	public static Double getStoploss_increment()
	{
		return stoploss_increment != null ? stoploss_increment : STOPLOSS_INCREMENT;
	}

	public static Double getCoins_increment()
	{
		return coins_increment != null ? coins_increment : COINS_INCREMENT;
	}

	public static Double getTakeprofit()
	{
		return takeprofit != null ? takeprofit : TAKEPROFIT;
	}

	public static Double getBalance_start_position()
	{
		return balance_start_position != null ? balance_start_position : BALANCE_START_POSITION;
	}

	public static Double getBalance_min_available()
	{
		return balance_min_available != null ? balance_min_available : BALANCE_MIN_AVAILABLE;
	}

	// -----------------------------------------------------------------------

	public static void setFavorite_symbols(String favorite_symbols)
	{
		Config.favorite_symbols = favorite_symbols;
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

	public static void setPrice_increment(Double price_increment)
	{
		Config.price_increment = price_increment;
	}

	public static void setPrice_increment(String price_increment)
	{
		Config.price_increment = Double.valueOf(price_increment);
	}

	public static void setStoploss_increment(Double stoploss_increment)
	{
		Config.stoploss_increment = stoploss_increment;
	}

	public static void setStoploss_increment(String stoploss_increment)
	{
		Config.stoploss_increment = Double.valueOf(stoploss_increment);
	}

	public static void setCoins_increment(Double coins_increment)
	{
		Config.coins_increment = coins_increment;
	}

	public static void setCoins_increment(String coins_increment)
	{
		Config.coins_increment = Double.valueOf(coins_increment);
	}

	public static void setTakeprofit(Double takeprofit)
	{
		Config.takeprofit = takeprofit;
	}

	public static void setTakeprofit(String takeprofit)
	{
		Config.takeprofit = Double.valueOf(takeprofit);
	}

	public static void setBalance_start_position(Double balance_start_position)
	{
		Config.balance_start_position = balance_start_position;
	}

	public static void setBalance_start_position(String balance_start_position)
	{
		Config.balance_start_position = Double.valueOf(balance_start_position);
	}

	public static void setBalance_min_available(Double balance_min_available)
	{
		Config.balance_min_available = balance_min_available;
	}

	public static void setBalance_min_available(String balance_min_available)
	{
		Config.balance_min_available = Double.valueOf(balance_min_available);
	}

	// -----------------------------------------------------------------------

	public static void save() throws FileNotFoundException, IOException
	{
		File file = new File(Constants.DEFAULT_USER_FOLDER, Constants.PROPERTIES_FILENAME);
		try (OutputStream output = new FileOutputStream(file))
		{
			Properties prop = new Properties();

			prop.setProperty("favorite_symbols", favorite_symbols);
			prop.setProperty("leverage", String.valueOf(leverage));
			prop.setProperty("iterations", String.valueOf(iterations));
			prop.setProperty("price_increment", String.valueOf(price_increment));
			prop.setProperty("stoploss_increment", String.valueOf(stoploss_increment));
			prop.setProperty("coins_increment", String.valueOf(coins_increment));
			prop.setProperty("takeprofit", String.valueOf(takeprofit));
			prop.setProperty("balance_start_position", String.valueOf(balance_start_position));
			prop.setProperty("balance_min_available", String.valueOf(balance_min_available));

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
					favorite_symbols = prop.getProperty("favorite_symbols");
				if (prop.containsKey("leverage"))
					leverage = Integer.valueOf(prop.getProperty("leverage"));
				if (prop.containsKey("iterations"))
					iterations = Integer.valueOf(prop.getProperty("iterations"));
				if (prop.containsKey("price_increment"))
					price_increment = Double.valueOf(prop.getProperty("price_increment"));
				if (prop.containsKey("stoploss_increment"))
					stoploss_increment = Double.valueOf(prop.getProperty("stoploss_increment"));
				if (prop.containsKey("coins_increment"))
					coins_increment = Double.valueOf(prop.getProperty("coins_increment"));
				if (prop.containsKey("takeprofit"))
					takeprofit = Double.valueOf(prop.getProperty("takeprofit"));
				if (prop.containsKey("balance_start_position"))
					balance_start_position = Double.valueOf(prop.getProperty("balance_start_position"));
				if (prop.containsKey("balance_min_available"))
					balance_min_available = Double.valueOf(prop.getProperty("balance_min_available"));
			}
		}
	}

}
