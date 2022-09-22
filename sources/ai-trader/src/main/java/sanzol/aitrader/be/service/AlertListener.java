package sanzol.aitrader.be.service;

import sanzol.aitrader.be.model.Alert;

@FunctionalInterface
public interface AlertListener
{
	void onAlert(Alert alert);
}
