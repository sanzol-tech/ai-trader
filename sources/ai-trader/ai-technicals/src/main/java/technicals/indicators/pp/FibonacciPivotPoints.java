package technicals.indicators.pp;

import technicals.model.pp.PivotPointsEntry;

/**
 * Fibonacci Pivot Points
 */
public class FibonacciPivotPoints extends PivotPoints
{
	private static final double[] LEVELS = { 0.382, 0.618, 1, 1.27, 1.618 };

	@Override
	public PivotPointsEntry calculate(double open, double high, double low, double close)
	{
		double pp = (high + low + close) / 3;

		double r1 = pp + (LEVELS[0] * (high - low));
		double r2 = pp + (LEVELS[1] * (high - low));
		double r3 = pp + (LEVELS[2] * (high - low));
		double r4 = pp + (LEVELS[3] * (high - low));
		double r5 = pp + (LEVELS[4] * (high - low));

		double s1 = pp - (LEVELS[0] * (high - low));
		double s2 = pp - (LEVELS[1] * (high - low));
		double s3 = pp - (LEVELS[2] * (high - low));
		double s4 = pp - (LEVELS[3] * (high - low));
		double s5 = pp - (LEVELS[4] * (high - low));

		return new PivotPointsEntry(pp, r1, s1, r2, s2, r3, s3, r4, s4, r5, s5);
	}

}
