package technicals.test;

import java.util.Arrays;
import java.util.List;

import exchanges.binance.BinanceApiClient;
import exchanges.binance.BinanceCandle;
import exchanges.binance.BinanceUtils;
import exchanges.binance.BinanceIntervalType;
import technicals.indicators.ma.ExponentialMovingAverage;
import technicals.indicators.ma.HullMovingAverage;
import technicals.indicators.ma.SimpleMovingAverage;
import technicals.indicators.ma.VWMovingAverage;
import technicals.indicators.ma.WeightedMovingAverage;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;

public class MovingAverage_
{

	public static void main(String[] args) throws Exception
	{
		List<BinanceCandle> lstBinanceCandles = BinanceApiClient.getKlines("BTCUSDT", BinanceIntervalType._1d, 30);
		TechCandle[] candles = BinanceUtils.toCandleArray(lstBinanceCandles);

		IndicatorEntry[] smaEntries = SimpleMovingAverage.calculate(candles, 9);
		IndicatorEntry[] wmaEntries = WeightedMovingAverage.calculate(candles, 9);
		IndicatorEntry[] emaEntries = ExponentialMovingAverage.calculate(candles, 9);
		IndicatorEntry[] vwmaEntries = VWMovingAverage.calculate(candles, 9);
		IndicatorEntry[] hmaEntries = HullMovingAverage.calculate(candles, 9);

		System.out.println("sma");
		Arrays.stream(smaEntries).forEach(s -> System.out.println(s));
		System.out.println("wma");
		Arrays.stream(wmaEntries).forEach(s -> System.out.println(s));
		System.out.println("ema");
		Arrays.stream(emaEntries).forEach(s -> System.out.println(s));
		System.out.println("vwma");
		Arrays.stream(vwmaEntries).forEach(s -> System.out.println(s));
		System.out.println("hma");
		Arrays.stream(hmaEntries).forEach(s -> System.out.println(s));
	}

}
