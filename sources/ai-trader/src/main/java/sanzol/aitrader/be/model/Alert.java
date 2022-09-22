package sanzol.aitrader.be.model;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import sanzol.aitrader.be.service.AlertState;

public class Alert
{
	private Symbol symbol;
	private BigDecimal shortAlert;
	private BigDecimal shortLimit;
	private BigDecimal longAlert;
	private BigDecimal longLimit;
	private AlertState alertState;
	private Long timeOut;

	public Alert()
	{
		//
	}

	public Alert(Symbol symbol, BigDecimal shortAlert, BigDecimal shortLimit, BigDecimal longAlert, BigDecimal longLimit)
	{
		this.symbol = symbol;
		this.shortAlert = shortAlert;
		this.shortLimit = shortLimit;
		this.longAlert = longAlert;
		this.longLimit = longLimit;
		this.alertState = AlertState.NONE;
		this.timeOut = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(8);
	}

	public Alert(Symbol symbol, BigDecimal shortAlert, BigDecimal shortLimit, BigDecimal longAlert, BigDecimal longLimit, AlertState alertState, Long timeOut)
	{
		this.symbol = symbol;
		this.shortAlert = shortAlert;
		this.shortLimit = shortLimit;
		this.longAlert = longAlert;
		this.longLimit = longLimit;
		this.alertState = alertState;
		this.timeOut = timeOut;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getShortAlert()
	{
		return shortAlert;
	}

	public void setShortAlert(BigDecimal shortAlert)
	{
		this.shortAlert = shortAlert;
	}

	public BigDecimal getShortLimit()
	{
		return shortLimit;
	}

	public void setShortLimit(BigDecimal shortLimit)
	{
		this.shortLimit = shortLimit;
	}

	public BigDecimal getLongAlert()
	{
		return longAlert;
	}

	public void setLongAlert(BigDecimal longAlert)
	{
		this.longAlert = longAlert;
	}

	public BigDecimal getLongLimit()
	{
		return longLimit;
	}

	public void setLongLimit(BigDecimal longLimit)
	{
		this.longLimit = longLimit;
	}

	public AlertState getAlertState()
	{
		return alertState;
	}

	public void setAlertState(AlertState alertState)
	{
		this.alertState = alertState;
	}

	public Long getTimeOut()
	{
		return timeOut;
	}

	public void setTimeOut(Long timeOut)
	{
		this.timeOut = timeOut;
	}

	@Override
	public String toString()
	{
		return "Alert [symbol=" + symbol + ", shortAlert=" + shortAlert + ", shortLimit=" + shortLimit + ", longAlert=" + longAlert + ", longLimit=" + longLimit + ", alertState=" + alertState + ", timeOut=" + timeOut + "]";
	}

}
