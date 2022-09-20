package api.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RateLimit
{
	private String rateLimitType;
	private String interval;
	private Long intervalNum;
	private Long limit;

	public String getRateLimitType()
	{
		return rateLimitType;
	}

	public void setRateLimitType(String rateLimitType)
	{
		this.rateLimitType = rateLimitType;
	}

	public String getInterval()
	{
		return interval;
	}

	public void setInterval(String interval)
	{
		this.interval = interval;
	}

	public Long getIntervalNum()
	{
		return intervalNum;
	}

	public void setIntervalNum(Long intervalNum)
	{
		this.intervalNum = intervalNum;
	}

	public Long getLimit()
	{
		return limit;
	}

	public void setLimit(Long limit)
	{
		this.limit = limit;
	}

}
