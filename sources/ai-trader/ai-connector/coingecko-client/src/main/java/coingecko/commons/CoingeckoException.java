package coingecko.commons;

public class CoingeckoException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private final String errCode;

	public CoingeckoException(String errCode, String errMsg)
	{
		super(errMsg);
		this.errCode = errCode;
	}
	
	public CoingeckoException(String errMsg)
	{
		super(errMsg);
		this.errCode = "";
	}

	public CoingeckoException(String errMsg, Throwable e)
	{
		super(errMsg, e);
		this.errCode = "";
	}

	public String getErrCode()
	{
		return errCode;
	}

}
