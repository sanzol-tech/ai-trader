package exchanges.bybit;

import java.util.List;

import technicals.model.TechCandle;

public class BybitCandleUtils
{

	public static TechCandle[] toCandleArray(List<BybitCandle> lstCandles)
	{
		TechCandle[] candle = new TechCandle[lstCandles.size()];
		for (int i = 0; i < lstCandles.size(); i++)
		{
			candle[i] = new TechCandle();
			candle[i].setOpenTime(lstCandles.get(i).getOpenTimeZoned());
			candle[i].setOpenPrice(lstCandles.get(i).getOpen().doubleValue());
			candle[i].setClosePrice(lstCandles.get(i).getClose().doubleValue());
			candle[i].setHighPrice(lstCandles.get(i).getHigh().doubleValue());
			candle[i].setLowPrice(lstCandles.get(i).getLow().doubleValue());
			candle[i].setVolume(lstCandles.get(i).getVolume().doubleValue());
		}
		return candle;
	}

}
