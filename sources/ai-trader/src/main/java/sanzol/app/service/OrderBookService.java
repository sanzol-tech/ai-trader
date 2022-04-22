package sanzol.app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.market.OrderBook;
import com.binance.client.model.market.OrderBookEntry;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.PrivateConfig;
import sanzol.app.model.OrderBookElement;
import sanzol.app.model.OrderBookInfo;
import sanzol.app.model.Symbol;
import sanzol.app.task.PriceService;

public class OrderBookService
{

	public static OrderBookInfo getShoks(Symbol coin)
	{
		OrderBookInfo points = new OrderBookInfo();

		RequestOptions options = new RequestOptions();
		SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

		OrderBook obook = syncRequestClient.getOrderBook(coin.getNameLeft() + Constants.DEFAULT_SYMBOL_RIGHT, 1000);

		List<OrderBookElement> asks = getElemnets(obook.getAsks());
		List<OrderBookElement> asksGrp = getAsksGrp(asks, coin.getTickSize());

		List<OrderBookElement> bids = getElemnets(obook.getBids());
		List<OrderBookElement> bidsGrp = getBidsGrp(bids, coin.getTickSize());

		points = new OrderBookInfo(coin, asks, asksGrp, bids, bidsGrp);

		// ----------------- EXPERIMENTAL ----------------
		points.fixShocks();
		// -----------------------------------------------

		return points;
	}

	private static List<OrderBookElement> getElemnets(List<OrderBookEntry> lst)
	{
		List<OrderBookElement> lstResult = new ArrayList<OrderBookElement>();

		BigDecimal sumQty = BigDecimal.ZERO;
		for (OrderBookEntry entry : lst)
		{
			sumQty = sumQty.add(entry.getQty());
			lstResult.add(new OrderBookElement(entry.getPrice(), entry.getQty(), sumQty));
		}

		completePercents(lstResult);

		return lstResult;
	}

	private static void completePercents(List<OrderBookElement> lst)
	{
		if (lst != null && !lst.isEmpty())
		{
			BigDecimal total = lst.get(lst.size() - 1).getSumQty();

			for (OrderBookElement entry : lst)
			{
				entry.setSumPercent(entry.getSumQty().divide(total, 4, RoundingMode.HALF_UP));
			}
		}
	}

	private static List<OrderBookElement> getAsksGrp(List<OrderBookElement> lst, int tickSize)
	{
		if (lst == null || lst.isEmpty())
		{
			return null;
		}

		List<OrderBookElement> lstResult = new ArrayList<OrderBookElement>();

		BigDecimal blockSize = BigDecimal.valueOf(Math.pow(10, -tickSize) * 100);

		BigDecimal price = lst.get(0).getPrice();
		BigDecimal priceBlock = price.add(blockSize);
		BigDecimal qty = lst.get(0).getQty();

		for (int i = 1; i < lst.size(); i++)
		{
			if (lst.get(i).getPrice().doubleValue() < priceBlock.doubleValue())
			{
				qty = qty.add(lst.get(i).getQty());
			}
			else
			{
				OrderBookElement prev = lst.get(i - 1);
				OrderBookElement newElement = new OrderBookElement();
				newElement.setPrice(prev.getPrice());
				newElement.setQty(qty);
				newElement.setSumQty(prev.getSumQty());
				newElement.setSumPercent(prev.getSumPercent());
				lstResult.add(newElement);

				priceBlock = priceBlock.add(blockSize);
				qty = lst.get(i).getQty();
			}
		}
		OrderBookElement prev = lst.get(lst.size() - 1);
		OrderBookElement newElement = new OrderBookElement();
		newElement.setPrice(prev.getPrice());
		newElement.setQty(qty);
		newElement.setSumQty(prev.getSumQty());
		newElement.setSumPercent(prev.getSumPercent());
		lstResult.add(newElement);

		return lstResult;
	}

