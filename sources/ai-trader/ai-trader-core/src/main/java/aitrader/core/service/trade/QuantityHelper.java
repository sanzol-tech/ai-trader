package aitrader.core.service.trade;

import java.math.BigDecimal;
import java.math.RoundingMode;

import aitrader.core.model.Symbol;
import aitrader.core.model.enums.QuantityType;
import aitrader.core.service.position.BalanceService;
import binance.futures.model.AccountBalance;

public class QuantityHelper
{
	private static final double MIN_USD_AMOUNT = 5.0;

	private Symbol symbol;
	private BigDecimal price;
	private BigDecimal qty;

	private QuantityHelper(Symbol symbol, BigDecimal price)
	{
		this.symbol = symbol;
		this.price = price;
	}

	public static QuantityHelper create(Symbol symbol, BigDecimal price)
	{
		return new QuantityHelper(symbol, price);
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public QuantityHelper from(QuantityType quantityType, BigDecimal value)
	{
		if (quantityType == QuantityType.BALANCE)
			fromBalance(value);

		if (quantityType == QuantityType.USD)
			fromUSD(value);

		if (quantityType == QuantityType.COIN)
			fromQty(value);
		
		return this;
	}

	public void fromBalance(BigDecimal percent)
	{
		AccountBalance accBalance = BalanceService.getAccountBalance();
		BigDecimal balance = accBalance.getBalance();
		BigDecimal value = (balance.multiply(percent)).divide(price, symbol.getPricePrecision(), RoundingMode.HALF_UP);
		qty = fixMinQty(value);
	}

	public void fromUSD(BigDecimal usd)
	{
		qty = fixMinQty(usd.divide(price, symbol.getPricePrecision(), RoundingMode.HALF_UP));
	}

	public void fromQty(BigDecimal value)
	{
		qty = fixMinQty(value);
	}

	private BigDecimal fixMinQty(BigDecimal value)
	{
		value = value.setScale(symbol.getPricePrecision(), RoundingMode.HALF_UP);

		value = value.max(symbol.getMinQty());

		if (value.multiply(price).doubleValue() >= MIN_USD_AMOUNT)
		{
			return value;
		}

		while (value.multiply(price).doubleValue() < MIN_USD_AMOUNT)
		{
			value = value.add(symbol.getMinQty());
		}

		return value;
	}

}
