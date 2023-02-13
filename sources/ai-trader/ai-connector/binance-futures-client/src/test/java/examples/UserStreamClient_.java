package examples;
import binance.futures.impl.SignedClient;
import binance.futures.impl.async.WsUserData;

public class UserStreamClient_
{

	public static void main(String[] args) throws Exception
	{
		PrivateConfig.load();
		SignedClient client = SignedClient.create(PrivateConfig.getApiKey(), PrivateConfig.getSecretKey());

		String listenKey = client.startUserDataStream();
		System.out.println(listenKey);

		/*
		String result1 = client.keepUserDataStream();
		System.out.println(result1);
		
		String result2 = client.closeUserDataStream();
		System.out.println(result2);
		*/

		try
		{
			WsUserData wsUserData = WsUserData.create(listenKey, (event) -> {
				System.out.println(event);
			});
			wsUserData.connect();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
		
	}

}
