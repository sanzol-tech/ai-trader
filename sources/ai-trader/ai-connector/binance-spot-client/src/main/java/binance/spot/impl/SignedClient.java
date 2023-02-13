package binance.spot.impl;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import binance.spot.commons.BinanceException;
import binance.spot.commons.ResponseStatus;
import binance.spot.commons.Signer;
import binance.spot.config.ApiConstants;
import binance.spot.enums.NewOrderRespType;
import binance.spot.enums.OrderSide;
import binance.spot.enums.OrderType;
import binance.spot.enums.TimeInForce;
import binance.spot.model.Account;
import binance.spot.model.AssetBalance;
import binance.spot.model.Order;

public class SignedClient
{
	private String apiKey;
	private String secretKey;

	public SignedClient(String apiKey, String secretKey)
	{
		this.apiKey = apiKey;
		this.secretKey = secretKey;
	}

	public static SignedClient create(String apiKey, String secretKey)
	{
		return new SignedClient(apiKey, secretKey);
	}

	// --------------------------------------------------------------------

	public Account getAccountInformation() throws Exception
	{
		final String path = "/api/v3/account";

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = ClientBuilder.newClient()
			.target(ApiConstants.BASE_URL)
			.path(path)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);

		String signature = Signer.createSignature(apiKey, secretKey, target.getUri().getQuery());
		URI uri = target.queryParam("signature", signature).getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("X-MBX-APIKEY", apiKey)
            .GET()
            .build();

		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());		

		if(response.statusCode() != 200)
		{
			ResponseStatus responseStatus = ResponseStatus.from(response.body());
			throw new BinanceException(responseStatus.getCode() + " : " + responseStatus.getMsg());
		}

		String jsonString = response.body();
		ObjectMapper mapper = new ObjectMapper();
		Account account = mapper.readValue(jsonString, Account.class);

		// Remove all zero balances
		account.getBalances().removeIf((AssetBalance entry) -> BigDecimal.ZERO.compareTo(entry.getFree()) == 0 && BigDecimal.ZERO.compareTo(entry.getLocked()) == 0);

		return account;
	}
	
	public List<Order> getOpenOrders() throws Exception
	{
		final String path = "/api/v3/openOrders";

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = ClientBuilder.newClient()
			.target(ApiConstants.BASE_URL)
			.path(path)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);

		String signature = Signer.createSignature(apiKey, secretKey, target.getUri().getQuery());
		URI uri = target.queryParam("signature", signature).getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("X-MBX-APIKEY", apiKey)
            .GET()
            .build();

		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());		

		if(response.statusCode() != 200)
		{
			ResponseStatus responseStatus = ResponseStatus.from(response.body());
			throw new BinanceException(responseStatus.getCode() + " : " + responseStatus.getMsg());
		}

		String jsonString = response.body();
		ObjectMapper mapper = new ObjectMapper();
		List<Order> lst = mapper.readValue(jsonString, new TypeReference<List<Order>>(){});

		return lst;	
	}

	public List<Order> getAllOrders(String symbol) throws Exception
	{
		final String path = "/api/v3/allOrders";

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = ClientBuilder.newClient()
			.target(ApiConstants.BASE_URL)
			.path(path)
			.queryParam("symbol", symbol)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);

		String signature = Signer.createSignature(apiKey, secretKey, target.getUri().getQuery());
		URI uri = target.queryParam("signature", signature).getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("X-MBX-APIKEY", apiKey)
            .GET()
            .build();

		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());		

		if(response.statusCode() != 200)
		{
			ResponseStatus responseStatus = ResponseStatus.from(response.body());
			throw new BinanceException(responseStatus.getCode() + " : " + responseStatus.getMsg());
		}
		
		String jsonString = response.body();
		ObjectMapper mapper = new ObjectMapper();
		List<Order> lstOrders = mapper.readValue(jsonString, new TypeReference<List<Order>>(){});

		return lstOrders;
	}
	
	public Order postOrder(String symbol, OrderSide side, OrderType orderType, TimeInForce timeInForce,
						   String quantity, String price, String newClientOrderId, String stopPrice,
						   String icebergQty, NewOrderRespType newOrderRespType) throws Exception
	{
		final String path = "/api/v3/order";

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = ClientBuilder.newClient()
			.target(ApiConstants.BASE_URL)
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

		String signature = Signer.createSignature(apiKey, secretKey, target.getUri().getQuery());
		URI uri = target.queryParam("signature", signature).getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("X-MBX-APIKEY", apiKey)
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();

		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());		

		if(response.statusCode() != 200)
		{
			ResponseStatus responseStatus = ResponseStatus.from(response.body());
			throw new BinanceException(responseStatus.getCode() + " : " + responseStatus.getMsg());
		}

		String jsonString = response.body();
		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.readValue(jsonString, Order.class);

		return order;			
	}	
	
	public Order cancelOrder(String symbol, Long orderId, String origClientOrderId) throws Exception
	{
		final String path = "/api/v3/order";

		String recvWindow = Long.toString(60_000L);
		String timestamp = Long.toString(System.currentTimeMillis());

		WebTarget target = ClientBuilder.newClient()
			.target(ApiConstants.BASE_URL)
			.path(path)
			.queryParam("symbol", symbol)
			.queryParam("orderId", orderId)
			.queryParam("origClientOrderId", origClientOrderId)
			.queryParam("recvWindow", recvWindow)
			.queryParam("timestamp", timestamp);

		String signature = Signer.createSignature(apiKey, secretKey, target.getUri().getQuery());
		URI uri = target.queryParam("signature", signature).getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("X-MBX-APIKEY", apiKey)
            .DELETE()
            .build();

		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());		

		if(response.statusCode() != 200)
		{
			ResponseStatus responseStatus = ResponseStatus.from(response.body());
			throw new BinanceException(responseStatus.getCode() + " : " + responseStatus.getMsg());
		}

		String jsonString = response.body();
		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.readValue(jsonString, Order.class);

		return order;			
	}

}
