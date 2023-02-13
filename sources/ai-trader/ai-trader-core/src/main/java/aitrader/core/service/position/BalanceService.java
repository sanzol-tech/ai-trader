package aitrader.core.service.position;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import aitrader.core.config.CoreConfig;
import aitrader.core.config.CoreLog;
import aitrader.core.config.PrivateConfig;
import aitrader.util.observable.Handler;
import binance.futures.impl.SignedClient;
import binance.futures.model.AccountBalance;

public final class BalanceService
{
	private static final long DEFAULT_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(30);

	private static boolean isStarted = false;

	private static AccountBalance accountBalance = null;

	public static AccountBalance getAccountBalance()
	{
		return accountBalance;
	}

	public static BigDecimal getBalance()
	{
		if (accountBalance == null)
			return null;

		return accountBalance.getBalance();
	}

	public static BigDecimal getWithdrawAvailable()
	{
		if (accountBalance == null)
			return null;

		return accountBalance.getWithdrawAvailable();
	}

	public static boolean isAvailable(BigDecimal qty, BigDecimal price)
	{
		return isAvailable(qty.multiply(price));
	}

	public static boolean isAvailable(BigDecimal usd)
	{
		return isAvailable(usd.doubleValue());
	}

	public static boolean isAvailable(double usd)
	{
		double leverage = CoreConfig.getLeverage();
		double minAvailable = CoreConfig.getBalanceMinAvailable();
		double balance = accountBalance.getBalance().doubleValue();
		double withdrawAvailable = accountBalance.getWithdrawAvailable().doubleValue();

		return (withdrawAvailable - (usd / leverage) >= balance * minAvailable);
	}
	
	// --------------------------------------------------------------------

	public synchronized static void checkBalance()
	{
		try
		{
			List<AccountBalance> lst = SignedClient.create(PrivateConfig.getApiKey(), PrivateConfig.getSecretKey()).getBalance();

			for (AccountBalance e : lst)
			{
				if (e.getAsset().equals(CoreConfig.getDefaultSymbolRight()))
				{
					accountBalance = e;
				}
			}
			
			notifyAllObservers();			
		}
		catch (Exception e)
		{
			CoreLog.error(e);
		}

	}

	// --------------------------------------------------------------------

	private static Timer timer1;

	public static void start()
	{
		if (isStarted)
		{
			return;
		}

		CoreLog.info("BalanceService - Start");

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				checkBalance();
			}
		};
		timer1 = new Timer("checkBalances");
		timer1.schedule(task, 0, DEFAULT_PERIOD_MILLIS);

		isStarted = true;
	}

	public static void close()
	{
		if (isStarted && timer1 != null)
		{
			timer1.cancel();
			isStarted = false;
		}

		accountBalance = null;

		notifyAllObservers();
	}

	// --------------------------------------------------------------------

	private static List<Handler<Void>> observers = new ArrayList<Handler<Void>>();

	public static void attachObserver(Handler<Void> observer)
	{
		observers.add(observer);
	}

	public static void deattachObserver(Handler<Void> observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllObservers()
	{
		for (Handler<Void> observer : observers)
		{
			observer.handle(null);
		}
	}

}
