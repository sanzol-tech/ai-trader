package technicals.indicators.pp;

import technicals.model.pp.PivotPointsEntry;

/**
 * Camarilla Pivot Points
 */
public class CamarillaPivotPoints extends PivotPoints
{

	@Override
	public PivotPointsEntry calculate(double open, double high, double low, double close)
	{
		double pp = (high + low + close) / 3;

		double r1 = close + ((high - low) * 1.1 / 12);
		double r2 = close + ((high - low) * 1.1 / 6);
		double r3 = close + ((high - low) * 1.1 / 4);
		double r4 = close + ((high - low) * 1.1 / 2);

		double s1 = close - ((high - low) * 1.1 / 12);
		double s2 = close - ((high - low) * 1.1 / 6);
		double s3 = close - ((high - low) * 1.1 / 4);
		double s4 = close - ((high - low) * 1.1 / 2);

		return new PivotPointsEntry(pp, r1, s1, r2, s2, r3, s3, r4, s4);
	}

}
