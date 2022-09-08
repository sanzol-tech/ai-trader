package api.client.futures.sync;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import api.client.constant.ApiClientConstants;
import api.client.exception.ApiException;
import api.client.futures.enums.NewOrderRespType;
import api.client.futures.enums.OrderSide;
import api.client.futures.enums.OrderType;
import api.client.futures.enums.PositionSide;
import api.client.futures.enums.TimeInForce;
import api.client.futures.enums.WorkingType;
import api.client.futures.sync.model.AccountBalance;
import api.client.futures.sync.model.Depth;
import api.client.futures.sync.model.ExchangeInfo;
import api.client.futures.sync.model.Order;
import api.client.futures.sync.model.PositionRisk;
import api.client.futures.sync.model.SymbolTicker;
import sanzol.app.config.PrivateConfig;

public class SyncFuturesClient
{
	private static final int MAX_DEPTH_LIMIT = 1000;

	public static ExchangeInfo getExchangeInformation() throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		final String path = "/fapi/v1/exchangeInfo";

		Client client = CustomClient.getClient();
		Response response = client
				.target(ApiClientConstants.FUTURES_BASE_URL)
				.path(path)
				.request()
				.accept(MediaType.TEXT_XML)
				.get();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL)
		{
			// System.out.println(response.getStatusInfo().getFamily().toString());
			// System.out.println(response.getStatusInfo().getStatusCode());
			// System.out.println(response.getStatusInfo().getReasonPhrase());

			throw new ApiException(response.readEntity(String.class));
		}

		return response.readEntity(ExchangeInfo.class);
	}

	public static List<SymbolTicker> getSymbolTickers() throws KeyManagementException, NoSuchAlgorithmException
	{
		final String path = "/fapi/v1/ticker/24hr";

		Client client = CustomClient.getClient();
		Response response = client
				.target(ApiClientConstants.FUTURES_BASE_URL)
				.path(path)
				.request()
				.accept(MediaType.TEXT_XML)
				.get();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL)
		{
			// System.out.println(response.getStatusInfo().getFamily().toString());
			// System.out.println(response.getStatusInfo().getStatusCode());
			// System.out.println(response.getStatusInfo().getReasonPhrase());

			throw new ApiException(response.readEntity(String.class));
		}

		return response.readEntity(new GenericType<List<SymbolTicker>>() {});
	}

	public static Depth getDepth(String symbol, Integer limit) throws KeyManagementException, NoSuchAlgorithmException
	{
		final String path = "/fapi/v1/depth";

		if (limit == null) limit = MAX_DEPTH_LIMIT;
		
		Client client = CustomClient.getClient();
		Response response = client
				.target(ApiClientConstants.FUTURES_BASE_URL)
				.path(path)
				.queryParam("symbol", symbol)
				.queryParam("limit", limit)
				.request()
				.accept(MediaType.TEXT_XML)
				.get();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL)
		{
			// System.out.println(response.getStatusInfo().getFamily().toString());
			// System.out.println(response.getStatusInfo().getStatusCode());
			// System.out.println(response.getStatusInfo().getReasonPhrase());

			throw new ApiException(response.readEntity(String.class));
		}

		return response.readEntity(Depth.class);
	}

	// ------------------------------------------------------------------------

	public static List<AccountBalance> getBalance() throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException
	{
		final String path = "/fapi/v1/balance";
		
		Client client = CustomClient.getClient();

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = client
			.target(ApiClientConstants.FUTURES_BASE_URL)
			.path(path)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);

		String signature = ApiSignature.createSignature(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, target.getUri().getQuery());

		Response response = target
			.queryParam("signature", signature)
			.request()
			.header("X-MBX-APIKEY", PrivateConfig.API_KEY)
			.accept(MediaType.TEXT_XML)
			.get();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL)
		{
			// System.out.println(response.getStatusInfo().getFamily().toString());
			// System.out.println(response.getStatusInfo().getStatusCode());
			// System.out.println(response.getStatusInfo().getReasonPhrase());

			throw new ApiException(response.readEntity(String.class));
		}

		return response.readEntity(new GenericType<List<AccountBalance>>() {});		
	}

	public static List<PositionRisk> getPositionRisk() throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException
	{
		final String path = "/fapi/v1/positionRisk";

		Client client = CustomClient.getClient();

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = client
			.target(ApiClientConstants.FUTURES_BASE_URL)
			.path(path)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);

		String signature = ApiSignature.createSignature(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, target.getUri().getQuery());

		Response response = target
			.queryParam("signature", signature)
			.request()
			.header("X-MBX-APIKEY", PrivateConfig.API_KEY)
			.accept(MediaType.TEXT_XML)
			.get();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL)
		{
			// System.out.println(response.getStatusInfo().getFamily().toString());
			// System.out.println(response.getStatusInfo().getStatusCode());
			// System.out.println(response.getStatusInfo().getReasonPhrase());

			throw new ApiException(response.readEntity(String.class));
		}
		
		return response.readEntity(new GenericType<List<PositionRisk>>() {});		
	}

	public static List<Order> getOpenOrders(String symbol) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException
	{
		final String path = "/fapi/v1/openOrders";
		
		Client client = CustomClient.getClient();
		
		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = client
			.target(ApiClientConstants.FUTURES_BASE_URL)
			.path(path)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);

		String signature = ApiSignature.createSignature(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, target.getUri().getQuery());

		Response response = target
			.queryParam("signature", signature)
			.request()
			.header("X-MBX-APIKEY", PrivateConfig.API_KEY)
			.accept(MediaType.TEXT_XML)
			.get();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL)
		{
			// System.out.println(response.getStatusInfo().getFamily().toString());
			// System.out.println(response.getStatusInfo().getStatusCode());
			// System.out.println(response.getStatusInfo().getReasonPhrase());

			throw new ApiException(response.readEntity(String.class));
		}

		return response.readEntity(new GenericType<List<Order>>() {});		
	}

	public static Order postOrder(String symbol, OrderSide side, PositionSide positionSide, OrderType orderType, TimeInForce timeInForce, 
								  String quantity, String price, Boolean reduceOnly, String newClientOrderId, String stopPrice, 
								  WorkingType workingType, NewOrderRespType newOrderRespType, Boolean closePosition) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException
	{
		final String path = "/fapi/v1/order";
		
		Client client = CustomClient.getClient();

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = client
			.target(ApiClientConstants.FUTURES_BASE_URL)
			.path(path)
			.queryParam("symbol", symbol)
			.queryParam("side", side)
			.queryParam("positionSide", positionSide)
			.queryParam("type", orderType)
			.queryParam("timeInForce", timeInForce)
			.queryParam("quantity", quantity)
			.queryParam("price", price)
			.queryParam("reduceOnly", reduceOnly)
			.queryParam("newClientOrderId", newClientOrderId)
			.queryParam("stopPrice", stopPrice)
			.queryParam("workingType", workingType)
			.queryParam("newOrderRespType", newOrderRespType)
			.queryParam("closePosition", closePosition)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);
		
		String signature = ApiSignature.createSignature(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, target.getUri().getQuery());

		Response response = target
			.queryParam("signature", signature)
			.request()
			.header("X-MBX-APIKEY", PrivateConfig.API_KEY)
			.accept(MediaType.TEXT_XML)
			.post(null);

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL)
		{
			// System.out.println(response.getStatusInfo().getFamily().toString());
			// System.out.println(response.getStatusInfo().getStatusCode());
			// System.out.println(response.getStatusInfo().getReasonPhrase());

			throw new ApiException(response.readEntity(String.class));
		}
		
		return response.readEntity(Order.class);		
	}

	public static Order cancelOrder(String symbol, Long orderId, String origClientOrderId) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException
	{
		final String path = "/fapi/v1/order";

		Client client = CustomClient.getClient();

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = client
			.target(ApiClientConstants.FUTURES_BASE_URL)
			.path(path)
			.queryParam("symbol", symbol)
			.queryParam("orderId", orderId)
			.queryParam("origClientOrderId", origClientOrderId)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);

		String signature = ApiSignature.createSignature(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, target.getUri().getQuery());

		Response response = target
			.queryParam("signature", signature)
			.request()
			.header("X-MBX-APIKEY", PrivateConfig.API_KEY)
			.accept(MediaType.TEXT_XML)
			.delete();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL)
		{
			// System.out.println(response.getStatusInfo().getFamily().toString());
			// System.out.println(response.getStatusInfo().getStatusCode());
			// System.out.println(response.getStatusInfo().getReasonPhrase());

			throw new ApiException(response.readEntity(String.class));
		}
		
		return response.readEntity(Order.class);		
	}

	// ------------------------------------------------------------------------

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException, InvalidKeyException
	{
		PrivateConfig.loadKey();
/*
		List<AccountBalance> lstAccountBalance = getBalance();
		for (AccountBalance entry : lstAccountBalance)
		{
			System.out.println(entry.getAsset() + " : " + entry.getBalance().toPlainString() + " : " + entry.getWithdrawAvailable().toPlainString());
		}

		List<PositionRisk> lstPositionRisk = getPositionRisk();
		for (PositionRisk entry : lstPositionRisk)
		{
			System.out.println(entry.getSymbol() + " : " + entry.getPositionAmt().toPlainString());
		}

		List<Order> lstOrder = getOpenOrders(null);
		for (Order entry : lstOrder)
		{
			System.out.println(entry.getSymbol() + " : " + entry.getPrice() + " : " + entry.getOrigQty());
		}

		try
		{
			postOrder(
					"BTCUSDT", OrderSide.BUY, PositionSide.BOTH, OrderType.LIMIT, TimeInForce.GTC, 
					"0.001", "15000", null, null, null, WorkingType.CONTRACT_PRICE,
					NewOrderRespType.RESULT, false);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
*/

	}

}
