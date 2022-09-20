package api.client.futures.model.enums;

public enum NewOrderRespType
{
	ACK("ACK"), RESULT("RESULT");
	
	private final String code;

	NewOrderRespType(String code)
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
