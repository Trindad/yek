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
	private Node node;
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
		catch(Exception e){
			e.printStackTrace();
		}

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
				executorService.submit(new Response(s, this.node));
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
		this.node = initNode();
		(new Thread(this)).start();

		// try {
		// 	String ip = "192.168.0.104";
		// 	Hash h = new Hash();
		// 	BigInteger b = h.sha1(ip);
		// 	NodeInfo n = new NodeInfo(b, ip);

		// 	this.node.join(n);

		// 	NodeInfo me = this.node.info;
		// 	Request.notify(this.node.routingTable.successor, me);
		// 	NodeInfo pred = Request.predecessor(this.node.routingTable.successor);
		// 	System.out.println(pred.ip);

		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }

		// connectToInitialServers();
	}
}
