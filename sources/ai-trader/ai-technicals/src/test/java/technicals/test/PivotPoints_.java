package technicals.test;

import java.util.List;

import exchanges.binance.BinanceApiClient;
import exchanges.binance.BinanceCandle;
import exchanges.binance.BinanceUtils;
import exchanges.binance.BinanceIntervalType;
import technicals.indicators.pp.CamarillaPivotPoints;
import technicals.indicators.pp.DemarksPivotPoints;
import technicals.indicators.pp.FibonacciPivotPoints;
import technicals.indicators.pp.StandardPivotPoints;
import technicals.indicators.pp.WoodiePivotPoints;
import technicals.model.TechCandle;

public class PivotPoints_
{

	public static void main(String[] args) throws Exception
	{
		List<BinanceCandle> lstBinanceCandles = BinanceApiClient.getKlines("BTCUSDT", BinanceIntervalType._1w, 2);
		TechCandle[] candles = BinanceUtils.toCandleArray(lstBinanceCandles);
		TechCandle candle = candles[candles.length - 2];

		System.out.println("Standard\n" + new StandardPivotPoints().calculate(candle));
		System.out.println("Fibonacci\n" + new FibonacciPivotPoints().calculate(candle));
		System.out.println("Camarilla\n" + new CamarillaPivotPoints().calculate(candle));
		System.out.println("Woodie\n" + new WoodiePivotPoints().calculate(candle));
		System.out.println("Demarks\n" + new DemarksPivotPoints().calculate(candle));
	}

}
