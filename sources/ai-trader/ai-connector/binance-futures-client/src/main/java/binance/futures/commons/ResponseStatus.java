package binance.futures.commons;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseStatus
{
	private int code;
	private String msg;

	public ResponseStatus()
	{
		//
	}

	public ResponseStatus(int code, String msg)
	{
		this.code = code;
		this.msg = msg;
	}

	public static ResponseStatus from(String jsonString) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		ResponseStatus responseStatus = mapper.readValue(jsonString, ResponseStatus.class);
		return responseStatus;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

}
