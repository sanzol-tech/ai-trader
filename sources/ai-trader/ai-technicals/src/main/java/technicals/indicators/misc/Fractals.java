package technicals.indicators.misc;

import java.util.ArrayList;
import java.util.List;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.FractalEntry;

/**
 * Williams Fractals
 */
public class Fractals
{
	public static FractalEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 2);
	}

	public static FractalEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length <= periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		List<FractalEntry> lstFractals = new ArrayList<FractalEntry>();

		for (int i = periods; i < candles.length - periods; i++)
		{
			boolean isFractalHigh = true;
			boolean isFractalLow = true;
			for (int x = 1; x <= periods; x++)
			{
				if (candles[i].getHighPrice() < candles[i - x].getHighPrice() || candles[i].getHighPrice() < candles[i + x].getHighPrice())
				{
					isFractalHigh = false;
				}
				if (candles[i].getLowPrice() > candles[i - x].getLowPrice() || candles[i].getLowPrice() > candles[i + x].getLowPrice())
				{
					isFractalLow = false;
				}
			}

			if (isFractalHigh || isFractalLow)
			{
				FractalEntry fractal = new FractalEntry(candles[i]);
				fractal.setHigh(isFractalHigh);
				fractal.setLow(isFractalLow);
				lstFractals.add(fractal);
			}
		}

		return lstFractals.toArray(new FractalEntry[0]);
	}

}
