package technicals.indicators.ma;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.IndicatorEntry;

/**
 * HMA - Hull Moving Average
 */
public class HullMovingAverage
{

	public static IndicatorEntry[] calculate(TechCandle[] candles, int periods)
	{
		
		int periodsHalf = periods / 2;
		int periodsSqrt = (int) Math.sqrt(periods);

		if (candles.length < periods + periodsSqrt)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		double[] rawValues = new double[candles.length - periods + 1];
		for (int i = periods - 1; i < candles.length; i++)
		{
			double wma1 = calcWAvg(candles, i - periodsHalf + 1, periodsHalf);
			double wma2 = calcWAvg(candles, i - periods + 1, periods);
			rawValues[i - periods + 1] = (2 * wma1) - wma2;
		}

		int len = candles.length - periods - periodsSqrt + 2;
		IndicatorEntry[] hmaEntries = new IndicatorEntry[len];
		for (int i = periodsSqrt - 1; i < rawValues.length; i++)
		{
			hmaEntries[i - periodsSqrt + 1] = new IndicatorEntry(candles[i + periods - 1]);

			double hma = calcWAvg(rawValues, i - periodsSqrt + 1, periodsSqrt);
			hmaEntries[i - periodsSqrt + 1].setValue(hma);
		}

		return hmaEntries;
	}

	private static double calcWAvg(double[] values, int startIndex, int periods)
	{
		double sum = 0;
		for (int i = 0; i < periods; i++)
		{
			sum += values[startIndex + i] * (i + 1);
		}
		return sum / ((periods * (periods + 1)) / 2);
	}

	private static double calcWAvg(TechCandle[] candles, int startIndex, int periods)
	{
		double sum = 0;
		for (int i = 0; i < periods; i++)
		{
			sum += candles[startIndex + i].getDefaultPrice() * (i + 1);
		}
		return sum / ((periods * (periods + 1)) / 2);
	}
	
	
}
