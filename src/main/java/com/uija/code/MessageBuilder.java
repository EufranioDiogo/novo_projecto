package com.uija.code;

import com.uija.code.enums.AppMessageType;
import com.uija.code.enums.AppRecuType;

import java.lang.reflect.Member;


public class MessageBuilder {
    private byte[] message = null;
    private byte messageType = -1;
    private byte[] seqPed = null;

    private byte recupartionType = -1;
    private byte[] timeOut = null;
    private byte[] payload = null;

    private byte[] sizeArray = null;

    private int size = 6 + 2; // size of app header + size of lenght indicator




    public MessageBuilder setMessageType(AppMessageType messageType) {
        this.messageType = (messageType.value());

        return this;
    }

    public MessageBuilder setMessageType(AppRecuType recuType) {
        this.recupartionType = (recuType.value());

        return this;
    }

    public MessageBuilder setSeqPed(int seq) {
        byte signativeByte = (byte) (seq / 256);
        byte lessSignativeByte = (byte) (seq % 256);

        this.seqPed = new byte[] {signativeByte, lessSignativeByte};
        return this;
    }

    public MessageBuilder setTimeout(int seconds) {
        byte signativeByte = (byte) (seconds / 256);
        byte lessSignativeByte = (byte) (seconds % 256);

        this.timeOut = new byte[] {signativeByte, lessSignativeByte};
        return this;
    }

    public MessageBuilder setPayload(byte[] payload) {
        this.payload = payload;

        return this;
    }

    public Message build() {
        this.computeSize();
        this.verifyFields();

        int quantBytes = this.size;

        message = new byte[quantBytes];

        System.arraycopy(sizeArray, 0, message, 0, 2);

        message[2] = this.messageType;

        System.arraycopy(seqPed, 0, message, 3, 2);

        message[5] = this.recupartionType;

        System.arraycopy(timeOut, 0, message, 6, 2);

        System.arraycopy(payload, 0, message, 8, payload.length);

        return new Message(message);
    }

    private void verifyFields() {
        if (this.messageType == -1 || this.seqPed == null || this.recupartionType == -1 || this.timeOut == null) {
            throw new IllegalArgumentException("You must provibe the obrigatory params: MESSAGE_TYPE, SEQ_PEDIDO, RECUPERATION_TYPE, TIME_OUT");
        }
    }

    private void computeSize() {
        this.size += this.payload.length;

        byte signativeByte = (byte) (this.size / 256);
        byte lessSignativeByte = (byte) (this.size % 256);

        sizeArray = new byte[2];

        sizeArray[0] = signativeByte;
        sizeArray[1] = lessSignativeByte;
    }

}
