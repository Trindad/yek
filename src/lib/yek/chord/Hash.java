/**
 * Copyright 2015 Silvana Trindade and Maurício André Cinelli
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
 */
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

	    return new BigInteger(1,d.digest());
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