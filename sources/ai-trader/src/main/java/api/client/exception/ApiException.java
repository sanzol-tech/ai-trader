package api.client.exception;

public class ApiException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ApiException(String errMsg)
	{
		super(errMsg);
	}

	public ApiException(String errMsg, Throwable e)
	{
		super(errMsg, e);
	}

}
