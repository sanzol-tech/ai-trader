package sanzol.app.task;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import com.binance.client.exception.BinanceApiException;

import sanzol.app.config.Constants;
import sanzol.app.listener.SignalListener;
import sanzol.app.model.SignalEntry;
import sanzol.app.model.SymbolInfo;
import sanzol.app.service.OBookService;
import sanzol.app.service.Symbol;
import sanzol.app.util.PriceUtil;

public final class SignalService
{
	private static boolean isStarted = false;

	private static List<SignalEntry> lstShocks;
	private static List<SignalEntry> lstShockStatus;

	private static String modified = "n/a";
	private static String errorMessage = "";

	public static List<SignalEntry> getLstShocks()
	{
		return lstShocks;
	}

	public static List<SignalEntry> getLstShockStatus()
	{
		return lstShockStatus;
	}

	public static String getModified()
	{
		return modified;
	}

	public static String getErrorMessage()
	{
		return errorMessage;
	}

	// -----------------------------------------------------------------------

	public static void start()
	{
		if (!isStarted)
		{
			loadShocks();

			Timer timer = new Timer("SignalService");
			timer.schedule(new MyTask(), 3000, 2000);

			isStarted = true;
		}
	}

	public static class MyTask extends TimerTask
	{
		@Override
		public void run()
		{
			verifyShocks();
			notifyAllLogObservers();
		}
	}

	// -----------------------------------------------------------------------

