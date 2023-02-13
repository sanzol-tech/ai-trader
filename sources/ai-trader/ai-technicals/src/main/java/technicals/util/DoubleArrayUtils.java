package technicals.util;

public class DoubleArrayUtils
{

	public static int maxIndex(double[] values, int startIndex, int endIndex)
	{
		double value = values[startIndex];
		int index = startIndex;

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (values[i] > value)
			{
				value = values[i];
				index = i;
			}
		}

		return index;
	}

	public static double maxValue(double[] values, int startIndex, int endIndex)
	{
		double value = values[startIndex];

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (values[i] > value)
			{
				value = values[i];
			}
		}

		return value;
	}

	public static int minIndex(double[] values, int startIndex, int endIndex)
	{
		double value = values[startIndex];
		int index = startIndex;

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (values[i] < value)
			{
				value = values[i];
				index = i;
			}
		}

		return index;
	}

	public static double minValue(double[] values, int startIndex, int endIndex)
	{
		double value = values[startIndex];

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (values[i] < value)
			{
				value = values[i];
			}
		}

		return value;
	}

	public static double sum(double[] values, int startIndex, int endIndex)
	{
		double sum = 0;
		for (int i = startIndex; i <= endIndex; i++)
		{
			sum += values[i];
		}
		return sum;
	}

	public static double avg(double[] values, int startIndex, int endIndex)
	{
		double sum = 0;
		for (int i = startIndex; i <= endIndex; i++)
		{
			sum += values[i];
		}
		return sum / (endIndex - startIndex + 1);
	}

	public static double[] slice(double[] values, int startIndex, int endIndex)
	{
		double[] newCandles = new double[endIndex - startIndex + 1];
		for (int i = startIndex; i <= endIndex; i++)
		{
			newCandles[i - startIndex] = values[i];
		}
		return newCandles;
	}

}
