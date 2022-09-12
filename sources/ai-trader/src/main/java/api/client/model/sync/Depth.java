package api.client.model.sync;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

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

	public TreeMap<BigDecimal, DepthEntry> getMapAsks()
	{
		TreeMap<BigDecimal, DepthEntry> map = new TreeMap<BigDecimal, DepthEntry>();
		for (List<BigDecimal> entry : asks)
		{
			BigDecimal price = entry.get(0).stripTrailingZeros();
			BigDecimal qty = entry.get(1).stripTrailingZeros();
			
			map.put(price, new DepthEntry(price, qty));
		}
		return map;
	}

	public TreeMap<BigDecimal, DepthEntry> getMapBids()
	{
		TreeMap<BigDecimal, DepthEntry> map = new TreeMap<BigDecimal, DepthEntry>(Collections.reverseOrder());
		for (List<BigDecimal> entry : bids)
		{
			BigDecimal price = entry.get(0).stripTrailingZeros();
			BigDecimal qty = entry.get(1).stripTrailingZeros();
			
			map.put(price, new DepthEntry(price, qty));
		}
		return map;
	}

}
