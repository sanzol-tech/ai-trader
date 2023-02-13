package aitrader.util.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import aitrader.util.ExceptionUtils;
import aitrader.util.observable.Handler;

public class LogService
{
	public enum LogType { DEBUG, INFO, WARN, ERROR }

	private boolean consoleEnabled = true;

	private boolean debugEnabled = false;
	private boolean infoEnabled = true;
	private boolean warnEnabled = true;
	private boolean errorEnabled = true;

	private long maxSize = 1000;

	private LinkedList<String> logLines = new LinkedList<String>();

	public LogService()
	{
		//
	}

	public LogService(boolean consoleEnabled, boolean debugEnabled, boolean infoEnabled, boolean warnEnabled, boolean errorEnabled, long maxSize)
	{
		this.consoleEnabled = consoleEnabled;
		this.debugEnabled = debugEnabled;
		this.infoEnabled = infoEnabled;
		this.warnEnabled = warnEnabled;
		this.errorEnabled = errorEnabled;
		this.maxSize = maxSize;
	}

	public String getLOG()
	{
		return String.join("\n", logLines);
	}

	public void cleanLOG()
	{
		logLines = new LinkedList<String>();
	}

	private synchronized void log(LogType type, String msg)
	{
		if ((type == LogType.DEBUG && !debugEnabled) || (type == LogType.INFO && !infoEnabled) || (type == LogType.WARN && !warnEnabled) || (type == LogType.ERROR && !errorEnabled))
		{
			return;
		}

		String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String text = String.format("%-19s : %s : %s", datetime, type.name(), msg);

		while (logLines.size() > maxSize)
		{
			logLines.removeFirst();
		}

		if (consoleEnabled)
		{
			System.out.println(text);
		}

		logLines.add(text);

		notifyAllObservers();
	}

	public void debug(String msg)
	{
		log(LogType.DEBUG, msg);
	}

	public void info(String msg)
	{
		log(LogType.INFO, msg);
	}

	public void warn(String msg)
	{
		log(LogType.WARN, msg);
	}

	public void error(String msg)
	{
		log(LogType.ERROR, msg);
	}

	public void error(Exception ex)
	{
		if (consoleEnabled)
		{
			ex.printStackTrace();
		}

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String simpleClassName = ste.getClassName().substring(ste.getClassName().lastIndexOf('.') + 1);
		String path = simpleClassName + "." + ste.getMethodName();

		log(LogType.ERROR, path + " : " + ExceptionUtils.getMessage(ex));
	}

	// --------------------------------------------------------------------

	private List<Handler<Void>> observers = new ArrayList<Handler<Void>>();

	public void attachObserver(Handler<Void> observer)
	{
		observers.add(observer);
	}

	public void deattachObserver(Handler<Void> observer)
	{
		observers.remove(observer);
	}

	public void notifyAllObservers()
	{
		for (Handler<Void> observer : observers)
		{
			observer.handle(null);
		}
	}

}
