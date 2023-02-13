package technicals.indicators.ma;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;

/**
 * VWMA - Volume-Weighted Moving Average
 */
public class VWMovingAverage
{

	public static IndicatorEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		IndicatorEntry[] smaEntries = new IndicatorEntry[len];

		for (int i = 0; i < len; i++)
		{
			smaEntries[i] = new IndicatorEntry(candles[i + periods - 1]);
			smaEntries[i].setValue(calcAvg(candles, i, periods));
		}

		return smaEntries;
	}

	private static double calcAvg(TechCandle[] candles, int startIndex, int periods)
	{
		double sum = 0;
		double vol = 0;

		for (int i = 0; i < periods; i++)
		{
			sum += candles[startIndex + i].getDefaultPrice() * candles[startIndex + i].getVolume();
			vol += candles[startIndex + i].getVolume();
		}

		return sum / vol;
	}
}
