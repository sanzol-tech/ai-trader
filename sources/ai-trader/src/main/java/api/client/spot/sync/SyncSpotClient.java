package api.client.spot.sync;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import api.client.config.ApiConstants;
import api.client.exception.ApiException;
import api.client.futures.enums.IntervalType;
import api.client.model.sync.Depth;
import api.client.model.sync.ExchangeInfo;
import api.client.model.sync.Kline;
import api.client.model.sync.SymbolTicker;
import api.client.util.CustomClient;
import sanzol.app.config.PrivateConfig;

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

	public static Depth getDepth(String symbol, Integer limit) throws KeyManagementException, NoSuchAlgorithmException
	{
		final String path = "/api/v3/depth";

		if (limit == null) limit = MAX_DEPTH_LIMIT;
		
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

	// ...
	
	// ------------------------------------------------------------------------

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException, InvalidKeyException
	{
		PrivateConfig.loadKey();

		ExchangeInfo ei = getExchangeInformation();
		System.out.println(ei.getSymbols().size());

		//Depth depth = getDepth("BTCUSDT", null);
		//System.out.println(depth.getLastUpdateId());
	}

}
