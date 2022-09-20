package api.client.impl.config;

import api.client.impl.model.enums.MarketType;

public final class ApiConfig
{
	public static MarketType MARKET_TYPE = MarketType.futures;
	public static String BASE_URL = ApiConstants.FUTURES_BASE_URL;
	public static String WS_BASE_URL = ApiConstants.FUTURES_WS_BASE_URL;

	public static void setMarketType(MarketType marketType)
	{
		if (marketType == MarketType.futures)
			setFutures();
		else
			setSpot();
	}

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
