package technicals.indicators.complex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import technicals.indicators.ma.ExponentialMovingAverage;
import technicals.indicators.ma.HullMovingAverage;
import technicals.indicators.ma.SimpleMovingAverage;
import technicals.indicators.ma.VWMovingAverage;
import technicals.indicators.misc.Ichimoku;
import technicals.indicators.oscillator.AverageDirectionalIndex;
import technicals.indicators.oscillator.AwesomeOscillator;
import technicals.indicators.oscillator.BullBearPower;
import technicals.indicators.oscillator.CommodityChannelIndex;
import technicals.indicators.oscillator.MACD;
import technicals.indicators.oscillator.Momentum;
import technicals.indicators.oscillator.RelativeStrengthIndex;
import technicals.indicators.oscillator.Stochastic;
import technicals.indicators.oscillator.StochasticRSI;
import technicals.indicators.oscillator.UltimateOscillator;
import technicals.indicators.oscillator.WilliamsR;
import technicals.model.TechCandle;
import technicals.model.indicators.IchimokuEntry;
import technicals.model.indicators.IndicatorEntry;
import technicals.model.oscillator.AdxEntry;
import technicals.model.oscillator.MACDEntry;
import technicals.model.oscillator.RsiEntry;
import technicals.model.oscillator.StochRsiEntry;
import technicals.model.oscillator.StochasticEntry;
import technicals.model.oscillator.WilliamsREntry;

public class TechnicalRatings
{
	public enum RatingStatus
	{
		STRONG_SELL(-2), SELL(-1), NEUTRAL(0), BUY(1), STRONG_BUY(2);

		private int value;

		RatingStatus(int value)
		{
			this.value = value;
		}

		public int getValue()
		{
			return value;
		}
	};

	public static final int UP_TREND = 1;
	public static final int DOWN_TREND = -1;
	public static final int NEUTRAL = 0;

	private int pricePrecision;

	private TechCandle[] candles;

	private double[] sma;
	private int[] smaTrend;
	private double[] ema;
	private int[] emaTrend;
	private double[] vwma;
	private int[] vwmaTrend;
	private double[] hma;
	private int[] hmaTrend;
	private double ichimokuBaseLine;
	private int ichimokuTrend;

	private double maRatingSum = 0;
	private double maRatingCount = 0;
	private RatingStatus maRatingStatus; 
	private int trend;

	private double rsi;
	private int rsiStatus;
	private double stoch;
	private int stochStatus;
	private double cci;
	private int cciStatus;
	private double adx;
	private int adxStatus;
	private double ao;
	private int aoStatus;
	private double mom;
	private int momStatus;
	private double macd;
	private int macdStatus;
	private double stochRsi;
	private int stochRsiStatus;
	private double williamsR;
	private int williamsRStatus;
	private double bbp;
	private int bbpStatus;
	private double uo;
	private int uoStatus;

	private double oscRatingSum = 0;
	private double oscRatingCount = 0;
	private RatingStatus oscRatingStatus; 

	// ---- CONSTRUCTOR ----------------------------------------------------------------

	public TechnicalRatings(int pricePrecision)
	{
		this.pricePrecision = pricePrecision;
	}
	
	// ---- PROPERTIES -----------------------------------------------------------------

	public TechCandle[] getCandles()
	{
		return candles;
	}

	public void setCandles(TechCandle[] candles)
	{
		this.candles = candles;
	}

	public double[] getSma()
	{
		return sma;
	}

	public int[] getSmaTrend()
	{
		return smaTrend;
	}

	public double[] getEma()
	{
		return ema;
	}

	public int[] getEmaTrend()
	{
		return emaTrend;
	}

	public double[] getVwma()
	{
		return vwma;
	}

	public int[] getVwmaTrend()
	{
		return vwmaTrend;
	}

	public double[] getHma()
	{
		return hma;
	}

	public int[] getHmaTrend()
	{
		return hmaTrend;
	}

	public double getIchimokuBaseLine()
	{
		return ichimokuBaseLine;
	}

	public int getIchimokuTrend()
	{
		return ichimokuTrend;
	}

	public RatingStatus getMaRatingStatus()
	{
		return maRatingStatus;
	}

	public int getTrend()
	{
		return trend;
	}

	public double getRsi()
	{
		return rsi;
	}

	public int getRsiStatus()
	{
		return rsiStatus;
	}

	public double getStoch()
	{
		return stoch;
	}

	public int getStochStatus()
	{
		return stochStatus;
	}

	public double getCci()
	{
		return cci;
	}

