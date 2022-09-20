package sanzol.aitrader.be.service;

@FunctionalInterface
public interface PositionListener
{
	void onPositionUpdate();
}
