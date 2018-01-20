package com.robut.rirc;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

public class RIRCClient {
    private IRCConnection conn;

    public RIRCClient(String serverURL, int serverPort, String userNick, String userAuth){
        conn = new IRCConnection(serverURL, serverPort, userNick, userAuth);
    }

    public RIRCClient(String serverURL, int serverPort, String userNick, String userAuth,
                      Collection channels){
        conn = new IRCConnection(serverURL, serverPort, userNick, userAuth, channels);
    }

    public void start(){
        (new Thread(conn)).start();
    }

    public PrivMsg getMessage() throws InterruptedException{
        return conn.getMessage();
    }
}
