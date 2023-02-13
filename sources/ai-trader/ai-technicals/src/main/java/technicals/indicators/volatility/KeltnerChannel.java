package technicals.indicators.volatility;

import technicals.config.Labels;
import technicals.indicators.ma.ExponentialMovingAverage;
import technicals.model.TechCandle;
import technicals.model.indicators.AtrEntry;
import technicals.model.indicators.IndicatorEntry;
import technicals.model.indicators.KeltnerChannelEntry;

public class KeltnerChannel
{

	public static KeltnerChannelEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 20, 1);
	}

	public static KeltnerChannelEntry[] calculate(TechCandle[] candles, int periods, double multiplier)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		IndicatorEntry[] ema = ExponentialMovingAverage.calculate(candles, periods);
		AtrEntry[] atr = AverageTrueRange.calculate(candles, periods);

		KeltnerChannelEntry[] kcEntries = new KeltnerChannelEntry[ema.length];

		for (int i = 0; i < ema.length; i++)
		{
			int atrIdx = i + atr.length - ema.length;

			kcEntries[i] = new KeltnerChannelEntry(ema[i]);
			kcEntries[i].setAtr(atr[atrIdx].getAtr());
			kcEntries[i].setUpperBand(ema[i].getValue() + atr[atrIdx].getAtr() * multiplier);
			kcEntries[i].setLowerBand(ema[i].getValue() - atr[atrIdx].getAtr() * multiplier);
			kcEntries[i].setMiddleBand(ema[i].getValue());
		}

		return kcEntries;
	}

}
