package sanzol.app.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import api.client.config.ApiConfig;
import api.client.enums.DepthMode;
import api.client.model.sync.DepthEntry;
import api.client.service.DepthService;
import api.client.service.ExchangeInfoService;
import api.client.service.PriceService;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.util.PriceUtil;

public class OrderBookService
{
	private Symbol symbol;
	private BigDecimal lastPrice;

	private TreeMap<BigDecimal, DepthEntry> mapAsks = new TreeMap<BigDecimal, DepthEntry>();
	private TreeMap<BigDecimal, DepthEntry> mapBids = new TreeMap<BigDecimal, DepthEntry>(Collections.reverseOrder());

	private List<DepthEntry> lstAsks;
	private List<DepthEntry> lstBids;

	private BigDecimal blockSize0;
	private List<DepthEntry> asksGrp0;
	private List<DepthEntry> bidsGrp0;

	private BigDecimal blockSize1;
	private List<DepthEntry> asksGrp1;
	private List<DepthEntry> bidsGrp1;

	private BigDecimal blockSize2;
	private List<DepthEntry> asksGrp2;
	private List<DepthEntry> bidsGrp2;

	private BigDecimal askBBlkPoint1;
	private BigDecimal bidBBlkPoint1;
	private BigDecimal askBBlkPoint2;
	private BigDecimal bidBBlkPoint2;

	private BigDecimal askWAvgPoint1;
	private BigDecimal bidWAvgPoint1;
	private BigDecimal askWAvgPoint2;
	private BigDecimal bidWAvgPoint2;
	private BigDecimal askWAvgPoint3;
	private BigDecimal bidWAvgPoint3;

	private BigDecimal askFixedPoint1;
	private BigDecimal bidFixedPoint1;
	private BigDecimal askFixedPoint2;
	private BigDecimal bidFixedPoint2;
	private BigDecimal askFixedPoint3;
	private BigDecimal bidFixedPoint3;

	private OrderBookService()
	{
		//
	}

	public static OrderBookService getInstance(Symbol symbol)
	{
		OrderBookService depth = new OrderBookService();
		depth.symbol = symbol;
		return depth;
	}

	private DepthService depthClient;

	public OrderBookService request(DepthMode mode, long timeOut) throws KeyManagementException, NoSuchAlgorithmException
	{
		depthClient = DepthService.getInstance(symbol.getPair(), mode, timeOut);

		mapAsks = depthClient.getMapAsks();
		mapBids = depthClient.getMapBids();

		return this;
	}

	public boolean verifyConnectTime(long minAge)
	{
		return (depthClient.getConnectTime() == null) || (depthClient.getConnectTime() + minAge < System.currentTimeMillis());
	}

	public enum BBType	{ classic, displaced }
	public enum WAType { price, previous }
	
	public OrderBookService calc()
	{
		return calc(BBType.classic, Config.getBlocksToAnalizeBB(), WAType.price, Config.getBlocksToAnalizeWA());
	}

	public OrderBookService calc(BBType bbType, int blocksToAnalizeBB, WAType waType , int blocksToAnalizeWA)
	{
		//long t1 = System.currentTimeMillis();
		
		lastPrice = PriceService.getLastPrice(symbol);

		blockSize0 = getBlockSize(lastPrice, BigDecimal.valueOf(0.1));
		blockSize1 = getBlockSize(lastPrice, BigDecimal.valueOf(1));
		blockSize2 = getBlockSize(lastPrice, BigDecimal.valueOf(10));

		lstAsks = new ArrayList<DepthEntry>(mapAsks.values());
		lstBids = new ArrayList<DepthEntry>(mapBids.values());

		if (bbType == BBType.classic)
			calcBBClassic(BigDecimal.valueOf(blocksToAnalizeBB));
		else
			calcBBlock(BigDecimal.valueOf(blocksToAnalizeBB));

		if (waType == WAType.price)
			calcWAvg0(BigDecimal.valueOf(blocksToAnalizeWA));
		else
			calcWAvg1(BigDecimal.valueOf(blocksToAnalizeWA));

		fixPoints(FixPointsMode.BB);

		//long t = System.currentTimeMillis() - t1;
		//System.out.println("calc " + symbol.getPair() + " -> " + t + " msecs " + t);

		return this;
	}

