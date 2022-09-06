package api.client.futures.sync.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeInfo
{
	private String timezone;
	private Long serverTime;
	private List<RateLimit> rateLimits;
	private List<ExchangeFilter> exchangeFilters;
	private List<ExchangeInfoEntry> symbols;

	public String getTimezone()
	{
		return timezone;
	}

	public void setTimezone(String timezone)
	{
		this.timezone = timezone;
	}

	public Long getServerTime()
	{
		return serverTime;
	}

	public void setServerTime(Long serverTime)
	{
		this.serverTime = serverTime;
	}

	public List<RateLimit> getRateLimits()
	{
		return rateLimits;
	}

	public void setRateLimits(List<RateLimit> rateLimits)
	{
		this.rateLimits = rateLimits;
	}

	public List<ExchangeFilter> getExchangeFilters()
	{
		return exchangeFilters;
	}

	public void setExchangeFilters(List<ExchangeFilter> exchangeFilters)
	{
		this.exchangeFilters = exchangeFilters;
	}

	public List<ExchangeInfoEntry> getSymbols()
	{
		return symbols;
	}

	public void setSymbols(List<ExchangeInfoEntry> symbols)
	{
		this.symbols = symbols;
	}

}
