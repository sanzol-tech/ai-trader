package sanzol.app.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.binance.client.model.trade.Order;
import com.binance.client.model.trade.PositionRisk;

import sanzol.app.config.Constants;
import sanzol.app.listener.BotListener;
import sanzol.app.service.SimpleTrader;
import sanzol.app.service.Symbol;
import sanzol.lib.util.BeepUtils;

public final class BotService
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

	public static void setTpPercent(BigDecimal tpPercent)
	{
		BotService.tpPercent = tpPercent;
	}

	public static boolean isSlRearrangement()
	{
		return isSlRearrangement;
	}

	public static BigDecimal getSlUsd()
	{
		return slUsd;
	}

	public static void setSlUsd(BigDecimal slUsd)
	{
		BotService.slUsd = slUsd;
	}

	public static void setTpRearrangement(boolean isTpRearrangement)
	{
		info("TP Rearrangement " + String.valueOf(isTpRearrangement));
		BotService.isTpRearrangement = isTpRearrangement;
	}

	public static void setSlRearrangement(boolean isSlRearrangement)
	{
		info("SL Rearrangement " + String.valueOf(isSlRearrangement));
		BotService.isSlRearrangement = isSlRearrangement;
	}

	// ------------------------------------------------------------------------
	// TP & SL
	// ------------------------------------------------------------------------

	public synchronized static void onPositionUpdate()
	{
		List<PositionRisk> lstPositionRisk = PositionService.getLstPositionRisk();

		if (lstPositionRisk != null && !lstPositionRisk.isEmpty())
		{
			for (PositionRisk entry : lstPositionRisk)
			{
				if (entry.getPositionAmt().compareTo(BigDecimal.ZERO) != 0)
				{
					Symbol symbol = Symbol.getInstance(entry.getSymbol());
					if (symbol != null)
					{
						String side = (entry.getPositionAmt().doubleValue() < 0 ? "SHORT" : "LONG");
						BigDecimal posPrice = entry.getEntryPrice();
						BigDecimal posQty = entry.getPositionAmt().abs();

						// ---------------------------------------------
						tpRearrangement(symbol, side, posQty, posPrice);
						slRearrangement(symbol, side, posQty, posPrice);
					}
				}
			}
		}
	}

	private static void tpRearrangement(Symbol symbol, String side, BigDecimal posQty, BigDecimal price)
	{
		if (isTpRearrangement)
		{
			BigDecimal tpCoef = "SHORT".equals(side) ? BigDecimal.ONE.subtract(tpPercent) : BigDecimal.ONE.add(tpPercent);
			BigDecimal newPrice = price.multiply(tpCoef).setScale(symbol.getTickSize(), RoundingMode.HALF_UP);

			Order tpOrder = PositionService.getTpOrder(symbol.getName(), side);
			if (tpOrder != null)
			{
				BigDecimal tpQty = tpOrder.getOrigQty();
				BigDecimal tpPrice = tpOrder.getPrice();

				if (posQty.compareTo(tpQty) != 0)
				{
					info(symbol.getNameLeft() + " TP-REARRANGEMENT");
					info(String.format("[qty: %f price: %f] --> [qty: %f price: %f]", tpQty, tpPrice, posQty, newPrice));
					BeepUtils.beep2();
		
					// CANCEL ORDER
					SimpleTrader.cancelOrder(tpOrder);
		
					// ADD NEW ORDER
					SimpleTrader.postTprofit(symbol, side, newPrice, posQty);
				}
			}
			else
			{
				info(symbol.getNameLeft() + " TP-REARRANGEMENT");
				info(String.format("NONE -> [qty: %f price: %f]", posQty, newPrice));
				BeepUtils.beep2();

				// ADD NEW ORDER
				SimpleTrader.postTprofit(symbol, side, newPrice, posQty);
			}
		}
	}

	private static void slRearrangement(Symbol symbol, String side, BigDecimal posQty, BigDecimal posPrice)
	{
		if (isSlRearrangement)
		{
			BigDecimal slPriceNew;
			if ("SHORT".equals(side)) {
				slPriceNew = slUsd.add(posPrice.multiply(posQty)).divide(posQty, symbol.getTickSize(), RoundingMode.HALF_UP);
			} else {
				slPriceNew = ((posPrice.multiply(posQty)).subtract(slUsd)).divide(posQty, symbol.getTickSize(), RoundingMode.HALF_UP);
			}

			Order slOrder = PositionService.getSlOrder(symbol.getName(), side);
			if (slOrder == null)
			{
				info(symbol.getNameLeft() + " SL-REARRANGEMENT");
				info(String.format("(%f %s) : NONE -> [price: %f]", slUsd, Constants.DEFAULT_SYMBOL_RIGHT, slPriceNew));
				BeepUtils.beep2();

				// ADD NEW SL-ORDER
				SimpleTrader.postSMarket(symbol, side, slPriceNew);
			}
			else
			{
				BigDecimal slPriceCur = slOrder.getStopPrice();
				boolean isFix = stopLossFix(symbol, side, slPriceCur, slPriceNew);
				if (isFix)
				{
					info(symbol.getNameLeft() + " SL-REARRANGEMENT");
					info(String.format("(%f %s) : [price: %f] --> [price: %f]", slUsd, Constants.DEFAULT_SYMBOL_RIGHT, slPriceCur, slPriceNew));
					BeepUtils.beep2();
	
					// CANCEL SL-ORDER
					SimpleTrader.cancelOrder(slOrder);
	
					// ADD NEW SL-ORDER
					SimpleTrader.postSMarket(symbol, side, slPriceNew);
				}
			}
		}
	}

	private static boolean stopLossFix(Symbol symbol, String side, BigDecimal slPriceCur, BigDecimal slPriceNew)
	{
		BigDecimal lastPrice = PriceService.getLastPrice(symbol);

		if ("LONG".equals(side))
		{
			return slPriceCur.doubleValue() < slPriceNew.doubleValue() && lastPrice.doubleValue() > slPriceNew.doubleValue();
		}
		else if ("SHORT".equals(side))
		{
			return slPriceCur.doubleValue() > slPriceNew.doubleValue() && lastPrice.doubleValue() < slPriceNew.doubleValue();
		}

		return false;
	}

	// ------------------------------------------------------------------------
	// LOG
	// ------------------------------------------------------------------------	

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

		notifyAllLogObservers();
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

	// ------------------------------------------------------------------------	

	private static List<BotListener> observers = new ArrayList<BotListener>();

	public static void attachRefreshObserver(BotListener observer)
	{
		observers.add(observer);
	}

	public static void deattachRefreshObserver(BotListener observer)
	{
		observers.remove(observer);
	}

	public static void notifyAllLogObservers()
	{
		for (BotListener observer : observers)
		{
			observer.onBotUpdate();
		}
	}

}
