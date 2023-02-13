package technicals.indicators.oscillator;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.oscillator.AdxEntry;

/**
 * ADX - Average Directional Index
 */
public class AverageDirectionalIndex
{

	public static AdxEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 14);
	}

	public static AdxEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods + 1)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		AdxEntry[] adxEntries = new AdxEntry[candles.length];

		for (int i = 1; i < candles.length; i++)
		{
			AdxEntry adxEntry = new AdxEntry(candles[i]);
			adxEntries[i] = adxEntry;

			adxEntry.setTr1(trueRange(candles, i));
			adxEntry.setPosDm1(positiveDirectionalMovement(candles, i));
			adxEntry.setNegDm1(negativeDirectionalMovement(candles, i));

			if (i >= periods)
			{
				adxEntry.setTrX(trueRange(adxEntries, i, periods));
				adxEntry.setPosDmX(positiveDirectionalMovement(adxEntries, i, periods));
				adxEntry.setNegDmX(negativeDirectionalMovement(adxEntries, i, periods));

				adxEntry.setPosDI(positiveDirectionalIndicator(adxEntries, i, periods));
				adxEntry.setNegDI(negativeDirectionalIndicator(adxEntries, i, periods));

				adxEntry.setDiffDI(directionalDiffPeriod(adxEntries, i, periods));
				adxEntry.setSumDI(directionalSumPeriod(adxEntries, i, periods));

				adxEntry.setDx(directionalIndex(adxEntries, i, periods));
				adxEntry.setAdx(averageDirectionalIndex(adxEntries, i, periods));
			}
		}

		return adxEntries;
	}

	// --------------------------------------------------------------------

	private static double trueRange(TechCandle[] candles, int index)
	{
		double tr = candles[index].getHighPrice() - candles[index].getLowPrice();

		if (index > 0)
		{
			double tr2 = Math.abs(candles[index].getHighPrice() - candles[index - 1].getClosePrice());
			double tr3 = Math.abs(candles[index].getLowPrice() - candles[index - 1].getClosePrice());

			tr = Math.max(Math.max(tr, tr2), tr3);
		}

		return tr;
	}

	private static double positiveDirectionalMovement(TechCandle[] candles, int index)
	{
		if ((candles[index].getHighPrice() - candles[index - 1].getHighPrice()) > (candles[index - 1].getLowPrice() - candles[index].getLowPrice()))
		{
			return Math.max((candles[index].getHighPrice() - candles[index - 1].getHighPrice()), 0);
		}
		else
		{
			return 0;
		}
	}

	private static double negativeDirectionalMovement(TechCandle[] candles, int index)
	{
		if ((candles[index - 1].getLowPrice() - candles[index].getLowPrice()) > (candles[index].getHighPrice() - candles[index - 1].getHighPrice()))
		{
			return Math.max((candles[index - 1].getLowPrice() - candles[index].getLowPrice()), 0);
		}
		else
		{
			return 0;
		}
	}

	// --------------------------------------------------------------------

	private static double trueRange(AdxEntry[] entries, int index, int periods)
	{
		if (index == periods)
		{
			double sum = 0;
			for (int i = 0; i < periods; i++)
			{
				sum = sum + entries[index - i].getTr1();
			}
			return sum;
		}
		else if (index > periods)
		{
			double prevTrX = entries[index - 1].getTrX();
			return prevTrX - (prevTrX / periods) + entries[index].getTr1();
		}
		else
		{
			return 0;
		}
	}

	private static double positiveDirectionalMovement(AdxEntry[] entries, int index, int periods)
	{
		if (index == periods)
		{
			double posDmX = 0;
			for (int i = 1; i < periods + 1; i++)
			{
				posDmX += entries[i].getPosDm1();
			}
			return posDmX;
		}
		else if (index > periods)
		{
			double prevPosDmX = entries[index - 1].getPosDmX();
			return prevPosDmX - (prevPosDmX / periods) + entries[index].getPosDm1();

		}
		else
		{
			return 0;
		}
	}

	private static double negativeDirectionalMovement(AdxEntry[] entries, int index, int periods)
	{
		if (index == periods)
		{
			double negDmX = 0;
			for (int i = 1; i < periods + 1; i++)
			{
				negDmX += entries[i].getNegDm1();
			}
			return negDmX;
		}
		else if (index > periods)
		{
			double prevNegDmX = entries[index - 1].getNegDmX();
			return prevNegDmX - (prevNegDmX / periods) + entries[index].getNegDm1();
		}
		else
		{
			return 0;
		}
	}

	// --------------------------------------------------------------------

	private static double positiveDirectionalIndicator(AdxEntry[] entries, int index, int periods)
	{
		double posDmX = entries[index].getPosDmX();
		double trX = entries[index].getTrX();

		if (trX > 0)
			return 100 * (posDmX / trX);
		else
			return 0;
	}

	private static double negativeDirectionalIndicator(AdxEntry[] entries, int index, int periods)
	{
		double negDmX = entries[index].getNegDmX();
		double trX = entries[index].getTrX();

		if (trX > 0)
			return 100 * (negDmX / trX);
		else
			return 0;
	}

	private static double directionalDiffPeriod(AdxEntry[] entries, int index, int periods)
	{
		return Math.abs(entries[index].getPosDI() - entries[index].getNegDI());
	}

	private static double directionalSumPeriod(AdxEntry[] entries, int index, int periods)
	{
		return Math.abs(entries[index].getPosDI() + entries[index].getNegDI());
	}

	private static double directionalIndex(AdxEntry[] entries, int index, int periods)
	{
		double diffDI = entries[index].getDiffDI();
		double sumDI = entries[index].getSumDI();

		if (sumDI > 0)
			return 100 * (diffDI / sumDI);
		else
			return 0;
	}

	private static double averageDirectionalIndex(AdxEntry[] entries, int index, int periods)
	{
		if (index == (periods + periods - 1))
		{
			double sum = 0;
			for (int i = 0; i < periods; i++)
			{
				sum += entries[index - i].getDx();
			}
			return sum / periods;
		}
		else if (index > (periods + periods - 1))
		{
			return ((entries[index - 1].getAdx() * (periods - 1)) + entries[index].getDx()) / periods;
		}
		else
		{
			return 0;
		}
	}

}
