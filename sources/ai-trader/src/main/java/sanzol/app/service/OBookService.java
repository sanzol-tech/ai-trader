package sanzol.app.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import com.binance.client.RequestOptions;
import com.binance.client.SubscriptionClient;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.market.OrderBook;
import com.binance.client.model.market.OrderBookEntry;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.PrivateConfig;
import sanzol.app.model.OrderBookElement;
import sanzol.app.task.PriceService;

public class OBookService
{
	private Symbol coin;

	private TreeMap<BigDecimal, BigDecimal> mapAsks = new TreeMap<BigDecimal, BigDecimal>();
	private TreeMap<BigDecimal, BigDecimal> mapBids = new TreeMap<BigDecimal, BigDecimal>(Collections.reverseOrder());

	private List<OrderBookElement> asks;
	private List<OrderBookElement> bids;

	private List<OrderBookElement> asksGrp;
	private List<OrderBookElement> bidsGrp;

	private BigDecimal shortPriceBBlk;
	private BigDecimal longPriceBBlk;

	private BigDecimal shortPriceWAvg;
	private BigDecimal longPriceWAvg;

	private BigDecimal shortPriceFixed;
	private BigDecimal longPriceFixed;
	
	public static OBookService getInstance(Symbol coin)
	{
		OBookService obServise = new OBookService();
		obServise.coin = coin;
		return obServise;
	}

	public OBookService request()
	{
		RequestOptions options = new RequestOptions();
		SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

		OrderBook obook = syncRequestClient.getOrderBook(coin.getNameLeft() + Constants.DEFAULT_SYMBOL_RIGHT, 1000);

		for (OrderBookEntry entry : obook.getAsks())
		{
			mapAsks.put(entry.getPrice(), entry.getQty());
		}
		for (OrderBookEntry entry : obook.getBids())
		{
			mapBids.put(entry.getPrice(), entry.getQty());
		}

		return this;
	}

	private SubscriptionClient client = null;
	public OBookService subscribeDiffDepthEvent() 
	{
		client = SubscriptionClient.create();
		client.subscribeDiffDepthEvent(coin.getName().toLowerCase(), ((event) -> {

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

		}), null);

		return this;
	}

	public OBookService calc()
	{
		return calc(0.5, 0.2);
	}
	
	public OBookService calc(double waMaxAccc, double waMaxDist)
	{
		if (client != null)
			client.unsubscribeAll();
		
		loadAsks();
		loadBids();
		loadAsksGrp();
		loadBidsGrp();

		this.shortPriceBBlk = getBestBlockAsks();
		this.longPriceBBlk = geBestBlockBids();

		this.shortPriceWAvg = weightedAverage(asks, waMaxAccc, waMaxDist);
		this.longPriceWAvg = weightedAverage(bids, waMaxAccc, waMaxDist);

		fixShocks();
		
		return this;
	}
	
	public void fixShocks()
	{
		if (shortPriceWAvg != null && shortPriceWAvg.doubleValue() > shortPriceBBlk.doubleValue())
			shortPriceFixed = shortPriceWAvg;
		else
			shortPriceFixed = shortPriceBBlk;

		if (longPriceWAvg != null && longPriceWAvg.doubleValue() < longPriceBBlk.doubleValue())
			longPriceFixed = longPriceWAvg;
		else
			longPriceFixed = longPriceBBlk;
	}	

	// ------------------------------------------------------------------------

	public BigDecimal getShortPriceBBlk()
	{
		return shortPriceBBlk;
	}

	public BigDecimal getLongPriceBBlk()
	{
		return longPriceBBlk;
	}

	public BigDecimal getShortPriceWAvg()
	{
		return shortPriceWAvg;
	}

	public BigDecimal getLongPriceWAvg()
	{
		return longPriceWAvg;
	}

	public BigDecimal getShortPriceFixed()
	{
		return shortPriceFixed;
	}

