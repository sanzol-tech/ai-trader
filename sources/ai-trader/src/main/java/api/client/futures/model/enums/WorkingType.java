package api.client.futures.model.enums;

public enum WorkingType
{
	MARK_PRICE("MARK_PRICE"), CONTRACT_PRICE("CONTRACT_PRICE");

	private final String code;

	WorkingType(String code)
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