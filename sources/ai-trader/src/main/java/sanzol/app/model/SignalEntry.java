package sanzol.app.model;

import java.math.BigDecimal;

import sanzol.app.config.CharConstants;

public class SignalEntry
{
	private Symbol coin;
	private BigDecimal price;
	private BigDecimal shShock;
	private BigDecimal lgShock;
	private BigDecimal distShLg;
	private BigDecimal distShort;
	private BigDecimal distLong;
	private String action;

	public SignalEntry()
	{
		//
	}

	public SignalEntry(Symbol coin, BigDecimal shShock, BigDecimal lgShock)
	{
		this.coin = coin;
		this.shShock = shShock;
		this.lgShock = lgShock;
	}

	public SignalEntry(Symbol coin, BigDecimal price, BigDecimal shShock, BigDecimal lgShock, BigDecimal distShLg, BigDecimal distShort, BigDecimal distLong, String action)
	{
		this.coin = coin;
		this.price = price;
		this.shShock = shShock;
		this.lgShock = lgShock;
		this.distShLg = distShLg;
		this.distShort = distShort;
		this.distLong = distLong;
		this.action = action;
	}

	public Symbol getCoin()
	{
		return coin;
	}

	public void setCoin(Symbol coin)
	{
		this.coin = coin;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getShShock()
	{
		return shShock;
	}

	public void setShShock(BigDecimal shShock)
	{
		this.shShock = shShock;
	}

	public BigDecimal getLgShock()
	{
		return lgShock;
	}

	public void setLgShock(BigDecimal lgShock)
	{
		this.lgShock = lgShock;
	}

	public BigDecimal getDistShLg()
	{
		return distShLg;
	}

	public void setDistShLg(BigDecimal distShLg)
	{
		this.distShLg = distShLg;
	}

	public BigDecimal getDistShort()
	{
		return distShort;
	}

	public void setDistShort(BigDecimal distShort)
	{
		this.distShort = distShort;
	}

	public BigDecimal getDistLong()
	{
		return distLong;
	}

	public void setDistLong(BigDecimal distLong)
	{
		this.distLong = distLong;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------

	public String getStrShort()
	{
		return coin.priceToStr(shShock);
	}

	public String getStrLong()
	{
		return coin.priceToStr(lgShock);
	}

	// -------------------------------------------------------------------------

	@Override
	public String toString()
	{
		return String.format("%s;%s;%s\n", coin.getNameLeft(), coin.priceToStr(shShock), coin.priceToStr(lgShock));
	}

	public String toStringAll()
	{
		if (price == null || price.equals(BigDecimal.valueOf(-1)))
		{
			return "";
		}

		return String.format("%-8s %14s %14s %14s %7.2f %% %7.2f %% %7.2f %%  %s\n", coin.getNameLeft(), coin.priceToStr(shShock), coin.priceToStr(price), coin.priceToStr(lgShock), distShLg, distShort, distLong, action);
	}

	public String toStringSmart()
	{
		if (price == null || price.equals(BigDecimal.valueOf(-1)))
		{
			return "";
		}

		if (action.startsWith("SHORT"))
			return String.format("%-8s :  %-10s %6.2f %%   %s", coin.getNameLeft(), action, distShort, coin.priceToStr(shShock) + " " + CharConstants.ARROW_RIGHT + " " + coin.priceToStr(price));
		else
			return String.format("%-8s :  %-10s %6.2f %%   %s", coin.getNameLeft(), action, distLong, coin.priceToStr(lgShock) + " " + CharConstants.ARROW_RIGHT + " " + coin.priceToStr(price));
	}

}
