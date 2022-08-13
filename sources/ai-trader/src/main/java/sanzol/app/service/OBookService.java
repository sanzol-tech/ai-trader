package sanzol.app.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.binance.client.SubscriptionClient;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.event.OrderBookEvent;
import com.binance.client.model.market.OrderBook;
import com.binance.client.model.market.OrderBookEntry;

import sanzol.app.config.Application;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.WsClient;
import sanzol.app.model.OrderBookElement;
import sanzol.app.task.LogService;
import sanzol.app.task.PriceService;

public class OBookService
{
	private Symbol symbol;
	private BigDecimal blockSize;
	private double lastPrice;

	private TreeMap<BigDecimal, BigDecimal> mapAsks = new TreeMap<BigDecimal, BigDecimal>();
	private TreeMap<BigDecimal, BigDecimal> mapBids = new TreeMap<BigDecimal, BigDecimal>(Collections.reverseOrder());

	private List<OrderBookElement> asks;
	private List<OrderBookElement> bids;

	private List<OrderBookElement> asksGrp;
	private List<OrderBookElement> asksGrp10;
	private List<OrderBookElement> bidsGrp;
	private List<OrderBookElement> bidsGrp10;

	private BigDecimal askBBlkPoint1;
	private BigDecimal bidBBlkPoint1;
	private BigDecimal askBBlkPoint2;
	private BigDecimal bidBBlkPoint2;

	private BigDecimal askWAvgPoint1;
	private BigDecimal bidWAvgPoint1;
	private BigDecimal askWAvgPoint2;
	private BigDecimal bidWAvgPoint2;

	private BigDecimal askFixedPoint1;
	private BigDecimal bidFixedPoint1;
	private BigDecimal askFixedPoint2;
	private BigDecimal bidFixedPoint2;

	private OBookService()
	{
		//
	}

	public static OBookService getInstance(Symbol symbol)
	{
		OBookService obServise = new OBookService();
		obServise.symbol = symbol;
		return obServise;
	}

	public OBookService request()
	{
		if (OBookCache.IS_CACHE_ACTIVE)
		{
			if ("BTC".equals(symbol.getNameLeft()))
			{
				return OBookCache.getObServiceBTC();
			}
			else if ("ETH".equals(symbol.getNameLeft()))
			{
				return OBookCache.getObServiceETH();
			}
			else if ("BNB".equals(symbol.getNameLeft()))
			{
				return OBookCache.getObServiceBNB();
			}
		}

		SyncRequestClient syncRequestClient = SyncRequestClient.create();
		OrderBook obook = syncRequestClient.getOrderBook(symbol.getNameLeft() + Config.DEFAULT_SYMBOL_RIGHT, 1000);

		//maxPrice = obook.getAsks().get(0).getPrice().doubleValue() * 10;
		for (OrderBookEntry entry : obook.getAsks())
		{
			/*
			if (entry.getPrice().doubleValue() >= maxPrice)
			{
				break;
			}
			*/
			mapAsks.put(entry.getPrice(), entry.getQty());
		}

		//minPrice = Math.pow(10, -symbol.getTickSize()) * 10;
		//minPrice = obook.getBids().get(0).getPrice().doubleValue() / 10;
		for (OrderBookEntry entry : obook.getBids())
		{
			/*
			if (entry.getPrice().doubleValue() <= minPrice)
			{
				break;
			}
			*/
			mapBids.put(entry.getPrice(), entry.getQty());
		}

		return this;
	}

