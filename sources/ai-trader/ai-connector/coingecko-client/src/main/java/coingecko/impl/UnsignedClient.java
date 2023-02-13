package coingecko.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import coingecko.commons.CoingeckoException;
import coingecko.commons.ResponseStatus;
import coingecko.model.CoinMarket;

public class UnsignedClient
{

	public static List<CoinMarket> getMarkets(String order, int limit) throws Exception
	{
		URI uri = UriBuilder.fromUri(coingecko.config.ApiConstants.BASE_URL + "/api/v3/coins/markets")
			.queryParam("vs_currency", "usd")
			.queryParam("order", order)
			.queryParam("per_page", limit)
			.queryParam("page", 1)
			.queryParam("sparkline", false)
			.build();

		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(uri)
            .build();

		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		if(response.statusCode() != 200)
		{
			ResponseStatus responseStatus = ResponseStatus.from(response.body());
			throw new CoingeckoException(responseStatus.getCode() + " : " + responseStatus.getMsg());
		}

		String jsonString = response.body();
		ObjectMapper mapper = new ObjectMapper();
		List<CoinMarket> lst = mapper.readValue(jsonString, new TypeReference<List<CoinMarket>>(){});		    

		return lst;
	}

}
