package api.client.spot.sync;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import api.client.config.ApiConstants;
import api.client.exception.ApiException;
import api.client.model.sync.Depth;
import api.client.model.sync.ExchangeInfo;
import api.client.model.sync.SymbolTicker;
import api.client.util.CustomClient;
import sanzol.app.config.PrivateConfig;

public class SyncSpotClient
{
	private static final int MAX_DEPTH_LIMIT = 5000;

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
		final String path = "/api/v3/ticker/24hr";

		Client client = CustomClient.getClient();
		Response response = client
				.target(ApiConstants.SPOT_BASE_URL)
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

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException, InvalidKeyException
	{
		PrivateConfig.loadKey();

		ExchangeInfo ei = getExchangeInformation();
		System.out.println(ei.getSymbols().size());

		//Depth depth = getDepth("BTCUSDT", null);
		//System.out.println(depth.getLastUpdateId());
	}

}