	private Long subscriptionTime = null;
	private OrderBookEvent prevEvent = null;
	public OBookService subscribeDiffDepthEvent(SubscriptionClient client) 
	{
		subscriptionTime = System.currentTimeMillis();

		client.subscribeDiffDepthEvent(symbol.getName().toLowerCase(), ((event) -> {

			if (prevEvent != null && !event.getLastUpdateIdInlastStream().equals(prevEvent.getLastUpdateId()))
			{
				mapAsks.clear();
				mapBids.clear();
				prevEvent = null;
				subscriptionTime = System.currentTimeMillis();

				LogService.info(symbol.getName() + " : Reset orderBook cache");
			}

			for (OrderBookEntry entry : event.getAsks())
			{
				if (entry.getQty().doubleValue() == 0)
					mapAsks.remove(entry.getPrice());
				else
					mapAsks.put(entry.getPrice(), entry.getQty());
			}
			for (OrderBookEntry entry : event.getBids())
			{
				if (entry.getQty().doubleValue() == 0)
					mapBids.remove(entry.getPrice());
				else
					mapBids.put(entry.getPrice(), entry.getQty());
			}

			prevEvent = event;

		}), (ex) -> { WsClient.restarByError(ex); });			

		return this;
	}

	public boolean isSubscriptionTimeOK()
	{
		return (subscriptionTime == null || subscriptionTime + TimeUnit.MINUTES.toMillis(3) < System.currentTimeMillis());
	}
	
	public OBookService calc()
	{
		return calc(Config.getBlocksToAnalizeBB(), Config.getBlocksToAnalizeWA());
	}

	public OBookService calc(int blocksToAnalizeBB, int blocksToAnalizeWA)
	{
		lastPrice = PriceService.getLastPrice(symbol).doubleValue();
		blockSize = getBlockSize(lastPrice);

		loadAsks();
		loadBids();
		asksGrp = loadAsksGrp(blockSize);
		asksGrp10 = loadAsksGrp(blockSize.multiply(BigDecimal.TEN));
		bidsGrp = loadBidsGrp(blockSize);
		bidsGrp10 = loadBidsGrp(blockSize.multiply(BigDecimal.TEN));

		//BB
		{
			double askPriceTo = lastPrice + (blockSize.doubleValue() * blocksToAnalizeBB);
			double bidPriceTo = lastPrice - (blockSize.doubleValue() * blocksToAnalizeBB);
			this.askBBlkPoint1 = getBestBlockAsks(asksGrp, lastPrice, askPriceTo);
			this.bidBBlkPoint1 = getBestBlockBids(bidsGrp, lastPrice, bidPriceTo);

			double askPriceFrom = askBBlkPoint1.doubleValue();
			double bidPriceFrom = bidBBlkPoint1.doubleValue();
			askPriceTo = askPriceFrom + (blockSize.doubleValue() * 10 * blocksToAnalizeBB);
			bidPriceTo = bidPriceFrom - (blockSize.doubleValue() * 10 * blocksToAnalizeBB);
			this.askBBlkPoint2 = getBestBlockAsks(asksGrp10, askPriceFrom, askPriceTo);
			this.bidBBlkPoint2 = getBestBlockBids(bidsGrp10, bidPriceFrom, bidPriceTo);
		}

		//WA
		{
			double askPriceTo = lastPrice + (blockSize.doubleValue() * blocksToAnalizeWA);
			double bidPriceTo = lastPrice - (blockSize.doubleValue() * blocksToAnalizeWA);
			this.askWAvgPoint1 = weightedAverageAsks(lastPrice, askPriceTo);
			this.bidWAvgPoint1 = weightedAverageBids(lastPrice, bidPriceTo);

			double askPriceFrom = askWAvgPoint1.doubleValue();
			double bidPriceFrom = bidWAvgPoint1.doubleValue();
			askPriceTo = lastPrice + (blockSize.doubleValue() * blocksToAnalizeWA * 2);
			bidPriceTo = lastPrice - (blockSize.doubleValue() * blocksToAnalizeWA * 2);
			this.askWAvgPoint2 = weightedAverageAsks(askPriceFrom, askPriceTo);
			this.bidWAvgPoint2 = weightedAverageBids(bidPriceFrom, bidPriceTo);
		}

		fixShocks();

		return this;
	}

