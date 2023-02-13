package binance.futures.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FundingRate
{
	private String symbol;
	private BigDecimal fundingRate;
	private long fundingTime;

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getFundingRate()
	{
		return fundingRate;
	}

	public void setFundingRate(BigDecimal fundingRate)
	{
		this.fundingRate = fundingRate;
	}

	public long getFundingTime()
	{
		return fundingTime;
	}

	public void setFundingTime(long fundingTime)
	{
		this.fundingTime = fundingTime;
	}

	// ---- CALCULATED FIELDS -------------------------------------------------

	public ZonedDateTime getFundingTimeZoned()
	{
		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(fundingTime), ZoneOffset.UTC);
	}

	// ---- TO STRING ---------------------------------------------------------

	@Override
	public String toString()
	{
		return "FundingRate [symbol=" + symbol + ", fundingRate=" + fundingRate + ", fundingTime=" + getFundingTimeZoned() + "]";
	}

}
