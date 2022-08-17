package sanzol.app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.NewOrderRespType;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.enums.TimeInForce;
import com.binance.client.model.enums.WorkingType;
import com.binance.client.model.trade.Order;
import com.binance.client.model.trade.PositionRisk;

import sanzol.app.config.PrivateConfig;
import sanzol.app.model.GOrder;
import sanzol.app.task.PositionService;

public class SimpleTrader
{

	public static Map<String, GOrder> calc(Symbol coin, String posSide, BigDecimal posPrice, BigDecimal posQty, BigDecimal shootPrice, BigDecimal shootQty) throws Exception 
	{
		Map<String, GOrder> mapPosition = new HashMap<String, GOrder>();

		// --- POSITION -------------------------------------------------------
		PositionRisk positionRisk = PositionService.getPositionRisk(coin.getName());
		boolean positionExists = (positionRisk != null && positionRisk.getPositionAmt().compareTo(BigDecimal.ZERO) != 0); 
		if (positionExists)
		{
			posSide = positionRisk.getPositionAmt().doubleValue() < 0 ? "SELL" : "BUY";
			posPrice = positionRisk.getEntryPrice();
			posQty = positionRisk.getPositionAmt().abs();
		}
		mapPosition.put("POS", new GOrder(posPrice, posQty));

		// --- SHOOT ----------------------------------------------------------
		mapPosition.put("SHOOT", new GOrder(shootPrice, shootQty));

		// --- RESULT ---------------------------------------------------------
		BigDecimal resultQty = posQty.add(shootQty);
		BigDecimal resultUSD = posPrice.multiply(posQty).add(shootPrice.multiply(shootQty));
		BigDecimal resultPrice = resultUSD.divide(resultQty, RoundingMode.HALF_UP);
		mapPosition.put("RESULT", new GOrder(resultPrice, resultQty, resultUSD));

		// --- DISTANCES ------------------------------------------------------
		if ("BUY".equals(posSide))
		{
			BigDecimal shootDist = BigDecimal.ONE.subtract(posPrice.divide(shootPrice, RoundingMode.HALF_UP));
			mapPosition.get("SHOOT").setDist(shootDist);
			BigDecimal resultDist = BigDecimal.ONE.subtract(resultPrice.divide(shootPrice, RoundingMode.HALF_UP));
			mapPosition.get("RESULT").setDist(resultDist);
		}
		else if ("SELL".equals(posSide))
		{
			BigDecimal shootDist = posPrice.divide(shootPrice, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
			mapPosition.get("SHOOT").setDist(shootDist);
			BigDecimal resultDist = resultPrice.divide(shootPrice, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
			mapPosition.get("RESULT").setDist(resultDist);
		}		

		return mapPosition;
	}	

	public static String postOrder(Symbol symbol, String side, BigDecimal price, BigDecimal coins)
	{
		OrderSide orderSide = "SHORT".equals(side) ? OrderSide.SELL : "LONG".equals(side) ? OrderSide.BUY : null;

		Order orderResult = postOrder(
								symbol, orderSide, OrderType.LIMIT, TimeInForce.GTC, 
								symbol.qtyToStr(coins), symbol.priceToStr(price), null, null, null, WorkingType.CONTRACT_PRICE,
								NewOrderRespType.RESULT, null);

		return (orderResult != null ? orderResult.getStatus() : "n/a");
	}

	public static String postTprofit(Symbol symbol, String side, BigDecimal price, BigDecimal coins)
	{
		OrderSide orderSide = "SHORT".equals(side) ? OrderSide.BUY : "LONG".equals(side) ? OrderSide.SELL : null;

		Order orderResult = postOrder(
								symbol, orderSide, OrderType.LIMIT, TimeInForce.GTC, 
								symbol.qtyToStr(coins), symbol.priceToStr(price), "true", null, null, WorkingType.CONTRACT_PRICE, 
								NewOrderRespType.RESULT, null);

		return (orderResult != null ? orderResult.getStatus() : "n/a");
	}
	
	public static String postSMarket(Symbol symbol, String side, BigDecimal price)
	{
		OrderSide orderSide = "SHORT".equals(side) ? OrderSide.BUY : "LONG".equals(side) ? OrderSide.SELL : null;

		Order orderResult = postOrder(
								symbol, orderSide, OrderType.STOP_MARKET, TimeInForce.GTE_GTC,
								null, null, null, null, symbol.priceToStr(price), WorkingType.CONTRACT_PRICE, 
								NewOrderRespType.RESULT, "true");	
		
		return (orderResult != null ? orderResult.getStatus() : "n/a");
	}		

	public static void cancelOrder(Order order)
	{
		RequestOptions options = new RequestOptions();
		SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

		syncRequestClient.cancelOrder(order.getSymbol(), order.getOrderId(), null);
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