	public int getCciStatus()
	{
		return cciStatus;
	}

	public double getAdx()
	{
		return adx;
	}

	public int getAdxStatus()
	{
		return adxStatus;
	}

	public double getAo()
	{
		return ao;
	}

	public int getAoStatus()
	{
		return aoStatus;
	}

	public double getMom()
	{
		return mom;
	}

	public int getMomStatus()
	{
		return momStatus;
	}

	public double getMacd()
	{
		return macd;
	}

	public int getMacdStatus()
	{
		return macdStatus;
	}

	public double getStochRsi()
	{
		return stochRsi;
	}

	public int getStochRsiStatus()
	{
		return stochRsiStatus;
	}

	public double getWilliamsR()
	{
		return williamsR;
	}

	public int getWilliamsRStatus()
	{
		return williamsRStatus;
	}

	public double getBbp()
	{
		return bbp;
	}

	public int getBbpStatus()
	{
		return bbpStatus;
	}

	public double getUo()
	{
		return uo;
	}

	public int getUoStatus()
	{
		return uoStatus;
	}

	public RatingStatus getOscRatingStatus()
	{
		return oscRatingStatus;
	}

	// ---- Calc -----------------------------------------------------------------------

	public void calculate(TechCandle[] candles) throws Exception
	{
		this.candles = candles;

		// ---- Trend ------------------------------------------------
		startCalcMA();
		// withSMA(13, 21, 34, 55, 89, 144, 233);
		withSMA(10, 20, 30, 50, 100, 200);
		// withEMA(13, 21, 34, 55, 89, 144, 233);
		withEMA(10, 20, 30, 50, 100, 200);
		withVWMA(20);
		withHMA(20);
		withIchimoku();
		calcRatingMA();

		// ---- Oscillators ------------------------------------------
		startCalcOsc();
		withRSI(14);
		withStochastic(14, 3, 3);
		withCCI(20);
		withADX(14);
		withAO();
		withMomentum(10);
		withMACD(12, 26, 9);
		withStochasticRSI(14, 14, 3, 3);
		withWilliamsR(14);
		withBullBearPower();
		withUO(7, 14, 28);
		calcRatingOsc();

	}

	// ---- Moving Averages ------------------------------------------------------------

	public TechnicalRatings withSMA(int... periods)
	{
		sma = new double[periods.length];
		smaTrend = new int[periods.length];

		double closePrice = last(candles).getClosePrice();

		// SMA
		for (int i = 0; i < periods.length; i++)
		{
			IndicatorEntry[] smaEntries = SimpleMovingAverage.calculate(candles, periods[i]);
			sma[i] = last(smaEntries).getValue();
			smaTrend[i] = calcMAvgTrend(sma[i], closePrice);

			addRatingMA(smaTrend[i]);
		}

		return this;
	}

	public TechnicalRatings withEMA(int... periods)
	{
		ema = new double[periods.length];
		emaTrend = new int[periods.length];
		
		double closePrice = last(candles).getClosePrice();

		// EMA
		for (int i = 0; i < periods.length; i++)
		{
			IndicatorEntry[] emaEntries = ExponentialMovingAverage.calculate(candles, periods[i]);
			ema[i] = last(emaEntries).getValue();
			emaTrend[i] = calcMAvgTrend(ema[i], closePrice);
			
			addRatingMA(emaTrend[i]);
		}

		return this;
	}

	public TechnicalRatings withVWMA(int... periods)
	{
		vwma = new double[periods.length];
		vwmaTrend = new int[periods.length];

		double closePrice = last(candles).getClosePrice();

		// VWMA
		for (int i = 0; i < periods.length; i++)
		{
			IndicatorEntry[] vwmaEntries = VWMovingAverage.calculate(candles, periods[i]);
			vwma[i] = last(vwmaEntries).getValue();
			vwmaTrend[i] = calcMAvgTrend(vwma[i], closePrice);

			addRatingMA(vwmaTrend[i]);
		}

		return this;
	}

	public TechnicalRatings withHMA(int... periods)
	{
		hma = new double[periods.length];
		hmaTrend = new int[periods.length];

		double closePrice = last(candles).getClosePrice();

		// HMA
		for (int i = 0; i < periods.length; i++)
		{
			IndicatorEntry[] vwmaEntries = HullMovingAverage.calculate(candles, periods[i]);
			hma[i] = last(vwmaEntries).getValue();
			hmaTrend[i] = calcMAvgTrend(hma[i], closePrice);

			addRatingMA(hmaTrend[i]);
		}

		return this;
	}

