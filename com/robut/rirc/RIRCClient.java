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

    public RIRCClient(String serverURL, int serverPort, String userNick, String userAuth,
                      PrivMsgHandler msgHandler){
        conn = new IRCConnection(serverURL, serverPort, userNick, userAuth, msgHandler);
    }

    public RIRCClient(String serverURL, int serverPort, String userNick, String userAuth,
                      Collection channels, PrivMsgHandler msgHandler){
        conn = new IRCConnection(serverURL, serverPort, userNick, userAuth, channels, msgHandler);
    }

    public void start(){
        Thread connThread = new Thread(conn);
        connThread.setDaemon(false);
        connThread.start();
    }

    public PrivMsg getMessage() throws InterruptedException, RIRCException{
        return conn.getMessage();
    }
}
