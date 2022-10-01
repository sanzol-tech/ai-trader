package sanzol.aitrader.be.enums;

public enum QtyIncrType
{
	ORDER, POSITION;

	public static QtyIncrType fromName(String name)
	{
		for (QtyIncrType e : QtyIncrType.values())
		{
			if (e.name().equals(name))
				return e;
		}
		return null;
	}

}
