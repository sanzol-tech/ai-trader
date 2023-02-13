package aitrader.core.service.trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import aitrader.core.config.PrivateConfig;
import aitrader.core.model.GridOrder;
import aitrader.core.model.GridPosition;
import aitrader.core.model.PriceQty;
import aitrader.core.model.Symbol;
import aitrader.core.model.enums.OpType;
import aitrader.core.model.enums.PriceIncrType;
import aitrader.core.model.enums.QtyIncrType;
import aitrader.core.service.position.BalanceService;
import aitrader.core.service.position.PositionsBot;
import aitrader.core.service.symbol.SymbolInfoService;
import binance.futures.enums.NewOrderRespType;
import binance.futures.enums.OrderSide;
import binance.futures.enums.OrderType;
import binance.futures.enums.PositionSide;
import binance.futures.enums.TimeInForce;
import binance.futures.enums.WorkingType;
import binance.futures.impl.SignedClient;
import binance.futures.model.Order;

public class GridTrade
{
	private static final double MIN_USD_AMOUNT = 5.0;
	
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
		final double minUsdAmount = MIN_USD_AMOUNT;

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

	// --------------------------------------------------------------------

	public GridTrade(GridPosition position)
	{
		this.gridPosition = position;
	}

	// --------------------------------------------------------------------
	// SHORT
	// --------------------------------------------------------------------

