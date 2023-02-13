package aitrader.util;

public final class ExceptionUtils
{
	public static String getMessage(Exception e)
	{
		if (e.getClass().getName().equals("java.lang.Exception"))
		{
			return e.getMessage();
		}
		else
		{
			return e.getClass().getSimpleName() + " : " + e.getMessage();
		}
	}

	public static String getSimpleMessage(Exception e)
	{
		if (e.getClass().getName().equals("java.lang.Exception"))
		{
			return e.getMessage();
		}
		else
		{
			return e.getClass().getName();
		}
	}

}
