package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;

/**
 * AO - Awesome Oscillator
 */
public class AwesomeOscillator
{

	public static IndicatorEntry[] calculate(TechCandle[] candles)
	{
		final int periodsFast = 5;
		final int periodsSlow = 34;

		if (candles.length < periodsSlow)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periodsSlow + 1;
		IndicatorEntry[] aoEntries = new IndicatorEntry[len];

		for (int i = 0; i < len; i++)
		{
			aoEntries[i] = new IndicatorEntry(candles[i + periodsSlow - 1]);

			double smaFast = avgHL2(candles, periodsFast, i + periodsSlow - 1);
			double smaSlow = avgHL2(candles, periodsSlow, i + periodsSlow - 1);
			double ao = smaFast - smaSlow;

			aoEntries[i].setValue(ao);
		}

		return aoEntries;
	}

	public static double avgHL2(TechCandle[] candles, int periods, int index)
	{
		double sum = 0;
		for (int i = 0; i < periods; i++)
		{
			sum += candles[index - i].getHL2();
		}
		return sum / periods;
	}

}
