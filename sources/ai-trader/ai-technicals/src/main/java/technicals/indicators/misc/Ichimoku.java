package technicals.indicators.misc;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IchimokuEntry;
import technicals.util.CandleUtils;

/**
 * Ichimoku Cloud
 */
public class Ichimoku
{
	public static IchimokuEntry[] calculate(TechCandle[] candles)
	{
		if (candles.length < 52)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		IchimokuEntry[] ichimokuEntries = new IchimokuEntry[candles.length - 52 + 1];

		for (int i = 52 - 1; i < candles.length; i++)
		{
			ichimokuEntries[i - 52 + 1] = new IchimokuEntry(candles[i]);

			double ph9 = CandleUtils.highestHigh(candles, i - 9 + 1, i);
			double pl9 = CandleUtils.lowestLow(candles, i - 9 + 1, i);

			double ph26 = CandleUtils.highestHigh(candles, i - 26 + 1, i);
			double pl26 = CandleUtils.lowestLow(candles, i - 26 + 1, i);

			double ph52 = CandleUtils.highestHigh(candles, i - 52 + 1, i);
			double pl52 = CandleUtils.lowestLow(candles, i - 52 + 1, i);

			double conversionLine = (ph9 + pl9) / 2;
			double baseLine = (ph26 + pl26) / 2;
			double LeadingSpanA = (conversionLine + baseLine) / 2;
			double LeadingSpanB = (ph52 + pl52) / 2;

			ichimokuEntries[i - 52 + 1].setConversionLine(conversionLine);
			ichimokuEntries[i - 52 + 1].setBaseLine(baseLine);
			ichimokuEntries[i - 52 + 1].setLeadingSpanA(LeadingSpanA);
			ichimokuEntries[i - 52 + 1].setLeadingSpanB(LeadingSpanB);
		}

		return ichimokuEntries;
	}
}
