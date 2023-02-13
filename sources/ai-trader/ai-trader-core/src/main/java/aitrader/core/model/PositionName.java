package aitrader.core.model;

import java.util.Objects;

public class PositionName
{
	private final String symbolPair;
	private final String side;
	private int hashCode;

	public PositionName(String symbolPair, String side)
	{
		this.symbolPair = symbolPair;
		this.side = side;
		this.hashCode = Objects.hash(symbolPair, side);
	}

	public static PositionName from(String symbolPair, String side)
	{
		return new PositionName(symbolPair, side);
	}

	public static PositionName from(Position position)
	{
		return new PositionName(position.getSymbol().getPair(), position.getPositionSide().name());
	}

	public String getSymbolPair()
	{
		return symbolPair;
	}

	public String getSide()
	{
		return side;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		PositionName that = (PositionName) o;

		return symbolPair.equals(that.symbolPair) && side.equals(that.side);
	}

	@Override
	public int hashCode()
	{
		return this.hashCode;
	}

}
