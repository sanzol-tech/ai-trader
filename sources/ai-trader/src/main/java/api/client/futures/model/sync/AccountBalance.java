package api.client.futures.model.sync;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalance
{
	private String asset;
	private BigDecimal balance;
	private BigDecimal withdrawAvailable;

	public String getAsset()
	{
		return asset;
	}

	public void setAsset(String asset)
	{
		this.asset = asset;
	}

	public BigDecimal getBalance()
	{
		return balance;
	}

	public void setBalance(BigDecimal balance)
	{
		this.balance = balance;
	}

	public BigDecimal getWithdrawAvailable()
	{
		return withdrawAvailable;
	}

	public void setWithdrawAvailable(BigDecimal withdrawAvailable)
	{
		this.withdrawAvailable = withdrawAvailable;
	}

}
