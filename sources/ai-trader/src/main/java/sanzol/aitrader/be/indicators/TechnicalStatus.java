package sanzol.aitrader.be.indicators;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import api.client.futures.impl.SyncFuturesClient;
import api.client.futures.model.enums.IntervalType;
import api.client.model.Kline;
import sanzol.aitrader.be.model.Symbol;
import technicals.indicators.oscillator.RelativeStrengthIndex;
import technicals.indicators.oscillator.Stochastic;
import technicals.indicators.oscillator.StochasticRSI;
import technicals.indicators.oscillator.UltimateOscillator;
import technicals.indicators.oscillator.WilliamsR;
import technicals.model.Candle;
import technicals.model.indicators.IndicatorEntry;
import technicals.model.oscillator.RsiEntry;
import technicals.model.oscillator.StochRsiEntry;
import technicals.model.oscillator.StochasticEntry;
import technicals.model.oscillator.WilliamsREntry;
import technicals.util.CandleUtils;

public class TechnicalStatus
{
	private Symbol symbol;
	private IntervalType intervalType;
	private int periods;
	private Candle[] candles;
	private Candle candleMerged;
	private double rsi;
	private double stoch;
	private double stochRsi;
	private double williamsR;
	private double uo;

	public TechnicalStatus(Symbol symbol, IntervalType intervalType, int periods)
	{
		this.periods = periods;
		this.symbol = symbol;
		this.intervalType = intervalType;
	}

	public Symbol getSymbol()
	{
		return symbol;
	}

	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}

	public IntervalType getIntervalType()
	{
		return intervalType;
	}

	public void setIntervalType(IntervalType intervalType)
	{
		this.intervalType = intervalType;
	}

	public Candle[] getCandles()
	{
		return candles;
	}

	public void setCandles(Candle[] candles)
	{
		this.candles = candles;
	}

	public Candle getCandleMerged()
	{
		return candleMerged;
	}

	public double getRsi()
	{
		return rsi;
	}

	public double getStoch()
	{
		return stoch;
	}

	public double getStochRsi()
	{
		return stochRsi;
	}

	public double getWilliamsR()
	{
		return williamsR;
	}

	public double getUo()
	{
		return uo;
	}

	public static TechnicalStatus create(Symbol symbol, IntervalType intervalType, int periods)
	{
		return new TechnicalStatus(symbol, intervalType, periods);
	}

	public TechnicalStatus readCandles() throws KeyManagementException, NoSuchAlgorithmException
	{
		List<Kline> lstExchangeCandles = SyncFuturesClient.getKlines(symbol.getPair(), intervalType, 100);
		candles = toCandleArray(lstExchangeCandles);
		return this;
	}

	public TechnicalStatus calculate()
	{
		candleMerged = CandleUtils.mergeCandles(candles, candles.length - periods, candles.length - 1);

		RsiEntry[] rsiEntries = RelativeStrengthIndex.calculate(candles, periods);
		rsi = rsiEntries[rsiEntries.length - 1].getRsi();

		StochasticEntry[] stochEntries = Stochastic.calculate(candles, periods, 1, 3);
		stoch = stochEntries[stochEntries.length - 1].getD();

		StochRsiEntry[] stochRsiEntries = StochasticRSI.calculate(candles, periods, periods, 3, 3);
		stochRsi = stochRsiEntries[stochRsiEntries.length - 1].getD();

		WilliamsREntry[] williamsREntries = WilliamsR.calculate(candles, periods);
		williamsR = williamsREntries[williamsREntries.length - 1].getR();

		IndicatorEntry[] uoEntries = UltimateOscillator.calculate(candles, 7, 14, 28);
		uo = uoEntries[uoEntries.length - 1].getValue();

		return this;
	}

	public static Candle[] toCandleArray(List<Kline> lstCandles)
	{
		Candle[] candle = new Candle[lstCandles.size()];
		for (int i = 0; i < lstCandles.size(); i++)
		{
			candle[i] = new Candle();
			candle[i].setOpenTime(lstCandles.get(i).getOpenTimeZoned());
			candle[i].setOpenPrice(lstCandles.get(i).getOpenPrice().doubleValue());
			candle[i].setClosePrice(lstCandles.get(i).getClosePrice().doubleValue());
			candle[i].setHighPrice(lstCandles.get(i).getHighPrice().doubleValue());
			candle[i].setLowPrice(lstCandles.get(i).getLowPrice().doubleValue());
			candle[i].setVolume(lstCandles.get(i).getVolume().doubleValue());
			candle[i].setQuoteVolume(lstCandles.get(i).getQuoteVolume().doubleValue());
			candle[i].setCount(lstCandles.get(i).getCount());
		}
		return candle;
	}

}
