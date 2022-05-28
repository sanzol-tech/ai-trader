package sanzol.app.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.binance.client.model.enums.PositionSide;

import sanzol.app.config.CharConstants;
import sanzol.app.model.PositionOrder.State;
import sanzol.app.model.PositionOrder.Type;
import sanzol.app.service.Symbol;

public class Position
{
	private Symbol symbol;
	
	private PositionSide side;
	private boolean markPrice;
	private Double inPrice = null;
	private Double inQty = null;
	private double distBeforeSL;
	private double takeProfit;
	private List<PriceQty> lstPriceQty = new ArrayList<PriceQty>();
	
	private List<PositionOrder> lstOrders;
	private double sumUsd;

	public Position()
	{
		//
	}

	public Position(Symbol symbol, PositionSide side)
	{
		this.symbol = symbol;
		this.side = side;
	}

	public Position(Symbol symbol, PositionSide side, double distBeforeSL, double takeProfit, List<PriceQty> lstPriceQty)
	{
		this.symbol = symbol;
		this.side = side;
		this.distBeforeSL = distBeforeSL;
		this.takeProfit = takeProfit;
		this.lstPriceQty = lstPriceQty;
	}

	// ------------------------------------------------------------------------

	public String getSymbol()
	{
		return symbol.getName();
	}

	public String getSymbolLeft()
	{
		return symbol.getNameLeft();
	}

	public boolean isShort()
	{
		return side == PositionSide.SHORT;
	}

	public boolean isLong()
	{
		return side == PositionSide.LONG;
	}

	public String getInPriceStr()
	{
		return symbol.priceToStr(inPrice);
	}

	public String getInQtyStr()
	{
		return symbol.qtyToStr(inQty);
	}

	// ------------------------------------------------------------------------

	public Symbol getCoin()
	{
		return symbol;
	}

	public PositionSide getSide()
	{
		return side;
	}

	public Double getInPrice()
	{
		return inPrice;
	}

	public void setInPrice(Double inPrice)
	{
		this.inPrice = inPrice;
	}
	
	public Double getInQty()
	{
		return inQty;
	}

	public void setInQty(Double inQty)
	{
		this.inQty = inQty;
	}

	public double getDistBeforeSL()
	{
		return distBeforeSL;
	}

	public double getTakeProfit()
	{
		return takeProfit;
	}

	public List<PriceQty> getLstPriceQty()
	{
		return lstPriceQty;
	}

	public void setLstPriceQty(List<PriceQty> lstPriceQty)
	{
		this.lstPriceQty = lstPriceQty;
	}

	public List<PositionOrder> getLstOrders()
	{
		return lstOrders;
	}

	public void setLstOrders(List<PositionOrder> lstOrders)
	{
		this.lstOrders = lstOrders;
	}

	public double getSumUsd()
	{
		return sumUsd;
	}

	public void setSumUsd(double sumUsd)
	{
		this.sumUsd = sumUsd;
	}

	public boolean isMarkPrice()
	{
		return markPrice;
	}

	public void setMarkPrice(boolean markPrice)
	{
		this.markPrice = markPrice;
	}

	// ------------------------------------------------------------------------

	private static String usdToStr(double usd)
	{
		String pattern = "#0.00";
		return new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(usd);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append(String.format("%s %s - in Price: %s - in Coins: %s", side.name(), symbol.getNameLeft(), getInPriceStr(), getInQtyStr()));
		sb.append("\n\n");

		String labels = String.format("%-3s %8s %8s %12s %12s %12s %12s %12s %12s | %12s %10s %12s %12s", "#", "TYPE", CharConstants.ARROW_UP + " %", "PRICE", "QTY", "USD", CharConstants.SIGNA + " QTY", CharConstants.SIGNA + " USD", "USD LOST", "AVG-PRICE", CharConstants.ARROW_UP_DOWN + " %", "TP-PRICE", "PROFIT");

		sb.append(labels);
		sb.append("\n");
		sb.append(StringUtils.repeat("-",151));
		sb.append("\n");

		for (PositionOrder entry : lstOrders)
		{
			if (entry.getType() == Type.SL_SELL || entry.getType() == Type.SL_BUY)
			{
				sb.append(StringUtils.repeat("-",151));
				sb.append("\n");
			}

			String line = String.format("%-3s %8s %8.2f %12s %12s %12s %12s %12s %12s | %12s %10.2f %12s %12s", 
					entry.getNumber(), 
					entry.getType().name(), 
					entry.getDistance() * 100, 
					symbol.priceToStr(entry.getPrice()),
					symbol.qtyToStr(entry.getCoins()), 
					usdToStr(entry.getUsd()), 
					symbol.qtyToStr(entry.getSumCoins()), 
					usdToStr(entry.getSumUsd()), 
					usdToStr(entry.getLost()), 
					symbol.priceToStr(entry.getNewPrice()), 
					entry.getRecoveryNeeded() * 100, 
					symbol.priceToStr(entry.getTakeProfit()), 
					usdToStr(entry.getProfit()));

			sb.append(line);
			sb.append("\n");

			if (entry.getState() != State.CREATED)
			{
				sb.append(entry.getState().name());
				if (entry.getResult() != null)
				{
					sb.append(" / " + entry.getResult());
				}
				sb.append("\n\n");
			}

		}

		return sb.toString();
	}
	
}