	public void fixShocks()
	{
		/*
		if (askWAvgPoint1 != null && askWAvgPoint1.doubleValue() > askBBlkPoint1.doubleValue())
			askFixedPoint1 = askWAvgPoint1;
		else
			askFixedPoint1 = askBBlkPoint1;

		if (bidWAvgPoint1 != null && bidWAvgPoint1.doubleValue() < bidBBlkPoint1.doubleValue())
			bidFixedPoint1 = bidWAvgPoint1;
		else
			bidFixedPoint1 = bidBBlkPoint1;
		

		if (askWAvgPoint2 != null && askWAvgPoint2.doubleValue() > askBBlkPoint2.doubleValue())
			askFixedPoint2 = askWAvgPoint2;
		else
			askFixedPoint2 = askBBlkPoint2;

		if (bidWAvgPoint2 != null && bidWAvgPoint2.doubleValue() < bidBBlkPoint2.doubleValue())
			bidFixedPoint2 = bidWAvgPoint2;
		else
			bidFixedPoint2 = bidBBlkPoint2;
		*/

		askFixedPoint1 = askBBlkPoint1;
		bidFixedPoint1 = bidBBlkPoint1;
		askFixedPoint2 = askBBlkPoint2;
		bidFixedPoint2 = bidBBlkPoint2;

		/*
		askFixedPoint1 = askWAvgPoint1;
		bidFixedPoint1 = bidWAvgPoint1;
		askFixedPoint2 = askWAvgPoint2;
		bidFixedPoint2 = bidWAvgPoint2;
		*/
	}

	// ------------------------------------------------------------------------

	public BigDecimal getAskBBlkPoint1()
	{
		return askBBlkPoint1;
	}

	public BigDecimal getBidBBlkPoint1()
	{
		return bidBBlkPoint1;
	}

	public BigDecimal getAskBBlkPoint2()
	{
		return askBBlkPoint2;
	}

	public BigDecimal getBidBBlkPoint2()
	{
		return bidBBlkPoint2;
	}

	public BigDecimal getAskWAvgPoint1()
	{
		return askWAvgPoint1;
	}

	public BigDecimal getBidWAvgPoint1()
	{
		return bidWAvgPoint1;
	}

	public BigDecimal getAskWAvgPoint2()
	{
		return askWAvgPoint2;
	}

	public BigDecimal getBidWAvgPoint2()
	{
		return bidWAvgPoint2;
	}

	public BigDecimal getAskFixedPoint1()
	{
		return askFixedPoint1;
	}

	public BigDecimal getBidFixedPoint1()
	{
		return bidFixedPoint1;
	}

	public BigDecimal getAskFixedPoint2()
	{
		return askFixedPoint2;
	}

	public BigDecimal getBidFixedPoint2()
	{
		return bidFixedPoint2;
	}
	
	// ------------------------------------------------------------------------

	public BigDecimal getTotalAsksQty()
	{
		BigDecimal total = BigDecimal.ZERO;
		if (mapAsks != null && !mapAsks.isEmpty())
		{
			for (BigDecimal qty : mapAsks.values())
			{
				total = total.add(qty);
			}
		}
		return total;
	}

	public BigDecimal getTotalBidsQty()
	{
		BigDecimal total = BigDecimal.ZERO;
		if (mapBids != null && !mapBids.isEmpty())
		{
			for (BigDecimal qty : mapBids.values())
			{
				total = total.add(qty);
			}
		}
		return total;
	}

	public BigDecimal getBestBlockAsks(List<OrderBookElement> asksGrp, double priceA, double priceB)
	{
		if (asksGrp != null && !asksGrp.isEmpty())
		{
			OrderBookElement eMax = null;
			for (OrderBookElement e : asksGrp)
			{
				if (e.getPrice().doubleValue() <= priceA)
				{
					continue;
				}
				if (e.getPrice().doubleValue() > priceB)
				{
					break;
				}

				if ((eMax == null) || (eMax.getQty().compareTo(e.getQty()) == -1))
				{
					eMax = e;
				}
			}
			return eMax.getPrice();
		}
		return null;
	}

