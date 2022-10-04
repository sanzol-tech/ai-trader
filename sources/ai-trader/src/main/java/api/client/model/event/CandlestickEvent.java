package api.client.model.event;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CandlestickEvent
{
	@JsonProperty("e")
	private String eventType;

	@JsonProperty("E")
	private Long eventTime;

	@JsonProperty("s")
	private String symbol;

	@JsonProperty("k")
	private Kline kline;

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

	public Kline getKline()
	{
		return kline;
	}

	public void setKline(Kline kline)
	{
		this.kline = kline;
	}

	public class Kline
	{
		@JsonProperty("t")
		private Long openTime;

		@JsonProperty("T")
		private Long closeTime;

		@JsonProperty("s")
		private String symbol;

		@JsonProperty("i")
		private String interval;

		@JsonProperty("f")
		private Long firstTradeId;

		@JsonProperty("L")
		private Long lastTradeId;

		@JsonProperty("o")
		private BigDecimal open;

		@JsonProperty("c")
		private BigDecimal close;

		@JsonProperty("h")
		private BigDecimal high;

		@JsonProperty("l")
		private BigDecimal low;

		@JsonProperty("v")
		private BigDecimal volume;

		@JsonProperty("n")
		private Long numTrades;

		@JsonProperty("x")
		private Boolean isClosed;

		@JsonProperty("q")
		private BigDecimal quoteAssetVolume;

		@JsonProperty("V")
		private BigDecimal takerBuyBaseAssetVolume;

		@JsonProperty("Q")
		private BigDecimal takerBuyQuoteAssetVolume;

		@JsonProperty("B")
		private Long ignore;

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

		public String getSymbol()
		{
			return symbol;
		}

		public void setSymbol(String symbol)
		{
			this.symbol = symbol;
		}

		public String getInterval()
		{
			return interval;
		}

		public void setInterval(String interval)
		{
			this.interval = interval;
		}

		public Long getFirstTradeId()
		{
			return firstTradeId;
		}

		public void setFirstTradeId(Long firstTradeId)
		{
			this.firstTradeId = firstTradeId;
		}

		public Long getLastTradeId()
		{
			return lastTradeId;
		}

		public void setLastTradeId(Long lastTradeId)
		{
			this.lastTradeId = lastTradeId;
		}

		public BigDecimal getOpen()
		{
			return open;
		}

		public void setOpen(BigDecimal open)
		{
			this.open = open;
		}

		public BigDecimal getClose()
		{
			return close;
		}

		public void setClose(BigDecimal close)
		{
			this.close = close;
		}

		public BigDecimal getHigh()
		{
			return high;
		}

		public void setHigh(BigDecimal high)
		{
			this.high = high;
		}

		public BigDecimal getLow()
		{
			return low;
		}

		public void setLow(BigDecimal low)
		{
			this.low = low;
		}

		public BigDecimal getVolume()
		{
			return volume;
		}

		public void setVolume(BigDecimal volume)
		{
			this.volume = volume;
		}

		public Long getNumTrades()
		{
			return numTrades;
		}

		public void setNumTrades(Long numTrades)
		{
			this.numTrades = numTrades;
		}

		public Boolean getIsClosed()
		{
			return isClosed;
		}

		public void setIsClosed(Boolean isClosed)
		{
			this.isClosed = isClosed;
		}

		public BigDecimal getQuoteAssetVolume()
		{
			return quoteAssetVolume;
		}

		public void setQuoteAssetVolume(BigDecimal quoteAssetVolume)
		{
			this.quoteAssetVolume = quoteAssetVolume;
		}

		public BigDecimal getTakerBuyBaseAssetVolume()
		{
			return takerBuyBaseAssetVolume;
		}

		public void setTakerBuyBaseAssetVolume(BigDecimal takerBuyBaseAssetVolume)
		{
			this.takerBuyBaseAssetVolume = takerBuyBaseAssetVolume;
		}

		public BigDecimal getTakerBuyQuoteAssetVolume()
		{
			return takerBuyQuoteAssetVolume;
		}

		public void setTakerBuyQuoteAssetVolume(BigDecimal takerBuyQuoteAssetVolume)
		{
			this.takerBuyQuoteAssetVolume = takerBuyQuoteAssetVolume;
		}

		public Long getIgnore()
		{
			return ignore;
		}

		public void setIgnore(Long ignore)
		{
			this.ignore = ignore;
		}

	}

}
