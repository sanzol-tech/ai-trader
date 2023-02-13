package exchanges.kucoin;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import exchanges.utils.IntervalPeriod;

public class KucoinApiClient
{
	private static final String BASE_URL = "https://api.kucoin.com";

	public static List<KucoinCandle> getKlines(String symbol, KucoinIntervalType interval, int limit) throws Exception
	{
		IntervalPeriod intervalPeriod = IntervalPeriod.create(interval.name(), limit).calc();
		return getKlines(symbol, interval, intervalPeriod.getStartAt(), intervalPeriod.getEndAt());
	}

	public static List<KucoinCandle> getKlines(String symbol, KucoinIntervalType interval, long startAt, long endAt) throws Exception
	{
		URI uri = UriBuilder.fromUri(BASE_URL + "/api/v1/market/candles")
			.queryParam("symbol", symbol)
			.queryParam("type", interval.getCode())
			.queryParam("startAt", startAt)
			.queryParam("endAt", endAt)
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
		KucoinCandlesResponse resp = mapper.readValue(jsonString, KucoinCandlesResponse.class);		    

		// Create list of Kline
		List<KucoinCandle> lstResult = new ArrayList<KucoinCandle>();
		for (String[] entry : resp.getData())
		{
			KucoinCandle kline = new KucoinCandle();

			kline.setOpenTime(Long.valueOf(entry[0]));
			kline.setOpen(new BigDecimal(entry[1]));
			kline.setClose(new BigDecimal(entry[2]));
			kline.setHigh(new BigDecimal(entry[3]));
			kline.setLow(new BigDecimal(entry[4]));
			kline.setVolume(new BigDecimal(entry[5]));

			lstResult.add(0, kline);
		}

		return lstResult;			
	}

	public static void main(String[] args) throws Exception
	{
		List<KucoinCandle> result = getKlines("BTC-USDT", KucoinIntervalType._1d, 5);
		result.forEach(s -> System.out.println(s));		
	}

}
