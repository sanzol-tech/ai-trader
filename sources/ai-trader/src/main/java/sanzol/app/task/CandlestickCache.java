package sanzol.app.task;

import java.util.ArrayList;
import java.util.List;

import org.decimal4j.util.DoubleRounder;

import com.binance.client.SubscriptionClient;
import com.binance.client.model.enums.CandlestickInterval;

import sanzol.app.listener.BtcChangeListener;

public final class CandlestickCache
{
	private static SubscriptionClient client = SubscriptionClient.create();

	private static double btcChange30m;

	public static double getBtcChange30m()
	{
		return btcChange30m;
	}

	public static void start()
	{
		client.subscribeCandlestickEvent("btcusdt", CandlestickInterval.HALF_HOURLY, ((event) -> {
			double open = event.getOpen().doubleValue();
			double close = event.getClose().doubleValue();

			double diff;
			if(open < close)
				diff = (close - open) / open;
			else
				diff = -((open - close) / open);

			btcChange30m = DoubleRounder.round(diff * 100, 2);

			notifyAllLogObservers();

		}), null);

	}
	
	public static void stop()
	{
		client.unsubscribeAll();
	}

	// ------------------------------------------------------------------------

	private static List<BtcChangeListener> observers = new ArrayList<BtcChangeListener>();

	public static void attachRefreshObserver(BtcChangeListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(BtcChangeListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers()
	{
		for (BtcChangeListener observer : observers)
		{
			observer.onBtcChangeUpdate();
		}
	}

	// ------------------------------------------------------------------------

	public static void main(String[] args) throws InterruptedException
	{
		System.out.println("...");
	}

}
