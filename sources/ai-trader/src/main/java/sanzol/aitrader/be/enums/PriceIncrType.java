package sanzol.aitrader.be.enums;

public enum PriceIncrType
{
	ARITHMETIC, GEOMETRIC;

	public static PriceIncrType fromName(String name)
	{
		for (PriceIncrType e : PriceIncrType.values())
		{
			if (e.name().equals(name))
				return e;
		}
		return null;
	}
}
