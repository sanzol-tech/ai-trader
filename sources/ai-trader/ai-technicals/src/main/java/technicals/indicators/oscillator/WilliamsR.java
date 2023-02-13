package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.oscillator.WilliamsREntry;
import technicals.util.CandleUtils;

/**
 * Williams %R
 */
public class WilliamsR
{

	public static WilliamsREntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 14);
	}

	public static WilliamsREntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		WilliamsREntry[] williamsREntries = new WilliamsREntry[len];

		for (int i = 0; i < len; i++)
		{
			williamsREntries[i] = new WilliamsREntry(candles[i + periods - 1]);

			double highestHigh = CandleUtils.highestHigh(candles, i, i + periods - 1);
			williamsREntries[i].setHighestHigh(highestHigh);

			double lowestLow = CandleUtils.lowestLow(candles, i, i + periods - 1);
			williamsREntries[i].setLowestLow(lowestLow);

			double closePrice = candles[i + periods - 1].getClosePrice();
			double r = (highestHigh - closePrice) / (highestHigh - lowestLow) * -100;
			williamsREntries[i].setR(r);
		}

		return williamsREntries;
	}

}
