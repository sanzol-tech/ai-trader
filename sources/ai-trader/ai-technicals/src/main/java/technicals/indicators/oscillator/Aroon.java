package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.oscillator.AroonEntry;
import technicals.util.CandleUtils;

/**
 * Aroon Oscillator
 */
public class Aroon
{

	public static AroonEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 14);
	}

	public static AroonEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		AroonEntry[] aroonEntries = new AroonEntry[len];

		for (int i = 0; i < len; i++)
		{
			aroonEntries[i] = new AroonEntry(candles[i + periods - 1]);

			// aroon Up
			int highestHighIndex = CandleUtils.highestHighIndex(candles, i, i + periods - 1);
			int sinceHigh = (i + periods - 1) - highestHighIndex;
			double aroonUp = 100 * (((double) periods - (double) sinceHigh) / (double) periods);
			aroonEntries[i].setAroonUp(aroonUp);

			// aroon Down
			int lowestLowIndex = CandleUtils.lowestLowIndex(candles, i, i + periods - 1);
			int sinceLow = (i + periods - 1) - lowestLowIndex;
			double aroonDown = 100 * (((double) periods - (double) sinceLow) / (double) periods);
			aroonEntries[i].setAroonDown(aroonDown);

			// aroon Oscillator
			aroonEntries[i].setOscillator(aroonUp - aroonDown);
		}

		return aroonEntries;
	}

}
