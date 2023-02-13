package technicals.util;

import technicals.model.TechCandle;

public class MaxMinOpenClose
{
	private TechCandle[] candles;
	private int periods;

	private double high;
	private int indexHigh;

	private double low;
	private int indexLow;

	private double distance;

	public MaxMinOpenClose(TechCandle[] candles, int periods)
	{
		this.candles = candles;
		this.periods = periods;
	}

	public static MaxMinOpenClose create(TechCandle[] candles, int periods)
	{
		return new MaxMinOpenClose(candles, periods);
	}

	public TechCandle[] getCandles()
	{
		return candles;
	}

	public int getPeriods()
	{
		return periods;
	}

	public double getHigh()
	{
		return high;
	}

	public int getIndexHigh()
	{
		return indexHigh;
	}

	public double getLow()
	{
		return low;
	}

	public int getIndexLow()
	{
		return indexLow;
	}

	public double getDistance()
	{
		return distance;
	}

	public MaxMinOpenClose calculate()
	{
		int endIndex = candles.length - 1;
		int startIndex = candles.length - periods;

		high = candles[startIndex].getHighPrice();
		indexHigh = 0;
		low = candles[startIndex].getHighPrice();
		indexLow = 0;

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getOpenPrice() > high)
			{
				high = candles[i].getOpenPrice();
				indexHigh = i;
			}
			if (candles[i].getClosePrice() > high)
			{
				high = candles[i].getClosePrice();
				indexHigh = i;
			}
			if (candles[i].getOpenPrice() < low)
			{
				low = candles[i].getOpenPrice();
				indexLow = i;
			}
			if (candles[i].getClosePrice() < low)
			{
				low = candles[i].getClosePrice();
				indexLow = i;
			}
		}

		if (indexHigh < indexLow)
			distance = ((low - high) / high);
		else
			distance = ((high - low) / low);

		return this;
	}

}
