package yek;

import java.math.BigInteger;
import java.net.*;
import java.io.*;
import yek.chord.*;

class Response implements Runnable {

  private Socket socket;
  private Node node;

  public Response(Socket connection, Node node) {
    this.socket = connection;
    this.node = node;
  }

  public void run() {
    //Make sure to close
    try {
      BufferedReader in = new BufferedReader(new
      InputStreamReader(socket.getInputStream()));

      while (!in.ready()) {}
      String m = in.readLine(); // Read one line and output it
      System.out.println("response: " + m);
      String[] parts = m.split(" ");
      System.out.println("'" + parts[0] + "'");
      String response = "";

      switch (parts[0]) {
        case "successor":
          response = this.successor(new BigInteger(parts[1]));
          break;
        case "predecessor":
          response = this.predecessor();
          break;
        case "notify":
          String ip = parts[1];
          BigInteger id = new BigInteger(parts[2]);

          response = this.notify(ip, id);
          break;
        case "store":
          response = this.store(parts[1], parts[2]);
          break;
        case "get":
          response = this.get(parts[1]);
          break;
        default:
          break;
      }

      OutputStream os = this.socket.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os);
      BufferedWriter bw = new BufferedWriter(osw);
      bw.write(response+"\n");
      bw.flush();
      socket.close();
    }catch(IOException ioe) {
      System.out.println("Error closing client connection");
    }
  }

  private String successor(BigInteger id)
  {
    NodeInfo n = this.node.findSuccessor(id);

    return n.ip + " " + n.id.toString();
  }

  private String predecessor()
  {
    NodeInfo n = this.node.routingTable.predecessor;

    if (n == null) 
    {
      return "";
    }

    return n.ip + " " + n.id.toString();
  }

  private String notify(String ip, BigInteger id)
  {
    NodeInfo n = new NodeInfo(id,ip);

    this.node.notify(n);

    return "";
  }

  private String store(String keyBase64, String dataBase64)
  {
    String key = Hash.base64Decode(keyBase64);
    String data = Hash.base64Decode(dataBase64);

    this.node.put(key,data);

    return "";
  }

   private String get(String keyBase64)
  {
    String key = Hash.base64Decode(keyBase64);

    return Hash.base64(this.node.get(key));
  }


}