	public TechnicalRatings withIchimoku()
	{
		double closePrice = last(candles).getClosePrice();
		double closePricePrev = prev1(candles).getClosePrice();

		// ICHIMOKU
		IchimokuEntry[] ichimokuEntries = Ichimoku.calculate(candles);
		ichimokuBaseLine = last(ichimokuEntries).getBaseLine();

		if (ichimokuBaseLine > closePrice && 
			last(ichimokuEntries).getConversionLine() < closePrice &&
			last(ichimokuEntries).getConversionLine() > closePricePrev &&
			last(ichimokuEntries).getLeadingSpanA() < closePrice && 
			last(ichimokuEntries).getLeadingSpanA() > last(ichimokuEntries).getLeadingSpanB())
		{
			ichimokuTrend = UP_TREND;
		} else if (ichimokuBaseLine < closePrice && 
				 last(ichimokuEntries).getConversionLine() > closePrice && 
				 last(ichimokuEntries).getConversionLine() < closePricePrev &&
				 last(ichimokuEntries).getLeadingSpanA() > closePrice && 
				 last(ichimokuEntries).getLeadingSpanA() < last(ichimokuEntries).getLeadingSpanB())
		{
			ichimokuTrend = UP_TREND;
		} else {
			ichimokuTrend = NEUTRAL;
		}

		addRatingMA(ichimokuTrend);
		return this;	
	}

	private int calcMAvgTrend(double avgPrice, double closePrice)
	{
		BigDecimal close = BigDecimal.valueOf(closePrice).setScale(pricePrecision - 1, RoundingMode.HALF_UP);
		BigDecimal price = BigDecimal.valueOf(avgPrice).setScale(pricePrecision - 1, RoundingMode.HALF_UP);
		
		return (price.doubleValue() < close.doubleValue()) ? UP_TREND : (price.doubleValue() > close.doubleValue()) ? DOWN_TREND : NEUTRAL;
	}

	public TechnicalRatings startCalcMA()
	{
		maRatingSum = 0;
		maRatingCount = 0;
		
		return this;
	}

	private void addRatingMA(int value)
	{
		maRatingSum += value;
		maRatingCount++;
	}

	public void calcRatingMA()
	{
		double rating = maRatingSum / maRatingCount;  

		// -------------------------------------------
		if (rating < -0.5)
			maRatingStatus = RatingStatus.STRONG_SELL;
		else if (rating > 0.5)
			maRatingStatus = RatingStatus.STRONG_BUY;
		else if (rating < -0.1 && rating >= -0.5)
			maRatingStatus = RatingStatus.SELL;
		else if (rating > 0.1 && rating <= 0.5)
			maRatingStatus = RatingStatus.BUY;
		else
			maRatingStatus = RatingStatus.NEUTRAL;

		// --- Trend ---------------------------------

		// trend = (rating > 0.1) ? UP_TREND : (rating < -0.1) ? DOWN_TREND : NEUTRAL_TREND;
		trend = emaTrend[0];
	}

	// ---- Oscillators ----------------------------------------------------------------

	public TechnicalRatings withRSI(int periods)
	{
		// RSI
		RsiEntry[] rsiEntries = RelativeStrengthIndex.calculate(candles, periods);
		rsi = last(rsiEntries).getRsi();
		if (rsi < 30 && prev1(rsiEntries).getRsi() < rsi)
			rsiStatus = 1;
		else if (rsi > 70 && prev1(rsiEntries).getRsi() > rsi)
			rsiStatus = -1;
		else
			rsiStatus = 0;
		
		addRatingOsc(rsiStatus);
		return this;
	}

	public TechnicalRatings withStochastic(int periods, int smoothK, int smoothD)
	{
		// Stochastic
		StochasticEntry[] stochEntries = Stochastic.calculate(candles, periods, smoothK, smoothD);
		stoch = last(stochEntries).getD();
		if (last(stochEntries).getK() < 20 && last(stochEntries).getD() < 20 && last(stochEntries).getK() > last(stochEntries).getD() && prev1(stochEntries).getK() < prev1(stochEntries).getD())
			stochStatus = 1;
		else if (last(stochEntries).getK() > 80 && last(stochEntries).getD() > 80 && last(stochEntries).getK() < last(stochEntries).getD() && prev1(stochEntries).getK() > prev1(stochEntries).getD())
			stochStatus = -1;
		else
			stochStatus = 0;

		addRatingOsc(stochStatus);
		return this;
	}

