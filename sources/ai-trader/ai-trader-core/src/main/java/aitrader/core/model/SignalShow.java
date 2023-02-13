package aitrader.core.model;

import java.math.BigDecimal;

public class SignalShow
{
	private Signal signal;
	private SymbolInfo symbolInfo;
	private BigDecimal distance;

	public SignalShow()
	{
		//
	}

	public SignalShow(Signal signal, SymbolInfo symbolInfo, BigDecimal distance)
	{
		this.signal = signal;
		this.symbolInfo = symbolInfo;
		this.distance = distance;
	}

	public Signal getSignal()
	{
		return signal;
	}

	public void setSignal(Signal signal)
	{
		this.signal = signal;
	}

	public SymbolInfo getSymbolInfo()
	{
		return symbolInfo;
	}

	public void setSymbolInfo(SymbolInfo symbolInfo)
	{
		this.symbolInfo = symbolInfo;
	}

	public BigDecimal getDistance()
	{
		return distance;
	}

	public void setDistance(BigDecimal distance)
	{
		this.distance = distance;
	}

}
