package yek;
import yek.chord.*;
import java.net.InetAddress;
import java.util.*;
import java.math.BigInteger;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class Server implements Runnable {
	public ServerSocket server;
	private ExecutorService executorService = Executors.newFixedThreadPool(20);

	private NodeInfo []initialServers;

	public Server()
	{
		initialServers = new NodeInfo[4];
		String []ips = {"192.168.0.107","192.168.0.108"};
		Hash h = new Hash();

		for (int i = 0; i < ips.length; i++) {
			try
			{
				BigInteger b = h.sha1(ips[i]);
				initialServers[i] = new NodeInfo(b, ips[i]);
			}
			catch(Exception e){}
		}
	}

	public Node initNode()
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

			return node;
		}
		catch(Exception e){}

		return null;
	}

	private void connectToInitialServers()
	{

	}

	private void initSocketServer()
	{
		try {
			ServerSocket server = new ServerSocket(9345);

			System.out.println("Listening on port 9345");

			while (true) {
				Socket s = server.accept();
				System.out.println("New connection!");
				executorService.submit(new Client(s));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run()
	{
		initSocketServer();
	}

	public void start()
	{
		Node node = initNode();
		(new Thread(this)).start();
		// connectToInitialServers();
	}
}
