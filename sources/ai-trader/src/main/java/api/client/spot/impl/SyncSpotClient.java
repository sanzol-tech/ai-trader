package api.client.spot.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import api.client.exception.ApiException;
import api.client.impl.config.ApiConstants;
import api.client.impl.utils.ApiSignature;
import api.client.impl.utils.CustomClient;
import api.client.model.Depth;
import api.client.model.ExchangeInfo;
import api.client.model.Kline;
import api.client.model.SymbolTicker;
import api.client.spot.model.Account;
import api.client.spot.model.AssetBalance;
import api.client.spot.model.Order;
import api.client.spot.model.enums.IntervalType;
import api.client.spot.model.enums.NewOrderRespType;
import api.client.spot.model.enums.OrderSide;
import api.client.spot.model.enums.OrderStatus;
import api.client.spot.model.enums.OrderType;
import api.client.spot.model.enums.TimeInForce;
import sanzol.aitrader.be.config.PrivateConfig;

public class SyncSpotClient
{
	private static final int MAX_DEPTH_LIMIT = 5000;

	private static void verifyResponseStatus(Response response)
	{
		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL)
		{
			System.out.println(response.getStatusInfo().getFamily().toString());
			System.out.println(response.getStatusInfo().getStatusCode());
			System.out.println(response.getStatusInfo().getReasonPhrase());

			throw new ApiException(response.readEntity(String.class));
		}
	}

	public static ExchangeInfo getExchangeInformation() throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		final String path = "/api/v3/exchangeInfo";

		Client client = CustomClient.getClient();
		Response response = client
				.target(ApiConstants.SPOT_BASE_URL)
				.path(path)
				.request()
				.accept(MediaType.TEXT_XML)
				.get();

		verifyResponseStatus(response);

		return response.readEntity(ExchangeInfo.class);
	}

	public static List<SymbolTicker> getSymbolTickers() throws KeyManagementException, NoSuchAlgorithmException
	{
		final String path = "/api/v3/ticker/24hr";

		Client client = CustomClient.getClient();
		Response response = client
				.target(ApiConstants.SPOT_BASE_URL)
				.path(path)
				.request()
				.accept(MediaType.TEXT_XML)
				.get();

		verifyResponseStatus(response);

		return response.readEntity(new GenericType<List<SymbolTicker>>() {});
	}

	public static Depth getDepth(String symbol) throws KeyManagementException, NoSuchAlgorithmException
	{
		return getDepth(symbol, MAX_DEPTH_LIMIT);
	}

	public static Depth getDepth(String symbol, int limit) throws KeyManagementException, NoSuchAlgorithmException
	{
		final String path = "/api/v3/depth";

		Client client = CustomClient.getClient();
		Response response = client
				.target(ApiConstants.SPOT_BASE_URL)
				.path(path)
				.queryParam("symbol", symbol)
				.queryParam("limit", limit)
				.request()
				.accept(MediaType.TEXT_XML)
				.get();

		verifyResponseStatus(response);

		return response.readEntity(Depth.class);
	}

	public static List<Kline> getKlines(String symbol, IntervalType interval, int limit) throws KeyManagementException, NoSuchAlgorithmException
	{
		final String path = "/api/v3/klines";

		Client client = CustomClient.getClient();
		Response response = client
				.target(ApiConstants.SPOT_BASE_URL)
				.path(path)
				.queryParam("symbol", symbol)
				.queryParam("interval", interval.toString())
				.queryParam("limit", limit)
				.request()
				.accept(MediaType.TEXT_XML)
				.get();

		verifyResponseStatus(response);

		List<List<String>> lst = response.readEntity(new GenericType<List<List<String>>>(){});

		// Create list of Kline
		List<Kline> lstResult = new ArrayList<Kline>();
		for (List<String> entry : lst)
		{
			Kline kline = new Kline();

			kline.setOpenPrice(new BigDecimal(entry.get(1)));
			kline.setHighPrice(new BigDecimal(entry.get(2)));
			kline.setLowPrice(new BigDecimal(entry.get(3)));
			kline.setClosePrice(new BigDecimal(entry.get(4)));
			kline.setVolume(new BigDecimal(entry.get(5)));
			kline.setQuoteVolume(new BigDecimal(entry.get(7)));
			kline.setCount(Long.valueOf(entry.get(8)));

			lstResult.add(kline);
		}

		return lstResult;
	}

	// --- SIGNED -------------------------------------------------------------

	public static Account getAccountInformation() throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException
	{
		final String path = "/api/v3/account";

		Client client = CustomClient.getClient();

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = client
			.target(ApiConstants.SPOT_BASE_URL)
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

		verifyResponseStatus(response);

		Account account = response.readEntity(Account.class);

		// Remove all zero balances
		account.getBalances().removeIf((AssetBalance entry) -> BigDecimal.ZERO.compareTo(entry.getFree()) == 0 && BigDecimal.ZERO.compareTo(entry.getLocked()) == 0);

		return account;
	}

	public static List<Order> getOpenOrders() throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException
	{
		final String path = "/api/v3/openOrders";

		Client client = CustomClient.getClient();

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = client
			.target(ApiConstants.SPOT_BASE_URL)
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

		verifyResponseStatus(response);

		return response.readEntity(new GenericType<List<Order>>() {});
	}

	public static List<Order> getFilledOrders(String symbol) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException
	{
		final String path = "/api/v3/allOrders";

		Client client = CustomClient.getClient();

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = client
			.target(ApiConstants.SPOT_BASE_URL)
			.path(path)
			.queryParam("symbol", symbol)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);

		String signature = ApiSignature.createSignature(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, target.getUri().getQuery());

		Response response = target
			.queryParam("signature", signature)
			.request()
			.header("X-MBX-APIKEY", PrivateConfig.API_KEY)
			.accept(MediaType.TEXT_XML)
			.get();

		verifyResponseStatus(response);

		List<Order> lstOrders = response.readEntity(new GenericType<List<Order>>() {});

		// Remove all no NEW
		lstOrders.removeIf((Order entry) -> entry.getStatus() != OrderStatus.FILLED);

		return lstOrders;
	}

	public static Order postOrder(String symbol, OrderSide side, OrderType orderType, TimeInForce timeInForce,
								  String quantity, String price, String newClientOrderId, String stopPrice,
								  String icebergQty, NewOrderRespType newOrderRespType) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException
	{
		final String path = "/api/v3/order";

		Client client = CustomClient.getClient();

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = client
			.target(ApiConstants.SPOT_BASE_URL)
			.path(path)
			.queryParam("symbol", symbol)
			.queryParam("side", side)
			.queryParam("type", orderType)
			.queryParam("timeInForce", timeInForce)
			.queryParam("quantity", quantity)
			.queryParam("price", price)
			.queryParam("newClientOrderId", newClientOrderId)
			.queryParam("stopPrice", stopPrice)
			.queryParam("icebergQty", icebergQty)
			.queryParam("newOrderRespType", newOrderRespType)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);

		String signature = ApiSignature.createSignature(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, target.getUri().getQuery());

		Response response = target
			.queryParam("signature", signature)
			.request()
			.header("X-MBX-APIKEY", PrivateConfig.API_KEY)
			.accept(MediaType.TEXT_XML)
			.post(null);

		verifyResponseStatus(response);

		return response.readEntity(Order.class);
	}

	public static Order cancelOrder(String symbol, Long orderId, String origClientOrderId) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException
	{
		final String path = "/api/v3/order";

		Client client = CustomClient.getClient();

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = client
			.target(ApiConstants.SPOT_BASE_URL)
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

		verifyResponseStatus(response);

		return response.readEntity(Order.class);
	}

	// ------------------------------------------------------------------------

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException, InvalidKeyException
	{
		PrivateConfig.loadKey();

		Account acc = getAccountInformation();

		for (AssetBalance entry : acc.getBalances())
		{
			System.out.println(entry);
		}

		// List<Order> lst = getOpenOrders();
		// System.out.println(lst.size());

		// Order order = postOrder("XRPUSDT", OrderSide.BUY, OrderType.LIMIT, TimeInForce.GTC, "36", "0.28", null, null, null, NewOrderRespType.RESULT);
		//Order order = cancelOrder("XRPUSDT", 4653644423L, "myzMLHvaXkY3u1aA5wBPD3");

		//System.out.println(order);

		//ExchangeInfo ei = getExchangeInformation();
		//System.out.println(ei.getSymbols().size());

		//Depth depth = getDepth("BTCUSDT", null);
		//System.out.println(depth.getLastUpdateId());
	}

}
