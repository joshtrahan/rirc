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

public class IRCConnection {
    private String serverAddress;
    private int port;
    private ArrayList<String> channels = new ArrayList<>();
    private String nick;
    private String auth;

    private Socket sock;
    private BufferedReader sockIn;
    private DataOutputStream sockOut;

    public IRCConnection(String serverURL, int serverPort, String userNick, String userAuth){
        this.serverAddress = serverURL;
        this.port = serverPort;
        this.nick = userNick;
        this.auth = userAuth;
    }

    public IRCConnection(String serverURL, int serverPort, String userNick, String userAuth, ArrayList<String> autoChannels){
        this(serverURL, serverPort, userNick, userAuth);
        this.channels.addAll(autoChannels);
    }

    public void connect() throws IOException {
        this.sock = new Socket(this.serverAddress, this.port);
        this.sockIn = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        this.sockOut = new DataOutputStream(this.sock.getOutputStream());

        System.out.printf("Sending auth: %s%n", this.auth);
        writeMessage("PASS " + this.auth);
        System.out.printf("Sending nick: %s%n", this.nick);
        writeMessage("NICK " + this.nick);

        System.out.printf("Finished connecting.%n");

        for (String channel : channels){
            System.out.printf("Joining channel #%s%n", channel);
            joinChannel(channel);
        }
    }

    public void disconnect() throws IOException {
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

    public PrivMsg getMessage() throws IOException, RIRCException{
        if (sock == null || !sock.isConnected()){
            throw new IOException("Socket isn't connected to a server. Call connect() method first.");
        }

        Message msg = new Message(sockIn.readLine());
        while (!msg.getOp().equalsIgnoreCase("PRIVMSG")){
            System.out.printf("op: %s%nprefix: %s%nargs: %s%n%n", msg.getOp(), msg.getPrefix(), msg.getArgs());
            handleNonPrivMsg(msg);
            msg = new Message(sockIn.readLine());
        }

        return new PrivMsg(msg);
    }

    public void sendPrivMsg(String channel, String message) throws IOException, RIRCException {
        if (!channels.contains(channel)){
            throw new RIRCException("A channel should be joined before a message can be sent to it.");
        }
        writeMessage("PRIVMSG #" + channel + " :" + message);
    }

    private void handleNonPrivMsg(Message msg) throws IOException{
        switch (msg.getOp()){
            case "PING":
                handlePing(msg);
                break;
        }
    }

    private void handlePing(Message msg) throws IOException{
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