	private void calcBBClassic(BigDecimal blocks)
	{
		BigDecimal askPriceFrom0 = roundNearest(lastPrice, blockSize0);
		BigDecimal bidPriceFrom0 = askPriceFrom0.add(blockSize0);
		BigDecimal askPriceFrom1 = roundNearest(lastPrice, blockSize1);
		BigDecimal bidPriceFrom1 = askPriceFrom1.add(blockSize1);
		BigDecimal askPriceFrom2 = roundNearest(lastPrice, blockSize2);
		BigDecimal bidPriceFrom2 = askPriceFrom2.add(blockSize2);

		BigDecimal askPriceTo1 = askPriceFrom1.add(blockSize1.multiply(blocks));
		BigDecimal bidPriceTo1 = bidPriceFrom1.subtract(blockSize1.multiply(blocks));
		BigDecimal askPriceTo2 = askPriceFrom2.add(blockSize2.multiply(blocks));
		BigDecimal bidPriceTo2 = bidPriceFrom2.subtract(blockSize2.multiply(blocks));

		asksGrp0 = loadAsksGrp(lstAsks, blockSize0, askPriceFrom0);
		bidsGrp0 = loadBidsGrp(lstBids, blockSize0, bidPriceFrom0);
		asksGrp1 = loadAsksGrp(lstAsks, blockSize1, askPriceFrom1);
		bidsGrp1 = loadBidsGrp(lstBids, blockSize1, bidPriceFrom1);
		asksGrp2 = loadAsksGrp(lstAsks, blockSize2, askPriceFrom2);
		bidsGrp2 = loadBidsGrp(lstBids, blockSize2, bidPriceFrom2);

		this.askBBlkPoint1 = getBestBlockAsks(asksGrp1, askPriceFrom1, askPriceTo1);
		this.bidBBlkPoint1 = getBestBlockBids(bidsGrp1, bidPriceFrom1, bidPriceTo1);
		this.askBBlkPoint2 = getBestBlockAsks(asksGrp2, askPriceFrom2, askPriceTo2);
		this.bidBBlkPoint2 = getBestBlockBids(bidsGrp2, bidPriceFrom2, bidPriceTo2);
	}

	private void calcBBlock(BigDecimal blocks)
	{
		BigDecimal askPriceTo1 = lastPrice.add(blockSize1.multiply(blocks));
		BigDecimal bidPriceTo1 = lastPrice.subtract(blockSize1.multiply(blocks));
		BigDecimal askPriceTo2 = lastPrice.add(blockSize2.multiply(blocks));
		BigDecimal bidPriceTo2 = lastPrice.subtract(blockSize2.multiply(blocks));

		asksGrp0 = loadAsksGrp(lstAsks, blockSize0, lastPrice);
		bidsGrp0 = loadBidsGrp(lstBids, blockSize0, lastPrice);
		asksGrp1 = loadAsksGrp(lstAsks, blockSize1, lastPrice);
		bidsGrp1 = loadBidsGrp(lstBids, blockSize1, lastPrice);
		asksGrp2 = loadAsksGrp(lstAsks, blockSize2, lastPrice);
		bidsGrp2 = loadBidsGrp(lstBids, blockSize2, lastPrice);

		this.askBBlkPoint1 = getBestBlockAsks(asksGrp1, lastPrice, askPriceTo1);
		this.bidBBlkPoint1 = getBestBlockBids(bidsGrp1, lastPrice, bidPriceTo1);
		this.askBBlkPoint2 = getBestBlockAsks(asksGrp2, lastPrice, askPriceTo2);
		this.bidBBlkPoint2 = getBestBlockBids(bidsGrp2, lastPrice, bidPriceTo2);
	}

