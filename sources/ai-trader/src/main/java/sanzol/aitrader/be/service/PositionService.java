package sanzol.aitrader.be.service;

import api.client.impl.config.ApiConfig;
import api.client.impl.model.enums.MarketType;

public class PositionService
{
	public static void start()
	{
		if (ApiConfig.MARKET_TYPE == MarketType.spot)
		{
			PositionSpotService.start();
		}
		else
		{
			PositionFuturesService.start();
		}
	}

	public static void close()
	{
		if (ApiConfig.MARKET_TYPE == MarketType.spot)
		{
			PositionSpotService.close();
		}
		else
		{
			PositionFuturesService.close();
		}
	}

}
