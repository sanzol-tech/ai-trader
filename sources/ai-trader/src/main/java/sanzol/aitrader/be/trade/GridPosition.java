package sanzol.aitrader.be.trade;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import api.client.futures.model.AccountBalance;
import sanzol.aitrader.be.enums.GridOrderStatus;
import sanzol.aitrader.be.enums.GridOrderType;
import sanzol.aitrader.be.enums.GridSide;
import sanzol.aitrader.be.enums.PriceIncrType;
import sanzol.aitrader.be.enums.QtyIncrType;
import sanzol.aitrader.be.model.GridOrder;
import sanzol.aitrader.be.model.PriceQty;
import sanzol.aitrader.be.model.Symbol;
import sanzol.aitrader.be.service.BalanceFuturesService;
import sanzol.aitrader.be.service.PriceService;
import sanzol.aitrader.ui.config.CharConstants;
import sanzol.util.Convert;

public class GridPosition
{
	private Symbol symbol;
	private GridSide side;
	private PriceIncrType priceIncrType = PriceIncrType.GEOMETRIC;
	private QtyIncrType qtyIncrType = QtyIncrType.POSITION;
	private boolean isLastPrice;
	private Double inPrice = null;
	private Double inQty = null;
	private double distBeforeSL;
	private double takeProfit;
	private List<PriceQty> lstPriceQty = new ArrayList<PriceQty>();
	private List<GridOrder> lstOrders;
	private double sumUsd;

	// ---- COSTRUCTORS -------------------------------------------------------

	public GridPosition()
	{
		//
	}

	public GridPosition(Symbol symbol, GridSide side)
	{
		this.symbol = symbol;
		this.side = side;
	}

	public GridPosition(Symbol symbol, GridSide side, double distBeforeSL, double takeProfit, List<PriceQty> lstPriceQty)
	{
		this.symbol = symbol;
		this.side = side;
		this.distBeforeSL = distBeforeSL;
		this.takeProfit = takeProfit;
		this.lstPriceQty = lstPriceQty;
	}

	// ------------------------------------------------------------------------

	public GridPosition withLastPrice()
	{
		this.inPrice = PriceService.getLastPrice(symbol).doubleValue();
		this.isLastPrice = true;
		return this;
	}

	public GridPosition withInPrice(double inPrice)
	{
		this.inPrice = inPrice;
		this.isLastPrice = false;
		return this;
	}

	public GridPosition withInQty(double inQty)
	{
		this.inQty = symbol.roundQty(inQty);
		return this;
	}

	public GridPosition withUsdAmount(double usd)
	{
		this.inQty = symbol.roundQty(usd / inPrice);
		return this;
	}

	public GridPosition withBalancePercent(double percent)
	{
		AccountBalance accBalance = BalanceFuturesService.getAccountBalance();
		double balance = accBalance.getBalance().doubleValue();
		this.inQty = symbol.roundQty((balance * percent) / inPrice);
		return this;
	}

	// ---- PROPERTIES --------------------------------------------------------

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public GridSide getSide()
	{
		return side;
	}

	public void setSide(GridSide side)
	{
		this.side = side;
	}

	public PriceIncrType getPriceIncrType()
	{
		return priceIncrType;
	}

	public void setPriceIncrType(PriceIncrType priceIncrType)
	{
		this.priceIncrType = priceIncrType;
	}

	public QtyIncrType getQtyIncrType()
	{
		return qtyIncrType;
	}

	public void setQtyIncrType(QtyIncrType qtyIncrType)
	{
		this.qtyIncrType = qtyIncrType;
	}

	public boolean isLastPrice()
	{
		return isLastPrice;
	}

	public void setLastPrice(boolean isLastPrice)
	{
		this.isLastPrice = isLastPrice;
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

	public void setDistBeforeSL(double distBeforeSL)
	{
		this.distBeforeSL = distBeforeSL;
	}

	public double getTakeProfit()
	{
		return takeProfit;
	}

	public void setTakeProfit(double takeProfit)
	{
		this.takeProfit = takeProfit;
	}

	public List<PriceQty> getLstPriceQty()
	{
		return lstPriceQty;
	}

	public void setLstPriceQty(List<PriceQty> lstPriceQty)
	{
		this.lstPriceQty = lstPriceQty;
	}

	public List<GridOrder> getLstOrders()
	{
		return lstOrders;
	}

	public void setLstOrders(List<GridOrder> lstOrders)
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

	// ------------------------------------------------------------------------

	public String getInPriceStr()
	{
		return symbol.priceToStr(inPrice);
	}

	public String getInQtyStr()
	{
		return symbol.qtyToStr(inQty);
	}

	// ------------------------------------------------------------------------

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

		for (GridOrder entry : lstOrders)
		{
			if (entry.getType() == GridOrderType.SL_SELL || entry.getType() == GridOrderType.SL_BUY)
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
					Convert.usdToStr(entry.getUsd()),
					symbol.qtyToStr(entry.getSumCoins()),
					Convert.usdToStr(entry.getSumUsd()),
					Convert.usdToStr(entry.getLost()),
					symbol.priceToStr(entry.getNewPrice()),
					entry.getRecoveryNeeded() * 100,
					symbol.priceToStr(entry.getTakeProfit()),
					Convert.usdToStr(entry.getProfit()));

			sb.append(line);
			sb.append("\n");

			if (entry.getStatus() != GridOrderStatus.NEW)
			{
				sb.append(entry.getStatus().name());
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
