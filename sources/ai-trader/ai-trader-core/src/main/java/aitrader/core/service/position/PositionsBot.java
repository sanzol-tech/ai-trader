package aitrader.core.service.position;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import aitrader.core.config.CoreConfig;
import aitrader.core.model.Position;
import aitrader.core.model.PositionName;
import aitrader.core.model.PositionOrder;
import aitrader.core.model.Symbol;
import aitrader.core.service.symbol.SymbolInfoService;
import aitrader.core.service.trade.OrderHelper;
import aitrader.util.BeepUtils;
import aitrader.util.observable.Handler;
import binance.futures.enums.OrderSide;
import binance.futures.enums.PositionSide;

public final class PositionsBot
{
	private static boolean isTpRearrangement = false;
	private static BigDecimal tpPercent = null;

	private static boolean isSlRearrangement = false;
	private static BigDecimal slUsd = null;

	public static boolean isTpRearrangement()
	{
		return isTpRearrangement;
	}

	public static BigDecimal getTpPercent()
	{
		return tpPercent;
	}

	public static boolean isSlRearrangement()
	{
		return isSlRearrangement;
	}

	public static BigDecimal getSlUsd()
	{
		return slUsd;
	}

	public static void tpRearrangementOn(BigDecimal tpPercent) throws Exception
	{
		PositionsBot.tpPercent = tpPercent;
		isTpRearrangement = true;
		onPositionUpdate();
	}

	public static void tpRearrangementOff() throws Exception
	{
		isTpRearrangement = false;
		onPositionUpdate();
	}
	
	public static void slRearrangementOn(BigDecimal slUsd) throws Exception
	{
		PositionsBot.slUsd = slUsd;
		isSlRearrangement = true;
		onPositionUpdate();
	}

	public static void slRearrangementOff() throws Exception
	{
		isSlRearrangement = false;
		onPositionUpdate();
	}
	
	// --------------------------------------------------------------------
	// TP & SL
	// --------------------------------------------------------------------

	public synchronized static void onPositionUpdate() throws Exception
	{
		Map<PositionName, Position> mapPositions = PositionService.getMapPositions();

		for (Position entry : mapPositions.values())
		{
			if (entry.isOpen() && entry.getQuantity().compareTo(BigDecimal.ZERO) != 0)
			{
				Symbol symbol = entry.getSymbol();

				PositionSide positionSide = (entry.getQuantity().doubleValue() < 0 ? PositionSide.SHORT : PositionSide.LONG);
				BigDecimal posPrice = entry.getEntryPrice();
				BigDecimal posQty = entry.getQuantity().abs();

				// --------------------------------------------------------------------
				tpRearrangement(symbol, positionSide, posQty, posPrice);
				slRearrangement(symbol, positionSide, posQty, posPrice);
			}
		}
	}

	private static void tpRearrangement(Symbol symbol, PositionSide positionSide, BigDecimal posQty, BigDecimal price) throws Exception
	{
		if (isTpRearrangement)
		{
			OrderSide orderSide = (positionSide == PositionSide.SHORT) ? OrderSide.BUY : OrderSide.SELL;
			BigDecimal tpCoef = (positionSide == PositionSide.SHORT) ? BigDecimal.ONE.subtract(tpPercent) : BigDecimal.ONE.add(tpPercent);
			BigDecimal newPrice = price.multiply(tpCoef).setScale(symbol.getPricePrecision(), RoundingMode.HALF_UP);

			PositionOrder tpOrder = PositionService.getTpOrder(symbol.getPair(), positionSide.name());
			if (tpOrder != null)
			{
				BigDecimal tpQty = tpOrder.getQuantity();
				BigDecimal tpPrice = tpOrder.getPrice();

				if (posQty.compareTo(tpQty) != 0)
				{
					info(symbol.getNameLeft() + " TP-REARRANGEMENT");
					info(String.format("[qty: %f price: %f] --> [qty: %f price: %f]", tpQty, tpPrice, posQty, newPrice));
					BeepUtils.beep2();

					// CANCEL ORDER
					OrderHelper.cancelOrder(tpOrder.getSymbol(), tpOrder.getOrderId());

					// ADD NEW ORDER
					OrderHelper.postTakeProfit(symbol, positionSide, orderSide, price, posQty);
				}
			}
			else
			{
				info(symbol.getNameLeft() + " TP-REARRANGEMENT");
				info(String.format("NONE -> [qty: %f price: %f]", posQty, newPrice));
				BeepUtils.beep2();

				// ADD NEW ORDER
				OrderHelper.postTakeProfit(symbol, positionSide, orderSide, newPrice, posQty);
			}
		}
	}

