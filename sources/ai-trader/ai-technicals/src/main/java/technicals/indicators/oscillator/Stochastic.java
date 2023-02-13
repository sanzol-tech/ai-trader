package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.oscillator.StochasticEntry;
import technicals.util.CandleUtils;

/**
 * Stochastic
 */
public class Stochastic
{

	public static StochasticEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 14, 1, 3);
	}

	public static StochasticEntry[] calculate(TechCandle[] candles, int periods, int smoothK, int smoothD)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		StochasticEntry[] stochasticEntries = new StochasticEntry[len];

		for (int i = 0; i < len; i++)
		{
			stochasticEntries[i] = new StochasticEntry(candles[i + periods - 1]);

			double highestHigh = CandleUtils.highestHigh(candles, i, i + periods - 1);
			stochasticEntries[i].setHighestHigh(highestHigh);

			double lowestLow = CandleUtils.lowestLow(candles, i, i + periods - 1);
			stochasticEntries[i].setLowestLow(lowestLow);

			double k1 = 100 * (candles[i + periods - 1].getClosePrice() - lowestLow) / (highestHigh - lowestLow);
			stochasticEntries[i].setK1(k1);

			double k = avg(stochasticEntries, FieldType.x, i, smoothK);
			stochasticEntries[i].setK(k);

			double d = avg(stochasticEntries, FieldType.k, i, smoothD);
			stochasticEntries[i].setD(d);
		}

		return stochasticEntries;
	}

	private enum FieldType
	{
		x, k
	}

	private static double avg(StochasticEntry[] entries, FieldType field, int index, int periods)
	{
		double sum = 0;
		int count = 0;

		for (int i = 0; i < periods; i++)
		{
			if (index - i < 0)
				break;

			if (field == FieldType.x)
				sum = sum + entries[index - i].getK1();
			else
				sum = sum + entries[index - i].getK();

			count++;
		}

		return sum / count;
	}

}
