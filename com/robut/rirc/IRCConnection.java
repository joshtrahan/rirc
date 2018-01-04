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
            System.out.printf("Joining channel %s%n", channel);
            joinChannel(channel);
        }
    }

    public void joinChannel(String channelName) throws IOException{
        if (!channels.contains(channelName)){
            channels.add(channelName);
        }
        if (this.sock.isConnected()) {
            writeMessage("JOIN #" + channelName);
        }
    }

    public Message getMessage() throws IOException, RIRCException{
        if (sock == null || !sock.isConnected()){
            throw new IOException("Socket isn't connected to a server. Call connect() method first.");
        }

        String rawMsg = sockIn.readLine();
        Message msg = new Message(rawMsg);
        while (!msg.getOp().equalsIgnoreCase("PRIVMSG")){
            System.out.printf("Message: %s%n%n", rawMsg);
            msg = new Message(sockIn.readLine());
        }

        return msg;
    }

    public void writeMessage(String msg) throws IOException {
        if (msg.contains("\r\n")){
            throw new IOException("Error: Message contains newline characters.");
        }
        this.sockOut.write((msg + "\r\n").getBytes("UTF-8"));
    }
}
