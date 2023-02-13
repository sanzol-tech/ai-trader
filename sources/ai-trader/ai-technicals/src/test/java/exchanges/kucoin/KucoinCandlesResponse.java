package exchanges.kucoin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KucoinCandlesResponse
{
	private String code;
	private String[][] data;

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String[][] getData()
	{
		return data;
	}

	public void setData(String[][] data)
	{
		this.data = data;
	}

}
