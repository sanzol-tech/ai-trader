package sanzol.aitrader.be.enums;

public enum PriceIncrType
{
	ARITHMETIC("A"), GEOMETRIC("G");

	private final String code;

	PriceIncrType(String code)
	{
		this.code = code;
	}

	public static PriceIncrType fromCode(String code)
	{
		for (PriceIncrType e : PriceIncrType.values())
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
