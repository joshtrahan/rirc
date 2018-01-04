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

public class PrivMsg {
    private String user = "";
    private String channel = "";
    private String message = "";

    PrivMsg(Message msg) throws RIRCException{
        this(msg.getPrefix(), msg.getArgs());
    }

    PrivMsg(String prefix, String argString) throws RIRCException {
        if (prefix == ""){
            throw new RIRCException("PRIVMSG received with no prefix.");
        }
        if (argString == ""){
            throw new RIRCException("PRIVMSG received with no arg string.");
        }

        String[] args = argString.split(" ", 2);
        setUser(prefix.substring(1, prefix.indexOf('!')));
        setChannel(args[0].substring(1));

        if (args.length > 1) {
            setMessage(args[1].substring(1));
        }
        else{
            throw new RIRCException("PRIVMSG receieved with no message.");
        }
    }

    public PrivMsg(String usr, String chan, String msg){
        this.user = usr;
        this.channel = chan;
        this.message = msg;
    }

    private void setUser(String user) {
        this.user = user;
    }

    private void setChannel(String channel) {
        this.channel = channel;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return this.user;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "PrivMsg{\n" +
                "\tuser: " + user + '\n' +
                "\tchannel: " + channel + '\n' +
                "\tmessage: " + message + '\n' +
                "}\n";
    }
}
