package sanzol.app.util;

public class Test
{
	public double decimal(double d)
	{
		if (d < 0.01)
			return 0.00001;
		else if (d < 0.1)
			return 0.0001;
		else if (d < 1)
			return 0.001;
		else if (d < 10)
			return 0.01;
		else if (d < 100)
			return 0.1;
		else if (d < 1000)
			return 1;
		else if (d < 10000)
			return 10;
		else
			return 100;
	}
	
}
