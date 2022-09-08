package api.client.futures.enums;

public enum OrderType
{
	LIMIT("LIMIT"), MARKET("MARKET"), STOP("STOP"), STOP_MARKET("STOP_MARKET"), TAKE_PROFIT("TAKE_PROFIT"), TAKE_PROFIT_MARKET("TAKE_PROFIT_MARKET"), INVALID(null);

	private final String code;

	OrderType(String code)
	{
		this.code = code;
	}

	@Override
	public String toString()
	{
		return code;
	}

}
