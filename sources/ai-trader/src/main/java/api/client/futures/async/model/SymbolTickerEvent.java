package api.client.futures.async.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SymbolTickerEvent
{
	@JsonProperty("e")
	private String eventType;

	@JsonProperty("E")
	private Long eventTime;

	@JsonProperty("s")
	private String symbol;

	@JsonProperty("p")
	private BigDecimal priceChange;

	@JsonProperty("P")
	private BigDecimal priceChangePercent;

	@JsonProperty("w")
	private BigDecimal weightedAvgPrice;

	@JsonProperty("c")
	private BigDecimal lastPrice;

	@JsonProperty("Q")
	private BigDecimal lastQty;

	@JsonProperty("o")
	private BigDecimal openPrice;

	@JsonProperty("h")
	private BigDecimal highPrice;

	@JsonProperty("l")
	private BigDecimal lowPrice;

	@JsonProperty("v")
	private BigDecimal volume;

	@JsonProperty("q")
	private BigDecimal quoteVolume;

	@JsonProperty("O")
	private Long openTime;

	@JsonProperty("C")
	private Long closeTime;

	@JsonProperty("F")
	private Long firstId;

	@JsonProperty("L")
	private Long lastId;

	@JsonProperty("n")
	private Long count;

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

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getPriceChange()
	{
		return priceChange;
	}

	public void setPriceChange(BigDecimal priceChange)
	{
		this.priceChange = priceChange;
	}

	public BigDecimal getPriceChangePercent()
	{
		return priceChangePercent;
	}

	public void setPriceChangePercent(BigDecimal priceChangePercent)
	{
		this.priceChangePercent = priceChangePercent;
	}

	public BigDecimal getWeightedAvgPrice()
	{
		return weightedAvgPrice;
	}

	public void setWeightedAvgPrice(BigDecimal weightedAvgPrice)
	{
		this.weightedAvgPrice = weightedAvgPrice;
	}

	public BigDecimal getLastPrice()
	{
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice)
	{
		this.lastPrice = lastPrice;
	}

	public BigDecimal getLastQty()
	{
		return lastQty;
	}

	public void setLastQty(BigDecimal lastQty)
	{
		this.lastQty = lastQty;
	}

	public BigDecimal getOpenPrice()
	{
		return openPrice;
	}

	public void setOpenPrice(BigDecimal openPrice)
	{
		this.openPrice = openPrice;
	}

	public BigDecimal getHighPrice()
	{
		return highPrice;
	}

	public void setHighPrice(BigDecimal highPrice)
	{
		this.highPrice = highPrice;
	}

	public BigDecimal getLowPrice()
	{
		return lowPrice;
	}

	public void setLowPrice(BigDecimal lowPrice)
	{
		this.lowPrice = lowPrice;
	}

	public BigDecimal getVolume()
	{
		return volume;
	}

	public void setVolume(BigDecimal volume)
	{
		this.volume = volume;
	}

	public BigDecimal getQuoteVolume()
	{
		return quoteVolume;
	}

	public void setQuoteVolume(BigDecimal quoteVolume)
	{
		this.quoteVolume = quoteVolume;
	}

	public Long getOpenTime()
	{
		return openTime;
	}

	public void setOpenTime(Long openTime)
	{
		this.openTime = openTime;
	}

	public Long getCloseTime()
	{
		return closeTime;
	}

	public void setCloseTime(Long closeTime)
	{
		this.closeTime = closeTime;
	}

	public Long getFirstId()
	{
		return firstId;
	}

	public void setFirstId(Long firstId)
	{
		this.firstId = firstId;
	}

	public Long getLastId()
	{
		return lastId;
	}

	public void setLastId(Long lastId)
	{
		this.lastId = lastId;
	}

	public Long getCount()
	{
		return count;
	}

	public void setCount(Long count)
	{
		this.count = count;
	}

}
