package sanzol.aitrader.be.service;

public enum AlertState
{
	NONE("NONE"), SHORT_ALERT("SHORT ALERT"), LONG_ALERT("LONG ALERT"), SHORT_LIMIT("SHORT LIMIT"), LONG_LIMIT("LONG LIMIT");

	private final String value;

	AlertState(String value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return value;
	}
}
