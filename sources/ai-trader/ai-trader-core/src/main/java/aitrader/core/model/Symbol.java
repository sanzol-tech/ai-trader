package aitrader.core.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.decimal4j.util.DoubleRounder;

public class Symbol
{
	private String pair;
	private String nameLeft;
	private String nameRight;

	private BigDecimal tickSize;
	private int pricePrecision;
	private String pricePattern;

	private BigDecimal minQty;
	private int qtyPrecision;
	private String qtyPattern;

	public Symbol()
	{
		//
	}

	public Symbol(String pair, String nameLeft, String nameRight, BigDecimal tickSize, int pricePrecision, String pricePattern, BigDecimal minQty, int qtyPrecision, String qtyPattern)
	{
		this.pair = pair;
		this.nameLeft = nameLeft;
		this.nameRight = nameRight;
		this.tickSize = tickSize;
		this.pricePrecision = pricePrecision;
		this.pricePattern = pricePattern;
		this.minQty = minQty;
		this.qtyPrecision = qtyPrecision;
		this.qtyPattern = qtyPattern;
	}

	public String getPair()
	{
		return pair;
	}

	public String getNameLeft()
	{
		return nameLeft;
	}

	public String getNameRight()
	{
		return nameRight;
	}

	public BigDecimal getTickSize()
	{
		return tickSize;
	}

	public int getPricePrecision()
	{
		return pricePrecision;
	}

	public String getPricePattern()
	{
		return pricePattern;
	}

	public BigDecimal getMinQty()
	{
		return minQty;
	}

	public int getQtyPrecision()
	{
		return qtyPrecision;
	}

	public String getQtyPattern()
	{
		return qtyPattern;
	}

	// ---- CALCULATED FIELDS ---------------------------------------------

	public String priceToStr(BigDecimal price)
	{
		return new DecimalFormat(pricePattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(price.doubleValue());
	}

	public String priceToStr(double price)
	{
		return new DecimalFormat(pricePattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(price);
	}

	public String qtyToStr(BigDecimal qty)
	{
		return new DecimalFormat(qtyPattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(qty.doubleValue());
	}

	public String qtyToStr(double qty)
	{
		return new DecimalFormat(qtyPattern, new DecimalFormatSymbols(Locale.ENGLISH)).format(qty);
	}

	public double roundPrice(double price)
	{
		return DoubleRounder.round(price, pricePrecision);
	}

	public double roundQty(double qty)
	{
		return DoubleRounder.round(qty, qtyPrecision);
	}

	public BigDecimal addTicksRound(BigDecimal price, int ticks)
	{
		return price.setScale(pricePrecision, RoundingMode.HALF_UP).add(tickSize.multiply(BigDecimal.valueOf(ticks)));
	}

	public BigDecimal addTicks(BigDecimal price, int ticks)
	{
		return price.add(tickSize.multiply(BigDecimal.valueOf(ticks)));
	}

	public double addTicks(double price, int ticks)
	{
		return price + Math.pow(10, -pricePrecision) * ticks;
	}

	public BigDecimal subTicksRound(BigDecimal price, int ticks)
	{
		return price.setScale(pricePrecision, RoundingMode.HALF_UP).subtract(tickSize.multiply(BigDecimal.valueOf(ticks)));
	}
	
	public BigDecimal subTicks(BigDecimal price, int ticks)
	{
		return price.subtract(tickSize.multiply(BigDecimal.valueOf(ticks)));
	}

	public double subTicks(double price, int ticks)
	{
		return price - Math.pow(10, -pricePrecision) * ticks;
	}

	// --------------------------------------------------------------------

	@Override
	public String toString()
	{
		return nameLeft;
	}

}
