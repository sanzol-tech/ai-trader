package sanzol.aitrader.be.enums;

import api.client.impl.config.ApiConfig;

public enum GridStrategy
{
	CUSTOM("Custom", null, null, null, null, null, null, null, null, null, null, null),
	SIGNAL("Signal", QuantityType.USD, ApiConfig.MIN_USD_AMOUNT, 0, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.02, 1.0, null, null),
	SIMPLE("Simple", QuantityType.USD, ApiConfig.MIN_USD_AMOUNT * 4, 0, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.02, 1.0, 0.01, 0.02),
	BTC("Btc", QuantityType.BALANCE, 10.0, 4, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.02, 1.0, 0.005, 0.02),
	ALTCOIN("Altcoin", QuantityType.USD, ApiConfig.MIN_USD_AMOUNT, 4, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.04, 2.0, 0.005, 0.02),
	CLASSIC6("Classic 6", QuantityType.USD, ApiConfig.MIN_USD_AMOUNT, 6, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.ORDER, 0.02, 1.4, 0.005, 0.02),
	CLASSIC8("Classic 8", QuantityType.USD, ApiConfig.MIN_USD_AMOUNT, 8, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.ORDER, 0.02, 1.4, 0.005, 0.02),
	INCREMENTAL1("Incremental 1", QuantityType.USD, ApiConfig.MIN_USD_AMOUNT, 6, 0.025, 1.1, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.025, 0.4, 0.01, 0.02),
	INCREMENTAL2("Incremental 2", QuantityType.USD, ApiConfig.MIN_USD_AMOUNT, 7, 0.025, 1.1, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.025, 0.5, 0.01, 0.02);

	private final String strategyName;
	private final QuantityType quantityType;
	private final Double inQty;
	private final Integer iterations;
	private final Double pipBase;
	private final Double pipCoef;
	private final PriceIncrType priceIncrType;
	private final QtyIncrType qtyIncrType;
	private final Double priceIncr;
	private final Double qtyIncr;
	private final Double stopLoss;
	private final Double takeProfit;

	private GridStrategy(String strategyName, QuantityType quantityType, Double inQty, Integer iterations,
						 Double pipBase, Double pipCoef, PriceIncrType priceIncrType, QtyIncrType qtyIncrType,
						 Double priceIncr, Double qtyIncr, Double stopLoss, Double takeProfit)
	{
		this.strategyName = strategyName;
		this.quantityType = quantityType;
		this.inQty = inQty;
		this.iterations = iterations;
		this.pipBase = pipBase;
		this.pipCoef = pipCoef;
		this.priceIncrType = priceIncrType;
		this.qtyIncrType = qtyIncrType;
		this.priceIncr = priceIncr;
		this.qtyIncr = qtyIncr;
		this.stopLoss = stopLoss;
		this.takeProfit = takeProfit;
	}

	public String getStrategyName()
	{
		return strategyName;
	}

	public QuantityType getQuantityType()
	{
		return quantityType;
	}

	public Double getInQty()
	{
		return inQty;
	}

	public Integer getIterations()
	{
		return iterations;
	}

	public Double getPipBase()
	{
		return pipBase;
	}

	public Double getPipCoef()
	{
		return pipCoef;
	}

	public PriceIncrType getPriceIncrType()
	{
		return priceIncrType;
	}

	public QtyIncrType getQtyIncrType()
	{
		return qtyIncrType;
	}

	public Double getPriceIncr()
	{
		return priceIncr;
	}

	public Double getQtyIncr()
	{
		return qtyIncr;
	}

	public Double getStopLoss()
	{
		return stopLoss;
	}

	public Double getTakeProfit()
	{
		return takeProfit;
	}

	public static GridStrategy fromName(String name)
	{
		for (GridStrategy e : GridStrategy.values())
		{
			if (e.name().equals(name))
				return e;
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		return strategyName;
	}

}
