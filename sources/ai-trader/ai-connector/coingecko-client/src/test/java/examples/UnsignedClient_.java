package examples;

import java.util.List;

import coingecko.impl.UnsignedClient;
import coingecko.model.CoinMarket;

public class UnsignedClient_
{

	public static void main(String[] args) throws Exception
	{
		List<CoinMarket> lstMarkets = UnsignedClient.getMarkets("volume_desc", 5);
		lstMarkets.forEach(s -> System.out.println(s));		
	}

}
