package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;

/**
 * UO - Ultimate Oscillator
 */
public class UltimateOscillator
{

	public static IndicatorEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 7, 14, 28);
	}

	public static IndicatorEntry[] calculate(TechCandle[] candles, int periods1, int periods2, int periods3)
	{
		if (periods1 >= periods2 || periods2 >= periods3)
		{
			throw new IllegalArgumentException("Periods must be periods1 < periods2 < periods3");
		}

		if (candles.length < periods3)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods3 + 1;
		IndicatorEntry[] uoEntries = new IndicatorEntry[len];

		double divider = (periods3 / periods1 + periods3 / periods2 + 1);

		for (int i = periods3 - 1; i < candles.length; i++)
		{
			double avg1 = calcAvg(candles, i - periods1 + 1, i);
			double avg2 = calcAvg(candles, i - periods2 + 1, i);
			double avg3 = calcAvg(candles, i - periods3 + 1, i);

			double uo = 100 * (((4 * avg1) + (2 * avg2) + avg3) / divider);

			uoEntries[i - periods3 + 1] = new IndicatorEntry(candles[i]);
			uoEntries[i - periods3 + 1].setValue(uo);
		}

		return uoEntries;
	}

	private static double calcAvg(TechCandle[] candles, int startIndex, int endIndex)
	{
		double sumBP = 0;
		double sumTR = 0;

		for (int i = startIndex; i <= endIndex; i++)
		{
			double prevClose = i > 0 ? candles[i - 1].getClosePrice() : candles[i].getOpenPrice();

			double buyingPressure = candles[i].getClosePrice() - Math.min(candles[i].getLowPrice(), prevClose);
			sumBP += buyingPressure;

			double trueRange = Math.max(candles[i].getHighPrice(), prevClose) - Math.min(candles[i].getLowPrice(), prevClose);
			sumTR += trueRange;
		}

		return sumBP / sumTR;
	}

}
