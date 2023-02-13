package aitrader.core.service.trade;

import aitrader.core.config.CoreLog;
import aitrader.core.config.PrivateConfig;
import binance.futures.enums.PositionMode;
import binance.futures.impl.SignedClient;

public class PositionModeService
{
	private static PositionMode positionMode;

	public static PositionMode getPositionMode()
	{
		return positionMode;
	}

	public static void loadPositionMode() throws Exception
	{
		SignedClient client = SignedClient.create(PrivateConfig.getApiKey(), PrivateConfig.getSecretKey());
		positionMode = client.getPositionMode();
		
		CoreLog.info("PositionMode: " + positionMode);
	}

}
