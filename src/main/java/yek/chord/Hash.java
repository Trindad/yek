package yek.chord;

import java.security.*;
import java.math.BigInteger;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class Hash {
	
	public BigInteger sha1(String str) throws NoSuchAlgorithmException
	{
		MessageDigest d = null;
	    d = MessageDigest.getInstance("SHA-1");
	    d.reset();
	    d.update(str.getBytes());

	    return new BigInteger(d.digest());
	}

	public static String base64(String str)
	{
        final byte[] authBytes = str.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(authBytes);
	}

	public static String base64Decode(String token) 
	{ 
		byte[] decodedBytes = Base64.getDecoder().decode(token.getBytes()); 
		return new String(decodedBytes, StandardCharsets.UTF_8);
	}
}