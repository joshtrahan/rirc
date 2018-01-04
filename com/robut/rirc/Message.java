package com.robut.rirc;

public class Message {
    private String prefix = "";
    private String op = "";
    private String args = "";

    public Message(String msg){
        String[] tokens = msg.split(" ", 2);
        if (tokens[0].charAt(0) == ':'){
            this.prefix = tokens[0];
            tokens = tokens[1].split(" ", 2);
        }

        this.op = tokens[0];

        if (tokens.length > 1) {
            this.args = tokens[1];
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

    @Override
    public String toString() {
        return "Message{" +
                "prefix='" + prefix + '\'' +
                ", op='" + op + '\'' +
                ", args='" + args + '\'' +
                '}';
    }
}
