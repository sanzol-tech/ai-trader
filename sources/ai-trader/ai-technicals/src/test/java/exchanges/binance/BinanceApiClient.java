package exchanges.binance;

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

import com.fasterxml.jackson.databind.ObjectMapper;

public class BinanceApiClient
{
	private static final String BASE_URL = "https://api.binance.com";
	
	public static List<BinanceCandle> getKlines(String symbol, BinanceIntervalType interval, int limit) throws Exception
	{
		final String path = "/api/v3/klines";

		Client client = ClientBuilder.newClient();

		WebTarget target = client
			.target(BASE_URL)
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
			throw new Exception(response.body());
		}

		String jsonString = response.body();
		ObjectMapper mapper = new ObjectMapper();
		String[][] lst = mapper.readValue(jsonString, String[][].class);		    

		// Create list of Kline
		List<BinanceCandle> lstResult = new ArrayList<BinanceCandle>();
		for (String[] entry : lst)
		{
			BinanceCandle kline = new BinanceCandle();

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

	public static Depth getDepth(String symbol, int limit) throws Exception
	{
		final String path = "/api/v3/depth";

		Client client = ClientBuilder.newClient();

		WebTarget target = client
			.target(BASE_URL)
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
			throw new Exception(response.body());
		}

		String jsonString = response.body();
		ObjectMapper mapper = new ObjectMapper();
		Depth depth = mapper.readValue(jsonString, Depth.class);

		return depth;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<BinanceCandle> result = getKlines("BTCUSDT", BinanceIntervalType._1h, 5);
		result.forEach(s -> System.out.println(s));
	}

}
