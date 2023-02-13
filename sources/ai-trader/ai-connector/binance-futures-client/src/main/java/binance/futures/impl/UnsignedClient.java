package binance.futures.impl;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import binance.futures.commons.BinanceException;
import binance.futures.commons.ResponseStatus;
import binance.futures.config.ApiConstants;
import binance.futures.enums.IntervalType;
import binance.futures.model.Candle;
import binance.futures.model.Depth;
import binance.futures.model.ExchangeInfo;
import binance.futures.model.FundingRate;
import binance.futures.model.PremiumIndex;
import binance.futures.model.SymbolTicker;

public class UnsignedClient
{

	public static List<Candle> getKlines(String symbol, IntervalType interval, int limit) throws Exception
	{
		final String path = "/fapi/v1/klines";

		Client client = ClientBuilder.newClient();

		WebTarget target = client
			.target(ApiConstants.BASE_URL)
			.path(path)
			.queryParam("symbol", symbol)
			.queryParam("interval", interval.getCode())
			.queryParam("limit", limit);

		URI uri = target.getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
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
		String[][] lst = mapper.readValue(jsonString, String[][].class);		    

		// Create list of Kline
		List<Candle> lstResult = new ArrayList<Candle>();
		for (String[] entry : lst)
		{
			Candle kline = new Candle();

			kline.setOpenTime(Long.valueOf(entry[0]));
			kline.setOpenPrice(new BigDecimal(entry[1]));
			kline.setHighPrice(new BigDecimal(entry[2]));
			kline.setLowPrice(new BigDecimal(entry[3]));
			kline.setClosePrice(new BigDecimal(entry[4]));
			kline.setVolume(new BigDecimal(entry[5]));
			kline.setQuoteVolume(new BigDecimal(entry[7]));
			kline.setCount(Long.valueOf(entry[8]));

			lstResult.add(kline);
		}

		return lstResult;			
	}

	public static ExchangeInfo getExchangeInformation() throws Exception
	{
		final String path = "/fapi/v1/exchangeInfo";

		WebTarget target = ClientBuilder.newClient()
			.target(ApiConstants.BASE_URL)
			.path(path);

		URI uri = target.getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
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
		ExchangeInfo exchangeInfo = mapper.readValue(jsonString, ExchangeInfo.class);

		return exchangeInfo;			
	}

	public static List<SymbolTicker> getSymbolTickers() throws Exception
	{
		final String path = "/fapi/v1/ticker/24hr";

		WebTarget target = ClientBuilder.newClient()
			.target(ApiConstants.BASE_URL)
			.path(path);

		URI uri = target.getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
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
		List<SymbolTicker> lst = mapper.readValue(jsonString, new TypeReference<List<SymbolTicker>>(){});

		return lst;			
	}	

	public static List<FundingRate> getFundingRate(String symbol, Integer limit) throws Exception
	{
		final String path = "/fapi/v1/fundingRate";

		WebTarget target = ClientBuilder.newClient()
			.target(ApiConstants.BASE_URL)
			.path(path)
			.queryParam("symbol", symbol)
			.queryParam("limit", limit);

		URI uri = target.getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
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
		List<FundingRate> lst = mapper.readValue(jsonString, new TypeReference<List<FundingRate>>(){});

		return lst;			
	}

	public static List<PremiumIndex> getPremiumIndex(String symbol) throws Exception
	{
		final String path = "/fapi/v1/premiumIndex";

		WebTarget target = ClientBuilder.newClient()
			.target(ApiConstants.BASE_URL)
			.path(path)
			.queryParam("symbol", symbol);

		URI uri = target.getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
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
		List<PremiumIndex> lst = mapper.readValue(jsonString, new TypeReference<List<PremiumIndex>>(){});

		return lst;			
	}
	
	public static Depth getDepth(String symbol) throws Exception
	{
		return getDepth(symbol, ApiConstants.MAX_DEPTH_LIMIT);
	}
	
	public static Depth getDepth(String symbol, int limit) throws Exception
	{
		final String path = "/fapi/v1/depth";

		Client client = ClientBuilder.newClient();

		WebTarget target = client
			.target(ApiConstants.BASE_URL)
			.path(path)
			.queryParam("symbol", symbol)
			.queryParam("limit", limit);

		URI uri = target.getUri();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
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
		Depth depth = mapper.readValue(jsonString, Depth.class);

		return depth;			
	}	

}