	public static void loadShocks()
	{
		errorMessage = "";
		lstShocks = new ArrayList<SignalEntry>();
		try
		{
			Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, Constants.SHOCKPOINTS_FILENAME);
			List<String> lines = Files.readAllLines(path);
			for (String line : lines)
			{
				String[] fields = line.split(";");
				Symbol coin = Symbol.getInstance(Symbol.getFullSymbol(fields[0]));
				BigDecimal shShock = new BigDecimal(fields[1]);
				BigDecimal lgShock = new BigDecimal(fields[2]);

				lstShocks.add(new SignalEntry(coin, shShock, lgShock));
			}

			modified = (new SimpleDateFormat("dd/MM HH:mm")).format(new Date(path.toFile().lastModified()));
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
		}
	}

	public static void searchShocks(boolean onlyFavorites, boolean onlyBetters)
	{
		errorMessage = "";
		lstShocks = new ArrayList<SignalEntry>();
		try
		{
			List<SymbolInfo> lstSymbolsInfo = Symbol.getLstSymbolsInfo(onlyFavorites, onlyBetters);

			int count = 0;
			for (SymbolInfo symbolInfo : lstSymbolsInfo)
			{
				try
				{
					Symbol coin = symbolInfo.getSymbol();
					OBookService obService = OBookService.getInstance(coin).request().calc();

					BigDecimal distShLg = PriceUtil.priceDistDown(obService.getShortPriceFixed(), obService.getLongPriceFixed(), true);

					if ((distShLg.doubleValue() < 1.5 || distShLg.doubleValue() > 8.0))
					{
						continue;
					}

					lstShocks.add(new SignalEntry(coin, obService.getShortPriceFixed(), obService.getLongPriceFixed()));

					// ------- TODO --------
					count++;
					if (count > 20)	{
						count = 0;
						Thread.sleep(5000);
					} else {
						Thread.sleep(300);
					}
					// ------- TODO --------

				}
				catch (BinanceApiException ex)
				{
					System.err.println("ERR: " + symbolInfo.getSymbolName() + " : " +  ex.getMessage());
				}
			}
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
			System.err.println(e.getMessage());
		}
	}

	// -----------------------------------------------------------------------

	private static void verifyShocks()
	{
		errorMessage = "";

		if (lstShocks == null || lstShocks.isEmpty())
		{
			return;
		}
		List<SignalEntry> list = new ArrayList<SignalEntry>();
		list.addAll(lstShocks);

		try
		{
			for (SignalEntry entry : list)
			{
				BigDecimal price = PriceService.getLastPrice(entry.getSymbol());
				if (price == null || price.equals(BigDecimal.valueOf(-1)))
				{
					continue;
				}

				entry.setPrice(price);

				BigDecimal distShLg = PriceUtil.priceDistDown(entry.getShShock(), entry.getLgShock(), true);
				BigDecimal distShort = PriceUtil.priceDistUp(price, entry.getShShock(), true);
				BigDecimal distLong = PriceUtil.priceDistDown(price, entry.getLgShock(), true);

				String action = "";
				if ((distShLg.doubleValue() >= 1.5 && distShLg.doubleValue() <= 8.0))
				{
					if ((distShort.doubleValue() <= 0.3 && distShort.doubleValue() > -3.0))
					{
						action += "SHORT";
					}
					else if ((distLong.doubleValue() <= 0.3 && distLong.doubleValue() > -3.0))
					{
						action += "LONG";
					}

					if (!action.isEmpty())
					{
						if ((distShort.doubleValue() <= -0.2 || distLong.doubleValue() <= -0.2))
						{
							action += " OVFL";
						}
						else if ((distShort.doubleValue() <= 0.1 || distLong.doubleValue() <= 0.1))
						{
							action += " NOW";
						}
						else if ((distShort.doubleValue() <= 0.2 || distLong.doubleValue() <= 0.2))
						{
							action += " SOON";
						}
					}
				}

				entry.setDistShLg(distShLg);
				entry.setDistShort(distShort);
				entry.setDistLong(distLong);
				entry.setAction(action);
			}
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
		}

		Comparator<SignalEntry> comparator = Comparator.comparing(SignalEntry::bestDistance);
		Collections.sort(list, comparator);

		lstShockStatus = list;
	}

	// -----------------------------------------------------------------------

	public static String toStringShocks()
	{
		if (lstShocks == null || lstShocks.isEmpty())
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (SignalEntry entry : lstShocks)
		{
			sb.append(entry.toString());
		}
		return sb.toString();
	}

	public static boolean saveShocks()
	{
		try
		{
			if (lstShocks != null & !lstShocks.isEmpty())
			{
				String text = "";
				for (SignalEntry entry : lstShocks)
				{
					text += entry.getSymbol().getNameLeft() + ";" + entry.getShShock() + ";" + entry.getLgShock() + "\n";
				}

				Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, Constants.SHOCKPOINTS_FILENAME);
				Files.write(path, text.getBytes(StandardCharsets.UTF_8));

				return true;
			}
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}

		return false;
	}

	public static String toStringStatus()
	{
		if (lstShockStatus == null || lstShockStatus.isEmpty())
		{
			return "";
		}

		String text = String.format("\n%-8s %14s %14s %14s %9s %9s %9s\n", "SYMBOL", "SHORT", "PRICE", "LONG", "DIST%", "SH%", "LG%");
		text += StringUtils.repeat("-", 102) + "\n";

		for (SignalEntry entry : lstShockStatus)
		{
			text += entry.toStringAll();
		}

		return text;
	}

	public static List<SignalEntry> getShockStatus()
	{
		List<SignalEntry> lst = new ArrayList<SignalEntry>();

		if (lstShockStatus != null && !lstShockStatus.isEmpty())
		{
			for (SignalEntry entry : lstShockStatus)
			{
				if (entry.getAction() != null && !entry.getAction().isEmpty())
				{
					lst.add(entry);
				}
			}
		}

		return lst;
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

		Thread.sleep(2000);

		/*
		loadShocks();
		System.out.println("");
		System.out.println(toStringShocks());
		*/

		searchShocks(true, true); 
		System.out.println(""); 
		System.out.println(toStringShocks());
		saveShocks();

		verifyShocks();
		System.out.println("");
		System.out.println(toStringStatus());

	}

}
