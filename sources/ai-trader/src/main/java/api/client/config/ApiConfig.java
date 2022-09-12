package api.client.config;

import api.client.enums.MarketType;

public class ApiConfig
{
	public static MarketType MARKET_TYPE = MarketType.futures;
	public static String BASE_URL = ApiConstants.FUTURES_BASE_URL;
	public static String WS_BASE_URL = ApiConstants.FUTURES_WS_BASE_URL;

	public static void setSpot()
	{
		MARKET_TYPE = MarketType.spot;
		BASE_URL = ApiConstants.SPOT_BASE_URL;
		WS_BASE_URL = ApiConstants.SPOT_WS_BASE_URL;
	}

	public static void setFutures()
	{
		MARKET_TYPE = MarketType.futures;
		BASE_URL = ApiConstants.FUTURES_BASE_URL;
		WS_BASE_URL = ApiConstants.FUTURES_WS_BASE_URL;
	}

}
