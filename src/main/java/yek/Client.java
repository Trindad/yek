package yek;

import java.net.*;
import java.io.*;

class Client implements Runnable {

    private Socket socket;

    public Client(Socket connection) {
        this.socket = connection;
    }

    public void run() {

        //Make sure to close
        try {
          BufferedReader in = new BufferedReader(new
          InputStreamReader(socket.getInputStream()));

          while (!in.ready()) {}
          String m = in.readLine(); // Read one line and output it
          System.out.println(m);
          OutputStream os = this.socket.getOutputStream();
          OutputStreamWriter osw = new OutputStreamWriter(os);
          BufferedWriter bw = new BufferedWriter(osw);
          bw.write("pong");
          bw.flush();
          socket.close();
        }catch(IOException ioe) {
            System.out.println("Error closing client connection");
        }
    }
    }
