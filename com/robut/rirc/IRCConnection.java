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

    public void connect() throws IOException{
        this.sock = new Socket(this.serverAddress, this.port);
        this.sockIn = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        this.sockOut = new DataOutputStream(this.sock.getOutputStream());
    }

    private String getMessage() throws IOException{
        if (sock == null || !sock.isConnected()){
            throw new IOException("Socket isn't connected to a server. Call connect() method first.");
        }


        do {
            return sockIn.readLine();
        }
        while (false);
    }
}
