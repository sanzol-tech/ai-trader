package exchanges.bybit;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BybitCandlesResponse
{
	private String success;
	private ArrayList<BybitCandle> result;

	public String getSuccess()
	{
		return success;
	}

	public void setSuccess(String success)
	{
		this.success = success;
	}

	public ArrayList<BybitCandle> getResult()
	{
		return result;
	}

	public void setResult(ArrayList<BybitCandle> result)
	{
		this.result = result;
	}

}
