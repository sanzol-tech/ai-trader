package exchanges.binance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Depth
{
	private Long lastUpdateId;
	private List<List<BigDecimal>> asks = new ArrayList<List<BigDecimal>>();
	private List<List<BigDecimal>> bids = new ArrayList<List<BigDecimal>>();

	public Long getLastUpdateId()
	{
		return lastUpdateId;
	}

	public void setLastUpdateId(Long lastUpdateId)
	{
		this.lastUpdateId = lastUpdateId;
	}

	public List<List<BigDecimal>> getAsks()
	{
		return asks;
	}

	public void setAsks(List<List<BigDecimal>> asks)
	{
		this.asks = asks;
	}

	public List<List<BigDecimal>> getBids()
	{
		return bids;
	}

	public void setBids(List<List<BigDecimal>> bids)
	{
		this.bids = bids;
	}

}