	public void createShort()
	{
		List<GridOrder> lstOrders = new ArrayList<GridOrder>();
		gridPosition.setLstOrders(lstOrders);

		// FIRST ORDER
		int number = 0;
		OrderSide orderSide = OrderSide.SELL;
		OpType opType = OpType.ORDER;
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
		lstOrders.add(new GridOrder(number, orderSide, opType, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// SELLS
		for (PriceQty entry : gridPosition.getLstPriceQty())
		{
			number++;
			orderSide = OrderSide.SELL;
			opType = OpType.ORDER;

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
			lstOrders.add(new GridOrder(number, orderSide, opType, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));
		}
		gridPosition.setSumUsd(sumUsd);

		// STOP LOSS
		number++;
		orderSide = OrderSide.BUY;
		opType = OpType.STOP_LOSS;

		if (gridPosition.getPriceIncrType() == PriceIncrType.ARITHMETIC)
			distance = distance + gridPosition.getStopLoss();
		else
			distance = (1 + distance) * (1 + gridPosition.getStopLoss()) - 1;

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
		lstOrders.add(new GridOrder(number, orderSide, opType, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// TAKE PROFIT
		number++;
		orderSide = OrderSide.BUY;
		opType = OpType.TAKE_PROFIT;
		distance = 0;
		price = lstOrders.get(0).getTakeProfit();
		qty = lstOrders.get(0).getQuantity();
		usd = qty * price;
		sumCoins = 0;
		sumUsd = 0;
		lost = 0;
		newPrice = 0;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = 0;
		lstOrders.add(new GridOrder(number, orderSide, opType, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

	}

	// --------------------------------------------------------------------
	// LONG
	// --------------------------------------------------------------------

	public void createLong()
	{
		List<GridOrder> lstOrders = new ArrayList<GridOrder>();
		gridPosition.setLstOrders(lstOrders);

		// FIRST ORDER
		int number = 0;
		OrderSide orderSide = OrderSide.BUY;
		OpType opType = OpType.ORDER;
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
		lstOrders.add(new GridOrder(number, orderSide, opType, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// BUYS
		for (PriceQty entry : gridPosition.getLstPriceQty())
		{
			number++;
			orderSide = OrderSide.BUY;
			opType = OpType.ORDER;

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
			lstOrders.add(new GridOrder(number, orderSide, opType, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));
		}
		gridPosition.setSumUsd(sumUsd);

		// STOP LOSS
		number++;
		orderSide = OrderSide.SELL;
		opType = OpType.STOP_LOSS;

		if (gridPosition.getPriceIncrType() == PriceIncrType.ARITHMETIC)
			distance = distance - gridPosition.getStopLoss();
		else
			distance = (1 + distance) * (1 - gridPosition.getStopLoss()) - 1;

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
		lstOrders.add(new GridOrder(number, orderSide, opType, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// TAKE PROFIT
		number++;
		orderSide = OrderSide.SELL;
		opType = OpType.TAKE_PROFIT;
		distance = 0;
		price = lstOrders.get(0).getTakeProfit();
		qty = lstOrders.get(0).getQuantity();
		usd = qty * price;
		sumCoins = 0;
		sumUsd = 0;
		lost = 0;
		newPrice = 0;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = 0;
		lstOrders.add(new GridOrder(number, orderSide, opType, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

	}

	// --------------------------------------------------------------------
	// POST
	// --------------------------------------------------------------------

	public String post(PostStyle postStyle) throws Exception
	{
		// --------------------------------------------------------------------
		// --------------------------------------------------------------------
		if (!BalanceService.isAvailable(gridPosition.getSumUsd()))
		{
			return "Insufficient withdrawal available";
		}

		// --------------------------------------------------------------------
		// --------------------------------------------------------------------

		BigDecimal lastPrice = SymbolInfoService.getLastPrice(gridPosition.getSymbol());
		BigDecimal inPrice = (gridPosition.getPositionSide() == PositionSide.SHORT) ? gridPosition.getSymbol().subTicks(lastPrice, 10) : gridPosition.getSymbol().addTicks(lastPrice, 10);
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
			if (entry.getStatus() != null)
			{
				continue;
			}

			if (entry.getOpType() == OpType.ORDER)
			{
				postOrder(entry);
				continue;
			}

			if (entry.getOpType() == OpType.TAKE_PROFIT && gridPosition.isTpEnabled())
			{
				if (PositionsBot.isTpRearrangement())
				{
					entry.setStatus("DISCARED");
					entry.setResult("TP rearrangement is enabled");
					continue;
				}

				postOrder(entry);
				continue;
			}

			if (entry.getOpType() == OpType.STOP_LOSS && gridPosition.isSlEnabled())
			{
				postOrder(entry);
				continue;
			}
				
		}

		return null;
	}

	private Order postOrder(GridOrder gridOrder) throws Exception
	{
		Order orderResult = null;

		if (gridOrder.getOpType() == OpType.ORDER)
		{
			orderResult = postOrder(gridPosition.getSymbol(),gridOrder.getOrderSide(), gridPosition.getPositionSide(), OrderType.LIMIT, TimeInForce.GTC,
									getSymbol().qtyToStr(gridOrder.getQuantity()), getSymbol().priceToStr(gridOrder.getPrice()),
									null, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (gridOrder.getOpType() == OpType.TAKE_PROFIT)
		{
			orderResult = postOrder(gridPosition.getSymbol(), gridOrder.getOrderSide(), gridPosition.getPositionSide(), OrderType.LIMIT, TimeInForce.GTC,
									getSymbol().qtyToStr(gridOrder.getQuantity()), getSymbol().priceToStr(gridOrder.getPrice()),
									true, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (gridOrder.getOpType() == OpType.STOP_LOSS)
		{
			double stopPrice = gridOrder.getPrice();
			orderResult = postOrder(gridPosition.getSymbol(), gridOrder.getOrderSide(), gridPosition.getPositionSide(), OrderType.STOP_MARKET, TimeInForce.GTE_GTC,
									null, null,
									null, null, getSymbol().priceToStr(stopPrice), WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, true);
		}

		if (orderResult != null)
		{
			gridOrder.setStatus("FILLED");
			gridOrder.setResult(orderResult.getStatus());
		}

		return orderResult;
	}

	private static Order postOrder(Symbol symbol, OrderSide orderSide, PositionSide positionSide, OrderType orderType, TimeInForce timeInForce,
								   String quantity, String price, Boolean reduceOnly, String newClientOrderId,
								   String stopPrice, WorkingType workingType, NewOrderRespType newOrderRespType, Boolean closePosition) throws Exception
	{
		SignedClient signedClient = SignedClient.create(PrivateConfig.getApiKey(), PrivateConfig.getSecretKey());

		return signedClient.postOrder(symbol.getPair(), orderSide, positionSide, orderType, timeInForce,
					   	  			  quantity, price, reduceOnly, newClientOrderId, stopPrice, workingType, newOrderRespType, closePosition);
	}	

}
