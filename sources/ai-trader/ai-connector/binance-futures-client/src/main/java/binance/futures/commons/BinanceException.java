package binance.futures.commons;

public class BinanceException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private final String errCode;

	public BinanceException(String errCode, String errMsg)
	{
		super(errMsg);
		this.errCode = errCode;
	}
	
	public BinanceException(String errMsg)
	{
		super(errMsg);
		this.errCode = "";
	}

	public BinanceException(String errMsg, Throwable e)
	{
		super(errMsg, e);
		this.errCode = "";
	}

	public String getErrCode()
	{
		return errCode;
	}

}