	public BigDecimal getBestBlockBids(List<OrderBookElement> bidsGrp, double priceA, double priceB)
	{
		if (bidsGrp != null && !bidsGrp.isEmpty())
		{
			OrderBookElement eMax = null;
			for (OrderBookElement e : bidsGrp)
			{
				if (e.getPrice().doubleValue() >= priceA)
				{
					continue;
				}
				if (e.getPrice().doubleValue() < priceB)
				{
					break;
				}

				if ((eMax == null) || (eMax.getQty().compareTo(e.getQty()) == -1))
				{
					eMax = e;
				}
			}
			return eMax.getPrice();
		}
		return null;
	}

	// ------------------------------------------------------------------------

	public BigDecimal weightedAverageAsks(double priceA, double priceB)
	{
		BigDecimal sumProd = BigDecimal.ZERO;
		BigDecimal sumQty = BigDecimal.ZERO;

		for (OrderBookElement entry : asks)
		{
			if (entry.getPrice().doubleValue() <= priceA)
			{
				continue;
			}
			if (entry.getPrice().doubleValue() > priceB)
			{
				break;
			}
			sumProd = sumProd.add(entry.getPrice().multiply(entry.getQty()));
			sumQty = sumQty.add(entry.getQty());
		}

		return sumProd.divide(sumQty, RoundingMode.HALF_UP);
	}

	public BigDecimal weightedAverageBids(double priceA, double priceB)
	{
		BigDecimal sumProd = BigDecimal.ZERO;
		BigDecimal sumQty = BigDecimal.ZERO;

		for (OrderBookElement entry : bids)
		{
			if (entry.getPrice().doubleValue() >= priceA)
			{
				continue;
			}
			if (entry.getPrice().doubleValue() < priceB)
			{
				break;
			}
			sumProd = sumProd.add(entry.getPrice().multiply(entry.getQty()));
			sumQty = sumQty.add(entry.getQty());
		}

		return sumProd.divide(sumQty, RoundingMode.HALF_UP);
	}
	
	// ------------------------------------------------------------------------

	private void loadAsks()
	{
		asks = new ArrayList<OrderBookElement>();
		if (mapAsks != null && !mapAsks.isEmpty())
		{
			BigDecimal sumQty = BigDecimal.ZERO;
			BigDecimal firstPrice = mapAsks.firstKey();
			BigDecimal totalQty = getTotalAsksQty();

			for (Map.Entry<BigDecimal, BigDecimal> entry : mapAsks.entrySet()) 
			{
				BigDecimal entryPrice = entry.getKey();
				BigDecimal entryQty = entry.getValue();
				
				sumQty = sumQty.add(entryQty);
				BigDecimal sumPercent = sumQty.divide(totalQty, 4, RoundingMode.HALF_UP);
				BigDecimal distance = entryPrice.subtract(firstPrice).divide(firstPrice, 4, RoundingMode.HALF_UP);

				asks.add(new OrderBookElement(entryPrice, entryQty, sumQty, sumPercent, distance));
			}
		}
	}

	private void loadBids()
	{
		bids = new ArrayList<OrderBookElement>();
		if (mapBids != null && !mapBids.isEmpty())
		{
			BigDecimal sumQty = BigDecimal.ZERO;
			BigDecimal firstPrice = mapBids.firstKey();
			BigDecimal totalQty = getTotalBidsQty();

			for (Map.Entry<BigDecimal, BigDecimal> entry : mapBids.entrySet()) 
			{
				BigDecimal entryPrice = entry.getKey();
				BigDecimal entryQty = entry.getValue();
				
				sumQty = sumQty.add(entryQty);
				BigDecimal sumPercent = sumQty.divide(totalQty, 4, RoundingMode.HALF_UP);
				BigDecimal distance = entryPrice.subtract(firstPrice).divide(firstPrice, 4, RoundingMode.HALF_UP).abs();

				bids.add(new OrderBookElement(entryPrice, entryQty, sumQty, sumPercent, distance));
			}
		}
	}

