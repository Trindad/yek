package yek.chord;

import java.math.BigInteger;
import java.net.*;
import java.io.*;

public class Request {
	public static String make(NodeInfo n, String message)
	{
		try
		{
			System.out.println(message);
			Socket socket = new Socket(n.ip, 9345);
			OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(message + "\n");
            bw.flush();
			
			BufferedReader in = new BufferedReader(new
			InputStreamReader(socket.getInputStream()));
			
			while (!in.ready()) {}
			String m = in.readLine(); // Read one line and output it
			System.out.println(m);
			in.close();
			return m;
		}
		catch(Exception e) {}

		return null;
	}

	public static NodeInfo find_successor(NodeInfo n, BigInteger id)
	{
		return null;
	}
}