	public TechnicalRatings withCCI(int periods)
	{
		// CCI
		IndicatorEntry[] cciEntries = CommodityChannelIndex.calculate(candles, periods);
		cci = last(cciEntries).getValue();
		if (cci < -100 && cci > prev1(cciEntries).getValue())
			cciStatus = 1;
		else if (cci > 100 && cci < prev1(cciEntries).getValue())
			cciStatus = -1;
		else
			cciStatus = 0;

		addRatingOsc(cciStatus);
		return this;
	}

	public TechnicalRatings withADX(int periods)
	{
		// ADX
		AdxEntry[] adxEntries = AverageDirectionalIndex.calculate(candles, periods);
		adx = last(adxEntries).getAdx();
		if (adx > 20 && last(adxEntries).getPosDI() > last(adxEntries).getNegDI() && prev1(adxEntries).getPosDI() < prev1(adxEntries).getNegDI())
			adxStatus = 1;
		else if (adx > 20 && last(adxEntries).getPosDI() < last(adxEntries).getNegDI() && prev1(adxEntries).getPosDI() > prev1(adxEntries).getNegDI())
			adxStatus = -1;
		else
			adxStatus = 0;

		addRatingOsc(adxStatus);
		return this;
	}

	public TechnicalRatings withAO()
	{
		// Awesome Oscillator
		IndicatorEntry[] aoEntries = AwesomeOscillator.calculate(candles);
		ao = last(aoEntries).getValue();
		if (ao > 0 && prev1(aoEntries).getValue() > 0 && ao > prev1(aoEntries).getValue() && prev2(aoEntries).getValue() > prev1(aoEntries).getValue())
			aoStatus = 1;
		else if (ao < 0 && prev1(aoEntries).getValue() < 0 && ao < prev1(aoEntries).getValue() && prev2(aoEntries).getValue() < prev1(aoEntries).getValue())
			aoStatus = -1;
		else
			aoStatus = 0;

		addRatingOsc(aoStatus);
		return this;
	}

	public TechnicalRatings withMomentum(int periods)
	{
		// Momentum
		IndicatorEntry[] momEntries = Momentum.calculate(candles, periods);
		mom = last(momEntries).getValue();
		double momPrev = prev1(momEntries).getValue();
		if (mom > 0 && momPrev > 0 && mom > momPrev )
			momStatus = 1;
		else if (mom < 0 && momPrev < 0 && mom < momPrev )
			momStatus = -1;
		else
			momStatus = 0;

		addRatingOsc(momStatus);
		return this;
	}

	public TechnicalRatings withMACD(int fastPeriods, int slowPeriods, int signalPeriods)
	{
		// MACD
		MACDEntry[] macdEntries = MACD.calculate(candles, fastPeriods, slowPeriods, signalPeriods);
		macd = last(macdEntries).getMacd();
		if (macd > last(macdEntries).getSignal())
			macdStatus = 1;
		else if (macd < last(macdEntries).getSignal())
			macdStatus = -1;
		else
			macdStatus = 0;

		addRatingOsc(macdStatus);
		return this;
	}

	public TechnicalRatings withStochasticRSI(int periodsRsi, int periodsStoch, int smoothK, int smoothD)
	{
		// Stochastic RSI
		StochRsiEntry[] stochRsiEntries = StochasticRSI.calculate(candles, periodsRsi, periodsStoch, smoothK, smoothD);
		stochRsi = last(stochRsiEntries).getD();
		if (trend == DOWN_TREND && last(stochRsiEntries).getK() < 20 && last(stochRsiEntries).getD() < 20 && last(stochRsiEntries).getK() > last(stochRsiEntries).getD() && prev1(stochRsiEntries).getK() < prev1(stochRsiEntries).getD())
			stochRsiStatus = 1;
		else if (trend == UP_TREND && last(stochRsiEntries).getK() > 80 && last(stochRsiEntries).getD() > 80 && last(stochRsiEntries).getK() < last(stochRsiEntries).getD() && prev1(stochRsiEntries).getK() > prev1(stochRsiEntries).getD())
			stochRsiStatus = -1;
		else
			stochRsiStatus = 0;

		addRatingOsc(stochRsiStatus);
		return this;
	}

	public TechnicalRatings withWilliamsR(int periods)
	{
		// Williams R
		WilliamsREntry[] williamsREntries = WilliamsR.calculate(candles);
		williamsR = last(williamsREntries).getR();
		if (williamsR < -80 && williamsR > prev1(williamsREntries).getR())
			williamsRStatus = 1;
		else if (williamsR > -20 && williamsR < prev1(williamsREntries).getR())
			williamsRStatus = -1;
		else
			williamsRStatus = 0;

		addRatingOsc(williamsRStatus);
		return this;
	}