	private void calcWAvg0(BigDecimal blocks)
	{
		BigDecimal minPrice = symbol.getTickSize().multiply(BigDecimal.valueOf(10));
		BigDecimal maxPrice = lastPrice.multiply(BigDecimal.valueOf(3));
		
		BigDecimal askPriceTo1 = lastPrice.add(blockSize1.multiply(blocks));
		BigDecimal bidPriceTo1 = lastPrice.subtract(blockSize1.multiply(blocks));
		this.askWAvgPoint1 = weightedAverageAsks(lstAsks, lastPrice, askPriceTo1);
		this.bidWAvgPoint1 = weightedAverageBids(lstBids, lastPrice, bidPriceTo1);

		BigDecimal askPriceTo2 = lastPrice.add(blockSize2.multiply(blocks));
		BigDecimal bidPriceTo2 = lastPrice.subtract(blockSize2.multiply(blocks));
		this.askWAvgPoint2 = weightedAverageAsks(lstAsks, lastPrice, askPriceTo2);
		this.bidWAvgPoint2 = weightedAverageBids(lstBids, lastPrice, bidPriceTo2);

		BigDecimal blockSize3 = blockSize2.multiply(BigDecimal.TEN);
		BigDecimal askPriceTo3 = lastPrice.add(blockSize3.multiply(blocks)).min(maxPrice);
		BigDecimal bidPriceTo3 = lastPrice.subtract(blockSize3.multiply(blocks)).max(minPrice);
		this.askWAvgPoint3 = weightedAverageAsks(lstAsks, lastPrice, askPriceTo3);
		this.bidWAvgPoint3 = weightedAverageBids(lstBids, lastPrice, bidPriceTo3);
	}

	private void calcWAvg1(BigDecimal blocks)
	{
		BigDecimal minPrice = symbol.getTickSize().multiply(BigDecimal.valueOf(10));
		BigDecimal maxPrice = lastPrice.multiply(BigDecimal.valueOf(3));
		
		BigDecimal askPriceTo1 = lastPrice.add(blockSize1.multiply(blocks));
		BigDecimal bidPriceTo1 = lastPrice.subtract(blockSize1.multiply(blocks));
		this.askWAvgPoint1 = weightedAverageAsks(lstAsks, lastPrice, askPriceTo1);
		this.bidWAvgPoint1 = weightedAverageBids(lstBids, lastPrice, bidPriceTo1);

		BigDecimal askPriceFrom2 = symbol.addTicks(askWAvgPoint1, 1);
		BigDecimal bidPriceFrom2 = symbol.subTicks(bidWAvgPoint1, 1);
		BigDecimal askPriceTo2 = lastPrice.add(blockSize2.multiply(blocks));
		BigDecimal bidPriceTo2 = lastPrice.subtract(blockSize2.multiply(blocks));
		this.askWAvgPoint2 = weightedAverageAsks(lstAsks, askPriceFrom2, askPriceTo2);
		this.bidWAvgPoint2 = weightedAverageBids(lstBids, bidPriceFrom2, bidPriceTo2);

		BigDecimal askPriceFrom3 = symbol.addTicks(askWAvgPoint2, 1);
		BigDecimal bidPriceFrom3 = symbol.subTicks(bidWAvgPoint2, 1);
		BigDecimal blockSize3 = blockSize2.multiply(BigDecimal.TEN);
		BigDecimal askPriceTo3 = lastPrice.add(blockSize3.multiply(blocks)).min(maxPrice);
		BigDecimal bidPriceTo3 = lastPrice.subtract(blockSize3.multiply(blocks)).max(minPrice);
		this.askWAvgPoint3 = weightedAverageAsks(lstAsks, askPriceFrom3, askPriceTo3);
		this.bidWAvgPoint3 = weightedAverageBids(lstBids, bidPriceFrom3, bidPriceTo3);
	}

