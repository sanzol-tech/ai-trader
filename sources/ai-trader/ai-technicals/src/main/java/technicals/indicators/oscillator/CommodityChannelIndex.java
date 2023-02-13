package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;

/**
 * CCI - Commodity Channel Index
 */
public class CommodityChannelIndex
{

	public static IndicatorEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 20);
	}

	public static IndicatorEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length <= periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		IndicatorEntry[] cciEntries = new IndicatorEntry[len];

		for (int i = 0; i < len; i++)
		{
			cciEntries[i] = new IndicatorEntry(candles[i + periods - 1]);
			TechCandle curr = candles[i + periods - 1];

			double typicalPrice = curr.getHLC3();

			// Calc sma
			double maSum = 0;
			for (int x = 0; x < periods; x++)
			{
				maSum += candles[i + x].getHLC3();
			}
			double ma = maSum / periods;

			// Calc mean deviation
			double devSum = 0;
			for (int x = i; x < i + periods; x++)
			{
				devSum += Math.abs(candles[x].getHLC3() - ma);
			}
			double meanDeviation = devSum / periods;

			// Calc cci
			double cci = (meanDeviation > 0) ? (typicalPrice - ma) / (0.015 * meanDeviation) : 0;

			cciEntries[i].setValue(cci);
		}

		return cciEntries;
	}

}
