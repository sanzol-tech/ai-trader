package aitrader.core.model;

import java.util.ArrayList;
import java.util.List;

import aitrader.core.model.enums.PriceIncrType;
import aitrader.core.model.enums.QtyIncrType;
import aitrader.core.service.position.BalanceService;
import aitrader.core.service.symbol.SymbolInfoService;
import aitrader.core.service.trade.PositionModeService;
import binance.futures.enums.PositionMode;
import binance.futures.enums.PositionSide;
import binance.futures.model.AccountBalance;

public class GridPosition
{
	private Symbol symbol;
	private PositionSide positionSide;
	private PriceIncrType priceIncrType = PriceIncrType.GEOMETRIC;
	private QtyIncrType qtyIncrType = QtyIncrType.POSITION;
	private boolean isLastPrice;
	private Double inPrice = null;
	private Double inQty = null;
	private double stopLoss;
	private boolean slEnabled;
	private double takeProfit;
	private boolean tpEnabled;
	private List<PriceQty> lstPriceQty = new ArrayList<PriceQty>();
	private List<GridOrder> lstOrders;
	private double sumUsd;

	// ---- COSTRUCTORS -------------------------------------------------------

	public GridPosition(Symbol symbol, PositionSide positionSide, double stopLoss, boolean slEnabled, double takeProfit, boolean tpEnabled, List<PriceQty> lstPriceQty)
	{
		this.symbol = symbol;
		
		if (PositionModeService.getPositionMode() == PositionMode.ONE_WAY)
			this.positionSide = PositionSide.BOTH;
		else
			this.positionSide = positionSide;

		this.stopLoss = stopLoss;
		this.slEnabled = slEnabled;
		this.takeProfit = takeProfit;
		this.tpEnabled = tpEnabled;

		this.lstPriceQty = lstPriceQty;
	}

	// --------------------------------------------------------------------

	public GridPosition withLastPrice()
	{
		this.inPrice = SymbolInfoService.getLastPrice(symbol).doubleValue();
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
		AccountBalance accBalance = BalanceService.getAccountBalance();
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

	public PositionSide getPositionSide()
	{
		return positionSide;
	}

	public void setPositionSide(PositionSide positionSide)
	{
		this.positionSide = positionSide;
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

	public double getStopLoss()
	{
		return stopLoss;
	}

	public void setStopLoss(double stopLoss)
	{
		this.stopLoss = stopLoss;
	}

	public boolean isSlEnabled()
	{
		return slEnabled;
	}

	public void setSlEnabled(boolean slEnabled)
	{
		this.slEnabled = slEnabled;
	}

	public double getTakeProfit()
	{
		return takeProfit;
	}

	public void setTakeProfit(double takeProfit)
	{
		this.takeProfit = takeProfit;
	}

	public boolean isTpEnabled()
	{
		return tpEnabled;
	}

	public void setTpEnabled(boolean tpEnabled)
	{
		this.tpEnabled = tpEnabled;
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

	// ---- CALCULATED FIELDS ---------------------------------------------

	public String getInPriceStr()
	{
		return symbol.priceToStr(inPrice);
	}

	public String getInQtyStr()
	{
		return symbol.qtyToStr(inQty);
	}

}
