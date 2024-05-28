package com.uija.code.enums;

public enum AppMessageType {
    PED_MESSAGE((byte) 0x01);

    private byte value;

    AppMessageType(byte messageType) {
        this.value = messageType;
    }

    public byte value() {
        return this.value;
    }

}
