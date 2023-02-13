package aitrader.core.service.market;

import java.util.List;

import aitrader.core.model.Symbol;
import aitrader.core.util.CandleUtils;
import binance.futures.enums.IntervalType;
import binance.futures.impl.UnsignedClient;
import binance.futures.model.Candle;
import technicals.indicators.complex.TechnicalRatings;
import technicals.model.TechCandle;

public class TechnicalStatus
{

	public static TechnicalRatings load(Symbol symbol, IntervalType intervalType) throws Exception
	{
		List<Candle> lstCandles = UnsignedClient.getKlines(symbol.getPair(), intervalType, 240);
		TechCandle[] candles = CandleUtils.toCandleArray(lstCandles);
		TechnicalRatings technicalRatings = new TechnicalRatings(symbol.getPricePrecision());
		technicalRatings.calculate(candles);
		return technicalRatings;
	}

}
