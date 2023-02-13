package technicals.indicators.volume;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;
import technicals.util.DoubleArrayUtils;

/**
 * MFI - Money Flow Index
 */
public class MoneyFlowIndex
{

	public static IndicatorEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 14);
	}

	public static IndicatorEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		IndicatorEntry[] mfiEntries = new IndicatorEntry[len];

		double[] moneyFlow = new double[candles.length];
		double[] posMoneyFlow = new double[candles.length];
		double[] negMoneyFlow = new double[candles.length];

		for (int i = 0; i < candles.length; i++)
		{
			double typicalPrice = candles[i].getHLC3();

			// Money Flow
			moneyFlow[i] = typicalPrice * candles[i].getVolume();

			if (i > 0)
			{
				// Positive Money Flow
				posMoneyFlow[i] = typicalPrice > candles[i - 1].getHLC3() ? moneyFlow[i] : 0;

				// Negative Money Flow
				negMoneyFlow[i] = typicalPrice < candles[i - 1].getHLC3() ? moneyFlow[i] : 0;
			}

			if (i >= periods - 1)
			{
				// Sum of Positive Money Flow
				double sumPosMf = DoubleArrayUtils.sum(posMoneyFlow, i - periods + 1, i);

				// Sum of Negative Money Flow
				double sumNegMf = DoubleArrayUtils.sum(negMoneyFlow, i - periods + 1, i);

				// Money Ratio
				double mr = sumPosMf / sumNegMf;

				// Money Flow Index
				double mfi = 100 - (100 / (1 + mr));

				mfiEntries[i - periods + 1] = new IndicatorEntry(candles[i]);
				mfiEntries[i - periods + 1].setValue(mfi);
			}

		}

		return mfiEntries;
	}

}
