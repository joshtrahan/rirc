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

import com.robut.rirc.IRCClient;
import com.robut.rirc.PrivMsg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class Driver {
    public static void main(String[] args) {
        if (args.length > 0) {
            String server = args[0];
            int port = Integer.parseInt(args[1]);
            String[] channels = new String[0];

            String userName;
            String auth;

            if (args.length > 2) {
                channels = Arrays.copyOfRange(args, 2, args.length);
            }

            try (BufferedReader credFile = new BufferedReader(new FileReader("resources/creds.txt"))) {
                userName = credFile.readLine();
                auth = credFile.readLine();
                credFile.close();
            } catch (Exception e) {
                System.err.printf("Exception reading credentials: %s%n", e);
                return;
            }

            try {
                testAsync(server, port, userName, auth, Arrays.asList(channels));
                //testSynchronous(server, port, userName, auth, Arrays.asList(channels));
            } catch (IOException e) {
                System.err.printf("Exception connecting to server: %s%n", e);
            }
        }
    }

    public static void testAsync(String server, int port, String userName, String auth, Collection channels)
    throws IOException {
        if (server == null) {
            throw new IOException("No server specified.");
        }

        TestMsgHandler msgHandler = new TestMsgHandler();

        IRCClient conn = new IRCClient(server, port, userName, auth, channels, msgHandler);
        conn.start();
    }

    public static void testSynchronous(String server, int port, String userName, String auth, Collection channels)
    throws IOException
    {
        if (server == null) {
            throw new IOException("No server specified.");
        }

        IRCClient conn = new IRCClient(server, port, userName, auth, channels);

        try {
            conn.start();
        }
        catch (Exception e){
            System.err.printf("Exception connecting: %s%n");
            return;
        }

        try{
            while (true) {
                PrivMsg msg = conn.getMessage();
                System.out.printf("%s%n", msg);
            }
        }
        catch (Exception e){
            System.err.printf("Exception getting message: %s%n", e);
        }
    }
}
