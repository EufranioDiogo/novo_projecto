package com.uija.code.enums;

public enum AppRecuType {
    NO_RECOVER_MSG((byte) 0x00),
    RECOVER_MSG((byte) 0x01),
    NO_RECOVER_ACK((byte) 0x02),
    RECOVER_MSG_ACK((byte) 0x03);

    private byte value;

    AppRecuType(byte messageType) {
        this.value = messageType;
    }

    public byte value() {
        return this.value;
    }
}
