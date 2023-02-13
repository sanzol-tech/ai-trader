package aitrader.util.log;

import java.util.HashMap;
import java.util.Map;

public class LogAppenders
{
	private static Map<String, LogService> mapAppenders = new HashMap<String, LogService>();

	public static Map<String, LogService> getMapAppenders()
	{
		return mapAppenders;
	}

	public static LogService getAppender(String name)
	{
		return mapAppenders.get(name);
	}

	public static LogService addAppender(String name)
	{
		LogService logService = new LogService();
		mapAppenders.put(name, logService);
		return logService;
	}

}
