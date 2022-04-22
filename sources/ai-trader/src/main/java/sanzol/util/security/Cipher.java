/**
*
* AUTHOR: Fernando Elenberg Sanzol <f@sanzol.com.ar>
* CREATE: Marzo 2020
*
*/
package sanzol.util.security;

import java.util.Base64;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cipher
{
	public enum Codec
	{
		Base64, Hex
	};

	private static final Logger LOG = LoggerFactory.getLogger(Cipher.class);

	private static final byte[] KEY = {
		(byte) 60, (byte) 140, (byte) 190, (byte) 62, (byte) 44, (byte) 149, (byte) 14, (byte) 79,
		(byte) 85, (byte) 107, (byte) 191, (byte) 56, (byte) 38, (byte) 153, (byte) 70, (byte) 196,
		(byte) 138, (byte) 157, (byte) 98, (byte) 39, (byte) 96, (byte) 1, (byte) 211, (byte) 185,
		(byte) 109, (byte) 224, (byte) 139, (byte) 131, (byte) 18, (byte) 253, (byte) 192, (byte) 145
	};

	private static final byte[] SALT = {
		(byte) 243, (byte) 255, (byte) 202, (byte) 47, (byte) 8, (byte) 182, (byte) 8, (byte) 79,
		(byte) 38, (byte) 60, (byte) 115, (byte) 109, (byte) 90, (byte) 234, (byte) 146, (byte) 211
	};

	public static String encrypt(String text)
	{
		return encrypt(text, Codec.Base64);
	}

	public static String encrypt(String text, Codec codec)
	{
		try
		{
			IvParameterSpec iv = new IvParameterSpec(SALT);
			SecretKeySpec skeySpec = new SecretKeySpec(KEY, "AES");

			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/PCBC/pkcs5padding");
			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(text.getBytes());

			if (codec == Codec.Hex)
				return Hex.encodeHexString(encrypted);
			else
				return Base64.getEncoder().withoutPadding().encodeToString(encrypted);
		}
		catch (Exception e)
		{
			LOG.error("encrypt failed", e);
		}
		return null;
	}

	public static String decrypt(String text)
	{
		return decrypt(text, Codec.Base64);
	}

	public static String decrypt(String text, Codec codec)
	{
		try
		{
			IvParameterSpec iv = new IvParameterSpec(SALT);
			SecretKeySpec skeySpec = new SecretKeySpec(KEY, "AES");

			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/PCBC/pkcs5padding");
			cipher.init(javax.crypto.Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] bytes = (codec == Codec.Hex) ? Hex.decodeHex(text) : Base64.getDecoder().decode(text);
			byte[] original = cipher.doFinal(bytes);
			return new String(original);
		}
		catch (Exception e)
		{
			LOG.error("decrypt failed", e);
		}

		return null;
	}

}