	private ArrayList<OrderBookElement> loadAsksGrp(BigDecimal blockSize)
	{
		ArrayList<OrderBookElement> asksGrp = new ArrayList<OrderBookElement>();

		if (asks == null || asks.isEmpty())
		{
			return asksGrp;
		}

		//BigDecimal blockSize = getBlockSize();
		BigDecimal price = asks.get(0).getPrice();
		BigDecimal priceBlock = price.add(blockSize);
		BigDecimal qty = asks.get(0).getQty();

		for (int i = 1; i < asks.size(); i++)
		{
			if (asks.get(i).getPrice().doubleValue() < priceBlock.doubleValue())
			{
				qty = qty.add(asks.get(i).getQty());
			}
			else
			{
				OrderBookElement prev = asks.get(i - 1);
				OrderBookElement newElement = new OrderBookElement();
				newElement.setPrice(prev.getPrice());
				newElement.setQty(qty);
				newElement.setSumQty(prev.getSumQty());
				newElement.setSumPercent(prev.getSumPercent());
				asksGrp.add(newElement);

				priceBlock = priceBlock.add(blockSize);
				qty = asks.get(i).getQty();
			}
		}
		OrderBookElement prev = asks.get(asks.size() - 1);
		OrderBookElement newElement = new OrderBookElement();
		newElement.setPrice(prev.getPrice());
		newElement.setQty(qty);
		newElement.setSumQty(prev.getSumQty());
		newElement.setSumPercent(prev.getSumPercent());
		asksGrp.add(newElement);
		
		return asksGrp; 
	}

	private ArrayList<OrderBookElement> loadBidsGrp(BigDecimal blockSize)
	{
		ArrayList<OrderBookElement> bidsGrp = new ArrayList<OrderBookElement>();

		if (bids == null || bids.isEmpty())
		{
			return bidsGrp;
		}

		//BigDecimal blockSize = getBlockSize();
		BigDecimal price = bids.get(0).getPrice();
		BigDecimal priceBlock = price.subtract(blockSize);
		BigDecimal qty = bids.get(0).getQty();

		for (int i = 1; i < bids.size(); i++)
		{
			if (bids.get(i).getPrice().doubleValue() > priceBlock.doubleValue())
			{
				qty = qty.add(bids.get(i).getQty());
			}
			else
			{
				OrderBookElement prev = bids.get(i - 1);
				OrderBookElement newElement = new OrderBookElement();
				newElement.setPrice(prev.getPrice());
				newElement.setQty(qty);
				newElement.setSumQty(prev.getSumQty());
				newElement.setSumPercent(prev.getSumPercent());
				bidsGrp.add(newElement);

				priceBlock = priceBlock.subtract(blockSize);
				qty = bids.get(i).getQty();
			}
		}
		OrderBookElement prev = bids.get(bids.size() - 1);
		OrderBookElement newElement = new OrderBookElement();
		newElement.setPrice(prev.getPrice());
		newElement.setQty(qty);
		newElement.setSumQty(prev.getSumQty());
		newElement.setSumPercent(prev.getSumPercent());
		bidsGrp.add(newElement);
		
		return bidsGrp;
	}

	private BigDecimal getBlockSize(double price)
	{
		if (price < 0.01)
			return BigDecimal.valueOf(0.00001);
		else if (price < 0.1)
			return BigDecimal.valueOf(0.0001);
		else if (price < 1)
			return BigDecimal.valueOf(0.001);
		else if (price < 10)
			return BigDecimal.valueOf(0.01);
		else if (price < 100)
			return BigDecimal.valueOf(0.1);
		else if (price < 1000)
			return BigDecimal.valueOf(1);
		else if (price < 10000)
			return BigDecimal.valueOf(10);
		else
			return BigDecimal.valueOf(100);
	}

	// ------------------------------------------------------------------------

	public List<OrderBookElement> searchSuperAskBlocks()
	{
		return searchSuperAskBlocks(Config.getBlocksToAnalizeWA() * 2);
	}

