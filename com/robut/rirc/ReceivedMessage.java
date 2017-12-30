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
