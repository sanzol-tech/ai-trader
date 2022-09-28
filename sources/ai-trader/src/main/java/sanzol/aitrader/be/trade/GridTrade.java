package sanzol.aitrader.be.trade;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import api.client.futures.impl.SyncFuturesClient;
import api.client.futures.model.AccountBalance;
import api.client.futures.model.Order;
import api.client.futures.model.enums.NewOrderRespType;
import api.client.futures.model.enums.OrderSide;
import api.client.futures.model.enums.OrderType;
import api.client.futures.model.enums.PositionSide;
import api.client.futures.model.enums.TimeInForce;
import api.client.futures.model.enums.WorkingType;
import api.client.impl.config.ApiConfig;
import sanzol.aitrader.be.config.Config;
import sanzol.aitrader.be.enums.GridOrderStatus;
import sanzol.aitrader.be.enums.GridOrderType;
import sanzol.aitrader.be.enums.GridSide;
import sanzol.aitrader.be.enums.PriceIncrType;
import sanzol.aitrader.be.enums.QtyIncrType;
import sanzol.aitrader.be.model.GridOrder;
import sanzol.aitrader.be.model.PriceQty;
import sanzol.aitrader.be.model.Symbol;
import sanzol.aitrader.be.service.BalanceFuturesService;
import sanzol.aitrader.be.service.BotService;
import sanzol.aitrader.be.service.PriceService;

public class GridTrade
{
	public enum PostStyle { ALL, FIRST, OTHERS }

	private GridPosition gridPosition;

	public GridPosition getGridPosition()
	{
		return gridPosition;
	}

	public Symbol getSymbol()
	{
		return gridPosition.getSymbol();
	}

	private double roundPrice(double price)
	{
		return gridPosition.getSymbol().roundPrice(price);
	}

	private double roundQty(double value)
	{
		final double minQty = gridPosition.getSymbol().getMinQty().doubleValue();
		double qty = gridPosition.getSymbol().roundQty(value);
		return Math.max(minQty, qty);
	}

	private double fixMinQty(double qty, double price)
	{
		final double minUsdAmount = ApiConfig.MIN_USD_AMOUNT;

		if (qty * price >= minUsdAmount)
		{
			return qty;
		}
		
		final double minQty = gridPosition.getSymbol().getMinQty().doubleValue();
		
		while (qty * price < minUsdAmount)
		{
			qty += minQty;
		}
		
		return qty;
	}

	// ------------------------------------------------------------------------

	public GridTrade(GridPosition position)
	{
		this.gridPosition = position;
	}

	// ------------------------------------------------------------------------
	// SHORT
	// ------------------------------------------------------------------------