	public List<OrderBookElement> searchSuperAskBlocks(int blocksToAnalize)
	{
		double maxPrice_ = lastPrice + (blockSize.doubleValue() * blocksToAnalize);
		
		List<OrderBookElement> list = new ArrayList<OrderBookElement>();

		double avgQty = 0;
		int count = 0;
		for (OrderBookElement e : asks)
		{
			if (e.getPrice().doubleValue() > maxPrice_)
			{
				break;
			}
			
			avgQty += e.getQty().doubleValue();
			count++;
		}
		avgQty = avgQty / count;

		for (OrderBookElement e : asks)
		{
			if (e.getPrice().doubleValue() > maxPrice_)
			{
				continue;
			}

			if (e.getQty().doubleValue() < avgQty * 3)
			{
				continue;
			}
			
			list.add(new OrderBookElement(e.getPrice(), e.getQty(), e.getSumQty(), e.getSumPercent(), e.getDistance()));
		}
		Collections.sort(list, Comparator.comparing(OrderBookElement::getQty).reversed());
		list = list.stream().limit(10).collect(Collectors.toList());
		Collections.sort(list, Comparator.comparing(OrderBookElement::getPrice).reversed());
		
		return list;
	}

	public List<OrderBookElement> searchSuperBidBlocks()
	{
		return searchSuperBidBlocks(Config.getBlocksToAnalizeWA() * 2);
	}

	public List<OrderBookElement> searchSuperBidBlocks(int blocksToAnalize)
	{
		double minPrice_ = lastPrice - (blockSize.doubleValue() * blocksToAnalize);
		
		List<OrderBookElement> list = new ArrayList<OrderBookElement>();

		double avgQty = 0;
		int count = 0;
		for (OrderBookElement e : bids)
		{
			if (e.getPrice().doubleValue() < minPrice_)
			{
				break;
			}
			
			avgQty += e.getQty().doubleValue();
			count++;
		}
		avgQty = avgQty / count;

		for (OrderBookElement e : bids)
		{
			if (e.getPrice().doubleValue() < minPrice_)
			{
				continue;
			}

			if (e.getQty().doubleValue() < avgQty * 3)
			{
				continue;
			}
			
			list.add(new OrderBookElement(e.getPrice(), e.getQty(), e.getSumQty(), e.getSumPercent(), e.getDistance()));
		}
		Collections.sort(list, Comparator.comparing(OrderBookElement::getQty).reversed());
		list = list.stream().limit(10).collect(Collectors.toList());
		Collections.sort(list, Comparator.comparing(OrderBookElement::getPrice).reversed());

		return list;
	}	

	public String printSuperBlks(List<OrderBookElement> list)
	{
		if (list != null && !list.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (OrderBookElement ele : list)
			{
				sb.append(String.format("%-10s  %10s\n", symbol.priceToStr(ele.getPrice()), symbol.qtyToStr(ele.getQty())));
			}
			return sb.toString();
		}
		return "";
	}

	// ------------------------------------------------------------------------

