package binance.spot.enums;

public enum TimeInForce
{
	GTC("GTC"), IOC("IOC"), FOK("FOK");

	private final String code;

	TimeInForce(String code)
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
