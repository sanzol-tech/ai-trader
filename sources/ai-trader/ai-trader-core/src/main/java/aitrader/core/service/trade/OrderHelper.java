package aitrader.core.service.trade;

import java.math.BigDecimal;

import aitrader.core.config.CoreLog;
import aitrader.core.config.PrivateConfig;
import aitrader.core.model.Symbol;
import aitrader.core.service.position.BalanceService;
import binance.futures.enums.NewOrderRespType;
import binance.futures.enums.OrderSide;
import binance.futures.enums.OrderType;
import binance.futures.enums.PositionMode;
import binance.futures.enums.PositionSide;
import binance.futures.enums.TimeInForce;
import binance.futures.enums.WorkingType;
import binance.futures.impl.SignedClient;
import binance.futures.model.Order;

public class OrderHelper
{
	public static Order postOrder(Symbol symbol, OrderSide orderSide, PositionSide positionSide, 
			  OrderType orderType, TimeInForce timeInForce, String quantity, 
			  String price, Boolean reduceOnly, String newClientOrderId,
			  String stopPrice, WorkingType workingType, 
			  NewOrderRespType newOrderRespType, Boolean closePosition) throws Exception
	{
		SignedClient signedClient = SignedClient.create(PrivateConfig.getApiKey(), PrivateConfig.getSecretKey());

		if (PositionModeService.getPositionMode() == PositionMode.ONE_WAY)
		{
			positionSide = PositionSide.BOTH;
		}

		Order orderResult = signedClient.postOrder(
					symbol.getPair(), orderSide, positionSide, 
					orderType, timeInForce, quantity,
					price, reduceOnly, newClientOrderId, 
					stopPrice, workingType,
					newOrderRespType, closePosition);

		CoreLog.info(String.format("Post order %s (%s) %s %s - price: %s  qty: %s - %s", orderResult.getSide(), orderResult.getPositionSide(), orderResult.getSymbol(), orderResult.getType(), orderResult.getPrice(), orderResult.getOrigQty(), orderResult.getStatus()));

		return orderResult;
	}

	public static String postOrder(Symbol symbol, PositionSide positionSide, OrderSide orderSide, BigDecimal price, BigDecimal quantity) throws Exception
	{

		if (!BalanceService.isAvailable(quantity, price))
		{
			throw new Exception(String.format("Insufficient withdrawal available to post order %s %s - price: %f  qty: %f", orderSide.name(), symbol.getPair(), price, quantity));
		}

		Order orderResult = postOrder(
								symbol, orderSide, positionSide, OrderType.LIMIT, 
								TimeInForce.GTC, symbol.qtyToStr(quantity), symbol.priceToStr(price), 
								null, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);

		return (orderResult != null ? orderResult.getStatus() : "n/a");
	}

	public static Order postMarketOrder(Symbol symbol, PositionSide positionSide, OrderSide orderSide, BigDecimal price, BigDecimal quantity) throws Exception
	{

		if (!BalanceService.isAvailable(quantity, price))
		{
			throw new Exception(String.format("Insufficient withdrawal available to post order %s %s - price: %f  qty: %f", orderSide.name(), symbol.getPair(), price, quantity));
		}

		Order order = postOrder(
						symbol, orderSide, positionSide, OrderType.MARKET, 
						null, symbol.qtyToStr(quantity), null, 
						null, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);

		return order;
	}

	public static String postTakeProfit(Symbol symbol, PositionSide positionSide, OrderSide orderSide, BigDecimal price, BigDecimal quantity) throws Exception
	{
		Order orderResult = postOrder(
								symbol, orderSide, positionSide, OrderType.LIMIT, 
								TimeInForce.GTC, symbol.qtyToStr(quantity), symbol.priceToStr(price),
								true, null, null, WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, null);

		return (orderResult != null ? orderResult.getStatus() : "n/a");
	}

	public static String postStopLoss(Symbol symbol, PositionSide positionSide, OrderSide orderSide, BigDecimal price) throws Exception
	{
		Order orderResult = postOrder(
								symbol, orderSide, positionSide, OrderType.STOP_MARKET, 
								TimeInForce.GTE_GTC, null, null, 
								null, null, symbol.priceToStr(price), WorkingType.CONTRACT_PRICE, NewOrderRespType.RESULT, true);

		return (orderResult != null ? orderResult.getStatus() : "n/a");
	}

	public static void cancelOrder(Symbol symbol, long orderId) throws Exception
	{
		SignedClient signedClient = SignedClient.create(PrivateConfig.getApiKey(), PrivateConfig.getSecretKey());

		signedClient.cancelOrder(symbol.getPair(), orderId, null);
	}

}
