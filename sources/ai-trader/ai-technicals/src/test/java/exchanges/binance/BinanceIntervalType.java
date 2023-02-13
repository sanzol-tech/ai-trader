package exchanges.binance;

public enum BinanceIntervalType
{
	_1m("1m"), _3m("3m"), _5m("5m"), _15m("15m"), _30m("30m"), _1h("1h"), _2h("2h"), _4h("4h"), _6h("6h"), _8h("8h"), _12h("12h"), _1d("1d"), _3d("3d"), _1w("1w"), _1M("1M");

	private final String code;

	BinanceIntervalType(String code)
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
