package sanzol.app.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.decimal4j.util.DoubleRounder;

import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.trade.AccountBalance;

import sanzol.app.config.CharConstants;
import sanzol.app.model.PositionOrder.State;
import sanzol.app.model.PositionOrder.Type;
import sanzol.app.task.BalanceService;
import sanzol.app.task.PriceService;

public class Position
{
	private Symbol coin;
	private PositionSide side;
	private Double inPrice = null;
	private Double inCoins = null;
	private int iterations;
	private double priceIncr;
	private double distBeforeSL;
	private double coinsIncr;
	private double takeProfit;
	private List<PositionOrder> lstOrders;
	private double sumUsd;


	public Position()
	{
		//
	}

	public Position(Symbol coin, PositionSide side)
	{
		this.coin = coin;
		this.side = side;
	}

	public Position(Symbol coin, PositionSide side, int iterations, double priceIncr, double distBeforeSL, double coinsIncr, double takeProfit)
	{
		this.coin = coin;
		this.side = side;
		this.iterations = iterations;
		this.priceIncr = priceIncr;
		this.distBeforeSL = distBeforeSL;
		this.coinsIncr = coinsIncr;
		this.takeProfit = takeProfit;
	}

	public static Position getInstance(Symbol coin, PositionSide side)
	{
		return new Position(coin, side);
	}

	public static Position getInstance(Symbol coin, PositionSide side, int iterations, double priceIncr, double distBeforeSL, double coinsIncr, double takeProfit)
	{
		return new Position(coin, side, iterations, priceIncr, distBeforeSL, coinsIncr, takeProfit);
	}

	// ------------------------------------------------------------------------

	public Position withCoinAuto(double balancePercent) throws Exception
	{
		if (inPrice == null)
		{
			throw new IllegalArgumentException("inPrice is null"); 
		}

		AccountBalance accBalance = BalanceService.getAccountBalance();
		double balance = accBalance.getBalance().doubleValue();
		this.inCoins = DoubleRounder.round(Math.max(5, balance * balancePercent) / inPrice, coin.getQuantityPrecision(), RoundingMode.CEILING);
		return this;
	}

	public Position withPriceAuto() throws Exception
	{
		BigDecimal lastPrice = PriceService.getLastPrice(coin);
		if (side == PositionSide.SHORT)
		{
			this.inPrice = coin.addFewTicks(lastPrice.doubleValue(), 5);
		}
		else
		{
			this.inPrice = coin.subFewTicks(lastPrice.doubleValue(), 5);
		}
		return this;
	}

	// ------------------------------------------------------------------------

	public String getSymbol()
	{
		return coin.getName();
	}

	public String getSymbolLeft()
	{
		return coin.getNameLeft();
	}

	public boolean isShort()
	{
		return side == PositionSide.SHORT;
	}

	public boolean isLong()
	{
		return side == PositionSide.LONG;
	}

	public String getInPriceStr()
	{
		return coin.priceToStr(inPrice);
	}

	public String getInCoinsStr()
	{
		return coin.coinsToStr(inCoins);
	}

	// ------------------------------------------------------------------------

	public Symbol getCoin()
	{
		return coin;
	}

	public void setCoin(Symbol coin)
	{
		this.coin = coin;
	}

	public PositionSide getSide()
	{
		return side;
	}

	public void setSide(PositionSide side)
	{
		this.side = side;
	}

	public double getInPrice()
	{
		return inPrice;
	}

	public void setInPrice(double inPrice)
	{
		this.inPrice = inPrice;
	}

	public double getInCoins()
	{
		return inCoins;
	}

	public void setInCoins(double inCoins)
	{
		this.inCoins = inCoins;
	}

	public int getIterations()
	{
		return iterations;
	}

	public void setIterations(int iterations)
	{
		this.iterations = iterations;
	}

	public double getPriceIncr()
	{
		return priceIncr;
	}

	public void setPriceIncr(double priceIncr)
	{
		this.priceIncr = priceIncr;
	}

	public double getDistBeforeSL()
	{
		return distBeforeSL;
	}

	public void setDistBeforeSL(double distBeforeSL)
	{
		this.distBeforeSL = distBeforeSL;
	}

	public double getCoinsIncr()
	{
		return coinsIncr;
	}

	public void setCoinsIncr(double coinsIncr)
	{
		this.coinsIncr = coinsIncr;
	}

	public List<PositionOrder> getLstOrders()
	{
		return lstOrders;
	}

	public void setLstOrders(List<PositionOrder> lstOrders)
	{
		this.lstOrders = lstOrders;
	}

	public double getTakeProfit()
	{
		return takeProfit;
	}

	public void setTakeProfit(double takeProfit)
	{
		this.takeProfit = takeProfit;
	}

	public double getSumUsd()
	{
		return sumUsd;
	}

	public void setSumUsd(double sumUsd)
	{
		this.sumUsd = sumUsd;
	}

	// ------------------------------------------------------------------------

	private static String usdToStr(double usd)
	{
		String pattern = "#0.00";
		return new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(usd);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append(String.format("%s %s - in Price: %s - in Coins: %s", side.name(), coin.getNameLeft(), getInPriceStr(), getInCoinsStr()));
		sb.append("\n\n");

		String labels = String.format("%-3s %8s %8s %12s %12s %12s %12s %12s %12s | %12s %10s %12s %12s", "#", "TYPE", CharConstants.ARROW_UP + " %", "PRICE", "COINS", "USD", CharConstants.SIGNA + " COINS", CharConstants.SIGNA + " USD", "USD LOST", "AVG-PRICE", CharConstants.ARROW_UP_DOWN + " %", "TP-PRICE", "PROFIT");

		sb.append(labels);
		sb.append("\n");
		sb.append(StringUtils.repeat("-",151));
		sb.append("\n");

		for (PositionOrder entry : lstOrders)
		{
			if (entry.getType() == Type.SL_SELL || entry.getType() == Type.SL_BUY)
			{
				sb.append(StringUtils.repeat("-",151));
				sb.append("\n");
			}

			String line = String.format("%-3s %8s %8.2f %12s %12s %12s %12s %12s %12s | %12s %10.2f %12s %12s", 
					entry.getNumber(), 
					entry.getType().name(), 
					entry.getDistance() * 100, 
					coin.priceToStr(entry.getPrice()),
					coin.coinsToStr(entry.getCoins()), 
					usdToStr(entry.getUsd()), 
					coin.coinsToStr(entry.getSumCoins()), 
					usdToStr(entry.getSumUsd()), 
					usdToStr(entry.getLost()), 
					coin.priceToStr(entry.getNewPrice()), 
					entry.getRecoveryNeeded() * 100, 
					coin.priceToStr(entry.getTakeProfit()), 
					usdToStr(entry.getProfit()));

			sb.append(line);
			sb.append("\n");

			if (entry.getState() != State.CREATED)
			{
				sb.append(entry.getState().name());
				if (entry.getResult() != null)
				{
					sb.append(" / " + entry.getResult());
				}
				sb.append("\n\n");
			}

		}

		return sb.toString();
	}

}
