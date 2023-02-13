package technicals.test;

import java.math.BigDecimal;
import java.util.Arrays;

import exchanges.binance.BinanceApiClient;
import exchanges.binance.BinanceUtils;
import exchanges.binance.Depth;
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

	public static void main(String[] args) throws Exception
	{
		Depth depth = BinanceApiClient.getDepth("ADAUSDT", 5000);
		int precision = 4;
		OrderBook orderBook = BinanceUtils.toOrderBook(depth, precision);

		long t1 = System.currentTimeMillis();

		// Volatility
		BigDecimal dtr = new DepthTrueRange(orderBook).calculate(0.15, true);
		System.out.println("dtr = " + dtr);

		// Trend
		BigDecimal dmp = new DepthMiddlePrice(orderBook).calculate(0.15, true);
		System.out.println("dma = " + dmp);

		// Delta
		DepthDelta delta = new DepthDelta(orderBook).calculate(0.15);
		System.out.println("AskSumQuoted = " + delta.getAskSumQuoted() + ", BidSumQuoted = " + delta.getBidSumQuoted());

		// Prices with the largest number of orders
		DepthSuperPrices depthSuperPrices = new DepthSuperPrices(orderBook).searchSuperAskPrices(12).searchSuperBidPrices(12);
		System.out.println(Arrays.toString(depthSuperPrices.getAsks().toArray()));
		System.out.println(Arrays.toString(depthSuperPrices.getBids().toArray()));

		// Support & Resistance - WA 
		DepthWeightedPoints dwap = new DepthWeightedPoints(orderBook).calculate(0.1, 0.2, 0.3);
		System.out.println("r3: " + dwap.getR3() + ", r2: " + dwap.getR2() + ", r1: " + dwap.getR1() + ", s1: " + dwap.getS1() + ", s2: " + dwap.getS2() + ", s3: " + dwap.getS3());		

		// Support & Resistance - BB
		DepthBlockPoints dbbp = new DepthBlockPoints(orderBook).calculate(10);
		System.out.println("r2: " + dbbp.getR2() + ", r1: " + dbbp.getR1() + ", s1: " + dbbp.getS1() + ", s2: " + dbbp.getS2());		

		DephMergedPoints fix = DephMergedPoints.getInstance().calculate(dbbp, dwap, MergeMode.MAX); 
		System.out.println("r2: " + fix.getR2() + ", r1: " + fix.getR1() + ", s1: " + fix.getS1() + ", s2: " + fix.getS2());		
		
		long t2 = System.currentTimeMillis();

		System.out.println(t2 - t1 + " msecs\n");
	}

}
