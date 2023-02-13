package exchanges.kucoin;

public enum KucoinIntervalType
{
	_1m("1min"), _3m("3min"), _5m("5min"), _15m("15min"), _30m("30min"), _1h("1hour"), _2h("2hour"), _4h("4hour"), _6h("6hour"), _8h("8hour"), _12h("12hour"), _1d("1day"), _1w("1week");

	private final String code;

	KucoinIntervalType(String code)
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
