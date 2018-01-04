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

import com.robut.rirc.IRCConnection;
import com.robut.rirc.Message;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) {
        String userName = "";
        String auth = "";
        ArrayList<String> channelList = new ArrayList<>();
        channelList.add("twitchpresents");

        try {
            BufferedReader credFile = new BufferedReader(new FileReader("resources/creds.txt"));
            userName = credFile.readLine();
            auth = credFile.readLine();
        }
        catch (Exception e){
            System.err.printf("Exception: %s%n", e);
            return;
        }

        IRCConnection conn = new IRCConnection("irc.chat.twitch.tv", 6667, userName, auth);
        try {
            conn.connect();
            conn.joinChannel("twitchpresents");
        }
        catch (IOException e){
            System.err.printf("Exception connecting: %s%n");
            return;
        }

        while (true){
            try{
                Message msg = conn.getMessage();
                printMsg(msg);
            }
            catch (Exception e){
                System.err.printf("Exception getting message: %s%n", e);
                break;
            }
        }
    }

    public static void tryMsg(String str) throws Exception{
        Message msg = new Message(str);
        printMsg(msg);

        if (msg.getOp().equalsIgnoreCase("PING")) {
            System.out.println("PONG " + msg.getMessage());
        }
    }

    public static void printMsg(Message msg){
        System.out.printf("Oper: %s%n", msg.getOp());
        System.out.printf("User: %s%n", msg.getUser());
        System.out.printf("Chan: %s%n", msg.getChannel());
        System.out.printf("Mesg: %s%n%n", msg.getMessage());
    }
}
