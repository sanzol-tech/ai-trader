package sanzol.aitrader.be.enums;

public enum GridStrategy
{
	DEFAULT("Default", null, null, null, null, null, null, null, null, null, null, null),
	SIGNAL("Signal", QuantityType.BALANCE, 0.3, 0, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.02, 1.0, null, null),
	SIMPLE("Simple", QuantityType.BALANCE, 0.3, 0, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.02, 1.0, 0.01, 0.02), 
	BTC("Btc", QuantityType.BALANCE, 0.05, 5, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.02, 1.0, 0.005, 0.02),
	ALTCOIN("Altcoin", QuantityType.BALANCE, 0.05, 6, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.025, 0.5, 0.005, 0.02), 
	CLASSIC6("Classic 6", QuantityType.BALANCE, 0.05, 6, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.ORDER, 0.02, 0.4, 0.005, 0.02),
	CLASSIC8("Classic 8", QuantityType.BALANCE, 0.05, 8, null, null, PriceIncrType.GEOMETRIC, QtyIncrType.ORDER, 0.02, 0.4, 0.005, 0.02),
	INCREMENTAL1("Incremental 1", QuantityType.BALANCE, 0.05, 6, 0.025, 1.1, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.025, 0.4, 0.01, 0.02),
	INCREMENTAL2("Incremental 2", QuantityType.BALANCE, 0.05, 7, 0.025, 1.1, PriceIncrType.GEOMETRIC, QtyIncrType.POSITION, 0.025, 0.5, 0.01, 0.02);

	private final String name;
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
	
	private GridStrategy(String name, QuantityType quantityType, Double inQty, Integer iterations,
						 Double pipBase, Double pipCoef, PriceIncrType priceIncrType, QtyIncrType qtyIncrType,
						 Double priceIncr, Double qtyIncr, Double stopLoss, Double takeProfit)
	{
		this.name = name;
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

	public String getName()
	{
		return name;
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
	
	public String toString()
	{
		return name;
	}

}
