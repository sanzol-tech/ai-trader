# ai-technicals
Java library for financial technical analysis with multiple technical indicators

### Moving Average
- SMA - Simple Moving Average
- WMA - Weighted Moving Average
- EMA - Exponential Moving Average
- VWMA - Volume-Weighted Moving Average
- HMA - Hull Moving Average

### Oscillators
- Aroon
- ADX - Average Directional Index
- AO - Awesome Oscillator
- BBP - Bull Bear Power
- CCI - Commodity Channel Index
- MACD - Moving Average Convergence / Divergence
- Momentum
- ROC - Rate Of Change
- RSI - Relative Strength Index
- Stochastic
- Stochastic RSI
- UO - Ultimate Oscillator
- Williams %R

### Volatility
- Average True Range
- Bollinger
- Keltner Channel
- Price Channel
- Standard Deviation

### Volume
- A/D - Accumulation / Distribution
- MFI - Money Flow Index
- OBV - On Balance Volume
- VWAP - Volume Weighted Average Price

### Pivot Points
- Classic Pivot Points
- Camarilla Pivot Points
- Demarks Pivot Points
- Fibonacci Pivot Points
- Woodie Pivot Points

### Others
- Ichimoku Cloud
- Williams Fractals

### Technical Ratings
- Technical Ratings

### Order Book Depth Indicators <sup>*Experimental*</sup>
- Depth True Range - Volatility indicator
- Depth Middle Price - Trend indicator
- Depth Delta - Balance between ask / bid
- Depth Block Points - Supports & Resistances
- Depth Weighted Points - Supports & Resistances
- Depth Super Prices - Prices with the largest number of orders

## How to use the library

### Example 1:

| Indicator | Exchange | Pair | Interval |
| --- | --- | --- | --- |
| Bollinger | Binance | BTCUSDT | 1 day |

```
List<BinanceCandle> lstBinanceCandles = BinanceApiClient.getKlines("BTCUSDT", BinanceIntervalType._1d, 30);
Candle[] candles = BinanceCandleUtils.toCandleArray(lstBinanceCandles);
BollingerEntry[] entries = Bollinger.calculate(candles, 20, 2);
Arrays.stream(entries).forEach(s -> System.out.println(s == null ? "null" : s));
```

### Example 2:

| Indicator | Exchange | Pair | Interval |
| --- | --- | --- | --- |
| Stochastic | Kucoin | BTC-USDT | 1 day |

```
List<KucoinCandle> lstKucoinCandle = KucoinApiClient.getKlines("BTC-USDT", KucoinIntervalType._1d, 30);
Candle[] candles = KucoinCandleUtils.toCandleArray(lstKucoinCandle);
StochasticEntry[] entries = Stochastic.calculate(candles, 14, 1, 3);
Arrays.stream(entries).forEach(s -> System.out.println(s == null ? "null" : s));
```

