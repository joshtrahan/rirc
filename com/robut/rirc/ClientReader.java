package com.robut.rirc;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ClientReader {
    private Socket sock;
    private BufferedReader sockIn;

    public ClientReader(Socket newSock, BufferedReader newSockIn){
        this.sock = newSock;
        this.sockIn = newSockIn;
    }

    public Message getMessage() throws IOException{
        if (sock == null || !sock.isConnected()){
            throw new IOException("Socket isn't connected to a server. Call connect() method first.");
        }

        return new Message(sockIn.readLine());
    }
}