	enum FixPointsMode { MIX, BB, WA }
	public void fixPoints(FixPointsMode mode)
	{
		if (mode == FixPointsMode.MIX)
		{
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
		}
		else if (mode == FixPointsMode.BB)
		{
			askFixedPoint1 = askBBlkPoint1;
			bidFixedPoint1 = bidBBlkPoint1;
			askFixedPoint2 = askBBlkPoint2;
			bidFixedPoint2 = bidBBlkPoint2;
		}
		else
		{
			askFixedPoint1 = askWAvgPoint1;
			bidFixedPoint1 = bidWAvgPoint1;
			askFixedPoint2 = askWAvgPoint2;
			bidFixedPoint2 = bidWAvgPoint2;
			askFixedPoint3 = askWAvgPoint3;
			bidFixedPoint3 = bidWAvgPoint3;
		}
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

	public BigDecimal getAskWAvgPoint3()
	{
		return askWAvgPoint3;
	}

	public BigDecimal getBidWAvgPoint3()
	{
		return bidWAvgPoint3;
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

	public BigDecimal getAskFixedPoint3()
	{
		return askFixedPoint3;
	}

	public BigDecimal getBidFixedPoint3()
	{
		return bidFixedPoint3;
	}

	// ------------------------------------------------------------------------
	
	private static BigDecimal getBlockSize(BigDecimal price, BigDecimal size)
	{
		if (price.doubleValue() < 0.0001)
			return BigDecimal.valueOf(0.0000001).multiply(size);
		else if (price.doubleValue() < 0.001)
			return BigDecimal.valueOf(0.000001).multiply(size);
		else if (price.doubleValue() < 0.01)
			return BigDecimal.valueOf(0.00001).multiply(size);
		else if (price.doubleValue() < 0.1)
			return BigDecimal.valueOf(0.0001).multiply(size);
		else if (price.doubleValue() < 1)
			return BigDecimal.valueOf(0.001).multiply(size);
		else if (price.doubleValue() < 10)
			return BigDecimal.valueOf(0.01).multiply(size);
		else if (price.doubleValue() < 100)
			return BigDecimal.valueOf(0.1).multiply(size);
		else if (price.doubleValue() < 1000)
			return BigDecimal.valueOf(1).multiply(size);
		else if (price.doubleValue() < 10000)
			return BigDecimal.valueOf(10).multiply(size);
		else
			return BigDecimal.valueOf(100).multiply(size);
	}

	private static BigDecimal roundNearest(BigDecimal price, BigDecimal blockSize)
	{
		double _blockSize = blockSize.doubleValue();
		if (_blockSize < 1)
		{
			if (_blockSize == 0)
			{
				return price.setScale(0, RoundingMode.DOWN);
			}
			else
			{
				int precision = (int) Math.log10(_blockSize) * -1;
				return price.setScale(precision, RoundingMode.DOWN);
			}
		}
		else
		{
			return price.divide(blockSize, 0, RoundingMode.DOWN).multiply(blockSize);
		}
	}

	private ArrayList<DepthEntry> loadAsksGrp(List<DepthEntry> lstAsks, BigDecimal blockSize, BigDecimal priceFrom)
	{
		ArrayList<DepthEntry> asksGrp = new ArrayList<DepthEntry>();

		if (mapAsks == null || mapAsks.isEmpty())
		{
			return asksGrp;
		}

		BigDecimal priceBlock = priceFrom.add(blockSize);
		BigDecimal qty = BigDecimal.ZERO;

		for (int i = 1; i < lstAsks.size(); i++)
		{
			if (lstAsks.get(i).getPrice().doubleValue() <= priceBlock.doubleValue())
			{
				qty = qty.add(lstAsks.get(i).getQty());
			}
			else
			{
				DepthEntry prev = lstAsks.get(i - 1);
				DepthEntry newElement = new DepthEntry();
				newElement.setPrice(prev.getPrice());
				newElement.setQty(qty);
				asksGrp.add(newElement);

				priceBlock = priceBlock.add(blockSize);
				qty = lstAsks.get(i).getQty();
			}
		}
		DepthEntry prev = lstAsks.get(lstAsks.size() - 1);
		DepthEntry newElement = new DepthEntry();
		newElement.setPrice(prev.getPrice());
		newElement.setQty(qty);
		asksGrp.add(newElement);
		
		return asksGrp; 
	}

	private ArrayList<DepthEntry> loadBidsGrp(List<DepthEntry> lstBids, BigDecimal blockSize, BigDecimal priceFrom)
	{
		ArrayList<DepthEntry> bidsGrp = new ArrayList<DepthEntry>();

		if (mapBids == null || mapBids.isEmpty())
		{
			return bidsGrp;
		}

		BigDecimal priceBlock = priceFrom.subtract(blockSize);
		BigDecimal qty = BigDecimal.ZERO; //bids.get(0).getQty();

		for (int i = 1; i < lstBids.size(); i++)
		{
			if (lstBids.get(i).getPrice().doubleValue() >= priceBlock.doubleValue())
			{
				qty = qty.add(lstBids.get(i).getQty());
			}
			else
			{
				DepthEntry prev = lstBids.get(i - 1);
				DepthEntry newElement = new DepthEntry();
				newElement.setPrice(prev.getPrice());
				newElement.setQty(qty);
				bidsGrp.add(newElement);

				priceBlock = priceBlock.subtract(blockSize);
				qty = lstBids.get(i).getQty();
			}
		}
		DepthEntry prev = lstBids.get(lstBids.size() - 1);
		DepthEntry newElement = new DepthEntry();
		newElement.setPrice(prev.getPrice());
		newElement.setQty(qty);
		bidsGrp.add(newElement);
		
		return bidsGrp;
	}

	// ------------------------------------------------------------------------

	public BigDecimal sumQtyAsks()
	{
		BigDecimal total = BigDecimal.ZERO;
		if (mapAsks != null && !mapAsks.isEmpty())
		{
			for (DepthEntry entry : mapAsks.values())
			{
				total = total.add(entry.getQty());
			}
		}
		return total;
	}

	public BigDecimal sumQtyBids()
	{
		BigDecimal total = BigDecimal.ZERO;
		if (mapBids != null && !mapBids.isEmpty())
		{
			for (DepthEntry entry : mapBids.values())
			{
				total = total.add(entry.getQty());
			}
		}
		return total;
	}

	public BigDecimal getBestBlockAsks(List<DepthEntry> asksGrp, BigDecimal priceA, BigDecimal priceB)
	{
		if (asksGrp != null && !asksGrp.isEmpty())
		{
			DepthEntry eMax = null;
			for (DepthEntry e : asksGrp)
			{
				if (e.getPrice().doubleValue() <= priceA.doubleValue())
				{
					continue;
				}
				if (e.getPrice().doubleValue() > priceB.doubleValue())
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

	public BigDecimal getBestBlockBids(List<DepthEntry> bidsGrp, BigDecimal priceA, BigDecimal priceB)
	{
		if (bidsGrp != null && !bidsGrp.isEmpty())
		{
			DepthEntry eMax = null;
			for (DepthEntry e : bidsGrp)
			{
				if (e.getPrice().doubleValue() >= priceA.doubleValue())
				{
					continue;
				}
				if (e.getPrice().doubleValue() < priceB.doubleValue())
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

	public BigDecimal weightedAverageAsks(List<DepthEntry> lstAsks, BigDecimal priceA, BigDecimal priceB)
	{
		BigDecimal sumProd = BigDecimal.ZERO;
		BigDecimal sumQty = BigDecimal.ZERO;

		for (DepthEntry entry : lstAsks)
		{
			if (entry.getPrice().doubleValue() <= priceA.doubleValue())
			{
				continue;
			}
			if (entry.getPrice().doubleValue() > priceB.doubleValue())
			{
				break;
			}
			sumProd = sumProd.add(entry.getPrice().multiply(entry.getQty()));
			sumQty = sumQty.add(entry.getQty());
		}

		return sumProd.divide(sumQty, RoundingMode.HALF_UP);
	}

	public BigDecimal weightedAverageBids(List<DepthEntry> lstBids, BigDecimal priceA, BigDecimal priceB)
	{
		BigDecimal sumProd = BigDecimal.ZERO;
		BigDecimal sumQty = BigDecimal.ZERO;

		for (DepthEntry entry : lstBids)
		{
			if (entry.getPrice().doubleValue() >= priceA.doubleValue())
			{
				continue;
			}
			if (entry.getPrice().doubleValue() < priceB.doubleValue())
			{
				break;
			}
			sumProd = sumProd.add(entry.getPrice().multiply(entry.getQty()));
			sumQty = sumQty.add(entry.getQty());
		}

		return sumProd.divide(sumQty, RoundingMode.HALF_UP);
	}

	// ------------------------------------------------------------------------

	public List<DepthEntry> searchSuperAskBlocks()
	{
		return searchSuperAskBlocks(Config.getBlocksToAnalizeBB() * 2);
	}

	public List<DepthEntry> searchSuperAskBlocks(int blocksToAnalize)
	{
		double maxPrice_ = lastPrice.doubleValue() + (blockSize2.doubleValue() * blocksToAnalize);
		
		List<DepthEntry> list = new ArrayList<DepthEntry>();

		double avgQty = 0;
		int count = 0;
		for (DepthEntry e : mapAsks.values())
		{
			if (e.getPrice().doubleValue() > maxPrice_)
			{
				break;
			}
			
			avgQty += e.getQty().doubleValue();
			count++;
		}
		avgQty = avgQty / count;

		for (DepthEntry e : mapAsks.values())
		{
			if (e.getPrice().doubleValue() > maxPrice_)
			{
				continue;
			}

			if (e.getQty().doubleValue() < avgQty * 2)
			{
				continue;
			}
			
			list.add(new DepthEntry(e.getPrice(), e.getQty()));
		}
		Collections.sort(list, Comparator.comparing(DepthEntry::getQty).reversed());
		list = list.stream().limit(20).collect(Collectors.toList());
		Collections.sort(list, Comparator.comparing(DepthEntry::getPrice).reversed());
		
		return list;
	}

	public List<DepthEntry> searchSuperBidBlocks()
	{
		return searchSuperBidBlocks(Config.getBlocksToAnalizeBB() * 2);
	}

	public List<DepthEntry> searchSuperBidBlocks(int blocksToAnalize)
	{
		double minPrice_ = lastPrice.doubleValue() - (blockSize2.doubleValue() * blocksToAnalize);

		List<DepthEntry> list = new ArrayList<DepthEntry>();

		double avgQty = 0;
		int count = 0;
		for (DepthEntry e : mapBids.values())
		{
			if (e.getPrice().doubleValue() < minPrice_)
			{
				break;
			}
			
			avgQty += e.getQty().doubleValue();
			count++;
		}
		avgQty = avgQty / count;

		for (DepthEntry e : mapBids.values())
		{
			if (e.getPrice().doubleValue() < minPrice_)
			{
				continue;
			}

			if (e.getQty().doubleValue() < avgQty * 2)
			{
				continue;
			}
			
			list.add(new DepthEntry(e.getPrice(), e.getQty()));
		}
		Collections.sort(list, Comparator.comparing(DepthEntry::getQty).reversed());
		list = list.stream().limit(20).collect(Collectors.toList());
		Collections.sort(list, Comparator.comparing(DepthEntry::getPrice).reversed());

		return list;
	}	

	public String printSuperBlks(List<DepthEntry> list)
	{
		if (list != null && !list.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (DepthEntry ele : list)
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
		if (mapAsks != null && !mapAsks.isEmpty())
		{
			List<DepthEntry> lstAsks = new ArrayList<DepthEntry>(mapAsks.values());

			StringBuilder sb = new StringBuilder();
			for (int i = lstAsks.size() - 1; i >= 0; i--)
			{
				DepthEntry ele = lstAsks.get(i);
				sb.append(String.format("%-10s  %10s\n", symbol.priceToStr(ele.getPrice()), symbol.qtyToStr(ele.getQty())));
			}
			return sb.toString();
		}
		return null;
	}

	public String printBids()
	{
		if (mapBids != null && !mapBids.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (DepthEntry ele : mapBids.values())
			{
				sb.append(String.format("%-10s  %10s\n", symbol.priceToStr(ele.getPrice()), symbol.qtyToStr(ele.getQty())));
			}
			return sb.toString();
		}
		return null;
	}

	public String printAsksGrp()
	{
		return printAsksGrp(asksGrp1);
	}

	public String printAsksGrp(List<DepthEntry> lst)
	{
		DecimalFormat df = getQtyDecimalFormat();

		if (lst != null && !lst.isEmpty())
		{
			BigDecimal firstPrice = lst.get(0).getPrice();

			StringBuilder sb = new StringBuilder();
			for (int i = lst.size() - 1; i >= 0; i--)
			{
				DepthEntry ele = lst.get(i);
				BigDecimal dist = PriceUtil.priceDistUp(firstPrice, ele.getPrice(), true);
				sb.append(String.format("%-10s %17s  %6.2f %%\n", symbol.priceToStr(ele.getPrice()), df.format(ele.getQty()), dist));
			}
			return sb.toString();
		}
		return null;
	}

	public String printBidsGrp()
	{
		return printBidsGrp(bidsGrp1);
	}

	public String printBidsGrp(List<DepthEntry> lst)
	{
		DecimalFormat df = getQtyDecimalFormat();

		if (lst != null && !lst.isEmpty())
		{
			BigDecimal firstPrice = lst.get(0).getPrice();

			StringBuilder sb = new StringBuilder();
			for (DepthEntry ele : lst)
			{
				BigDecimal dist = PriceUtil.priceDistDown(firstPrice, ele.getPrice(), true);
				sb.append(String.format("%-10s %17s  %6.2f %%\n", symbol.priceToStr(ele.getPrice()), df.format(ele.getQty()), dist));
			}
			return sb.toString();
		}
		return null;
	}

	private DecimalFormat getQtyDecimalFormat()
	{
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
		formatSymbols.setGroupingSeparator(' ');
		formatSymbols.setDecimalSeparator('.');

		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(symbol.getQtyPrecision());
		//df.setMaximumFractionDigits(8);
		df.setDecimalFormatSymbols(formatSymbols);

		return df;
	}

	// -----------------------------------------------------------------------

	public void export() throws IOException
	{
		File basepath = new File(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString());
		File path = new File(basepath, Constants.DEFAULT_EXPORT_FOLDER);

		if (mapAsks != null && !mapAsks.isEmpty())
		{
			StringBuilder sbAsks = new StringBuilder();
			for (DepthEntry ele : lstAsks)
			{
				sbAsks.append(ele.toString());
				sbAsks.append("\n");
			}
			File fileExportAsks = new File(path, symbol.getNameLeft() + "_depth_asks.csv");
			FileUtils.writeStringToFile(fileExportAsks, sbAsks.toString(), StandardCharsets.UTF_8);
		}

		if (mapAsks != null && !mapAsks.isEmpty())
		{
			StringBuilder sbAsks = new StringBuilder();
			for (DepthEntry ele : asksGrp0)
			{
				sbAsks.append(ele.toString());
				sbAsks.append("\n");
			}
			File fileExportAsks = new File(path, symbol.getNameLeft() + "_depth_asks0.csv");
			FileUtils.writeStringToFile(fileExportAsks, sbAsks.toString(), StandardCharsets.UTF_8);
		}

		if (mapBids != null && !mapBids.isEmpty())
		{
			StringBuilder sbBids = new StringBuilder();
			for (DepthEntry ele : lstBids)
			{
				sbBids.append(ele.toString());
				sbBids.append("\n");
			}
			File fileExportBids = new File(path, symbol.getNameLeft() + "_depth_bids.csv");
			FileUtils.writeStringToFile(fileExportBids, sbBids.toString(), StandardCharsets.UTF_8);
		}

		if (mapBids != null && !mapBids.isEmpty())
		{
			StringBuilder sbBids = new StringBuilder();
			for (DepthEntry ele : bidsGrp0)
			{
				sbBids.append(ele.toString());
				sbBids.append("\n");
			}
			File fileExportBids = new File(path, symbol.getNameLeft() + "_depth_bids0.csv");
			FileUtils.writeStringToFile(fileExportBids, sbBids.toString(), StandardCharsets.UTF_8);
		}

	}

	// ------------------------------------------------------------------------

	public static void main(String[] args) throws Exception
	{
		ExchangeInfoService.start();
		PriceService.start();
		
		String pair = "EOSUSDT";
		Symbol symbol = Symbol.getInstance(pair);

		OrderBookService obService = OrderBookService.getInstance(symbol).request(DepthMode.snapshot_only, 0);		
		obService.calc(BBType.classic, 8, WAType.price, 8);
		obService.export();

		System.out.println("");
		System.out.println(symbol.getNameLeft());

/*
		System.out.println("");
		System.out.println(obService.printAsks());
		System.out.println("");
		System.out.println(obService.printBids());
		System.out.println("");
		System.out.println("--- x 1 ---");
		System.out.println("");
		System.out.println(obService.printAsksGrp(obService.asksGrp0));
		System.out.println("");
		System.out.println(obService.printBidsGrp(obService.bidsGrp0));
		System.out.println("");
		System.out.println("--- X 10 ---");
		System.out.println("");
		System.out.println(obService.printAsksGrp(obService.asksGrp1));
		System.out.println("");
		System.out.println(obService.printBidsGrp(obService.bidsGrp1));
*/

		System.out.println("");
		System.out.println("--- X10 ---");
		System.out.println("");
		System.out.println(obService.printAsksGrp(obService.asksGrp2));
		System.out.println("");
		System.out.println(obService.printBidsGrp(obService.bidsGrp2));

		System.out.println("");
		System.out.println("SHORT B.Blk:  " + obService.getAskBBlkPoint1().toPlainString());
		System.out.println("SHORT B.Blk2: " + obService.getAskBBlkPoint2().toPlainString());
		System.out.println("SHORT W.AVG:  " + obService.getAskWAvgPoint1().toPlainString());
		System.out.println("SHORT W.AVG2: " + obService.getAskWAvgPoint2().toPlainString());

		System.out.println("");
		System.out.println("Price:        " + PriceService.getLastPrice(symbol));

		System.out.println("");
		System.out.println("LONG B.Blk:   " + obService.getBidBBlkPoint1().toPlainString());
		System.out.println("LONG B.Blk2:  " + obService.getBidBBlkPoint2().toPlainString());
		System.out.println("LONG W.AVG:   " + obService.getBidWAvgPoint1().toPlainString());
		System.out.println("LONG W.AVG2:  " + obService.getBidWAvgPoint2().toPlainString());

		System.out.println("");
		List<DepthEntry> listSa = obService.searchSuperAskBlocks();
		System.out.println(obService.printSuperBlks(listSa));

		System.out.println("");
		List<DepthEntry> listSb = obService.searchSuperBidBlocks();
		System.out.println(obService.printSuperBlks(listSb));

	}

}
