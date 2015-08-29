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
      
      String[] parts = m.split(" ");
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
      }

      OutputStream os = this.socket.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os);
      BufferedWriter bw = new BufferedWriter(osw);
      bw.write(response);
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

    return n.ip + " " + n.id.toString();
  }

  private String notify(String ip, BigInteger id)
  {
    NodeInfo n = new NodeInfo(id,ip);

    this.node.notify(n);

    return "";
  }

}
