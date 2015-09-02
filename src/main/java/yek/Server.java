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

			String ip = Server.getIpAddress();
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
		this.node.create();

		(new Thread(this)).start();
		(new Thread(new BackgroundWorker(this.node))).start();

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

	public static String getIpAddress() {
		String eth0 = "";
		String wlan0 = "";

    try {
      for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
        NetworkInterface intf = (NetworkInterface) en.nextElement();
        if ("eth0".compareTo(intf.getName()) == 0 || "wlan0".compareTo(intf.getName()) == 0) {
	        Enumeration<InetAddress> inetAddresses = intf.getInetAddresses();
	        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	        		if (!inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress()) {
	        			if (intf.getName() == "eth0") {
	            		eth0 = inetAddress.toString().replace("/", "");
	        			} else {
	        				wlan0 = inetAddress.toString().replace("/", "");
	        			}
	        		}
	        }
	      }
      }
    } catch (SocketException ex) {
      // Log.e("Socket exception in GetIP Address of Utilities", ex.toString());
    }
    return wlan0.length() > 0 ? wlan0 : eth0;
  }
}
