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

  /**
   * Inicia serviços
   */
  public void run() {
    //Make sure to close
    try {
      BufferedReader in = new BufferedReader(new
      InputStreamReader(socket.getInputStream()));

      String m = in.readLine(); // Read one line and output it
      // System.out.println("response: " + m);
      String[] parts = m.split(" ");
      // System.out.println("'" + parts[0] + "'");
      String response = "";

      switch (parts[0]) {
        case "findSuccessor":
          response = this.findSuccessor(new BigInteger(parts[1]));
          break;
        case "predecessor":
          response = this.predecessor();
          break;
        case "successor":
          response = this.successor();
          break;
        case "areyounew":
          response = this.amINew();
          break;
        case "notnewanymore":
          response = this.notNewAnymore();
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
        case "saver":
          response = this.saveReplica(new BigInteger(parts[1]), parts[2], Integer.parseInt(parts[3]), parts[4], parts[5]);
          break;
        case "remover":
          response = this.removeReplica(parts[1]);
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

  /**
   * Encontra sucessor através do id recursivamente
   */
  private String findSuccessor(BigInteger id)
  {
    NodeInfo n = this.node.findSuccessor(id);

    return n.id.toString() + " " + n.ip + " " + n.port;
  }

  /**
   * Retorna predecessor que está armazenado na posição '0' da lista
   */
  private String predecessor()
  {
    NodeInfo n = this.node.routingTable.predecessorList[0];

    if (n == null)
    {
      return "";
    }

    return n.id.toString() + " " + n.ip + " " + n.port;
  }

  /**
   * Retorna sucessor que está armazenado na posição '0' da lista
   */
  private String successor()
  {
    NodeInfo n = this.node.routingTable.successorList[0];

    if (n == null)
    {
      return "";
    }

    return n.id.toString() + " " + n.ip + " " + n.port;
  }

  /**
   * Chama notify do nó, quando um nó informa outro nó que é predecessor dele
   */
  private String notify(BigInteger id, String ip, int port)
  {
    NodeInfo n = new NodeInfo(id,ip,port);

    this.node.notify(n);

    return "";
  }

  private String amINew()
  {
    return this.node.iAmNew ? "yes" : "no";
  }

  private String notNewAnymore()
  {
    this.node.iAmNew = false;
    return "";
  }

  /**
   * Insere no nó a chave e o dado
   */
  private String store(String keyBase64, String dataBase64)
  {
    String key = Hash.base64Decode(keyBase64);
    String data = Hash.base64Decode(dataBase64);

    this.node.put(key,data);

    return "";
  }

  /**
   * Atualiza dado
   */
  private String update(String keyBase64, String dataBase64)
  {
    String key = Hash.base64Decode(keyBase64);
    String data = Hash.base64Decode(dataBase64);

    this.node.update(key,data);

    return "";
  }

  /**
   * Deleta um dado através da chave
   */
  private String delete(String keyBase64)
  {
    String key = Hash.base64Decode(keyBase64);

    this.node.delete(key);

    return "";
  }

  /**
   * Retorna uma chave 
   */
  private String get(String keyBase64)
  {
    String key = Hash.base64Decode(keyBase64);

    return Hash.base64(this.node.get(key));
  }

  /**
   * Salva um réplica, fazendo a conversão
   */
  private String saveReplica(BigInteger id, String ip, int port, String keyBase64, String dataBase64)
  {
    String key = Hash.base64Decode(keyBase64);
    String data = Hash.base64Decode(dataBase64);
    NodeInfo n = new NodeInfo(id,ip,port);

    this.node.saveReplica(n, key,data);

    return "";
  }

  /**
   * Remove uma réplica buscando pela chave
   */
  private String removeReplica(String keyBase64)
  {
    String key = Hash.base64Decode(keyBase64);

    this.node.removeReplica(key);

    return "";
  }


}
