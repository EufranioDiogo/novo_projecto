package com.uija.code;

public class Message extends MessageBuilder {
    private byte[] message;

    public Message(byte[] message) {
        this.message = message;
    }

    public byte[] message() {
        return this.message;
    }
}
