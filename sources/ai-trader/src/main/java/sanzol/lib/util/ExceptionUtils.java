/**
 *
 * AUTHOR: Fernando Elenberg Sanzol <f@sanzol.com.ar>
 * CREATE: August 2020
 *
 */
package sanzol.lib.util;

public final class ExceptionUtils
{
	private static final String MY_NAMESPACE = "sanzol.";

	public static String getMessage(Exception e)
	{
		if (e.getClass().getName().equals("java.lang.Exception"))
		{
			return e.getMessage();
		}
		else
		{
			return e.getClass().getName() + " : " + e.getMessage();
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

	public static String getStackTrace(Exception e)
	{
		StringBuffer sb = new StringBuffer();

		for (StackTraceElement ste : e.getStackTrace())
		{
			if (ste.getClassName().startsWith(MY_NAMESPACE))
			{
				sb.append(String.format("%s.%s(%s:%s)", ste.getClassName(), ste.getMethodName(), ste.getFileName(), ste.getLineNumber()));
				sb.append(System.getProperty("line.separator"));
			}
		}

		return sb.toString();
	}

}
