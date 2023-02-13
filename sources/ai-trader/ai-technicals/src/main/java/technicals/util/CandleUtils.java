package technicals.util;

import technicals.model.TechCandle;

public class CandleUtils
{

	public static double[] toDoubleArray(TechCandle[] candles)
	{
		double[] prices = new double[candles.length];
		for (int i = 0; i < candles.length; i++)
		{
			prices[i] = candles[i].getDefaultPrice();
		}
		return prices;
	}

	public static double sumPrice(TechCandle[] candles, int startIndex, int endIndex)
	{
		double sum = 0;
		for (int i = startIndex; i <= endIndex; i++)
		{
			sum += candles[i].getDefaultPrice();
		}
		return sum;
	}

	public static double avgPrice(TechCandle[] candles, int startIndex, int endIndex)
	{
		double sum = 0;
		for (int i = startIndex; i <= endIndex; i++)
		{
			sum += candles[i].getDefaultPrice();
		}
		return sum / (endIndex - startIndex + 1);
	}

	public static TechCandle[] slice(TechCandle[] candles, int startIndex, int endIndex)
	{
		TechCandle[] newCandles = new TechCandle[endIndex - startIndex + 1];
		for (int i = startIndex; i <= endIndex; i++)
		{
			newCandles[i - startIndex] = candles[i];
		}
		return newCandles;
	}

	public static int highestHighIndex(TechCandle[] candles, int startIndex, int endIndex)
	{
		double value = candles[startIndex].getHighPrice();
		int index = startIndex;

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getHighPrice() > value)
			{
				value = candles[i].getHighPrice();
				index = i;
			}
		}

		return index;
	}

	public static int lowestLowIndex(TechCandle[] candles, int startIndex, int endIndex)
	{
		double value = candles[startIndex].getLowPrice();
		int index = startIndex;

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getLowPrice() < value)
			{
				value = candles[i].getLowPrice();
				index = i;
			}
		}

		return index;
	}

	public static double highestHigh(TechCandle[] candles, int startIndex, int endIndex)
	{
		double value = candles[startIndex].getHighPrice();

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getHighPrice() > value)
			{
				value = candles[i].getHighPrice();
			}
		}

		return value;
	}

	public static double lowestLow(TechCandle[] candles, int startIndex, int endIndex)
	{
		double value = candles[startIndex].getLowPrice();

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getLowPrice() < value)
			{
				value = candles[i].getLowPrice();
			}
		}

		return value;
	}

	public static TechCandle mergeLastCandles(TechCandle[] candles, int periods)
	{
		return mergeCandles(candles, candles.length - periods, candles.length - 1);
	}

	public static TechCandle mergeCandles(TechCandle[] candles, int startIndex, int endIndex)
	{
		double high = candles[startIndex].getHighPrice();
		double low = candles[startIndex].getLowPrice();
		double volume = 0;
		double quoteVolume = 0;
		long count = 0;

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getHighPrice() > high)
			{
				high = candles[i].getHighPrice();
			}
			if (candles[i].getLowPrice() < low)
			{
				low = candles[i].getLowPrice();
			}

			volume += candles[i].getVolume();
			quoteVolume += candles[i].getQuoteVolume();
			count += candles[i].getCount();
		}

		TechCandle newCandle = new TechCandle();
		newCandle.setOpenTime(candles[startIndex].getOpenTime());
		newCandle.setOpenPrice(candles[startIndex].getOpenPrice());
		newCandle.setHighPrice(high);
		newCandle.setLowPrice(low);
		newCandle.setClosePrice(candles[endIndex].getClosePrice());
		newCandle.setVolume(volume);
		newCandle.setQuoteVolume(quoteVolume);
		newCandle.setCount(count);

		return newCandle;
	}

}
