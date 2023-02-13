package technicals.indicators.pp;

import technicals.model.pp.PivotPointsEntry;

/**
 * Classic Pivot Points
 */
public class StandardPivotPoints extends PivotPoints
{

	public PivotPointsEntry calculate(double open, double high, double low, double close)
	{
		double pp = (high + low + close) / 3;

		double r1 = (pp * 2) - low;
		double s1 = (pp * 2) - high;

		double r2 = pp + (r1 - s1);
		double s2 = pp - (r1 - s1);

		double r3 = pp + (r2 - s2);
		double s3 = pp - (r2 - s2);

		double r4 = pp + (r3 - s3);
		double s4 = pp - (r3 - s3);

		double r5 = pp + (r4 - s4);
		double s5 = pp - (r4 - s4);

		return new PivotPointsEntry(pp, r1, s1, r2, s2, r3, s3, r4, s4, r5, s5);
	}

}
