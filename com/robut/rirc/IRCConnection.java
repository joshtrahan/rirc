package com.robut.rirc;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class IRCConnection {
    private String serverAddress;
    private int port;
    private ArrayList<String> channels = new ArrayList<>();
    private String nick;
    private String auth;

    private Socket sock;
    private BufferedReader sockIn;
    private DataOutputStream sockOut;

    IRCConnection(String serverURL, int serverPort, String userNick, String userAuth){
        this.serverAddress = serverURL;
        this.port = serverPort;
        this.nick = userNick;
        this.auth = userAuth;
    }

    IRCConnection(String serverURL, int serverPort, String userNick, String userAuth, ArrayList<String> autoChannels){
        this(serverURL, serverPort, userNick, userAuth);
        this.channels.addAll(autoChannels);
    }

    public void connect() throws IOException {
        this.sock = new Socket(this.serverAddress, this.port);
        this.sockIn = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        this.sockOut = new DataOutputStream(this.sock.getOutputStream());
    }

    public String getMessage(){
        return "";
    }
}
