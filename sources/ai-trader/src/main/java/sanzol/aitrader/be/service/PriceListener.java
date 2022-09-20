package sanzol.aitrader.be.service;

@FunctionalInterface
public interface PriceListener
{
	void onPriceUpdate();
}
