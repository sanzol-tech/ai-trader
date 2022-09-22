package api.client.spot.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetBalance
{
	private String asset;
	private BigDecimal free;
	private BigDecimal locked;

	public String getAsset()
	{
		return asset;
	}

	public void setAsset(String asset)
	{
		this.asset = asset;
	}

	public BigDecimal getFree()
	{
		return free;
	}

	public void setFree(BigDecimal free)
	{
		this.free = free;
	}

	public BigDecimal getLocked()
	{
		return locked;
	}

	public void setLocked(BigDecimal locked)
	{
		this.locked = locked;
	}

	public BigDecimal getQuantity()
	{
		return free.add(locked);
	}

	@Override
	public String toString()
	{
		return "AssetBalance [asset=" + asset + ", free=" + free.toPlainString() + ", locked=" + locked.toPlainString() + "]";
	}

}
