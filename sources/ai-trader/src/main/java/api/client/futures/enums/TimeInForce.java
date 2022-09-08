package api.client.futures.enums;

public enum TimeInForce
{
	GTC("GTC"), IOC("IOC"), FOK("FOK"), GTE_GTC("GTE_GTC");
	
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
