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
          String ip = parts[2];
          int port = Integer.parseInt(parts[3]);
          BigInteger id = new BigInteger(parts[1]);

          response = this.notify(id, ip, port);
          break;
        case "store":
          response = this.store(parts[1], parts[2]);
          break;
        case "update":
          response = this.update(parts[1], parts[2]);
          break;
        case "delete":
          response = this.delete(parts[1]);
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

    return n.id.toString() + " " + n.ip + " " + n.port;
  }

  private String predecessor()
  {
    NodeInfo n = this.node.routingTable.predecessor;

    if (n == null)
    {
      return "";
    }

    return n.id.toString() + " " + n.ip + " " + n.port;
  }

  private String notify(BigInteger id, String ip, int port)
  {
    NodeInfo n = new NodeInfo(id,ip,port);

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

  private String update(String keyBase64, String dataBase64)
  {
    String key = Hash.base64Decode(keyBase64);
    String data = Hash.base64Decode(dataBase64);

    this.node.update(key,data);

    return "";
  }

  private String delete(String keyBase64)
  {
    String key = Hash.base64Decode(keyBase64);

    this.node.delete(key);

    return "";
  }

   private String get(String keyBase64)
  {
    String key = Hash.base64Decode(keyBase64);

    return Hash.base64(this.node.get(key));
  }


}
