package yek;
import yek.chord.*;
import java.net.InetAddress;
import java.util.*;
import java.math.BigInteger;

public class Server {

	public void start()
	{
		try
		{
			InetAddress addr = InetAddress.getLocalHost();
			
			String ip = addr.getHostAddress();
			System.out.println(ip);

			Hash h = new Hash();
			BigInteger b = h.sha1(ip);
		    System.out.println( b.toString() );

			Node node = new Node(ip,b);
		}
		catch(Exception e){}
	}
}