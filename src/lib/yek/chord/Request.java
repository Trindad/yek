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

import java.math.BigInteger;
import java.net.*;
import java.io.*;

public class Request {
	public static String _make(NodeInfo n, String message) throws Exception {
		// System.out.println(message);
		Socket socket = new Socket(n.ip, n.port);
		OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(message + "\n");
        bw.flush();

		BufferedReader in = new BufferedReader(new
		InputStreamReader(socket.getInputStream()));

		while (!in.ready()) {}
		String m = in.readLine(); // Read one line and output it
		// System.out.println(m);
		in.close();
		return m;
	}

	public static String make(NodeInfo n, String message)
	{
		try
		{
			return _make(n, message);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static NodeInfo findSuccessor(NodeInfo n, BigInteger id)
	{
		String message = "findSuccessor "+id.toString();

		String answer = make(n,message);

		String[] p = answer.split(" ");//retorna ip e id

		NodeInfo node = new NodeInfo(new BigInteger(p[0]), p[1], Integer.parseInt(p[2]));

		return node;
	}

	public static NodeInfo predecessor(NodeInfo s)
	{
		String message = "predecessor";

		String answer = make(s,message);

		String[] p = answer.split(" ");

		if (p.length < 2)
		{
			return null;
		}

		//                                          ID      IP           PORT
		NodeInfo node = new NodeInfo(new BigInteger(p[0]), p[1], Integer.parseInt(p[2]));

		return node;
	}

	public static NodeInfo successor(NodeInfo s)
	{
		String message = "successor";

		String answer = make(s,message);

		String[] p = answer.split(" ");

		if (p.length < 2)
		{
			return null;
		}

		//                                          ID      IP           PORT
		NodeInfo node = new NodeInfo(new BigInteger(p[0]), p[1], Integer.parseInt(p[2]));

		return node;
	}

	public static void notify(NodeInfo s, NodeInfo i)
	{
		String message = "notify " + i.id.toString() + " " + i.ip + " " + i.port;

		make(s,message);
	}

	public static void store(NodeInfo s, String key, String data)
	{

		String message = "store " + Hash.base64(key) + " " + Hash.base64(data);

		make(s,message);
	}

	public static void update(NodeInfo s, String key, String data)
	{

		String message = "update " + Hash.base64(key) + " " + Hash.base64(data);

		make(s,message);
	}

	public static void delete(NodeInfo s, String key)
	{

		String message = "delete " + Hash.base64(key);

		make(s,message);
	}

	public static String get(NodeInfo s, String key)
	{

		String message = "get " + Hash.base64(key);

		return Hash.base64Decode( make(s,message) );
	}

	public static void saveReplica(NodeInfo s, NodeInfo i, String data, String key)
	{

		String message = "saver " + i.id.toString() + " " + i.ip + " " + i.port + " " + Hash.base64(key) + " " + Hash.base64(data);

		make(s,message);
	}

	public static void removeReplica(NodeInfo s, String key)
	{

		String message = "remover " + Hash.base64(key);

		make(s,message);
	}
}
