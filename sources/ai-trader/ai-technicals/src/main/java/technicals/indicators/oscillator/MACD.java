package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.indicators.ma.ExponentialMovingAverage;
import technicals.model.TechCandle;
import technicals.model.oscillator.MACDEntry;
import technicals.util.CandleUtils;

/**
 * MACD - Moving Average Convergence / Divergence
 */
public class MACD
{
	public static MACDEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 12, 26, 9);
	}

	public static MACDEntry[] calculate(TechCandle[] candles, int fastPeriods, int slowPeriods, int signalPeriods)
	{
		if (candles.length < slowPeriods + signalPeriods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		if (fastPeriods >= slowPeriods)
		{
			throw new IllegalArgumentException("'slowPeriods' must be greater than 'fastPeriods'");
		}

		// ---- ema fast & slow -------------------
		double[] prices = CandleUtils.toDoubleArray(candles);
		double[] emaFast = ExponentialMovingAverage.calculate(prices, fastPeriods);
		double[] emaSlow = ExponentialMovingAverage.calculate(prices, slowPeriods);
		int baseFast = emaFast.length - emaSlow.length;

		// ---- macd ------------------------------
		double[] macd = new double[emaSlow.length];
		for (int i = 0; i < emaSlow.length; i++)
		{
			macd[i] = emaFast[i + baseFast] - emaSlow[i];
		}

		// ---- Signal ----------------------------
		double[] signals = ExponentialMovingAverage.calculate(macd, signalPeriods);
		int baseMacd = macd.length - signals.length;
		int baseCandles = candles.length - signals.length;

		// ---- Create response -------------------
		MACDEntry[] macdEntries = new MACDEntry[signals.length];

		// ---- Historam --------------------------
		for (int i = 0; i < signals.length; i++)
		{
			double histogram = macd[i + baseMacd] - signals[i];
			macdEntries[i] = new MACDEntry(candles[i + baseCandles], emaFast[i + baseFast], emaSlow[i + baseMacd], macd[i + baseMacd], signals[i], histogram);
		}

		return macdEntries;
	}

}
