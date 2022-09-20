package api.client.model.event;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepthEvent
{
	@JsonProperty("e")
	private String eventType;

	@JsonProperty("E")
	private Long eventTime;

	@JsonProperty("T")
	private Long transactionTime;

	@JsonProperty("s")
	private String symbol;

	@JsonProperty("U")
	private Long firstUpdateId;

	@JsonProperty("u")
	private Long lastUpdateId;

	@JsonProperty("pu")
	private Long lastUpdateIdInlastStream;

	@JsonProperty("b")
	private List<List<BigDecimal>> bids;

	@JsonProperty("a")
	private List<List<BigDecimal>> asks;

	public String getEventType()
	{
		return eventType;
	}

	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}

	public Long getEventTime()
	{
		return eventTime;
	}

	public void setEventTime(Long eventTime)
	{
		this.eventTime = eventTime;
	}

	public Long getTransactionTime()
	{
		return transactionTime;
	}

	public void setTransactionTime(Long transactionTime)
	{
		this.transactionTime = transactionTime;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public Long getFirstUpdateId()
	{
		return firstUpdateId;
	}

	public void setFirstUpdateId(Long firstUpdateId)
	{
		this.firstUpdateId = firstUpdateId;
	}

	public Long getLastUpdateId()
	{
		return lastUpdateId;
	}

	public void setLastUpdateId(Long lastUpdateId)
	{
		this.lastUpdateId = lastUpdateId;
	}

	public Long getLastUpdateIdInlastStream()
	{
		return lastUpdateIdInlastStream;
	}

	public void setLastUpdateIdInlastStream(Long lastUpdateIdInlastStream)
	{
		this.lastUpdateIdInlastStream = lastUpdateIdInlastStream;
	}

	public List<List<BigDecimal>> getBids()
	{
		return bids;
	}

	public void setBids(List<List<BigDecimal>> bids)
	{
		this.bids = bids;
	}

	public List<List<BigDecimal>> getAsks()
	{
		return asks;
	}

	public void setAsks(List<List<BigDecimal>> asks)
	{
		this.asks = asks;
	}

}
