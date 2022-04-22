package sanzol.app.service;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.trade.Order;
import com.binance.client.model.trade.PositionRisk;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.PrivateConfig;
import sanzol.app.model.Symbol;
import sanzol.app.util.Convert;

public class PositionService
{
	private RequestOptions options = new RequestOptions();
	private SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

	private Map<String, BigDecimal> mapAmountsPrev = new HashMap<String, BigDecimal>();
	private Set<String> setAmountChange = new HashSet<String>();

	private List<PositionRisk> lstPositionRisk;
	private List<Order> lstOpenOrders;

	public boolean wasChange()
	{
		if (setAmountChange != null && !setAmountChange.isEmpty())
		{
			setAmountChange.clear();
			return true;
		}
		return false;
	}

	public PositionService getPosition()
	{
		lstPositionRisk = syncRequestClient.getPositionRisk();

		verifyAmountChange();

		lstOpenOrders = syncRequestClient.getOpenOrders(null);

		Comparator<Order> orderComparator = Comparator
												.comparing(Order::getSymbol)
												.thenComparing(Order::getPrice)
												.thenComparing(Order::getStopPrice);
		Collections.sort(lstOpenOrders, orderComparator);

		return this;
	}

	private void verifyAmountChange()
	{
		if (lstPositionRisk != null && lstPositionRisk.isEmpty())
		{
			for (PositionRisk entry : lstPositionRisk)
			{
				if (!mapAmountsPrev.get(entry.getSymbol()).equals(entry.getPositionAmt()))
				{
					mapAmountsPrev.put(entry.getSymbol(), entry.getPositionAmt());
					setAmountChange.add(entry.getSymbol());
				}
			}
		}
	}

	public String toStringOrders(String symbolName)
	{
		StringBuilder sb = new StringBuilder();

		for (Order entry : lstOpenOrders)
		{
			if (entry.getSymbol().equals(symbolName) && entry.getStatus().equals("NEW"))
				sb.append(String.format("%-22s %-6s %-13s %10s %14s %12s %14s\n", convertTime(entry.getUpdateTime()), entry.getSide(), entry.getType(), entry.getOrigQty(), entry.getPrice(), entry.getStopPrice(), entry.getReduceOnly() ? "R.Only" : ""));
		}

		return sb.toString();
	}

	public String toStringPositions()
	{
		if (lstPositionRisk == null || lstPositionRisk.isEmpty())
		{
			return "NO RECORDS";
		}

		StringBuilder sb = new StringBuilder();

		sb.append(String.format("%-22s %-20s %10s %14s %12s %14s\n", "SYMBOL", "TYPE", "AMOUNT", "PRICE", "AVG PRICE", "PNL"));
		sb.append(StringUtils.repeat("-", 97));
		sb.append("\n");

		for (PositionRisk entry : lstPositionRisk)
		{
			if (entry.getPositionAmt().doubleValue() != 0.0)
			{
				Symbol symbol = Symbol.getInstance(entry.getSymbol());

				String side = entry.getPositionAmt().doubleValue() > 0 ? "LONG" : "SHORT";

				sb.append(String.format("%-22s %-20s %10s %14s %12s %14s\n",
										entry.getSymbol(),
										side + " " + entry.getMarginType() + " " + entry.getLeverage(),
										entry.getPositionAmt(),
										symbol.priceToStr(entry.getMarkPrice()),
										symbol.priceToStr(entry.getEntryPrice()), 
										Convert.usdToStr(entry.getUnrealizedProfit().doubleValue())));

				sb.append(StringUtils.repeat("-",97));
				sb.append("\n");
				sb.append(toStringOrders(entry.getSymbol()));
				sb.append(StringUtils.repeat("-",97));
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	public static String convertTime(long time)
	{
		Date date = new Date(time);
		Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static PositionRisk getPosition(String symbolLeft)
	{
		RequestOptions options = new RequestOptions();
		SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

		List<PositionRisk> lstPositionRisk = syncRequestClient.getPositionRisk();
		for (PositionRisk entry : lstPositionRisk)
		{
			if (entry.getSymbol().equals(symbolLeft + Constants.DEFAULT_SYMBOL_RIGHT))
			{
				return entry;
			}
		}

		return null;
	}

	public static void main(String[] args)
	{
		Application.initialize();

		PositionService service = new PositionService();
		service.getPosition();

		System.out.println(service.toStringPositions());
	}

}
