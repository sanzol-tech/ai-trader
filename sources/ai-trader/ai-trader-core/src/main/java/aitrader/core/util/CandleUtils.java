package aitrader.core.util;

import java.util.List;

import binance.futures.model.Candle;
import technicals.model.TechCandle;

public class CandleUtils
{

	public static TechCandle[] toCandleArray(List<Candle> lstCandles)
	{
		TechCandle[] candle = new TechCandle[lstCandles.size()];
		for (int i = 0; i < lstCandles.size(); i++)
		{
			candle[i] = new technicals.model.TechCandle();
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
