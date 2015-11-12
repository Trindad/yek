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
	int port;
	private Node node;
	private ExecutorService executorService = Executors.newCachedThreadPool();

	public Node initNode()
	{
		try
		{
			InetAddress addr = InetAddress.getLocalHost();

			String ip = Server.getIpAddress();
			System.out.println(ip);

			Hash h = new Hash();
			BigInteger b = h.sha1(ip + "/" + this.port);
		    System.out.println( b.toString() );

			Node node = new Node(ip, this.port, b);

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
			System.out.println("Listening on port " + this.port);

			while (true) {
				Socket s = server.accept();
				// System.out.println("New connection!");
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

	public void start(boolean initial, String ip)
	{
		try {
			this.server = new ServerSocket(initial ? 32000 : 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.port = server.getLocalPort();

		this.node = initNode();
		if (initial) {
			this.node.create();
		}

		(new Thread(this)).start();
		(new Thread(new BackgroundWorker(this.node))).start();

		if (!initial) {
			try {
				int port = 32000;
				Hash h = new Hash();
				BigInteger b = h.sha1(ip + "/" + port);
				NodeInfo n = new NodeInfo(b, ip, port);

				this.node.join(n);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
