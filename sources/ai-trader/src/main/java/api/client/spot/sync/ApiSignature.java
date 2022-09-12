package api.client.spot.sync;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class ApiSignature
{
	private static final String signatureMethodValue = "HmacSHA256";

	public static String createSignature(String apiKey, String secretKey, String payload) throws NoSuchAlgorithmException, InvalidKeyException
	{
		Mac hmacSha256 = Mac.getInstance(signatureMethodValue);
		SecretKeySpec secKey = new SecretKeySpec(secretKey.getBytes(), signatureMethodValue);
		hmacSha256.init(secKey);
		return new String(Hex.encodeHex(hmacSha256.doFinal(payload.getBytes())));
	}

}
