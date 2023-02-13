package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;

/**
 * MOM - Momentum
 */
public class Momentum
{
	public static IndicatorEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 10, 0);
	}

	public static IndicatorEntry[] calculate(TechCandle[] candles, int periods)
	{
		return calculate(candles, periods, 0);
	}

	public static IndicatorEntry[] calculate(TechCandle[] candles, int periods, int version)
	{
		if (candles.length <= periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods;
		IndicatorEntry[] momEntries = new IndicatorEntry[len];

		for (int i = 0; i < len; i++)
		{
			momEntries[i] = new IndicatorEntry(candles[i + periods]);
			double previousPrice = candles[i].getDefaultPrice();
			double currentPrice = candles[i + periods].getDefaultPrice();

			double mom;
			if (version == 0)
				mom = currentPrice - previousPrice;
			else
				mom = (currentPrice / previousPrice) * 100;

			momEntries[i].setValue(mom);
		}

		return momEntries;
	}

}