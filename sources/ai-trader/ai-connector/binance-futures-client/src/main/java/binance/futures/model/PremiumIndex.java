package binance.futures.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremiumIndex
{

	private String symbol;
	private BigDecimal markPrice;
	private BigDecimal indexPrice;
	private BigDecimal estimatedSettlePrice;
	private BigDecimal lastFundingRate;
	private long nextFundingTime;
	private BigDecimal interestRate;
	private long time;

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getMarkPrice()
	{
		return markPrice;
	}

	public void setMarkPrice(BigDecimal markPrice)
	{
		this.markPrice = markPrice;
	}

	public BigDecimal getIndexPrice()
	{
		return indexPrice;
	}

	public void setIndexPrice(BigDecimal indexPrice)
	{
		this.indexPrice = indexPrice;
	}

	public BigDecimal getEstimatedSettlePrice()
	{
		return estimatedSettlePrice;
	}

	public void setEstimatedSettlePrice(BigDecimal estimatedSettlePrice)
	{
		this.estimatedSettlePrice = estimatedSettlePrice;
	}

	public BigDecimal getLastFundingRate()
	{
		return lastFundingRate;
	}

	public void setLastFundingRate(BigDecimal lastFundingRate)
	{
		this.lastFundingRate = lastFundingRate;
	}

	public long getNextFundingTime()
	{
		return nextFundingTime;
	}

	public void setNextFundingTime(long nextFundingTime)
	{
		this.nextFundingTime = nextFundingTime;
	}

	public BigDecimal getInterestRate()
	{
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate)
	{
		this.interestRate = interestRate;
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	// ---- CALCULATED FIELDS -------------------------------------------------

	public ZonedDateTime getNextFundingTimeZoned()
	{
		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(nextFundingTime), ZoneOffset.UTC);
	}

	public ZonedDateTime getTimeZoned()
	{
		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.UTC);
	}

	// ---- TO STRING ---------------------------------------------------------

	@Override
	public String toString()
	{
		return "FundingRate [symbol=" + symbol + ", lastFundingRate=" + lastFundingRate + ", nextFundingTime=" + getNextFundingTimeZoned() + "]";
	}

}
