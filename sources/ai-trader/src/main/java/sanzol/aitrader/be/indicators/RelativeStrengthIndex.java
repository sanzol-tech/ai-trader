package sanzol.aitrader.be.indicators;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import api.client.futures.impl.SyncFuturesClient;
import api.client.futures.model.enums.IntervalType;
import api.client.model.Kline;
import sanzol.aitrader.be.model.Symbol;
import sanzol.util.log.LogService;

public class RelativeStrengthIndex
{
	private static final int PERIODS = 14;
	private static final IntervalType INTERVAL = IntervalType._2h;
	private static final double OVERBOUGHT = 0.7;
	private static final double OVERSOLD = 0.3;

	public static Double calc(Symbol symbol) throws KeyManagementException, NoSuchAlgorithmException
	{
		List<Kline> lstCandlestick = SyncFuturesClient.getKlines(symbol.getPair(), INTERVAL, PERIODS + 1);

		if (lstCandlestick == null || lstCandlestick.size() < PERIODS + 1)
		{
			return null;
		}

		return calc(lstCandlestick, PERIODS, INTERVAL, OVERBOUGHT, OVERSOLD);
	}

	public static Double calc(List<Kline> lstCandlestick, int periods, IntervalType intervalType, double overbought, double oversold)
	{
		try
		{
			int len = lstCandlestick.size();

			double gain = 0;
			double loss = 0;
			double avgGain = 0;
			double avgLoss = 0;

			for (int i = len - periods; i < len; i++)
			{
				Kline prev = lstCandlestick.get(i - 1);
				Kline entry = lstCandlestick.get(i);

				if (entry.getClosePrice().doubleValue() > prev.getClosePrice().doubleValue())
				{
					gain += entry.getClosePrice().doubleValue() - prev.getClosePrice().doubleValue();
					loss += 0;
				}
				else
				{
					gain += 0;
					loss += prev.getClosePrice().doubleValue() - entry.getClosePrice().doubleValue();
				}
			}

			avgGain = gain / periods;
			avgLoss = loss / periods;

			double rs = avgLoss != 0 ? avgGain / avgLoss : avgGain;
			double rsi = 1 - 1 / (1 + rs);

			return rsi;
		}
		catch (Exception e)
		{
			LogService.error(e);
			return null;
		}
	}

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		Double rsi = calc(Symbol.fromPair("BNBUSDT"));
		System.out.println("RSI: " + rsi + " %");
	}

}