	private static void slRearrangement(Symbol symbol, PositionSide positionSide, BigDecimal posQty, BigDecimal posPrice) throws Exception
	{
		if (isSlRearrangement)
		{
			OrderSide orderSide = (positionSide == PositionSide.SHORT) ? OrderSide.BUY : OrderSide.SELL;

			BigDecimal slPriceNew;
			if (positionSide == PositionSide.SHORT) {
				slPriceNew = slUsd.add(posPrice.multiply(posQty)).divide(posQty, symbol.getPricePrecision(), RoundingMode.HALF_UP);
			} else {
				slPriceNew = ((posPrice.multiply(posQty)).subtract(slUsd)).divide(posQty, symbol.getPricePrecision(), RoundingMode.HALF_UP);
			}

			PositionOrder slOrder = PositionService.getSlOrder(symbol.getPair(), positionSide.name());
			if (slOrder == null)
			{
				info(symbol.getNameLeft() + " SL-REARRANGEMENT");
				info(String.format("(%f %s) : NONE -> [price: %f]", slUsd, CoreConfig.getDefaultSymbolRight(), slPriceNew));
				BeepUtils.beep2();

				// ADD NEW SL-ORDER
				OrderHelper.postStopLoss(symbol, positionSide, orderSide, slPriceNew);
			}
			else
			{
				BigDecimal slPriceCur = slOrder.getQuantity();
				boolean isFix = stopLossFix(symbol, positionSide, slPriceCur, slPriceNew);
				if (isFix)
				{
					info(symbol.getNameLeft() + " SL-REARRANGEMENT");
					info(String.format("(%f %s) : [price: %f] --> [price: %f]", slUsd, CoreConfig.getDefaultSymbolRight(), slPriceCur, slPriceNew));
					BeepUtils.beep2();

					// CANCEL SL-ORDER
					OrderHelper.cancelOrder(slOrder.getSymbol(), slOrder.getOrderId());

					// ADD NEW SL-ORDER
					OrderHelper.postStopLoss(symbol, positionSide, orderSide, slPriceNew);
				}
			}
		}
	}

	private static boolean stopLossFix(Symbol symbol, PositionSide positionSide, BigDecimal slPriceCur, BigDecimal slPriceNew)
	{
		BigDecimal lastPrice = SymbolInfoService.getLastPrice(symbol);

		if (positionSide == PositionSide.SHORT)
		{
			return slPriceCur.doubleValue() > slPriceNew.doubleValue() && lastPrice.doubleValue() < slPriceNew.doubleValue();
		}
		else if (positionSide == PositionSide.LONG)
		{
			return slPriceCur.doubleValue() < slPriceNew.doubleValue() && lastPrice.doubleValue() > slPriceNew.doubleValue();
		}

		return false;
	}

	// --------------------------------------------------------------------
	// LOG
	// --------------------------------------------------------------------

	private static final long LOG_MAXSIZE = 10000;

	private static LinkedList<String> logLines = new LinkedList<String>();

	public static String getLOG()
	{
		return StringUtils.join(logLines, "\n");
	}

	public static void cleanLOG()
	{
		logLines = new LinkedList<String>();
	}

	public static void log(String type, String msg)
	{
		String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String text = String.format("%-19s : %s : %s", datetime, type, msg);

		logLines.add(text);
		if (logLines.size() > LOG_MAXSIZE)
		{
			logLines.removeFirst();
		}

		notifyAllObservers();
	}

	public static void info(String msg)
	{
		log("INFO", msg);
	}

	public static void warn(String msg)
	{
		log("WARN", msg);
	}

	public static void error(String msg)
	{
		log("ERROR", msg);
	}

	// --------------------------------------------------------------------

	private static List<Handler<Void>> observers = new ArrayList<Handler<Void>>();

	public static void attachObserver(Handler<Void> observer)
	{
		observers.add(observer);
	}

	public static void deattachObserver(Handler<Void> observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllObservers()
	{
		for (Handler<Void> observer : observers)
		{
			observer.handle(null);
		}
	}
	
}
