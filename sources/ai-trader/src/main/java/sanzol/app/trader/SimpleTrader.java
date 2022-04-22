package sanzol.app.trader;

import java.math.BigDecimal;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.NewOrderRespType;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.enums.TimeInForce;
import com.binance.client.model.enums.WorkingType;
import com.binance.client.model.trade.Order;

import sanzol.app.config.PrivateConfig;
import sanzol.app.model.Symbol;

public class SimpleTrader
{
	public enum Side
	{
		SHORT, LONG
	};

	public static String postHammerOrder(Symbol symbol, Side side, BigDecimal price, BigDecimal coins)
	{
		Order orderResult = null;

		if (side == Side.SHORT)
		{
			orderResult = postOrder(symbol, OrderSide.SELL, OrderType.LIMIT, TimeInForce.GTC, 
									symbol.coinsToStr(coins), symbol.priceToStr(price), null, null, null, WorkingType.CONTRACT_PRICE,
									NewOrderRespType.RESULT, null);
		}
		else if (side == Side.LONG)
		{
			orderResult = postOrder(symbol, OrderSide.BUY, OrderType.LIMIT, TimeInForce.GTC, 
									symbol.coinsToStr(coins), symbol.priceToStr(price), null, null, null, WorkingType.CONTRACT_PRICE,
									NewOrderRespType.RESULT, null);
		}

		return (orderResult != null ? orderResult.getStatus() : "n/a");
	}

	private static Order postOrder(Symbol symbol, OrderSide side, OrderType orderType, TimeInForce timeInForce, 
								   String quantity, String price, String reduceOnly, String newClientOrderId,
								   String stopPrice, WorkingType workingType, NewOrderRespType newOrderRespType, String closePosition)
	{
		RequestOptions options = new RequestOptions();
		SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);
	
		return syncRequestClient.postOrder(symbol.getName(), side, PositionSide.BOTH, orderType, timeInForce, 
								   		   quantity, price, reduceOnly, newClientOrderId, stopPrice, workingType, newOrderRespType, closePosition);
	}

}
