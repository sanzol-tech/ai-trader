package binance.futures.enums;

public enum PositionSide
{
	BOTH("BOTH"), SHORT("SHORT"), LONG("LONG");

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
