package sanzol.app.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class LogService
{
	public enum LogType { DEBUG, INFO, WARN, ERROR }
	
	private static final boolean CONSOLE_ENABLED = false;

	private static final boolean DEBUG_ENABLED = false;
	private static final boolean INFO_ENABLED = true;
	private static final boolean WARN_ENABLED = true;
	private static final boolean ERROR_ENABLED = true;

	private static final long LOG_MAXSIZE = 1000;

	private static LinkedList<String> logLines = new LinkedList<String>();

	public static String getLOG()
	{
		return StringUtils.join(logLines, "\n");
	}

	public static void cleanLOG()
	{
		logLines = new LinkedList<String>();
	}

	private static synchronized void log(LogType type, String msg)
	{
		if ((type == LogType.DEBUG && !DEBUG_ENABLED) || (type == LogType.INFO && !INFO_ENABLED) || (type == LogType.WARN && !WARN_ENABLED) || (type == LogType.ERROR && !ERROR_ENABLED))
		{
			return;
		}

		String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String text = String.format("%-19s : %s : %s", datetime, type.name(), msg);

		while (logLines.size() > LOG_MAXSIZE)
		{
			logLines.removeFirst();
		}

		if (CONSOLE_ENABLED)
		{
			System.out.println(text);
		}

		logLines.add(text);

		notifyAllLogObservers();
	}

	public static void debug(String msg)
	{
		log(LogType.DEBUG, msg);
	}

	public static void info(String msg)
	{
		log(LogType.INFO, msg);
	}

	public static void warn(String msg)
	{
		log(LogType.WARN, msg);
	}

	public static void error(String msg)
	{
		log(LogType.ERROR, msg);
	}

	public static void error(Exception ex)
	{
		if (CONSOLE_ENABLED)
		{
			ex.printStackTrace();
		}

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String simpleClassName = ste.getClassName().substring(ste.getClassName().lastIndexOf('.') + 1);
		String path = simpleClassName + "." + ste.getMethodName();

		log(LogType.ERROR, path + " : " + ExceptionUtils.getMessage(ex));
	}

	// ------------------------------------------------------------------------	

	private static List<LogListener> observers = new ArrayList<LogListener>();

	public static void attachRefreshObserver(LogListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(LogListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers()
	{
		for (LogListener observer : observers)
		{
			observer.onLogUpdate();
		}
	}

}
