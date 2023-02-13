package technicals.indicators.depth;

import java.math.BigDecimal;

/**
 * Merge 'DepthWeightedPoints' and 'DepthBlockPoints' indicators 
 * to obtain value of supports and resistances with a given criterion
 */
public class DephMergedPoints 
{
	public enum MergeMode { 
		MAX, MIN, BB, WA; 

		public static MergeMode fromName(String name)
		{
			for (MergeMode e : MergeMode.values())
			{
				if (e.name().equals(name))
					return e;
			}
			return null;
		}
	}

	private BigDecimal r1;
	private BigDecimal s1;
	private BigDecimal r2;
	private BigDecimal s2;

	public BigDecimal getR1() {
		return r1;
	}

	public BigDecimal getS1() {
		return s1;
	}

	public BigDecimal getR2() {
		return r2;
	}

	public BigDecimal getS2() {
		return s2;
	}

	public static DephMergedPoints getInstance()
	{
		return new DephMergedPoints();
	}

	public DephMergedPoints calculate(DepthBlockPoints depthBlockPoints, DepthWeightedPoints depthWeightedPoints, MergeMode mode)
	{
		if (mode == MergeMode.BB)
		{
			r2 = depthBlockPoints.getR2();
			r1 = depthBlockPoints.getR1();
			s1 = depthBlockPoints.getS1();
			s2 = depthBlockPoints.getS2();
		}
		else if (mode == MergeMode.WA)
		{
			r2 = depthWeightedPoints.getR2();
			r1 = depthWeightedPoints.getR1();
			s1 = depthWeightedPoints.getS1();
			s2 = depthWeightedPoints.getS2();
		}
		else if (mode == MergeMode.MAX)
		{
			if (depthWeightedPoints.getR1() != null && depthWeightedPoints.getR1().doubleValue() > depthBlockPoints.getR1().doubleValue())
				r1 = depthWeightedPoints.getR1();
			else
				r1 = depthBlockPoints.getR1();

			if (depthWeightedPoints.getS1() != null && depthWeightedPoints.getS1().doubleValue() < depthBlockPoints.getS1().doubleValue())
				s1 = depthWeightedPoints.getS1();
			else
				s1 = depthBlockPoints.getS1();

			if (depthWeightedPoints.getR2() != null && depthWeightedPoints.getR2().doubleValue() > depthBlockPoints.getR2().doubleValue())
				r2 = depthWeightedPoints.getR2();
			else
				r2 = depthBlockPoints.getR2();

			if (depthWeightedPoints.getS2() != null && depthWeightedPoints.getS2().doubleValue() < depthBlockPoints.getS2().doubleValue())
				s2 = depthWeightedPoints.getS2();
			else
				s2 = depthBlockPoints.getS2();
		}
		else if (mode == MergeMode.MIN)
		{
			// TODO
		}

		return this;
	}

}
