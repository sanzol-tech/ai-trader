package aitrader.core.model.enums;

public enum PositionSide
{
	SHORT("SHORT"), LONG("LONG"), BOTH("BOTH");

	private final String code;

	PositionSide(String side)
	{
		this.code = side;
	}

	@Override
	public String toString()
	{
		return code;
	}
}
