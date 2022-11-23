package sanzol.aitrader.be.indicators;

import technicals.model.Candle;

public class HighLowChange
{
	public static double calculate(Candle[] candles, int periods)
	{
		int endIndex = candles.length - 1;
		int startIndex = candles.length - periods;
		
		double high = candles[startIndex].getHighPrice();
		int indexHigh = 0;
		double low = candles[startIndex].getHighPrice();
		int indexLow = 0;

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getHighPrice() > high)
			{
				high = candles[i].getHighPrice();
				indexHigh = i;
			}
			if (candles[i].getLowPrice() < low)
			{
				low = candles[i].getLowPrice();
				indexLow = i;
			}
		}
		
		if (indexHigh < indexLow)
			return ((low - high) / high) * 100;
		else
			return ((high - low) / low) * 100;
	}

}
