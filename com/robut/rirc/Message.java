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
    private String prefix = "";
    private String op = "";
    private String args = "";

    public Message(String msg){
        String[] tokens = msg.split(" ", 2);
        if (tokens[0].charAt(0) == ':'){
            setPrefix(tokens[0]);
            tokens = tokens[1].split(" ", 2);
        }

        setOp(tokens[0]);

        if (tokens.length > 1) {
            setArgs(tokens[1]);
        }
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getOp() {
        return this.op;
    }

    public String getArgs() {
        return this.args;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setOp(String op) {
        this.op = op.toUpperCase();
    }

    public void setArgs(String args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "Message{" +
                "prefix='" + prefix + '\'' +
                ", op='" + op + '\'' +
                ", args='" + args + '\'' +
                '}';
    }
}
