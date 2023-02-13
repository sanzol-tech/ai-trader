package technicals.indicators.volatility;

import technicals.config.Labels;
import technicals.indicators.ma.SimpleMovingAverage;
import technicals.model.TechCandle;
import technicals.model.indicators.BollingerEntry;
import technicals.model.indicators.IndicatorEntry;

/**
 * Bollinger bands
 */
public class Bollinger
{
	public static BollingerEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 20, 2);
	}

	public static BollingerEntry[] calculate(TechCandle[] candles, int periods, int mult)
	{
		if (candles.length <= periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		BollingerEntry[] bollinger = new BollingerEntry[len];

		IndicatorEntry[] smaEntries = SimpleMovingAverage.calculate(candles, periods);
		IndicatorEntry[] sdEntries = StandardDeviation.calculate(candles, periods);

		for (int i = 0; i < len; i++)
		{
			bollinger[i] = new BollingerEntry(candles[i + periods - 1]);

			bollinger[i].setStandardDeviation(sdEntries[i].getValue());
			bollinger[i].setMiddleBand(smaEntries[i].getValue());

			bollinger[i].setUpperBand(smaEntries[i].getValue() + mult * sdEntries[i].getValue());
			bollinger[i].setLowerBand(smaEntries[i].getValue() - mult * sdEntries[i].getValue());
		}

		return bollinger;
	}

}
