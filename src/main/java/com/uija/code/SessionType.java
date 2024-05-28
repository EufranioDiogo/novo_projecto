package com.uija.code;

public enum SessionType {
    EMISSOR("sibs.deswin.prt.PrtClientReqGateway"),
    RECEPTOR("sibs.deswin.prt.PrtSrvGateway");

    private String sibsSessionClass;
    SessionType(String sibsSessionClass) {
        this.sibsSessionClass = sibsSessionClass;
    }

    @Override
    public String toString() {
        return this.sibsSessionClass;
    }
}
