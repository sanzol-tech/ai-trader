package binance.futures.model.user;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionUpdate
{
	@JsonProperty("s")
	private String symbol;

	@JsonProperty("pa")
	private BigDecimal positionAmount;

	@JsonProperty("ep")
	private BigDecimal entryPrice;

	@JsonProperty("cr")
	private BigDecimal preFee;

	@JsonProperty("up")
	private BigDecimal unrealizedPnl;

	@JsonProperty("ps")
	private String positionSide;

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getPositionAmount()
	{
		return positionAmount;
	}

	public void setPositionAmount(BigDecimal positionAmount)
	{
		this.positionAmount = positionAmount;
	}

	public BigDecimal getEntryPrice()
	{
		return entryPrice;
	}

	public void setEntryPrice(BigDecimal entryPrice)
	{
		this.entryPrice = entryPrice;
	}

	public BigDecimal getPreFee()
	{
		return preFee;
	}

	public void setPreFee(BigDecimal preFee)
	{
		this.preFee = preFee;
	}

	public BigDecimal getUnrealizedPnl()
	{
		return unrealizedPnl;
	}

	public void setUnrealizedPnl(BigDecimal unrealizedPnl)
	{
		this.unrealizedPnl = unrealizedPnl;
	}

	public String getPositionSide()
	{
		return positionSide;
	}

	public void setPositionSide(String positionSide)
	{
		this.positionSide = positionSide;
	}

	@Override
	public String toString()
	{
		return "PositionUpdate [symbol=" + symbol + ", positionAmount=" + positionAmount + ", entryPrice=" + entryPrice 
				+ ", preFee=" + preFee + ", unrealizedPnl=" + unrealizedPnl + ", positionSide="	+ positionSide + "]";
	}

}
