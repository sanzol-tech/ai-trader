package exchanges.bybit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import exchanges.utils.IntervalPeriod;

public class BybitApiClient
{
	private static final String BASE_URL = "https://api.bybit.com/public/linear/kline";

	public static List<BybitCandle> getKlines(String symbol, BybitIntervalType interval, int limit) throws Exception
	{
		IntervalPeriod intervalPeriod = IntervalPeriod.create(interval.name(), limit).calc();
		return getKlines(symbol, interval, limit, intervalPeriod.getStartAt());
	}

	public static List<BybitCandle> getKlines(String symbol, BybitIntervalType interval, int limit, long startAt) throws Exception
	{
		URI uri = UriBuilder.fromUri(BASE_URL)
			.queryParam("symbol", symbol)
			.queryParam("interval", interval.getCode())
			.queryParam("limit", limit)
			.queryParam("from", startAt)
			.build();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(uri)
            .build();

		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		if (response.statusCode() != 200)
		{
			throw new Exception(response.body()); 
		}

		String jsonString = response.body();

		ObjectMapper mapper = new ObjectMapper();
		BybitCandlesResponse resp = mapper.readValue(jsonString, BybitCandlesResponse.class);		    

		return resp.getResult();
	}

	public static void main(String[] args) throws Exception
	{
		List<BybitCandle> result = getKlines("BTCUSDT", BybitIntervalType._1d, 5);
		result.forEach(s -> System.out.println(s));		
	}

}
