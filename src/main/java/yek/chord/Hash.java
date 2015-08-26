package yek.chord;

import java.security.*;
import java.math.BigInteger;

public class Hash {
	
	public BigInteger sha1(String str) throws NoSuchAlgorithmException
	{
		MessageDigest d = null;
	    d = MessageDigest.getInstance("SHA-1");
	    d.reset();
	    d.update(str.getBytes());

	    return new BigInteger(d.digest());
	}
}