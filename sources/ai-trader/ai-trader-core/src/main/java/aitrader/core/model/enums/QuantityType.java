package aitrader.core.model.enums;

public enum QuantityType
{
	USD, COIN, BALANCE;

	public static QuantityType fromName(String name)
	{
		for (QuantityType e : QuantityType.values())
		{
			if (e.name().equals(name))
				return e;
		}
		return null;
	}

}
