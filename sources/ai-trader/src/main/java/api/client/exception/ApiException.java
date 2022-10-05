package api.client.exception;

public class ApiException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private final String errCode;

	public ApiException(String errCode, String errMsg)
	{
		super(errMsg);
		this.errCode = errCode;
	}
	
	public ApiException(String errMsg)
	{
		super(errMsg);
		this.errCode = "";
	}

	public ApiException(String errMsg, Throwable e)
	{
		super(errMsg, e);
		this.errCode = "";
	}

	public String getErrCode()
	{
		return errCode;
	}

}
