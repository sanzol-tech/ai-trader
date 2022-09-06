package api.client.futures.sync.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeFilter
{
	private String filterType;
	private Long maxNumOrders;
	private Long maxNumAlgoOrders;

	public String getFilterType()
	{
		return filterType;
	}

	public void setFilterType(String filterType)
	{
		this.filterType = filterType;
	}

	public Long getMaxNumOrders()
	{
		return maxNumOrders;
	}

	public void setMaxNumOrders(Long maxNumOrders)
	{
		this.maxNumOrders = maxNumOrders;
	}

	public Long getMaxNumAlgoOrders()
	{
		return maxNumAlgoOrders;
	}

	public void setMaxNumAlgoOrders(Long maxNumAlgoOrders)
	{
		this.maxNumAlgoOrders = maxNumAlgoOrders;
	}

}
