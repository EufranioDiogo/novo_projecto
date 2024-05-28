package com.uija.code;

import java.io.IOException;

public class Receiver {
    public static void main(String[] args) throws InterruptedException {
        PrtConnection prtConnectionR = new PrtConnection("127.0.0.1", 4002);
        prtConnectionR.startSocket();

        PrtSession prtSessionR = new PrtSession(prtConnectionR, SessionType.RECEPTOR, "93400101");

        prtSessionR.estabilishSession();



    }
}
