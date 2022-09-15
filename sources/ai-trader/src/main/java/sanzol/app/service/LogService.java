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

	private static synchronized void log(String type, String msg)
	{
		String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String text = String.format("%-19s : %s : %s", datetime, type, msg);

		while (logLines.size() > LOG_MAXSIZE)
		{
			logLines.removeFirst();
		}

		System.out.println (text);
		logLines.add(text);

		notifyAllLogObservers();
	}

	public static void debug(String msg)
	{
		if (DEBUG_ENABLED)
			log("DEBUG", msg);
	}

	public static void info(String msg)
	{
		if (INFO_ENABLED)
			log("INFO", msg);
	}

	public static void warn(String msg)
	{
		if (WARN_ENABLED)
			log("WARN", msg);
	}

	public static void error(String msg)
	{
		if (ERROR_ENABLED)
			log("ERROR", msg);
	}

	public static void error(Exception ex)
	{
		ex.printStackTrace();

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String simpleClassName = ste.getClassName().substring(ste.getClassName().lastIndexOf('.') + 1);
		String path = simpleClassName + "." + ste.getMethodName();

		log("ERROR", path + " : " + ExceptionUtils.getMessage(ex));
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
