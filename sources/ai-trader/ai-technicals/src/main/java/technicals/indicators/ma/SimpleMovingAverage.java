package technicals.indicators.ma;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;
import technicals.util.CandleUtils;
import technicals.util.DoubleArrayUtils;

/**
 * SMA - Simple Moving Average
 */
public class SimpleMovingAverage
{

	public static double[] calculate(double[] values, int periods)
	{
		if (values.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = values.length - periods + 1;
		double[] results = new double[len];

		for (int i = 0; i < len; i++)
		{
			results[i] = DoubleArrayUtils.avg(values, i, i + periods - 1);
		}

		return results;
	}

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
			smaEntries[i].setValue(CandleUtils.avgPrice(candles, i, i + periods - 1));
		}

		return smaEntries;
	}

}
