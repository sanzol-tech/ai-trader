package binance.spot.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account
{
	private int makerCommission;
	private int takerCommission;
	private int buyerCommission;
	private int sellerCommission;
	private boolean canTrade;
	private boolean canWithdraw;
	private boolean canDeposit;
	private long updateTime;
	private ArrayList<AssetBalance> balances;

	public int getMakerCommission()
	{
		return makerCommission;
	}

	public void setMakerCommission(int makerCommission)
	{
		this.makerCommission = makerCommission;
	}

	public int getTakerCommission()
	{
		return takerCommission;
	}

	public void setTakerCommission(int takerCommission)
	{
		this.takerCommission = takerCommission;
	}

	public int getBuyerCommission()
	{
		return buyerCommission;
	}

	public void setBuyerCommission(int buyerCommission)
	{
		this.buyerCommission = buyerCommission;
	}

	public int getSellerCommission()
	{
		return sellerCommission;
	}

	public void setSellerCommission(int sellerCommission)
	{
		this.sellerCommission = sellerCommission;
	}

	public boolean isCanTrade()
	{
		return canTrade;
	}

	public void setCanTrade(boolean canTrade)
	{
		this.canTrade = canTrade;
	}

	public boolean isCanWithdraw()
	{
		return canWithdraw;
	}

	public void setCanWithdraw(boolean canWithdraw)
	{
		this.canWithdraw = canWithdraw;
	}

	public boolean isCanDeposit()
	{
		return canDeposit;
	}

	public void setCanDeposit(boolean canDeposit)
	{
		this.canDeposit = canDeposit;
	}

	public long getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(long updateTime)
	{
		this.updateTime = updateTime;
	}

	public ArrayList<AssetBalance> getBalances()
	{
		return balances;
	}

	public void setBalances(ArrayList<AssetBalance> balances)
	{
		this.balances = balances;
	}

}
