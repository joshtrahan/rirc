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

public class Message {
    private String user;
    private String type;
    private String channel;
    private String message;

    public Message(String rawMsg){
        String[] tokens = rawMsg.split(" ", 4);

        if (tokens[0].equals("PING")){
            type = "PING";
        }
        else {
            user = tokens[0].substring(tokens[0].indexOf(':') + 1, tokens[0].indexOf('!'));
            type = tokens[1];
            channel = tokens[2].replace("#", "");
            message = tokens[3].substring(1);
        }
    }

    public String getUser() {
        return this.user;
    }

    public String getType() {
        return this.type;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getMessage() {
        return this.message;
    }
}
