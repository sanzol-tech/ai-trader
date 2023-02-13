package binance.futures.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDataUpdateEvent
{
	@JsonProperty("e")
	private String eventType;

	@JsonProperty("E")
	private Long eventTime;

	@JsonProperty("T")
	private Long transactionTime;

	@JsonProperty("a")
	private AccountUpdate accountUpdate;

	@JsonProperty("o")
	private OrderUpdate orderUpdate;

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

	public AccountUpdate getAccountUpdate()
	{
		return accountUpdate;
	}

	public void setAccountUpdate(AccountUpdate accountUpdate)
	{
		this.accountUpdate = accountUpdate;
	}

	public OrderUpdate getOrderUpdate()
	{
		return orderUpdate;
	}

	public void setOrderUpdate(OrderUpdate orderUpdate)
	{
		this.orderUpdate = orderUpdate;
	}

	@Override
	public String toString()
	{
		return "UserDataUpdateEvent [eventType=" + eventType + ", eventTime=" + eventTime + ", transactionTime=" + transactionTime + "]\naccountUpdate\n" + accountUpdate + "\norderUpdate\n" + orderUpdate;
	}

}
