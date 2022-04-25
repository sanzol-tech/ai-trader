package sanzol.app.trader;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.decimal4j.util.DoubleRounder;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.NewOrderRespType;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.enums.TimeInForce;
import com.binance.client.model.enums.WorkingType;
import com.binance.client.model.trade.AccountBalance;
import com.binance.client.model.trade.Order;

import sanzol.app.config.Config;
import sanzol.app.config.PrivateConfig;
import sanzol.app.model.Position;
import sanzol.app.model.PositionOrder;
import sanzol.app.model.PositionOrder.State;
import sanzol.app.model.PositionOrder.Type;
import sanzol.app.task.BalanceService;
import sanzol.app.task.PriceService;

public class PositionTrader
{
	public enum TestMode { TEST, PROD }

	public static TestMode TEST_MODE = TestMode.PROD;

	public enum PostStyle { ALL, FIRST, OTHERS }

	private Position position;

	// API
	private RequestOptions options = new RequestOptions();
	private SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);


	public Position getPosition()
	{
		return position;
	}

	// ------------------------------------------------------------------------

	public PositionTrader(Position position)
	{
		this.position = position;
	}

	// ------------------------------------------------------------------------
	// CONVERSIONS
	// ------------------------------------------------------------------------

	public static String usdToStr(double usd)
	{
		String pattern = "#0.00";
		return new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(usd);
	}

	public String priceToStr(double price)
	{
		return position.getCoin().priceToStr(price);
	}

	public String coinsToStr(double coins)
	{
		return position.getCoin().coinsToStr(coins);
	}

	public double roundPrice(double price)
	{
		return DoubleRounder.round(price, position.getCoin().getTickSize());
	}

	public double roundCoins(double coins)
	{
		return DoubleRounder.round(coins, position.getCoin().getQuantityPrecision());
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
		double price = position.getInPrice();
		double coins = position.getInCoins();
		double usd = coins * price;
		double sumCoins = coins;
		double sumUsd = usd;
		double lost = 0;
		double newPrice = sumUsd / sumCoins;
		double takeProfit = newPrice * (1 - position.getTakeProfit());
		double profit = sumUsd - sumCoins * takeProfit;
		double recoveryNeeded = newPrice / price - 1;
		lstOrders.add(new PositionOrder(number, type, distance, price, coins, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// RE SELLS
		for (int i = 1; i <= position.getIterations(); i++)
		{
			number = i;
			type = Type.SELL;
			distance = Math.pow(1 + position.getPriceIncr(), i) - 1;
			price = roundPrice(position.getInPrice() * Math.pow(1 + position.getPriceIncr(), i));
			coins = roundCoins(position.getInCoins() * Math.pow(1 + position.getCoinsIncr(), i));
			usd = price * coins;
			sumCoins += coins;
			sumUsd += usd;
			lost = sumUsd - sumCoins * price;
			newPrice = sumUsd / sumCoins;
			takeProfit = newPrice * (1 - position.getTakeProfit());
			profit = sumUsd - sumCoins * takeProfit;
			recoveryNeeded = newPrice / price - 1;
			lstOrders.add(new PositionOrder(number, type, distance, price, coins, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));
		}
		position.setSumUsd(sumUsd);

		// STOP LOSS
		number = position.getIterations() + 1;
		type = Type.SL_BUY;
		distance = (Math.pow(1 + position.getPriceIncr(), position.getIterations()) * (1 + position.getDistBeforeSL())) - 1;
		price = roundPrice(position.getInPrice() * (1 + distance));
		coins = sumCoins;
		usd = sumUsd;
		sumCoins = 0;
		sumUsd = 0;
		lost = usd - coins * price;
		newPrice = 0;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = 0;
		lstOrders.add(new PositionOrder(number, type, distance, price, coins, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// TAKE PROFIT
		number = position.getIterations() + 2;
		type = Type.TP_BUY;
		distance = 0;
		price = lstOrders.get(0).getTakeProfit();
		coins = lstOrders.get(0).getCoins();
		usd = price * coins;
		sumCoins = 0;
		sumUsd = 0;
		lost = 0;
		newPrice = 0;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = 0;
		lstOrders.add(new PositionOrder(number, type, distance, price, coins, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

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
		double price = position.getInPrice();
		double coins = position.getInCoins();
		double usd = coins * price;
		double sumCoins = coins;
		double sumUsd = usd;
		double lost = 0;
		double newPrice = sumUsd / sumCoins;
		double takeProfit = newPrice * (1 + position.getTakeProfit());
		double profit = sumCoins * takeProfit - sumUsd;
		double recoveryNeeded = newPrice / price - 1;
		lstOrders.add(new PositionOrder(number, type, distance, price, coins, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// RE SELLS
		for (int i = 1; i <= position.getIterations(); i++)
		{
			number = i;
			type = Type.BUY;
			distance = Math.pow(1 - position.getPriceIncr(), i) - 1;
			price = roundPrice(position.getInPrice() * Math.pow(1 - position.getPriceIncr(), i));
			coins = roundCoins(position.getInCoins() * Math.pow(1 + position.getCoinsIncr(), i));
			usd = price * coins;
			sumCoins += coins;
			sumUsd += usd;
			lost = -1 * (sumUsd - sumCoins * price);
			newPrice = sumUsd / sumCoins;
			takeProfit = newPrice * (1 + position.getTakeProfit());
			profit = sumCoins * takeProfit - sumUsd;
			recoveryNeeded = newPrice / price - 1;
			lstOrders.add(new PositionOrder(number, type, distance, price, coins, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));
		}
		position.setSumUsd(sumUsd);

		// STOP LOSS
		number = position.getIterations() + 1;
		type = Type.SL_SELL;
		distance = (Math.pow(1 - position.getPriceIncr(), position.getIterations()) * (1 - position.getDistBeforeSL())) - 1;
		price = roundPrice(position.getInPrice() * (1 + distance));
		coins = sumCoins;
		usd = sumUsd;
		sumCoins = 0;
		sumUsd = 0;
		lost = -1 * (usd - coins * price);
		newPrice = 0;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = 0;
		lstOrders.add(new PositionOrder(number, type, distance, price, coins, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

		// TAKE PROFIT
		number = position.getIterations() + 2;
		type = Type.TP_SELL;
		distance = 0;
		price = lstOrders.get(0).getTakeProfit();
		coins = lstOrders.get(0).getCoins();
		usd = price * coins;
		sumCoins = 0;
		sumUsd = 0;
		lost = 0;
		newPrice = 0;
		takeProfit = 0;
		profit = 0;
		recoveryNeeded = 0;
		lstOrders.add(new PositionOrder(number, type, distance, price, coins, usd, sumCoins, sumUsd, lost, newPrice, takeProfit, profit, recoveryNeeded));

	}

	// ------------------------------------------------------------------------
	// POST
	// ------------------------------------------------------------------------

	public String post(PostStyle postStyle) throws Exception
	{
		// --------------------------------------------------------------------
		// --------------------------------------------------------------------
		AccountBalance accBalance = BalanceService.getAccountBalanceNow();
		double balance = accBalance.getBalance().doubleValue();
		double withdrawAvailable = accBalance.getWithdrawAvailable().doubleValue();
		if (withdrawAvailable - (position.getSumUsd() / Config.getLeverage()) < balance * Config.getBalance_min_available())
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
				if (markPrice > position.getInPrice())
				{
					return "ERR: mark Price > order Price";
				}
			}
			else
			{
				if (markPrice < position.getInPrice())
				{
					return "ERR: mark Price < order Price";
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

	private Order postOrder(PositionOrder pOrder)
	{
		Order orderResult = null;

		if (pOrder.getType() == Type.BUY)
		{
			orderResult = postOrder(OrderSide.BUY, OrderType.LIMIT, TimeInForce.GTC, 
									coinsToStr(pOrder.getCoins()), priceToStr(pOrder.getPrice()), 
									null, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == Type.SELL)
		{
			orderResult = postOrder(OrderSide.SELL, OrderType.LIMIT, TimeInForce.GTC,
									coinsToStr(pOrder.getCoins()), priceToStr(pOrder.getPrice()),
									null, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		if (pOrder.getType() == Type.TP_BUY)
		{
			orderResult = postOrder(OrderSide.BUY, OrderType.LIMIT, TimeInForce.GTC, 
					coinsToStr(pOrder.getCoins()), priceToStr(pOrder.getPrice()),
									"true", null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == Type.TP_SELL)
		{
			orderResult = postOrder(OrderSide.SELL, OrderType.LIMIT, TimeInForce.GTC,
									coinsToStr(pOrder.getCoins()), priceToStr(pOrder.getPrice()),
									"true", null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);
		}
		else if (pOrder.getType() == Type.SL_BUY)
		{
			double stopPrice = pOrder.getPrice();
			orderResult = postOrder(OrderSide.BUY, OrderType.STOP_MARKET, TimeInForce.GTC,
									null, null,
									null, null, priceToStr(stopPrice), WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, "true");
		}
		else if (pOrder.getType() == Type.SL_SELL)
		{
			double stopPrice = pOrder.getPrice();
			orderResult = postOrder(OrderSide.SELL, OrderType.STOP_MARKET, TimeInForce.GTC,
									null, null,
									null, null, priceToStr(stopPrice), WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, "true");
		}

		if (orderResult != null)
		{
			pOrder.setState(State.EXECUTED);
			pOrder.setResult(orderResult.getStatus());
		}

		return orderResult;
	}

	private Order postOrder(OrderSide side, OrderType orderType, TimeInForce timeInForce, 
							String quantity, String price, String reduceOnly, String newClientOrderId,
							String stopPrice, WorkingType workingType, NewOrderRespType newOrderRespType, String closePosition)
	{
		if (TEST_MODE != TestMode.PROD)
		{
			return null;
		}

		return syncRequestClient.postOrder(position.getSymbol(), side, PositionSide.BOTH, orderType, timeInForce, 
										   quantity, price, reduceOnly, newClientOrderId, stopPrice, workingType, newOrderRespType, closePosition);
	}

}
