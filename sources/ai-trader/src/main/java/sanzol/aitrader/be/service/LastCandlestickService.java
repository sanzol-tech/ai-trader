package sanzol.aitrader.be.service;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import api.client.futures.model.enums.IntervalType;
import api.client.impl.async.WsCandlestick;
import api.client.impl.config.ApiConfig;
import api.client.model.event.CandlestickEvent;
import sanzol.util.price.PriceUtil;

public class LastCandlestickService
{
	private static WsCandlestick wsCandlestick;

	private static BigDecimal priceChangePercent = BigDecimal.ZERO;
	private static BigDecimal priceMove = BigDecimal.ZERO;

	public static BigDecimal getPriceChangePercent()
	{
		return priceChangePercent;
	}

	public static BigDecimal getPriceMove()
	{
		return priceMove;
	}

	// ------------------------------------------------------------------------
	
	private static List<LastCandlestickListener> observers = new ArrayList<LastCandlestickListener>();

	public static void attachRefreshObserver(LastCandlestickListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(LastCandlestickListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers()
	{
		for (LastCandlestickListener observer : observers)
		{
			observer.onBtcChangeUpdate();
		}
	}

	// ------------------------------------------------------------------------

	public static void onMessage(CandlestickEvent event)
	{
		BigDecimal openPrice = event.getKline().getOpen();
		BigDecimal highPrice = event.getKline().getHigh();
		BigDecimal lowPrice = event.getKline().getLow();
		BigDecimal closePrice = event.getKline().getClose();

		priceChangePercent = PriceUtil.priceChange(openPrice, closePrice, true);
		priceMove = PriceUtil.priceChange(lowPrice, highPrice, true);
		
		notifyAllLogObservers();
	}

	// ------------------------------------------------------------------------

	public static void start(String symbolPair)
	{
		start(symbolPair, IntervalType._30m);
	}

	public static void start(String symbolPair, IntervalType intervalType)
	{
		wsCandlestick = WsCandlestick.create(symbolPair, intervalType, (event) -> {
			onMessage(event);
		});
		wsCandlestick.connect();
	}

	public static void main(String[] args) throws URISyntaxException
	{
		ApiConfig.setFutures();
		start("btcusdt", IntervalType._30m);
	}

}
