package api.client.enums;

public enum MarketType
{
	spot("spot"), futures("futures");

	private final String code;

	MarketType(String code)
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
