package api.client.exception;

public class ApiException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private final String errCode;

	public ApiException(String errMsg)
	{
		super(errMsg);
		this.errCode = "-99999";
	}

	public ApiException(String errType, String errMsg)
	{
		super(errMsg);
		this.errCode = errType;
	}

	public ApiException(String errType, String errMsg, Throwable e)
	{
		super(errMsg, e);
		this.errCode = errType;
	}

	public String getErrType()
	{
		return this.errCode;
	}
}
