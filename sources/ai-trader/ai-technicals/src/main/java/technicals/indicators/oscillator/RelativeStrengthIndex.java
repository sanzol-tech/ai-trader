package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.oscillator.RsiEntry;
import technicals.util.DoubleArrayUtils;

/**
 * RSI - Relative Strength Index
 */
public class RelativeStrengthIndex
{

	public static RsiEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 14);
	}

	public static RsiEntry[] calculate(TechCandle[] candles, int periods)
	{
		RsiEntry[] rsiEntries;

		if (candles.length <= periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		rsiEntries = new RsiEntry[candles.length - periods];
		int idx = 0;

		double[] change = new double[candles.length];
		double[] gain = new double[candles.length];
		double[] loss = new double[candles.length];
		double avgGain;
		double avgLoss;

		for (int i = 1; i < candles.length; i++)
		{
			change[i] = candles[i].getDefaultPrice() - candles[i - 1].getDefaultPrice();

			if (change[i] > 0)
				gain[i] = change[i];
			else if (change[i] < 0)
				loss[i] = change[i] * -1;

			if (i >= periods)
			{
				if (i == periods)
				{
					avgGain = DoubleArrayUtils.avg(gain, 1, periods);
					avgLoss = DoubleArrayUtils.avg(loss, 1, periods);
				}
				else
				{
					avgGain = (rsiEntries[idx - 1].getAvgGain() * (periods - 1) + gain[i]) / periods;
					avgLoss = (rsiEntries[idx - 1].getAvgLoss() * (periods - 1) + loss[i]) / periods;
				}
				double rs = avgGain / avgLoss;
				double rsi = 100 - (100 / (1 + rs));

				rsiEntries[idx] = new RsiEntry(candles[i]);
				rsiEntries[idx].setChange(change[i]);
				rsiEntries[idx].setGain(gain[i]);
				rsiEntries[idx].setLoss(loss[i]);
				rsiEntries[idx].setAvgGain(avgGain);
				rsiEntries[idx].setAvgLoss(avgLoss);
				rsiEntries[idx].setRs(rs);
				rsiEntries[idx].setRsi(rsi);

				idx++;
			}

		}

		return rsiEntries;
	}

}