	public TechnicalRatings withBullBearPower()
	{
		// Bull Bear Power
		IndicatorEntry[] bbpEntries = BullBearPower.calculate(candles, 13);
		bbp = last(bbpEntries).getValue();
		if (trend == UP_TREND && bbp < 0 && bbp > prev1(bbpEntries).getValue())
			bbpStatus = 1;
		else if (trend == DOWN_TREND && bbp > 0 && bbp < prev1(bbpEntries).getValue())
			bbpStatus = -1;
		else
			bbpStatus = 0;

		addRatingOsc(bbpStatus);
		return this;
	}
	
	public TechnicalRatings withUO(int periods1, int periods2, int periods3)
	{
		// UO
		IndicatorEntry[] uoEntries = UltimateOscillator.calculate(candles, periods1, periods2, periods3);
		uo = last(uoEntries).getValue();
		if (uo > 70)
			uoStatus = 1;
		else if (uo < 30)
			uoStatus = -1;
		else
			uoStatus = 0;

		addRatingOsc(uoStatus);
		return this;
	}

	public TechnicalRatings startCalcOsc()
	{
		oscRatingSum = 0;
		oscRatingCount = 0;

		return this;
	}
	
	private void addRatingOsc(int value)
	{
		oscRatingSum += value;
		oscRatingCount++;
	}

	public void calcRatingOsc()
	{
		double rating = oscRatingSum / oscRatingCount;  

		if (rating < -0.5)
			oscRatingStatus = RatingStatus.STRONG_SELL;
		else if (rating > 0.5)
			oscRatingStatus = RatingStatus.STRONG_BUY;
		else if (rating < -0.1 && rating >= -0.5)
			oscRatingStatus = RatingStatus.SELL;
		else if (rating > 0.1 && rating <= 0.5)
			oscRatingStatus = RatingStatus.BUY;
		else
			oscRatingStatus = RatingStatus.NEUTRAL;
	}

	// ---------------------------------------------------------------------------------
	
	private static <T> T last(T[] t)
	{
		return t[t.length - 1];
	}

	private static <T> T prev1(T[] t)
	{
		return t[t.length - 2];
	}

	private static <T> T prev2(T[] t)
	{
		return t[t.length - 3];
	}

	// ---------------------------------------------------------------------------------

	@Override
	public String toString()
	{
		double close = candles[candles.length - 1].getClosePrice();

		return "closePrice=" + close
				+ "\nsma=" + Arrays.toString(sma) + ", smaTrend=" + Arrays.toString(smaTrend) 
				+ "\nema=" + Arrays.toString(ema) + ", emaTrend=" + Arrays.toString(emaTrend)
				+ "\nvwma=" + vwma + ", vwmaTrend=" + vwmaTrend 
				+ "\nhma=" + hma + ", hmaTrend=" + hmaTrend 
				+ "\nichimokuBaseLine=" + ichimokuBaseLine + ", ichimokuTrend=" + ichimokuTrend 
				+ "\n\nmaRatingStatus=" + maRatingStatus + ", maRatingSum=" + maRatingSum + ", maRatingCount=" + maRatingCount 
				+ "\n\ntrend=" + trend
				+ "\n\nrsi=" + rsi + ", rsiStatus=" + rsiStatus 
				+ "\nstoch=" + stoch + ", stochStatus=" + stochStatus
				+ "\ncci=" + cci + ", cciStatus=" + cciStatus 
				+ "\nadx=" + adx + ", adxStatus=" + adxStatus 
				+ "\nao=" + ao + ", aoStatus=" + aoStatus 
				+ "\nmom=" + mom + ", momStatus=" + momStatus 
				+ "\nmacd=" + macd + ", macdStatus=" + macdStatus 
				+ "\nstochRsi=" + stochRsi + ", stochRsiStatus=" + stochRsiStatus 
				+ "\nwilliamsR=" + williamsR + ", williamsRStatus=" + williamsRStatus 
				+ "\nbbp=" + bbp + ", bbpStatus=" + bbpStatus 
				+ "\nuo=" + uo + ", uoStatus=" + uoStatus
				+ "\n\noscRatingStatus=" + oscRatingStatus + ", oscRatingSum=" + oscRatingSum + ", oscRatingCount=" + oscRatingCount; 
	}
	
}
