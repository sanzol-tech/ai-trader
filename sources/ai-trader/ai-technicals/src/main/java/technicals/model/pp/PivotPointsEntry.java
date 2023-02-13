package technicals.model.pp;

public class PivotPointsEntry
{
	private double pp;

	private double r1;
	private double s1;

	private Double r2;
	private Double s2;

	private Double r3;
	private Double s3;

	private Double r4;
	private Double s4;

	private Double r5;
	private Double s5;

	public PivotPointsEntry(double pp, double r1, double s1)
	{
		this.pp = pp;
		this.r1 = r1;
		this.s1 = s1;
	}

	public PivotPointsEntry(double pp, double r1, double s1, Double r2, Double s2)
	{
		this.pp = pp;
		this.r1 = r1;
		this.s1 = s1;
		this.r2 = r2;
		this.s2 = s2;
	}

	public PivotPointsEntry(double pp, double r1, double s1, Double r2, Double s2, Double r3, Double s3)
	{
		this.pp = pp;
		this.r1 = r1;
		this.s1 = s1;
		this.r2 = r2;
		this.s2 = s2;
		this.r3 = r3;
		this.s3 = s3;
	}

	public PivotPointsEntry(double pp, double r1, double s1, Double r2, Double s2, Double r3, Double s3, Double r4, Double s4)
	{
		this.pp = pp;
		this.r1 = r1;
		this.s1 = s1;
		this.r2 = r2;
		this.s2 = s2;
		this.r3 = r3;
		this.s3 = s3;
		this.r4 = r4;
		this.s4 = s4;
	}
	
	public PivotPointsEntry(double pp, double r1, double s1, Double r2, Double s2, Double r3, Double s3, Double r4, Double s4, Double r5, Double s5)
	{
		this.pp = pp;
		this.r1 = r1;
		this.s1 = s1;
		this.r2 = r2;
		this.s2 = s2;
		this.r3 = r3;
		this.s3 = s3;
		this.r4 = r4;
		this.s4 = s4;
		this.r5 = r5;
		this.s5 = s5;
	}

	public double getPp()
	{
		return pp;
	}

	public void setPp(double pp)
	{
		this.pp = pp;
	}

	public double getR1()
	{
		return r1;
	}

	public void setR1(double r1)
	{
		this.r1 = r1;
	}

	public double getS1()
	{
		return s1;
	}

	public void setS1(double s1)
	{
		this.s1 = s1;
	}

	public Double getR2()
	{
		return r2;
	}

	public void setR2(Double r2)
	{
		this.r2 = r2;
	}

	public Double getS2()
	{
		return s2;
	}

	public void setS2(Double s2)
	{
		this.s2 = s2;
	}

	public Double getR3()
	{
		return r3;
	}

	public void setR3(Double r3)
	{
		this.r3 = r3;
	}

	public Double getS3()
	{
		return s3;
	}

	public void setS3(Double s3)
	{
		this.s3 = s3;
	}

	public Double getR4()
	{
		return r4;
	}

	public void setR4(Double r4)
	{
		this.r4 = r4;
	}

	public Double getS4()
	{
		return s4;
	}

	public void setS4(Double s4)
	{
		this.s4 = s4;
	}

	public Double getR5()
	{
		return r5;
	}

	public void setR5(Double r5)
	{
		this.r5 = r5;
	}

	public Double getS5()
	{
		return s5;
	}

	public void setS5(Double s5)
	{
		this.s5 = s5;
	}

	@Override
	public String toString()
	{
		return String.format("r5: %f\nr4: %f\nr3: %f\nr2: %f\nr1: %f\npp: %f\ns1: %f\ns2: %f\ns3: %f\ns4: %f\ns5: %f\n", r5, r4, r3, r2, r1, pp, s1, s2, s3, s4, s5);
	}

}