	public void createShort()
	{
		List<GridOrder> lstOrders = new ArrayList<GridOrder>();
		gridPosition.setLstOrders(lstOrders);

		// FIRST ORDER
		int number = 0;
		GridOrderType type = GridOrderType.SELL;
		double distance = 0;
		double price = gridPosition.getInPrice();
		double _qty = fixMinQty(roundQty(gridPosition.getInQty()), price);
		double qty = _qty;
		double usd = qty * price;
		double sumCoins = qty;
		double sumUsd = usd;
		double lost = 0;
		double newPrice = sumUsd / sumCoins;
		double takeProfit = newPrice * (1 - gridPosition.getTakeProfit());
		double profit = sumUsd - sumCoins * takeProfit;
		double recoveryNeeded = newPrice / price - 1;
		lstOrders.add(new GridOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// SELLS
		for (PriceQty entry : gridPosition.getLstPriceQty())
		{
			number++;
			type = GridOrderType.SELL;

			if (gridPosition.getPriceIncrType() == PriceIncrType.ARITHMETIC)
				distance = distance + entry.getPriceDist();
			else
				distance = (1 + distance) * (1 + entry.getPriceDist()) - 1;

			price = roundPrice(gridPosition.getInPrice() * (1 + distance));

			if (gridPosition.getQtyIncrType() == QtyIncrType.ORDER)
				_qty = _qty * entry.getQtyIncr();
			else
				_qty = sumCoins * entry.getQtyIncr();

			qty = fixMinQty(roundQty(_qty), price);

			usd = price * qty;
			sumCoins += qty;
			sumUsd += usd;
			lost = sumUsd - sumCoins * price;
			newPrice = sumUsd / sumCoins;
			takeProfit = newPrice * (1 - gridPosition.getTakeProfit());
			profit = sumUsd - sumCoins * takeProfit;
			recoveryNeeded = newPrice / price - 1;
			lstOrders.add(new GridOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));
		}
		gridPosition.setSumUsd(sumUsd);

		// STOP LOSS
		number++;
		type = GridOrderType.SL_BUY;

		if (gridPosition.getPriceIncrType() == PriceIncrType.ARITHMETIC)
			distance = distance + gridPosition.getDistBeforeSL();
		else
			distance = (1 + distance) * (1 + gridPosition.getDistBeforeSL()) - 1;

		price = roundPrice(gridPosition.getInPrice() * (1 + distance));
		qty = sumCoins;
		usd = sumUsd;
		sumCoins = 0;
		sumUsd = 0;
		lost = usd - qty * price;
		// newPrice = usd / qty;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = newPrice / price - 1;
		lstOrders.add(new GridOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// TAKE PROFIT
		number++;
		type = GridOrderType.TP_BUY;
		distance = 0;
		price = lstOrders.get(0).getTakeProfit();
		qty = lstOrders.get(0).getCoins();
		usd = qty * price;
		sumCoins = 0;
		sumUsd = 0;
		lost = 0;
		newPrice = 0;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = 0;
		lstOrders.add(new GridOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

	}

	// ------------------------------------------------------------------------
	// LONG
	// ------------------------------------------------------------------------

	public void createLong()
	{
		List<GridOrder> lstOrders = new ArrayList<GridOrder>();
		gridPosition.setLstOrders(lstOrders);

		// FIRST ORDER
		int number = 0;
		GridOrderType type = GridOrderType.BUY;
		double distance = 0;
		double price = gridPosition.getInPrice();
		double _qty = fixMinQty(roundQty(gridPosition.getInQty()), price);
		double qty = _qty;
		double usd = qty * price;
		double sumCoins = qty;
		double sumUsd = usd;
		double lost = 0;
		double newPrice = sumUsd / sumCoins;
		double takeProfit = newPrice * (1 + gridPosition.getTakeProfit());
		double profit = sumCoins * takeProfit - sumUsd;
		double recoveryNeeded = newPrice / price - 1;
		lstOrders.add(new GridOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// BUYS
		for (PriceQty entry : gridPosition.getLstPriceQty())
		{
			number++;
			type = GridOrderType.BUY;

			if (gridPosition.getPriceIncrType() == PriceIncrType.ARITHMETIC)
				distance = distance - entry.getPriceDist();
			else
				distance = (1 + distance) * (1 - entry.getPriceDist()) - 1;

			price = roundPrice(gridPosition.getInPrice() * (1 + distance));

			if (gridPosition.getQtyIncrType() == QtyIncrType.ORDER)
				_qty = _qty * entry.getQtyIncr();
			else
				_qty = sumCoins * entry.getQtyIncr();

			qty = fixMinQty(roundQty(_qty), price);
			
			usd = price * qty;
			sumCoins += qty;
			sumUsd += usd;
			lost = -1 * (sumUsd - sumCoins * price);
			newPrice = sumUsd / sumCoins;
			takeProfit = newPrice * (1 + gridPosition.getTakeProfit());
			profit = sumCoins * takeProfit - sumUsd;
			recoveryNeeded = newPrice / price - 1;
			lstOrders.add(new GridOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));
		}
		gridPosition.setSumUsd(sumUsd);

		// STOP LOSS
		number++;
		type = GridOrderType.SL_SELL;

		if (gridPosition.getPriceIncrType() == PriceIncrType.ARITHMETIC)
			distance = distance - gridPosition.getDistBeforeSL();
		else
			distance = (1 + distance) * (1 - gridPosition.getDistBeforeSL()) - 1;

		price = roundPrice(gridPosition.getInPrice() * (1 + distance));
		qty = sumCoins;
		usd = sumUsd;
		sumCoins = 0;
		sumUsd = 0;
		lost = -1 * (usd - qty * price);
		// newPrice = usd / qty;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = newPrice / price - 1;
		lstOrders.add(new GridOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// TAKE PROFIT
		number++;
		type = GridOrderType.TP_SELL;
		distance = 0;
		price = lstOrders.get(0).getTakeProfit();
		qty = lstOrders.get(0).getCoins();
		usd = qty * price;
		sumCoins = 0;
		sumUsd = 0;
		lost = 0;
		newPrice = 0;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = 0;
		lstOrders.add(new GridOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

	}

	// ------------------------------------------------------------------------
	// POST
	// ------------------------------------------------------------------------

	private static boolean insufficientBalance(Double usdt)
	{
		AccountBalance accBalance = BalanceFuturesService.getAccountBalanceNow();
		double balance = accBalance.getBalance().doubleValue();
		double withdrawAvailable = accBalance.getWithdrawAvailable().doubleValue();

		return  (withdrawAvailable - (usdt / Config.getLeverage()) < balance * Config.getBalanceMinAvailable());
	}

	public String post(PostStyle postStyle) throws Exception
	{
		// --------------------------------------------------------------------
		// --------------------------------------------------------------------
		if (insufficientBalance(gridPosition.getSumUsd()))
		{
			return "Insufficient withdrawal available";
		}

		// --------------------------------------------------------------------
		// --------------------------------------------------------------------

		BigDecimal lastPrice = PriceService.getLastPrice(gridPosition.getSymbol());
		BigDecimal inPrice = (gridPosition.getSide() == GridSide.SHORT) ? gridPosition.getSymbol().subTicks(lastPrice, 10) : gridPosition.getSymbol().addTicks(lastPrice, 10);
		gridPosition.setInPrice(inPrice.doubleValue());

		// --------------------------------------------------------------------
		// --------------------------------------------------------------------
		for (int i = 0; i < gridPosition.getLstOrders().size(); i++)
		{
			GridOrder entry = gridPosition.getLstOrders().get(i);

			if (postStyle == PostStyle.FIRST && i > 0)
			{
				break;
			}
			if (postStyle == PostStyle.OTHERS && i == 0)
			{
				continue;
			}
			if (entry.getStatus() == GridOrderStatus.FILLED)
			{
				continue;
			}

			if (entry.getType().equals(GridOrderType.BUY) || entry.getType().equals(GridOrderType.SELL))
			{
				postOrder(entry);
			}
			else
			{
				if (postStyle == PostStyle.OTHERS)
				{
					postOrder(entry);
				}
			}
		}

		return null;
	}

	private Order postOrder(GridOrder pOrder) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException
	{
		Order orderResult = null;

		if ((pOrder.getType() == GridOrderType.TP_BUY || pOrder.getType() == GridOrderType.TP_SELL) && BotService.isTpRearrangement())
		{
			pOrder.setStatus(GridOrderStatus.DISCARED);
			pOrder.setResult("TP rearrangement is enabled");
			return orderResult;
		}

		if (pOrder.getType() == GridOrderType.BUY)
		{
			orderResult = postOrder(OrderSide.BUY, OrderType.LIMIT, TimeInForce.GTC,
									getSymbol().qtyToStr(pOrder.getCoins()), getSymbol().priceToStr(pOrder.getPrice()),
									null, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == GridOrderType.SELL)
		{
			orderResult = postOrder(OrderSide.SELL, OrderType.LIMIT, TimeInForce.GTC,
									getSymbol().qtyToStr(pOrder.getCoins()), getSymbol().priceToStr(pOrder.getPrice()),
									null, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == GridOrderType.TP_BUY)
		{
			orderResult = postOrder(OrderSide.BUY, OrderType.LIMIT, TimeInForce.GTC,
									getSymbol().qtyToStr(pOrder.getCoins()), getSymbol().priceToStr(pOrder.getPrice()),
									true, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == GridOrderType.TP_SELL)
		{
			orderResult = postOrder(OrderSide.SELL, OrderType.LIMIT, TimeInForce.GTC,
									getSymbol().qtyToStr(pOrder.getCoins()), getSymbol().priceToStr(pOrder.getPrice()),
									true, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == GridOrderType.SL_BUY)
		{
			double stopPrice = pOrder.getPrice();
			orderResult = postOrder(OrderSide.BUY, OrderType.STOP_MARKET, TimeInForce.GTE_GTC,
									null, null,
									null, null, getSymbol().priceToStr(stopPrice), WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, true);
		}
		else if (pOrder.getType() == GridOrderType.SL_SELL)
		{
			double stopPrice = pOrder.getPrice();
			orderResult = postOrder(OrderSide.SELL, OrderType.STOP_MARKET, TimeInForce.GTE_GTC,
									null, null,
									null, null, getSymbol().priceToStr(stopPrice), WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, true);
		}

		if (orderResult != null)
		{
			pOrder.setStatus(GridOrderStatus.FILLED);
			pOrder.setResult(orderResult.getStatus());
		}

		return orderResult;
	}

	private Order postOrder(OrderSide side, OrderType orderType, TimeInForce timeInForce,
							String quantity, String price, Boolean reduceOnly, String newClientOrderId,
							String stopPrice, WorkingType workingType, NewOrderRespType newOrderRespType, Boolean closePosition) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException
	{
		return SyncFuturesClient.postOrder(gridPosition.getSymbol().getPair(), side, PositionSide.BOTH, orderType, timeInForce,
										   quantity, price, reduceOnly, newClientOrderId, stopPrice, workingType, newOrderRespType, closePosition);
	}

}
