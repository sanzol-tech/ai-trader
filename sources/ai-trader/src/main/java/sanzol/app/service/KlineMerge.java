package sanzol.app.service;

import java.math.BigDecimal;
import java.util.List;

import api.client.model.sync.Kline;
import sanzol.app.util.PriceUtil;

public class KlineMerge extends Kline
{
	BigDecimal priceChange;
	BigDecimal priceChangePercent;
	BigDecimal maxPriceVariance;

	public KlineMerge()
	{
		super();
	}

	public KlineMerge(BigDecimal openPrice, BigDecimal highPrice, BigDecimal lowPrice, BigDecimal closePrice, BigDecimal volume, BigDecimal quoteVolume, Long count, 
					  BigDecimal priceChange, BigDecimal priceChangePercent, BigDecimal maxPriceVariance, BigDecimal weightedAvgPrice)
	{
		super(openPrice, highPrice, lowPrice, closePrice, volume, quoteVolume, count);

		this.priceChange = priceChange;
		this.priceChangePercent = priceChangePercent;
		this.maxPriceVariance = maxPriceVariance;
	}

	public static KlineMerge getInstance(List<Kline> lst)
	{
		if (lst == null || lst.isEmpty())
		{
			return null;
		}

		BigDecimal openPrice = lst.get(0).getOpenPrice();
		BigDecimal highPrice = lst.get(0).getHighPrice();
		BigDecimal lowPrice = lst.get(0).getLowPrice();
		BigDecimal closePrice = lst.get(lst.size() - 1).getClosePrice();
		BigDecimal volume = BigDecimal.ZERO;
		BigDecimal quoteVolume = BigDecimal.ZERO;
		Long count = 0L;

		for (Kline entry : lst)
		{
			if (highPrice.doubleValue() < entry.getHighPrice().doubleValue())
				highPrice = entry.getHighPrice();

			if (lowPrice.doubleValue() > entry.getLowPrice().doubleValue())
				lowPrice = entry.getLowPrice();

			volume = volume.add(entry.getVolume());
			quoteVolume = quoteVolume.add(entry.getQuoteVolume());
			count = count + entry.getCount();
		}

		BigDecimal priceChange = closePrice.subtract(openPrice);
		BigDecimal priceChangePercent = PriceUtil.priceChange(openPrice, closePrice, false);
		BigDecimal maxPriceVariance = PriceUtil.priceChange(lowPrice, highPrice, false);
		BigDecimal weightedAvgPrice = BigDecimal.ZERO;
		
		KlineMerge klineMerge = new KlineMerge(openPrice, highPrice, lowPrice, closePrice, volume, quoteVolume, count, priceChange, priceChangePercent, maxPriceVariance, weightedAvgPrice);
		return klineMerge;
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

	public BigDecimal getMaxPriceVariance()
	{
		return maxPriceVariance;
	}

	public void setMaxPriceVariance(BigDecimal maxPriceVariance)
	{
		this.maxPriceVariance = maxPriceVariance;
	}

	@Override
	public String toString()
	{
		return super.toString() + "\n" + "KlineMerge [priceChange=" + priceChange + ", priceChangePercent=" + priceChangePercent + ", maxPriceVariance=" + maxPriceVariance + "]";
	}

}