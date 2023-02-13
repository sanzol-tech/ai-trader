package aitrader.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import aitrader.util.security.Cipher;

public final class Properties
{
	private Map<String, String> mapProperties = new LinkedHashMap<String, String>();
	private boolean obfuscate;

	public static Properties create(boolean obfuscate)
	{
		Properties p = new Properties();
		p.obfuscate = obfuscate;

		return p;
	}

	public Properties put(String key, String value)
	{
		mapProperties.put(key, value);
		return this;
	}

	public Properties put(String key, Number value)
	{
		mapProperties.put(key, String.valueOf(value));
		return this;
	}

	public Properties put(String key, Boolean value)
	{
		mapProperties.put(key, String.valueOf(value));
		return this;
	}

	public String getValue(String key)
	{
		return mapProperties.get(key);
	}

	public Integer getInteger(String key)
	{
		if (mapProperties.containsKey(key))
			return Integer.valueOf(mapProperties.get(key));
		else
			return null;
	}

	public Long getLong(String key)
	{
		if (mapProperties.containsKey(key))
			return Long.valueOf(mapProperties.get(key));
		else
			return null;
	}

	public Double getDouble(String key)
	{
		if (mapProperties.containsKey(key))
			return Double.valueOf(mapProperties.get(key));
		else
			return null;
	}

	public Boolean getBoolean(String key)
	{
		return "true".equalsIgnoreCase(mapProperties.get(key));
	}

	public Properties obfuscate()
	{
		return this;
	}

	public Properties deobfuscate()
	{
		return this;
	}

	public Properties load(Path path) throws IOException
	{
		String content = Files.readString(path);

		content = obfuscate ? Cipher.decrypt(content) : content;

		Stream<String> lines = content.lines();
		
		lines.forEach(line -> { 
			int i = line.indexOf("=");
			if (i >= 0 && i < line.length())
			{
				String key = line.substring(0, i);
				String value = line.substring(i + 1);
				mapProperties.put(key, value);
			}
		});

		return this;
	}

	public Properties save(Path path) throws IOException
	{
		String content = obfuscate ? Cipher.encrypt(toString()) : toString();

		Files.writeString(path, content, StandardOpenOption.CREATE);

		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : mapProperties.entrySet())
		{
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
		}
		return sb.toString();
	}

}
