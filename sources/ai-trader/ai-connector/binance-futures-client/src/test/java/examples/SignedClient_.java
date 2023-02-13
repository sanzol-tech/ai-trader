package examples;
import java.util.List;

import binance.futures.impl.SignedClient;
import binance.futures.model.Order;

public class SignedClient_
{

	public static void main(String[] args) throws Exception
	{
		PrivateConfig.load();
		SignedClient client = SignedClient.create(PrivateConfig.getApiKey(), PrivateConfig.getSecretKey());

		/*
		System.out.println("");
		System.out.println(client.setLeverage("BTCUSDT", 10));

		try
		{
			client.postOrder(
					"BTCUSDT", OrderSide.BUY, PositionSide.BOTH, OrderType.LIMIT, TimeInForce.GTC,
					"0.001", "15000", null, null, null, WorkingType.CONTRACT_PRICE,
					NewOrderRespType.RESULT, false);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println("");
		List<AccountBalance> lstAccountBalance = client.getBalance();
		lstAccountBalance.forEach(s -> System.out.println(s.getAsset() + "\t\t" + s.getBalance() + "\t\t" + s.getWithdrawAvailable()));

		System.out.println("");
		List<PositionRisk> lstPositionRisk = client.getPositionRisk();
		lstPositionRisk.forEach(s -> System.out.println(s.getSymbol() + "\t\t" + s.getPositionAmt()));
 		*/

		System.out.println("");
		List<Order> lstOrders = client.getOpenOrders();
		lstOrders.forEach(s -> System.out.println(s.getSymbol() + "\t\t" + s.getAvgPrice() + "\t\t" + s.getOrigQty()));

	}

}
