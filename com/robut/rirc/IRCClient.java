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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

public class IRCClient implements Runnable{
    private String serverAddress;
    private int port;
    private ArrayList<String> channels = new ArrayList<>();
    private String nick;
    private String auth;

    private LinkedBlockingQueue<PrivMsg> privMsgQueue;
    private PrivMsgHandler privMsgHandler;

    private Socket sock;
    private BufferedReader sockIn;
    private DataOutputStream sockOut;

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
        this.sockIn = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        this.sockOut = new DataOutputStream(this.sock.getOutputStream());

        System.out.printf("Sending auth: %s%n", this.auth);
        writeMessage("PASS " + this.auth);
        System.out.printf("Sending nick: %s%n", this.nick);
        writeMessage("NICK " + this.nick);

        System.out.printf("Finished connecting.%n");
    }

    public void startThread(){
        Thread connThread = new Thread(this);
        connThread.setDaemon(false);
        connThread.start();
    }

    public void run() {
        try {
            connect();
        } catch (IOException e) {
            System.err.printf("Exception connecting to server: %s%n", e);
        }

        while (isConnected()) {
            try {
                getPrivMsgFromServer();
            } catch (Exception e) {
                System.err.printf("Exception getting privmsg: %s%n", e);
            }
        }
    }

    public synchronized PrivMsg getMessage() throws InterruptedException, RIRCException{
        if (this.privMsgHandler != null){
            throw new RIRCException("PrivMsgs are already being retrieved by a handler class.");
        }
        return privMsgQueue.take();
    }

    public synchronized boolean isConnected(){
        return this.sock.isConnected();
    }

    public synchronized void disconnect() throws IOException {
        this.sock.close();
    }

    public void joinChannel(String channelName) throws IOException{
        if (!channels.contains(channelName)){
            channels.add(channelName);
        }
        if (this.sock.isConnected()) {
            writeMessage("JOIN #" + channelName);
        }
    }

    public synchronized boolean isChannelJoined(String channel){
        return this.channels.contains(channel);
    }

    public synchronized String[] getChannelsJoined(){
        return this.channels.toArray(new String[this.channels.size()]);
    }

    public synchronized String getServerAddress(){
        return this.serverAddress;
    }

    public int getPort(){
        return this.port;
    }

    public String getNick(){
        return this.nick;
    }

    public synchronized PrivMsgHandler getPrivMsgHandler(){
        return this.privMsgHandler;
    }

    public synchronized void getPrivMsgFromServer() throws IOException, RIRCException{
        if (sock == null || !sock.isConnected()){
            throw new IOException("Socket isn't connected to a server. Call connect() method first.");
        }

        Message msg = new Message(sockIn.readLine());
        while (!msg.getOp().equalsIgnoreCase("PRIVMSG")){
            System.out.printf("%s%n%n", msg.toString());
            handleNonPrivMsg(msg);
            msg = new Message(sockIn.readLine());
        }

        if (this.privMsgHandler != null){
            this.privMsgHandler.handleNewMessage(new PrivMsg(msg));
        }
        else {
            System.err.printf("Error: No PrivMsgHandler present. Adding to message queue.%n");
            this.privMsgQueue.add(new PrivMsg(msg));
        }
    }

    public synchronized void sendPrivMsg(String channel, String message) throws IOException, RIRCException {
        if (!channels.contains(channel)){
            throw new RIRCException("A channel should be joined before a message can be sent to it.");
        }
        writeMessage("PRIVMSG #" + channel + " :" + message);
    }

    private synchronized void handleNonPrivMsg(Message msg) throws IOException{
        switch (msg.getOp()){
            case "PING":
                handlePing(msg);
                break;
        }
    }

    private synchronized void handlePing(Message msg) throws IOException{
        String pongMsg = "PONG " + msg.getArgs();
        System.out.printf("Received ping. Responding with pong: %s%n%n", pongMsg);
        writeMessage(pongMsg);
    }

    private void writeMessage(String msg) throws IOException {
        if (msg.contains("\r\n")){
            throw new IOException("Error: PrivMsg contains newline characters.");
        }
        this.sockOut.write((msg + "\r\n").getBytes("UTF-8"));
    }
}
