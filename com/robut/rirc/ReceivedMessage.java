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

public class ReceivedMessage {
    private String channel;
    private String sender;
    private String message;

    ReceivedMessage(String msgChannel, String msgSender, String msg){
        setChannel(msgChannel);
        setSender(msgSender);
        setMessage(msg);
    }

    public String getChannel() {
        return this.channel;
    }

    public String getSender() {
        return this.sender;
    }
    public String getMessage() {
        return this.message;
    }

    private void setChannel(String channel) {
        this.channel = channel;
    }

    private void setSender(String sender) {
        this.sender = sender;
    }

    private void setMessage(String message) {
        this.message = message;
    }
}
