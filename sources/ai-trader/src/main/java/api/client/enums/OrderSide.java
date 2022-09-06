package api.client.enums;

public enum OrderSide
{
	BUY("BUY"), SELL("SELL");

	private final String code;

	OrderSide(String side)
	{
		this.code = side;
	}

	@Override
	public String toString()
	{
		return code;
	}

}