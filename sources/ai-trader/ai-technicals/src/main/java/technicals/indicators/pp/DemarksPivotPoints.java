package technicals.indicators.pp;

import technicals.model.pp.PivotPointsEntry;

/**
 * Demarks Pivot Points
 */
public class DemarksPivotPoints extends PivotPoints
{

	@Override
	public PivotPointsEntry calculate(double open, double high, double low, double close)
	{
		double x;
		if (close < open)
			x = high + (2 * low) + close;
		else if (close > open)
			x = (2 * high) + low + close;
		else
			x = high + low + (2 * close);

		double pp = x / 4;
		
		double r1 = x / 2 - low;

		double s1 = x / 2 - high;
		
		return new PivotPointsEntry(pp, r1, s1);		
	}

}
