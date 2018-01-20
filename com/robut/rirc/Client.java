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

import java.util.Collection;

public class Client {
    private Connection conn;

    public Client(String serverURL, int serverPort, String userNick, String userAuth){
        conn = new Connection(serverURL, serverPort, userNick, userAuth);
    }

    public Client(String serverURL, int serverPort, String userNick, String userAuth,
                  Collection channels){
        conn = new Connection(serverURL, serverPort, userNick, userAuth, channels);
    }

    public Client(String serverURL, int serverPort, String userNick, String userAuth,
                  PrivMsgHandler msgHandler){
        conn = new Connection(serverURL, serverPort, userNick, userAuth, msgHandler);
    }

    public Client(String serverURL, int serverPort, String userNick, String userAuth,
                  Collection channels, PrivMsgHandler msgHandler){
        conn = new Connection(serverURL, serverPort, userNick, userAuth, channels, msgHandler);
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
