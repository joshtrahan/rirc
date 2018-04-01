/*
    RIRC - My personal IRC library
    Copyright (C) 2017  Joshua Trahan

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.robut.rirc;

import javax.management.OperationsException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class IRCClient implements Runnable{
    private String serverAddress;
    private int port;

    private boolean tryReconnect = false;
    private int reconnecTimeMillis = 5000;

    private ArrayList<String> channels = new ArrayList<>();
    private ArrayList<String> channelsToJoin = new ArrayList<>();
    private String nick;
    private String auth;

    private boolean channelsJoinable = false;

    private PrivMsgHandler privMsgHandler;

    private Socket sock;
    private ClientReader clientIn;
    private ClientWriter clientOut;

    public IRCClient(String serverURL, int serverPort, String userNick, String userAuth,
                     PrivMsgHandler msgHandler){
        this.serverAddress = serverURL;
        this.port = serverPort;
        this.nick = userNick;
        this.auth = userAuth;
        this.privMsgHandler = msgHandler;
    }

    public synchronized void connect() throws IOException {
        this.sock = new Socket(this.serverAddress, this.port);
        BufferedReader sockIn = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        DataOutputStream sockOut = new DataOutputStream(this.sock.getOutputStream());

        this.clientIn = new ClientReader(this.sock, sockIn);
        this.clientOut = new ClientWriter(this.sock, sockOut);

        System.out.printf("Sending auth: %s%n", this.auth);
        writeMessage("PASS " + this.auth);
        System.out.printf("Sending nick: %s%n", this.nick);
        writeMessage("NICK " + this.nick);

        this.tryReconnect = true;

        System.out.printf("Finished connecting.%n");
    }

    public void startThread(){
        Thread connThread = new Thread(this);
        connThread.setDaemon(false);
        connThread.start();
    }

    public void run() {
        try{
            this.connect();
        }
        catch (IOException e){
            System.err.printf("Error connecting: %s%n", e);
        }

        while (this.tryReconnect) {
            if (isConnected()) {
                try {
                    getServerMessage();
                } catch (Exception e) {
                    System.err.printf("Exception getting message: %s%n", e);
                    e.printStackTrace();
                }
            }
            else {
                try {
                    tryReconnect();
                } catch (InterruptedException e){
                    System.err.printf("Error waiting for reconnect: %s%n", e);
                    System.err.printf("Aborting.%n");
                    e.printStackTrace();
                    this.tryReconnect = false;
                }
            }
        }
    }

    public void disconnect() throws IOException {
        this.tryReconnect = false;
        this.sock.close();
    }

    public synchronized void joinChannel(String channelName) throws IOException{
        if (this.channelsJoinable) {
            System.out.printf("Joining channel: %s%n", channelName);

            if (!channels.contains(channelName)) {
                channels.add(channelName);
                writeMessage("JOIN #" + channelName);
            }
        }
        else{
            this.channelsToJoin.add(channelName);
        }
    }

     public void sendPrivMsg(String channel, String message) throws IOException, RIRCException {
        if (!channels.contains(channel)){
            throw new RIRCException("A channel should be joined before a message can be sent to it.");
        }
        writeMessage("PRIVMSG #" + channel + " :" + message);
    }

    private void tryReconnect() throws InterruptedException{
        this.channelsToJoin.addAll(this.channels);
        this.channels.clear();
        this.channelsJoinable = false;

        System.err.printf("Trying reconnect in %d seconds.%n", this.reconnecTimeMillis / 1000);
        Thread.sleep(this.reconnecTimeMillis);

        try {
            connect();
        } catch (IOException e){
            System.err.printf("Still can't reconnect.%n");
        }
    }

    private void getServerMessage() throws IOException{
        Message msg = getMessage();

        System.out.printf("%s%n", msg);

        switch (msg.getOp()){
            case "PRIVMSG":
                handlePrivMsg(msg);
                break;

            case "PING":
                handlePing(msg);
                break;

            case "376":
                handleNowJoinable();
                break;
        }
    }

    private void handlePing(Message msg) throws IOException{
        String pongMsg = "PONG " + msg.getArgs();
        System.out.printf("Received ping. Responding with pong: %s%n%n", pongMsg);
        writeMessage(pongMsg);
    }

    private void handlePrivMsg(Message msg){
        try {
            this.privMsgHandler.handleNewMessage(new PrivMsg(msg));
        }
        catch (RIRCException e){
            System.err.printf("Error handling PrivMsg: %s%n");
        }
    }

    private synchronized void handleNowJoinable() throws IOException{
        System.out.printf("Channels should now be joinable.%n");
        this.channelsJoinable = true;
        for (String channel : channelsToJoin){
            joinChannel(channel);
        }
        channelsToJoin.clear();
    }

    private Message getMessage() throws IOException {
        return this.clientIn.getMessage();
    }

    private void writeMessage(String msg) throws IOException {
        this.clientOut.writeMessage(msg);
    }

    public boolean isConnected(){
        if (this.sock == null){
            return false;
        }
        else {
            return this.sock.isConnected();
        }
    }

    public boolean isChannelJoined(String channel){
        return this.channels.contains(channel);
    }

    public String[] getChannelsJoined(){
        return this.channels.toArray(new String[this.channels.size()]);
    }

    public String getServerAddress(){
        return this.serverAddress;
    }

    public int getPort(){
        return this.port;
    }

    public String getNick(){
        return this.nick;
    }

    public PrivMsgHandler getPrivMsgHandler(){
        return this.privMsgHandler;
    }

}
