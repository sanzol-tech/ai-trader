package technicals.indicators.pp;

import technicals.model.pp.PivotPointsEntry;

/**
 * Woodie Pivot Points
 */
public class WoodiePivotPoints extends PivotPoints
{

	@Override
	public PivotPointsEntry calculate(double open, double high, double low, double close)
	{
		double pp = (high + low + (2 * close)) / 4;

		double r1 = (2 * pp) - low;
		double r2 = pp + (high - low);
		double r3 = high + 2 * (pp - low);

		double s1 = (2 * pp) - high;
		double s2 = pp - (high - low);
		double s3 = low - 2 * (high - pp);

		return new PivotPointsEntry(pp, r1, s1, r2, s2, r3, s3);
	}

}
