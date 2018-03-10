package com.robut.rirc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientWriter {
    private Socket sock;
    private DataOutputStream sockOut;

    public ClientWriter(Socket newSock, DataOutputStream newSockOut){
        this.sock = newSock;
        this.sockOut = newSockOut;
    }

    public synchronized void writeMessage(String msg) throws IOException{
        if (msg.contains("\r\n")){
            throw new IOException("Error: PrivMsg contains newline characters.");
        }
        this.sockOut.write((msg + "\r\n").getBytes("UTF-8"));
    }
}
