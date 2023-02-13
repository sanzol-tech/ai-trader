package technicals.indicators.volume;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.VWAPEntry;

/**
 * VWAP - Volume Weighted Average Price
 */
public class VolumeWeightedAveragePrice
{

	public static VWAPEntry[] calculate(TechCandle[] candles)
	{
		if (candles.length < 2)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		VWAPEntry[] entries = new VWAPEntry[candles.length];

		entries[0] = new VWAPEntry(candles[0]);
		double tpv = candles[0].getHLC3() * candles[0].getVolume();
		entries[0].setTpv(tpv);
		double sumVol = candles[0].getVolume();
		double sumTpv = tpv;
		double vwap = candles[0].getHLC3();
		entries[0].setVwap(vwap);

		for (int i = 1; i < candles.length; i++)
		{
			TechCandle curr = candles[i];
			entries[i] = new VWAPEntry(curr);
			tpv = curr.getHLC3() * curr.getVolume();
			entries[i].setTpv(tpv);
			sumVol += curr.getVolume();
			sumTpv += tpv;
			vwap = sumTpv / sumVol;
			entries[i].setVwap(vwap);
		}

		return entries;
	}

}
