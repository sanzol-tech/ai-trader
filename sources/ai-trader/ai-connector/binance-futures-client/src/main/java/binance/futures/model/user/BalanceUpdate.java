package binance.futures.model.user;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceUpdate
{
	@JsonProperty("a")
	private String asset;

	@JsonProperty("wb")
	private BigDecimal walletBalance;

	@JsonProperty("cw")
	private BigDecimal crossWalletBalance;

	@JsonProperty("bc")
	private BigDecimal balanceChange;

	public String getAsset()
	{
		return asset;
	}

	public void setAsset(String asset)
	{
		this.asset = asset;
	}

	public BigDecimal getWalletBalance()
	{
		return walletBalance;
	}

	public void setWalletBalance(BigDecimal walletBalance)
	{
		this.walletBalance = walletBalance;
	}

	public BigDecimal getCrossWalletBalance()
	{
		return crossWalletBalance;
	}

	public void setCrossWalletBalance(BigDecimal crossWalletBalance)
	{
		this.crossWalletBalance = crossWalletBalance;
	}

	public BigDecimal getBalanceChange()
	{
		return balanceChange;
	}

	public void setBalanceChange(BigDecimal balanceChange)
	{
		this.balanceChange = balanceChange;
	}

	@Override
	public String toString()
	{
		return "BalanceUpdate [asset=" + asset + ", walletBalance=" + walletBalance + ", crossWalletBalance=" + crossWalletBalance + ", balanceChange=" + balanceChange + "]";
	}

}
