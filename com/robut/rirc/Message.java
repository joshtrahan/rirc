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
    private String user = "";
    private String op = "";
    private String channel = "";
    private String message = "";

    public Message(String rawMsg) throws RIRCException {
        String[] tokens = rawMsg.split(" ", 2);
        String prefix = "";
        String[] args;

        if (tokens[0].charAt(0) == ':'){
            prefix = tokens[0].substring(1);
            tokens = tokens[1].split(" ", 2);
        }

        this.op = tokens[0];
        args = tokens[1].split(" ", 2);

        if (this.op.equalsIgnoreCase("PRIVMSG")){
            if (prefix == ""){
                throw new RIRCException("PRIVMSG received with no prefix.");
            }
            this.user = prefix.substring(0, prefix.indexOf('!'));
            this.channel = args[0].substring(1);
            this.message = args[1].substring(1);
        }

        if (this.op.equalsIgnoreCase("PING")){
            this.message = args[0];
        }
    }

    public String getUser() {
        return this.user;
    }

    public String getOp() {
        return this.op;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getMessage() {
        return this.message;
    }
}
