package technicals.indicators.ma;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;
import technicals.util.DoubleArrayUtils;
import technicals.util.CandleUtils;

/**
 * EMA - Exponential Moving Average
 */
public class ExponentialMovingAverage
{
	
	public static double[] calculate(double[] values, int periods)
	{
		if (values.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = values.length - periods + 1;
		double[] emaEntries = new double[len];

		double smoothing = 2d / (periods + 1);

		for (int i = 0; i < len; i++)
		{
			if (i == 0)
			{
				double[] slice = DoubleArrayUtils.slice(values, 0, periods - 1);
				double[] smaEntries = SimpleMovingAverage.calculate(slice, periods);
				double smaEntry = smaEntries[smaEntries.length - 1];
				emaEntries[i] = smaEntry;
			}
			else
			{
				emaEntries[i] = values[i + periods - 1] * smoothing + emaEntries[i - 1] * (1 - smoothing);
			}
		}

		return emaEntries;
	}	

	public static IndicatorEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		IndicatorEntry[] emaEntries = new IndicatorEntry[len];

		double smoothing = 2d / (periods + 1);

		for (int i = 0; i < len; i++)
		{
			emaEntries[i] = new IndicatorEntry(candles[i + periods - 1]);

			if (i == 0)
			{
				TechCandle[] slice = CandleUtils.slice(candles, 0, periods - 1);
				IndicatorEntry[] smaEntries = SimpleMovingAverage.calculate(slice, periods);
				double smaEntry = smaEntries[smaEntries.length - 1].getValue();
				emaEntries[i].setValue(smaEntry);
			}
			else
			{
				double emaEntry = candles[i + periods - 1].getDefaultPrice() * smoothing + emaEntries[i - 1].getValue() * (1 - smoothing);
				emaEntries[i].setValue(emaEntry);
			}
		}

		return emaEntries;
	}

}