	private static List<OrderBookElement> getBidsGrp(List<OrderBookElement> lst, int tickSize)
	{
		if (lst == null || lst.isEmpty())
		{
			return null;
		}

		List<OrderBookElement> lstResult = new ArrayList<OrderBookElement>();

		BigDecimal blockSize = BigDecimal.valueOf(Math.pow(10, -tickSize) * 100);

		BigDecimal price = lst.get(0).getPrice();
		BigDecimal priceBlock = price.subtract(blockSize);
		BigDecimal qty = lst.get(0).getQty();

		for (int i = 1; i < lst.size(); i++)
		{
			if (lst.get(i).getPrice().doubleValue() > priceBlock.doubleValue())
			{
				qty = qty.add(lst.get(i).getQty());
			}
			else
			{
				OrderBookElement prev = lst.get(i - 1);
				OrderBookElement newElement = new OrderBookElement();
				newElement.setPrice(prev.getPrice());
				newElement.setQty(qty);
				newElement.setSumQty(prev.getSumQty());
				newElement.setSumPercent(prev.getSumPercent());
				lstResult.add(newElement);

				priceBlock = priceBlock.subtract(blockSize);
				qty = lst.get(i).getQty();
			}
		}
		OrderBookElement prev = lst.get(lst.size() - 1);
		OrderBookElement newElement = new OrderBookElement();
		newElement.setPrice(prev.getPrice());
		newElement.setQty(qty);
		newElement.setSumQty(prev.getSumQty());
		newElement.setSumPercent(prev.getSumPercent());
		lstResult.add(newElement);

		return lstResult;
	}

	public static String toString(Symbol coin, List<OrderBookElement> list)
	{
		String text = "";

		if (list != null && !list.isEmpty())
		{
			for (OrderBookElement ele : list)
			{
				text += String.format("%-10s : %10s  %8.2f %%\n", coin.priceToStr(ele.getPrice()), coin.coinsToStr(ele.getQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0)));
			}
		}

		return text;
	}

	public static String toStringInv(Symbol coin, List<OrderBookElement> list)
	{
		String text = "";

		if (list != null && !list.isEmpty())
		{
			for (int i = list.size() - 1; i >= 0; i--)
			{
				OrderBookElement ele = list.get(i);
				text += String.format("%-10s : %10s  %8.2f %%\n", coin.priceToStr(ele.getPrice()), coin.coinsToStr(ele.getQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0)));
			}
		}

		return text;
	}

	public static String toStringAll(Symbol coin, List<OrderBookElement> list)
	{
		String text = "";

		if (list != null && !list.isEmpty())
		{
			for (OrderBookElement ele : list)
			{
				text += String.format("%-10s  %10s  %10s  %8.2f %%\n", coin.priceToStr(ele.getPrice()), coin.coinsToStr(ele.getQty()), coin.coinsToStr(ele.getSumQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0)));
			}
		}

		return text;
	}

	public static String toStringInvAll(Symbol coin, List<OrderBookElement> list)
	{
		String text = "";

		if (list != null && !list.isEmpty())
		{
			for (int i = list.size() - 1; i >= 0; i--)
			{
				OrderBookElement ele = list.get(i);
				text += String.format("%-10s  %10s  %10s  %8.2f %%\n", coin.priceToStr(ele.getPrice()), coin.coinsToStr(ele.getQty()), coin.coinsToStr(ele.getSumQty()), ele.getSumPercent().multiply(BigDecimal.valueOf(100.0)));
			}
		}

		return text;
	}

	public static void main(String[] args) throws Exception
	{
		Application.initialize();

		Thread.sleep(5000);

		String symbol = "BTC";
		Symbol coin = Symbol.getInstance(Symbol.getFullSymbol(symbol));

		System.out.println("");
		System.out.println(coin.getNameLeft());

		OrderBookInfo obInfo = getShoks(coin);

		System.out.println("");
		System.out.println(toStringInvAll(coin, obInfo.getAsks()));
		System.out.println("");
		System.out.println(toStringInvAll(coin, obInfo.getAsksGrp()));
		System.out.println("");
		System.out.println("Max Ask: " + obInfo.getStrShShock());
		System.out.println("[PERCENT]: " + obInfo.getAskPriceBetween(0.3, 0.4));
		System.out.println("");

		System.out.println("Price " + PriceService.getLastPrice(coin));

		System.out.println("");
		System.out.println(toStringAll(coin, obInfo.getBids()));
		System.out.println("");
		System.out.println(toStringAll(coin, obInfo.getBidsGrp()));
		System.out.println("");
		System.out.println("Max Bid: " + obInfo.getStrLgShock());
		System.out.println("[PERCENT]: " + obInfo.getBidPriceBetween(0.3, 0.4));
	}

}
