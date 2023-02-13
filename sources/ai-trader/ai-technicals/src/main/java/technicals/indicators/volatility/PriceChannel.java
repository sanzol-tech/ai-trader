package technicals.indicators.volatility;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.PriceChannelEntry;
import technicals.util.CandleUtils;

/**
 * Price Channel
 */
public class PriceChannel
{

	public static PriceChannelEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 20);
	}

	public static PriceChannelEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		PriceChannelEntry[] priceChannelEntries = new PriceChannelEntry[len];

		for (int i = 0; i < len; i++)
		{
			priceChannelEntries[i] = new PriceChannelEntry(candles[i + periods - 1]);

			double highestHigh = CandleUtils.highestHigh(candles, i, i + periods - 1);
			priceChannelEntries[i].setUpperChannel(highestHigh);

			double lowestLow = CandleUtils.lowestLow(candles, i, i + periods - 1);
			priceChannelEntries[i].setLowerChannel(lowestLow);

			double centerLine = (highestHigh + lowestLow) / 2;
			priceChannelEntries[i].setCenterLine(centerLine);
		}

		return priceChannelEntries;
	}

}
