package api.client.impl.model.enums;

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
