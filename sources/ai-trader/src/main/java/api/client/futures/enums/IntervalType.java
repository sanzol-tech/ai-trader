package api.client.futures.enums;

public enum IntervalType
{
	_1m("1m"), _5m("5m"), _15m("15m"), _30m("30m"), _1h("1h"), _2h("2h"), _4h("4h"), _6h("6h"), _12h("12h"), _1d("1d");

	private final String code;

	IntervalType(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	@Override
	public String toString()
	{
		return code;
	}

}
