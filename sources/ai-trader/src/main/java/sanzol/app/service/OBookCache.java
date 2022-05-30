package sanzol.app.service;

import sanzol.app.config.Config;

public class OBookCache
{
	private static OBookService obServiceBTC;
	private static OBookService obServiceETH;
	private static OBookService obServiceBNB;

	public static OBookService getObServiceBTC()
	{
		return obServiceBTC;
	}

	public static OBookService getObServiceETH()
	{
		return obServiceETH;
	}

	public static OBookService getObServiceBNB()
	{
		return obServiceBNB;
	}

	public static void start()
	{
		Symbol btc = Symbol.getInstance("BTC" + Config.DEFAULT_SYMBOL_RIGHT);
		obServiceBTC = OBookService.getInstance(btc).subscribeDiffDepthEvent();

		Symbol eth = Symbol.getInstance("ETH" + Config.DEFAULT_SYMBOL_RIGHT);
		obServiceETH = OBookService.getInstance(eth).subscribeDiffDepthEvent();
		
		Symbol bnb = Symbol.getInstance("BNB" + Config.DEFAULT_SYMBOL_RIGHT);
		obServiceBNB = OBookService.getInstance(bnb).subscribeDiffDepthEvent();
	}

}
