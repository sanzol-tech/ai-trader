package sanzol.aitrader.be.enums;

public enum QtyIncrType
{
	ORDER("O"), POSITION("P");

	private final String code;

	QtyIncrType(String code)
	{
		this.code = code;
	}

	public static QtyIncrType fromCode(String code)
	{
		for (QtyIncrType e : QtyIncrType.values())
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
