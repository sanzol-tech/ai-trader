package sanzol.app.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import api.client.service.PriceListener;
import api.client.service.PriceService;
import sanzol.app.model.Alert;
import sanzol.app.util.PriceUtil;

public class AlertService implements PriceListener
{
	private List<Alert> lstAlerts = new ArrayList<Alert>();

	private static AlertService alertService;

	public static AlertService getInstance()
	{
		if (alertService == null)
		{
			alertService = new AlertService();
		}
		return alertService;
	}

	private AlertService()
	{
		//
	}

	public static void start()
	{
		AlertService alertService = AlertService.getInstance();
		PriceService.attachRefreshObserver(alertService);
	}

	public void add(Alert alert)
	{
		lstAlerts.add(alert);
	}

	public void remove(Alert alert)
	{
		lstAlerts.remove(alert);
	}

	@Override
	public void onPriceUpdate()
	{
		if (!lstAlerts.isEmpty())
		{
			for (Alert alert : lstAlerts)
			{
				BigDecimal lastPrice = PriceService.getLastPrice(alert.getSymbol());
				if (lastPrice.doubleValue() < 0)
				{
					continue;
				}

				BigDecimal dist;
				if ("SHORT".equals(alert.getSide()))
					dist = PriceUtil.priceDistUp(lastPrice, alert.getPrice(), false);
				else
					dist = PriceUtil.priceDistDown(lastPrice, alert.getPrice(), false);

				if (dist.doubleValue() < 0.005)
				{
					System.out.println("alert trigger !");
				}

				if (dist.doubleValue() <= 0)
				{
					lstAlerts.remove(alert);
				}
			}
		}
	}

}
