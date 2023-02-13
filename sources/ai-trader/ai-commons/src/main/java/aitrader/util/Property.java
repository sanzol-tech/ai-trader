package aitrader.util;

public class Property
{
	private String name;
	private Object value;
	private Object defaultValue;

	public Property(String name, Object value, Object defaultValue)
	{
		this.name = name;
		this.value = value;
		this.defaultValue = defaultValue;
	}

	public static Property get(String name, Object defaultValue)
	{
		return new Property(name, null, defaultValue);
	}

	public static Property get(String name, Object value, Object defaultValue)
	{
		return new Property(name, value, defaultValue);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Object getValue()
	{
		if (value != null || defaultValue == null)
			return value;
		else
			return defaultValue;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}

	public Object getDefaultValue()
	{
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString()
	{
		Object o = getValue();

		if (o == null)
			return "";

		return o.toString();
	}

}