	public BigDecimal getLongPriceFixed()
	{
		return longPriceFixed;
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

	public BigDecimal getBestBlockAsks()
	{
		if (asksGrp != null && !asksGrp.isEmpty())
		{
			OrderBookElement eMax = null;
			for (OrderBookElement e : asksGrp)
			{
				if ((eMax == null) || (eMax.getQty().compareTo(e.getQty()) == -1))
					eMax = e;
			}
			return eMax.getPrice();
		}
		return null;
	}

	public BigDecimal geBestBlockBids()
	{
		if (bidsGrp != null && !bidsGrp.isEmpty())
		{
			double minPrice = Math.pow(10, -coin.getTickSize()) * 2;

			OrderBookElement eMax = null;
			for (OrderBookElement e : bidsGrp)
			{
				if (e.getPrice().doubleValue() <= minPrice)
					break;
				
				if ((eMax == null) || (eMax.getQty().compareTo(e.getQty()) == -1))
					eMax = e;
			}
			return eMax.getPrice();
		}
		return null;
	}

	public static BigDecimal weightedAverage(List<OrderBookElement> lst, double maxAccumPercent, double maxDist)
	{
		BigDecimal sumProd = BigDecimal.ZERO;
		BigDecimal sumQty = BigDecimal.ZERO;

		for (OrderBookElement entry : lst)
		{
			if (entry.getSumPercent().doubleValue() > maxAccumPercent || entry.getDistance().doubleValue() > maxDist)
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

	private void loadAsksGrp()
	{
		if (asks == null || asks.isEmpty())
		{
			return;
		}

		asksGrp = new ArrayList<OrderBookElement>();

		BigDecimal blockSize = getBlockSize();
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
	}

	private void loadBidsGrp()
	{
		if (bids == null || bids.isEmpty())
		{
			return;
		}

		bidsGrp = new ArrayList<OrderBookElement>();

		BigDecimal blockSize = getBlockSize();
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
	}

	private BigDecimal getBlockSize()
	{
		final BigDecimal[] BLOCK_SIZE = { BigDecimal.valueOf(10), BigDecimal.valueOf(1), BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.01), BigDecimal.valueOf(0.001), BigDecimal.valueOf(0.001) };

		if ("1000SHIB".equalsIgnoreCase(coin.getNameLeft()))
		{
			return BigDecimal.valueOf(0.0001);
		}
		if ("BTC".equalsIgnoreCase(coin.getNameLeft()))
		{
			return BigDecimal.valueOf(100);
		}

		return BLOCK_SIZE[coin.getTickSize() - 1];
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
				sb.append(String.format("%-10s  %10s  %10s  %8.2f %%  %8.2f %%\n", coin.priceToStr(ele.getPrice()), coin.qtyToStr(ele.getQty()), coin.qtyToStr(ele.getSumQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0)), ele.getDistance().multiply(BigDecimal.valueOf(100.0))));
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
				sb.append(String.format("%-10s  %10s  %10s  %8.2f %%  %8.2f %%\n", coin.priceToStr(ele.getPrice()), coin.qtyToStr(ele.getQty()), coin.qtyToStr(ele.getSumQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0)), ele.getDistance().multiply(BigDecimal.valueOf(100.0))));
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
				sb.append(String.format("%-10s : %10s  %8.2f %%\n", coin.priceToStr(ele.getPrice()), coin.qtyToStr(ele.getQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0))));
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
				sb.append(String.format("%-10s : %10s  %8.2f %%\n", coin.priceToStr(ele.getPrice()), coin.qtyToStr(ele.getQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0))));
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
			File fileExportAsks = new File(Constants.DEFAULT_EXPORT_FOLDER, coin.getNameLeft() + "_depth_asks.csv");
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
			File fileExportBids = new File(Constants.DEFAULT_EXPORT_FOLDER, coin.getNameLeft() + "_depth_bids.csv");
			FileUtils.writeStringToFile(fileExportBids, sbBids.toString(), StandardCharsets.UTF_8);
		}
	}	
	
	// ------------------------------------------------------------------------

	public static void main(String[] args) throws Exception
	{
		Application.initialize();

		String symbol = "ETH";
		Symbol coin = Symbol.getInstance(Symbol.getFullSymbol(symbol));

		OBookService obService = OBookService.getInstance(coin).request().subscribeDiffDepthEvent();
		Thread.sleep(20000);
		obService.calc(1, 1);

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
		System.out.println("SHORT B.Blk:  " + obService.getShortPriceBBlk());
		System.out.println("SHORT W.AVG:  " + obService.getShortPriceWAvg());

		System.out.println("");
		System.out.println("Price:        " + PriceService.getLastPrice(coin));

		System.out.println("");
		System.out.println("LONG B.Blk:   " + obService.getLongPriceBBlk());
		System.out.println("LONG W.AVG:   " + obService.getLongPriceWAvg());

	}

}
