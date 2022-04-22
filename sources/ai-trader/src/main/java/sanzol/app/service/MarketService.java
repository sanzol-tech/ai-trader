package sanzol.app.service;

import sanzol.app.config.Application;

public class MarketService
{
	public static String getMarketStatus()
	{
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}

	public static void main(String[] args)
	{
		Application.initialize();

		System.out.println(getMarketStatus());
	}

}
