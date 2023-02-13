package aitrader.core.service.market;

import java.math.BigDecimal;

import aitrader.core.model.Symbol;
import aitrader.core.model.enums.DepthMode;
import technicals.indicators.depth.DephMergedPoints;
import technicals.indicators.depth.DephMergedPoints.MergeMode;
import technicals.indicators.depth.DepthBlockPoints;
import technicals.indicators.depth.DepthDelta;
import technicals.indicators.depth.DepthMiddlePrice;
import technicals.indicators.depth.DepthSuperPrices;
import technicals.indicators.depth.DepthTrueRange;
import technicals.indicators.depth.DepthWeightedPoints;
import technicals.model.OrderBook;

public class DepthSummary
{
	private DepthService depthService;
	private OrderBook orderBook;

	private BigDecimal depthTrueRange = null;
	private BigDecimal depthMiddlePrice = null;
	private DepthDelta[] depthDelta = null;
	private DepthBlockPoints depthBlockPoints = null;
	private DepthWeightedPoints depthWeightedPoints = null;
	private DephMergedPoints dephMergedPoints = null;
	private DepthSuperPrices depthSuperPrices = null;

	public OrderBook getOrderBook()
	{
		return orderBook;
	}

	public BigDecimal getDepthTrueRange()
	{
		return depthTrueRange;
	}

	public BigDecimal getDepthMiddlePrice()
	{
		return depthMiddlePrice;
	}

	public DepthDelta[] getDepthDelta()
	{
		return depthDelta;
	}

	public DepthBlockPoints getDepthBlockPoints()
	{
		return depthBlockPoints;
	}

	public DepthWeightedPoints getDepthWeightedPoints()
	{
		return depthWeightedPoints;
	}

	public DephMergedPoints getDephMergedPoints()
	{
		return dephMergedPoints;
	}

	public DepthSuperPrices getDepthSuperPrices()
	{
		return depthSuperPrices;
	}

	public static DepthSummary getInstance(Symbol symbol, DepthMode mode, long timeOut) throws Exception
	{
		DepthSummary depthSummary = new DepthSummary();

		DepthService depthService = DepthService.getInstance(symbol.getPair(), mode, timeOut);
		depthSummary.depthService = depthService;

		OrderBook orderBook = depthService.toOrderBook();
		depthSummary.orderBook = orderBook;

		return depthSummary;
	}

	public boolean verifyConnectTime(long minAge)
	{
		return (depthService.getConnectTime() == null) || (depthService.getConnectTime() + minAge < System.currentTimeMillis());
	}

	public DepthSummary calcDepthTrueRange(double distance, boolean inPercentage)
	{
		depthTrueRange = new DepthTrueRange(orderBook).calculate(distance, inPercentage);
		return this;
	}

	public DepthSummary calcDepthMiddlePrice(double distance, boolean quoted)
	{
		depthMiddlePrice = new DepthMiddlePrice(orderBook).calculate(distance, quoted);
		return this;
	}

	public DepthSummary calcDepthDelta(Double... depthPercent)
	{
		depthDelta = new DepthDelta[depthPercent.length];

		int i = 0;
		for (Double distance : depthPercent)
		{
			depthDelta[i] = new DepthDelta(orderBook).calculate(distance);
			i++;
		}
		return this;
	}

	public DepthSummary calcDepthBlockPoints(int blocks)
	{
		depthBlockPoints = new DepthBlockPoints(orderBook).calculate(blocks);
		return this;
	}

	public DepthSummary calcDepthWeightedPoints(double distance1, double distance2, double distance3)
	{
		depthWeightedPoints = new DepthWeightedPoints(orderBook).calculate(distance1, distance2, distance3);
		return this;
	}

	public DepthSummary calcDephMergedPoints(MergeMode mergeMode)
	{
		dephMergedPoints = DephMergedPoints.getInstance().calculate(depthBlockPoints, depthWeightedPoints, mergeMode);
		return this;
	}

	public DepthSummary calcDepthSuperPrices(int maxSize)
	{
		depthSuperPrices = new DepthSuperPrices(orderBook)
							.searchSuperAskPrices(maxSize)
							.searchSuperBidPrices(maxSize);
		return this;
	}

}
