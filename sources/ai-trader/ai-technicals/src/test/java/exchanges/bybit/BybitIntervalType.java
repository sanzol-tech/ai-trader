package exchanges.bybit;

public enum BybitIntervalType
{
	_1m("1"), _3m("3"), _5m("5"), _15m("15"), _30m("30"), _1h("60"), _2h("120"), _4h("240"), _6h("360"), _12h("720"), _1d("D"), _1w("W"), _1M("M");

	private final String code;

	BybitIntervalType(String code)
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
