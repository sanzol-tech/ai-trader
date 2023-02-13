package technicals.indicators.pp;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.pp.PivotPointsEntry;
import technicals.util.CandleUtils;

public abstract class PivotPoints
{

	public PivotPointsEntry calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		return calculate(CandleUtils.mergeCandles(candles, candles.length - periods, candles.length - 1));
	}

	public PivotPointsEntry calculate(TechCandle candle)
	{
		// System.out.println("-> " + candle);
		return calculate(candle.getOpenPrice(), candle.getHighPrice(), candle.getLowPrice(), candle.getClosePrice());
	}

	abstract PivotPointsEntry calculate(double open, double high, double low, double close);

}
