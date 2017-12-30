package com.robut.rirc;

import java.util.ArrayList;

public class ServerConnection {
    private String url;
    private int port;
    private ArrayList<String> channels = new ArrayList<>();
    private String nick;
    private String auth;

    ServerConnection(String serverURL, int serverPort, String userNick, String userAuth){
        this.url = serverURL;
        this.port = serverPort;
        this.nick = userNick;
        this.auth = userAuth;
    }

    ServerConnection(String serverURL, int serverPort, String userNick, String userAuth, ArrayList<String> autoChannels){
        this(serverURL, serverPort, userNick, userAuth);
        this.channels.addAll(autoChannels);
    }
}
