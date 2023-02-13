package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.oscillator.RsiEntry;
import technicals.model.oscillator.StochRsiEntry;

/**
 * StochRSI - Stochastic RSI
 */
public class StochasticRSI
{

	public static StochRsiEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 14, 14, 3, 3);
	}

	public static StochRsiEntry[] calculate(TechCandle[] candles, int periodsRsi, int periodsStoch, int smoothK, int smoothD)
	{
		if (candles.length < periodsRsi + periodsStoch)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		RsiEntry[] rsiEntries = RelativeStrengthIndex.calculate(candles, periodsRsi);

		int len = rsiEntries.length - periodsStoch + 1;
		StochRsiEntry[] stochasticEntries = new StochRsiEntry[len];

		for (int i = 0; i < len; i++)
		{
			stochasticEntries[i] = new StochRsiEntry(rsiEntries[i + periodsStoch - 1]);

			double rsi = rsiEntries[i + periodsStoch - 1].getRsi();
			double maxRsi = maxRsi(rsiEntries, i, i + periodsStoch - 1);
			double minRsi = minRsi(rsiEntries, i, i + periodsStoch - 1);

			stochasticEntries[i].setRsi(rsi);

			double k1 = 100 * (rsi - minRsi) / (maxRsi - minRsi);
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

	private static double avg(StochRsiEntry[] entries, FieldType field, int index, int periods)
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

	private static double maxRsi(RsiEntry[] entries, int startIndex, int endIndex)
	{
		double value = entries[startIndex].getRsi();

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (entries[i].getRsi() > value)
			{
				value = entries[i].getRsi();
			}
		}

		return value;
	}

	private static double minRsi(RsiEntry[] entries, int startIndex, int endIndex)
	{
		double value = entries[startIndex].getRsi();

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (entries[i].getRsi() < value)
			{
				value = entries[i].getRsi();
			}
		}

		return value;
	}

}
