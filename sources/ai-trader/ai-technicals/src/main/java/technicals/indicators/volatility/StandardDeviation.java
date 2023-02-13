package technicals.indicators.volatility;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;

/**
 * STDEV - Standard Deviation
 */
public class StandardDeviation
{
	public static IndicatorEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 5);
	}

	public static IndicatorEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		IndicatorEntry[] sdEntries = new IndicatorEntry[len];

		for (int i = 0; i < len; i++)
		{
			sdEntries[i] = new IndicatorEntry(candles[i + periods - 1]);
			sdEntries[i].setValue(standardDeviation(candles, i, periods));
		}

		return sdEntries;
	}

	private static double standardDeviation(TechCandle[] candles, int startIndex, int periods)
	{
		double priceSum = 0.0;
		double stdevSum = 0.0;
		int lenght = periods;

		for (int i = startIndex; i < startIndex + periods; i++)
		{
			priceSum += candles[i].getDefaultPrice();
		}
		double mean = priceSum / lenght;

		for (int i = startIndex; i < startIndex + periods; i++)
		{
			stdevSum += Math.pow(candles[i].getDefaultPrice() - mean, 2);
		}

		return Math.sqrt(stdevSum / lenght);
	}

}
