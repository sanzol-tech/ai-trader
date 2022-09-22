package sanzol.aitrader.be.service;

import sanzol.aitrader.be.model.Alert;

public interface AlertListener
{
	void onAlertsUptade();

	void onAlert(Alert alert);
}
