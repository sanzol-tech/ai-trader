package sanzol.aitrader.be.enums;

public enum QuantityType
{
	USD("U"), COIN("C"), BALANCE("B");

	private final String code;

	QuantityType(String code)
	{
		this.code = code;
	}

	public static QuantityType fromCode(String code)
	{
		for (QuantityType e : QuantityType.values())
		{
			if (e.code.equals(code))
				return e;
		}
		return null;
	}

	public String getCode()
	{
		return code;
	}

}
