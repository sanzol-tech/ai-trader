package sanzol.aitrader.be.service;

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
import sanzol.aitrader.be.config.Config;
import sanzol.aitrader.be.model.Position;
import sanzol.aitrader.be.model.PositionOrder;
import sanzol.aitrader.be.model.PriceQty;
import sanzol.aitrader.be.model.Symbol;
import sanzol.aitrader.be.model.PositionOrder.State;
import sanzol.aitrader.be.model.PositionOrder.Type;

public class PositionTrader
{
	public enum TestMode { TEST, PROD }

	public enum PostStyle { ALL, FIRST, OTHERS }

	private Position position;

	public Position getPosition()
	{
		return position;
	}

	public Symbol getSymbol()
	{
		return position.getCoin();
	}
	
	// ------------------------------------------------------------------------

	public PositionTrader(Position position)
	{
		this.position = position;
	}

	// ------------------------------------------------------------------------
	// SHORT
	// ------------------------------------------------------------------------

	public void createShort()
	{
		List<PositionOrder> lstOrders = new ArrayList<PositionOrder>();
		position.setLstOrders(lstOrders);

		// FIRST ORDER
		int number = 0;
		Type type = Type.SELL;
		double distance = 0;
		double qtyIncr = 0;
		double price = position.getInPrice();
		double qty = position.getInQty();
		double usd = qty * price;
		double sumCoins = qty;
		double sumUsd = usd;
		double lost = 0;
		double newPrice = sumUsd / sumCoins;
		double takeProfit = newPrice * (1 - position.getTakeProfit());
		double profit = sumUsd - sumCoins * takeProfit;
		double recoveryNeeded = newPrice / price - 1;
		lstOrders.add(new PositionOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// RE SELLS
		for (PriceQty entry : position.getLstPriceQty())
		{
			number++;
			type = Type.SELL;

			if (position.isArithmetic())
				distance = distance + entry.getPriceDist();
			else
				distance = (1 + distance) * (1 + entry.getPriceDist()) - 1;

			price = getSymbol().roundPrice(position.getInPrice() * (1 + distance));

			qtyIncr = (1 + qtyIncr) * ( 1 + entry.getQtyIncr()) - 1;
			qty = getSymbol().roundQty(position.getInQty() * (1 + qtyIncr));
			//qty = qty * (1 + entry.getQtyIncr());

			usd = price * qty;
			sumCoins += qty;
			sumUsd += usd;
			lost = sumUsd - sumCoins * price;
			newPrice = sumUsd / sumCoins;
			takeProfit = newPrice * (1 - position.getTakeProfit());
			profit = sumUsd - sumCoins * takeProfit;
			recoveryNeeded = newPrice / price - 1;
			lstOrders.add(new PositionOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));
		}
		position.setSumUsd(sumUsd);

		// STOP LOSS
		number++;
		type = Type.SL_BUY;
		
		if (position.isArithmetic())
			distance = distance + position.getDistBeforeSL();
		else
			distance = (1 + distance) * (1 + position.getDistBeforeSL()) - 1;

		price = getSymbol().roundPrice(position.getInPrice() * (1 + distance));
		qty = sumCoins;
		usd = sumUsd;
		sumCoins = 0;
		sumUsd = 0;
		lost = usd - qty * price;
		// newPrice = usd / qty;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = newPrice / price - 1;
		lstOrders.add(new PositionOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// TAKE PROFIT
		number++;
		type = Type.TP_BUY;
		distance = 0;
		price = lstOrders.get(0).getTakeProfit();
		qty = lstOrders.get(0).getCoins();
		usd = price * qty;
		sumCoins = 0;
		sumUsd = 0;
		lost = 0;
		newPrice = 0;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = 0;
		lstOrders.add(new PositionOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

	}

	// ------------------------------------------------------------------------
	// LONG
	// ------------------------------------------------------------------------

	public void createLong()
	{
		List<PositionOrder> lstOrders = new ArrayList<PositionOrder>();
		position.setLstOrders(lstOrders);

		// FIRST ORDER
		int number = 0;
		Type type = Type.BUY;
		double distance = 0;
		double qtyIncr = 0;
		double price = position.getInPrice();
		double qty = position.getInQty();
		double usd = qty * price;
		double sumCoins = qty;
		double sumUsd = usd;
		double lost = 0;
		double newPrice = sumUsd / sumCoins;
		double takeProfit = newPrice * (1 + position.getTakeProfit());
		double profit = sumCoins * takeProfit - sumUsd;
		double recoveryNeeded = newPrice / price - 1;
		lstOrders.add(new PositionOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// RE BUYS
		for (PriceQty entry : position.getLstPriceQty())
		{
			number++;
			type = Type.BUY;

			if (position.isArithmetic())
				distance = distance - entry.getPriceDist();
			else
				distance = (1 + distance) * (1 - entry.getPriceDist()) - 1;

			price = getSymbol().roundPrice(position.getInPrice() * (1 + distance));

			qtyIncr = (1 + qtyIncr) * ( 1 + entry.getQtyIncr()) - 1;
			qty = getSymbol().roundQty(position.getInQty() * (1 + qtyIncr));
			//qty = qty * (1 + entry.getQtyIncr());

			usd = price * qty;
			sumCoins += qty;
			sumUsd += usd;
			lost = -1 * (sumUsd - sumCoins * price);
			newPrice = sumUsd / sumCoins;
			takeProfit = newPrice * (1 + position.getTakeProfit());
			profit = sumCoins * takeProfit - sumUsd;
			recoveryNeeded = newPrice / price - 1;
			lstOrders.add(new PositionOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));
		}
		position.setSumUsd(sumUsd);

		// STOP LOSS
		number++;
		type = Type.SL_SELL;

		if (position.isArithmetic())
			distance = distance - position.getDistBeforeSL();
		else
			distance = (1 + distance) * (1 - position.getDistBeforeSL()) - 1;

		price = getSymbol().roundPrice(position.getInPrice() * (1 + distance));
		qty = sumCoins;
		usd = sumUsd;
		sumCoins = 0;
		sumUsd = 0;
		lost = -1 * (usd - qty * price);
		// newPrice = usd / qty;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = newPrice / price - 1;
		lstOrders.add(new PositionOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// TAKE PROFIT
		number++;
		type = Type.TP_SELL;
		distance = 0;
		price = lstOrders.get(0).getTakeProfit();
		qty = lstOrders.get(0).getCoins();
		usd = price * qty;
		sumCoins = 0;
		sumUsd = 0;
		lost = 0;
		newPrice = 0;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = 0;
		lstOrders.add(new PositionOrder(number, type, distance, price, qty, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

	}

	// ------------------------------------------------------------------------
	// POST
	// ------------------------------------------------------------------------

	private static boolean insufficientBalance(Double usdt)
	{
		AccountBalance accBalance = BalanceService.getAccountBalanceNow();
		double balance = accBalance.getBalance().doubleValue();
		double withdrawAvailable = accBalance.getWithdrawAvailable().doubleValue();

		return  (withdrawAvailable - (usdt / Config.getLeverage()) < balance * Config.getBalanceMinAvailable());
	}

	public String post(PostStyle postStyle) throws Exception
	{
		// --------------------------------------------------------------------
		// --------------------------------------------------------------------
		if (insufficientBalance(position.getSumUsd()))
		{
			return "Insufficient withdrawal available";
		}
		
		// --------------------------------------------------------------------
		// --------------------------------------------------------------------
		if (postStyle != PostStyle.OTHERS)
		{
			double markPrice = PriceService.getLastPrice(position.getCoin()).doubleValue();
			if (position.isShort())
			{
				if (position.isMarkPrice())
				{
					if (markPrice < getSymbol().subTicks(position.getInPrice(), 20))
					{
						return "ERR: mark Price < in Price";
					}
					position.setInPrice(markPrice);
				}
				else
				{
					if (markPrice > position.getInPrice())
					{
						return "ERR: mark Price > in Price";
					}
				}
			}
			else
			{
				if (position.isMarkPrice())
				{
					if (markPrice > getSymbol().addTicks(position.getInPrice(), 20))
					{
						return "ERR: mark Price > in Price";
					}
					position.setInPrice(markPrice);
				}
				else
				{
					if (markPrice < position.getInPrice())
					{
						return "ERR: mark Price < in Price";
					}
					
				}
			}
		}

		// --------------------------------------------------------------------
		// --------------------------------------------------------------------
		for (int i = 0; i < position.getLstOrders().size(); i++)
		{
			PositionOrder entry = position.getLstOrders().get(i);

			if (postStyle == PostStyle.FIRST && i > 0)
			{
				break;
			}
			if (postStyle == PostStyle.OTHERS && i == 0)
			{
				continue;
			}
			if (entry.getState() == State.EXECUTED)
			{
				continue;
			}

			if (entry.getType().equals(Type.BUY) || entry.getType().equals(Type.SELL))
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

	private Order postOrder(PositionOrder pOrder) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException
	{
		Order orderResult = null;

		if ((pOrder.getType() == Type.TP_BUY || pOrder.getType() == Type.TP_SELL) && BotService.isTpRearrangement())
		{
			pOrder.setState(State.DISCARED);
			pOrder.setResult("TP rearrangement is enabled");
			return orderResult;
		}

		if (pOrder.getType() == Type.BUY)
		{
			orderResult = postOrder(OrderSide.BUY, OrderType.LIMIT, TimeInForce.GTC, 
									getSymbol().qtyToStr(pOrder.getCoins()), getSymbol().priceToStr(pOrder.getPrice()), 
									null, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == Type.SELL)
		{
			orderResult = postOrder(OrderSide.SELL, OrderType.LIMIT, TimeInForce.GTC,
									getSymbol().qtyToStr(pOrder.getCoins()), getSymbol().priceToStr(pOrder.getPrice()),
									null, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == Type.TP_BUY)
		{
			orderResult = postOrder(OrderSide.BUY, OrderType.LIMIT, TimeInForce.GTC, 
									getSymbol().qtyToStr(pOrder.getCoins()), getSymbol().priceToStr(pOrder.getPrice()),
									true, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == Type.TP_SELL)
		{
			orderResult = postOrder(OrderSide.SELL, OrderType.LIMIT, TimeInForce.GTC,
									getSymbol().qtyToStr(pOrder.getCoins()), getSymbol().priceToStr(pOrder.getPrice()),
									true, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == Type.SL_BUY)
		{
			double stopPrice = pOrder.getPrice();
			orderResult = postOrder(OrderSide.BUY, OrderType.STOP_MARKET, TimeInForce.GTE_GTC,
									null, null,
									null, null, getSymbol().priceToStr(stopPrice), WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, true);
		}
		else if (pOrder.getType() == Type.SL_SELL)
		{
			double stopPrice = pOrder.getPrice();
			orderResult = postOrder(OrderSide.SELL, OrderType.STOP_MARKET, TimeInForce.GTE_GTC,
									null, null,
									null, null, getSymbol().priceToStr(stopPrice), WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, true);
		}

		if (orderResult != null)
		{
			pOrder.setState(State.EXECUTED);
			pOrder.setResult(orderResult.getStatus());
		}

		return orderResult;
	}

	private Order postOrder(OrderSide side, OrderType orderType, TimeInForce timeInForce, 
							String quantity, String price, Boolean reduceOnly, String newClientOrderId,
							String stopPrice, WorkingType workingType, NewOrderRespType newOrderRespType, Boolean closePosition) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException
	{
		return SyncFuturesClient.postOrder(position.getSymbol(), side, PositionSide.BOTH, orderType, timeInForce, 
										   quantity, price, reduceOnly, newClientOrderId, stopPrice, workingType, newOrderRespType, closePosition);
	}

}
