package sanzol.aitrader.be.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import api.client.futures.model.enums.OrderSide;
import sanzol.aitrader.be.model.Alert;
import sanzol.util.price.PriceUtil;

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

				BigDecimal distLimit, distAlert;

				if (alert.isAlerted())
				{
					if (alert.getSide() == OrderSide.SELL)
					{
						distLimit = PriceUtil.priceDistUp(lastPrice, alert.getPriceLimit(), false);
					}
					else
					{
						distLimit = PriceUtil.priceDistDown(lastPrice, alert.getPriceLimit(), false);
					}

					if (distLimit.doubleValue() <= 0)
					{
						System.out.println("remove alert: " + alert.toString());

						lstAlerts.remove(alert);
					}
				}
				else
				{
					if (alert.getSide() == OrderSide.SELL)
					{
						distAlert = PriceUtil.priceDistUp(lastPrice, alert.getPriceAlert(), false);
					}
					else
					{
						distAlert = PriceUtil.priceDistDown(lastPrice, alert.getPriceAlert(), false);
					}

					if (distAlert.doubleValue() <= 0)
					{
						System.out.println("actived alert: " + alert.toString());
					}
				}
			}
		}
	}

}