	public String printAsks()
	{
		if (asks != null && !asks.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (int i = asks.size() - 1; i >= 0; i--)
			{
				OrderBookElement ele = asks.get(i);
				sb.append(String.format("%-10s  %10s  %10s  %8.2f %%  %8.2f %%\n", symbol.priceToStr(ele.getPrice()), symbol.qtyToStr(ele.getQty()), symbol.qtyToStr(ele.getSumQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0)), ele.getDistance().multiply(BigDecimal.valueOf(100.0))));
			}
			return sb.toString();
		}
		return null;
	}

	public String printBids()
	{
		if (bids != null && !bids.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (OrderBookElement ele : bids)
			{
				sb.append(String.format("%-10s  %10s  %10s  %8.2f %%  %8.2f %%\n", symbol.priceToStr(ele.getPrice()), symbol.qtyToStr(ele.getQty()), symbol.qtyToStr(ele.getSumQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0)), ele.getDistance().multiply(BigDecimal.valueOf(100.0))));
			}
			return sb.toString();
		}
		return null;
	}

	public String printAsksGrp()
	{
		if (asksGrp != null && !asksGrp.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (int i = asksGrp.size() - 1; i >= 0; i--)
			{
				OrderBookElement ele = asksGrp.get(i);
				sb.append(String.format("%-12s  %12s  %9.2f %%\n", symbol.priceToStr(ele.getPrice()), symbol.qtyToStr(ele.getQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0))));
			}
			return sb.toString();
		}
		return null;
	}

	public String printBidsGrp()
	{
		if (bidsGrp != null && !bidsGrp.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (OrderBookElement ele : bidsGrp)
			{
				sb.append(String.format("%-12s  %12s  %9.2f %%\n", symbol.priceToStr(ele.getPrice()), symbol.qtyToStr(ele.getQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0))));
			}
			return sb.toString();
		}
		return null;
	}

	// -----------------------------------------------------------------------

	public void export() throws IOException
	{
		if (asks != null && !asks.isEmpty())
		{
			StringBuilder sbAsks = new StringBuilder();
			for (OrderBookElement ele : asks)
			{
				sbAsks.append(ele.toString());
				sbAsks.append("\n");
			}
			File fileExportAsks = new File(Constants.DEFAULT_EXPORT_FOLDER, symbol.getNameLeft() + "_depth_asks.csv");
			FileUtils.writeStringToFile(fileExportAsks, sbAsks.toString(), StandardCharsets.UTF_8);
		}

		if (bids != null && !bids.isEmpty())
		{
			StringBuilder sbBids = new StringBuilder();
			for (OrderBookElement ele : bids)
			{
				sbBids.append(ele.toString());
				sbBids.append("\n");
			}
			File fileExportBids = new File(Constants.DEFAULT_EXPORT_FOLDER, symbol.getNameLeft() + "_depth_bids.csv");
			FileUtils.writeStringToFile(fileExportBids, sbBids.toString(), StandardCharsets.UTF_8);
		}
	}	
	
	// ------------------------------------------------------------------------

	public static void main(String[] args) throws Exception
	{
		Application.initialize();

		String symbol = "LINK";
		Symbol coin = Symbol.getInstance(Symbol.getFullSymbol(symbol));

		//OBookService obService = OBookService.getInstance(coin).request().subscribeDiffDepthEvent();
		//OBookService obService = OBookService.getInstance(coin).subscribeDiffDepthEvent();
		OBookService obService = OBookService.getInstance(coin).request();		
		Thread.sleep(500);
		obService.calc(6, 10);

		System.out.println("");
		System.out.println(coin.getNameLeft());

		//System.out.println("");
		//System.out.println(obService.printAsks());
		//System.out.println("");
		//System.out.println(obService.printBids());
		System.out.println("");
		System.out.println(obService.printAsksGrp());
		System.out.println("");
		System.out.println(obService.printBidsGrp());

		System.out.println("");
		System.out.println("SHORT B.Blk:  " + obService.getAskBBlkPoint1());
		System.out.println("SHORT W.AVG:  " + obService.getAskWAvgPoint1());
		System.out.println("SHORT W.AVG2: " + obService.getAskWAvgPoint2());

		System.out.println("");
		System.out.println("Price:        " + PriceService.getLastPrice(coin));

		System.out.println("");
		System.out.println("LONG B.Blk:   " + obService.getBidBBlkPoint1());
		System.out.println("LONG W.AVG:   " + obService.getBidWAvgPoint1());
		System.out.println("LONG W.AVG2:  " + obService.getBidWAvgPoint2());
		
		System.out.println("");
		List<OrderBookElement> listSa = obService.searchSuperAskBlocks();
		System.out.println(obService.printSuperBlks(listSa));

		System.out.println("");
		List<OrderBookElement> listSb = obService.searchSuperBidBlocks();
		System.out.println(obService.printSuperBlks(listSb));

	}
	
}
