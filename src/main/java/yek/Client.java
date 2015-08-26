package yek;

import java.net.*;
import java.io.*;

class Client implements Runnable {

    private Socket socket;

    public Client(Socket connection) {
        this.socket = connection;
    }

    public void run() {

        //Do your logic here. You have the `socket` available to read/write data.

        //Make sure to close
        try {
            socket.close();
        }catch(IOException ioe) {
            System.out.println("Error closing client connection");
        }
    }        
    }