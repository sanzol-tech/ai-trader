package examples;

import java.util.List;

import binance.spot.enums.IntervalType;
import binance.spot.impl.UnsignedClient;
import binance.spot.model.Candle;
import binance.spot.model.Depth;
import binance.spot.model.SymbolTicker;

public class UnsignedClient_
{

	public static void main(String[] args) throws Exception
	{
		List<SymbolTicker> lstSymbolTickers = UnsignedClient.getSymbolTickers();
		lstSymbolTickers.forEach(s -> System.out.println(s));		

		List<Candle> lstCandles = UnsignedClient.getKlines("BTCUSDT", IntervalType._1h, 5);
		lstCandles.forEach(s -> System.out.println(s));		

		Depth depth = UnsignedClient.getDepth("BTCUSDT", 5);
		System.out.println(depth.getLastUpdateId());
	}

}
