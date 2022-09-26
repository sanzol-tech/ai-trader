package sanzol.aitrader.be.service;

import api.client.impl.config.ApiConfig;
import api.client.impl.model.enums.MarketType;

public class BalanceService
{
	public static void start()
	{
		if (ApiConfig.MARKET_TYPE == MarketType.spot)
		{
			BalanceSpotService.start();
		}
		else
		{
			BalanceFuturesService.start();
		}
	}

	public static void close()
	{
		if (ApiConfig.MARKET_TYPE == MarketType.spot)
		{
			BalanceSpotService.close();
		}
		else
		{
			BalanceFuturesService.close();
		}
	}
}
