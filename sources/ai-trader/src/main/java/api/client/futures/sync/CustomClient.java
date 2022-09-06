package api.client.futures.sync;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class CustomClient
{
	public static Client getClient() throws KeyManagementException, NoSuchAlgorithmException
	{
		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(null, new TrustManager[] { new X509TrustManager()
		{
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
			{
				//
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
			{
				//
			}

			@Override
			public X509Certificate[] getAcceptedIssuers()
			{
				return new X509Certificate[0];
			}

		} }, new java.security.SecureRandom());

		//return ClientBuilder.newClient();
		//return ClientBuilder.newClient().register(new Authenticator(API_USER, API_PASSWORD));
		return ClientBuilder
				.newBuilder()
				.sslContext(sslcontext)
				.build();
	}

}
