package examples;

import binance.spot.impl.SignedClient;
import binance.spot.model.Account;

public class SignedClient_
{

	public static void main(String[] args) throws Exception
	{
		PrivateConfig.load();
		SignedClient client = SignedClient.create(PrivateConfig.getApiKey(), PrivateConfig.getSecretKey());

		System.out.println("");
		Account acc = client.getAccountInformation();
		acc.getBalances().forEach(s -> System.out.println(s));

		/*
		System.out.println("");
		List<Order> lstOrders = client.getOpenOrders();
		lstOrders.forEach(s -> System.out.println(s));

		System.out.println("");
		List<Order> lstAllOrders = client.getAllOrders("BTCUSDT");
		lstAllOrders.forEach(s -> System.out.println(s));
		*/

		/*
		Order order = client.postOrder("BTCUSDT", OrderSide.BUY, OrderType.LIMIT, TimeInForce.GTC, "0.001", "14200", null, null, null, NewOrderRespType.RESULT);
		Order order = client.cancelOrder("BTCUSDT", 16368540689L, "l4QlDxgNmbEcdUwpbv1QQr");
		System.out.println(order);
		*/
	}

}
