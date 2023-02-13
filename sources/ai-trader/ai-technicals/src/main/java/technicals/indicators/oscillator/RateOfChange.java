package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;

/**
 * ROC - Rate Of Change
 */
public class RateOfChange
{

	public static IndicatorEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 9);
	}

	public static IndicatorEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods + 1)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods;
		IndicatorEntry[] rocEntries = new IndicatorEntry[len];

		for (int i = 0; i < len; i++)
		{
			rocEntries[i] = new IndicatorEntry(candles[i + periods]);

			double prevPrice = candles[i].getDefaultPrice();
			double price = candles[i + periods].getDefaultPrice();
			double roc = 100 * ((price - prevPrice) / prevPrice);

			rocEntries[i].setValue(roc);
		}

		return rocEntries;
	}

}
