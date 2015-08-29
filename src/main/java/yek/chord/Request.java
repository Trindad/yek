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

	public static NodeInfo findSuccessor(NodeInfo n, BigInteger id)
	{
		String message = "successor "+id.toString();

		String answer = make(n,message);

		String[] p = answer.split(" ");//retorna ip e id

		NodeInfo node = new NodeInfo(new BigInteger(p[1]), p[0]);

		return node;
	}

	public static NodeInfo predecessor(NodeInfo s)
	{
		String message = "predecessor";

		String answer = make(s,message);

		String[] p = answer.split(" ");

		NodeInfo node = new NodeInfo(new BigInteger(p[1]), p[0]);

		return node;
	}

	public static void notify(NodeInfo s, NodeInfo i)
	{
		String message = "notify "+i.ip + " "+i.id.toString();

		make(s,message);
	}
}