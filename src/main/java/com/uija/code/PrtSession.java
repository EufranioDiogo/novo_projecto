package com.uija.code;

import com.uija.code.enums.AppMessageType;
import com.uija.code.enums.AppRecuType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PrtSession {
    private PrtConnection prtConnection = null;
    private String sessionID = "";

    private SessionType sessionType;

    int seqPed = 1;

    boolean isSessionEstablished = false;



    public PrtSession(PrtConnection prtConnection, SessionType sessionType, String sessionID) {
        if (prtConnection == null || !prtConnection.isPrtConnectionOn()) {
            throw new RuntimeException("PRT Connection can not be null");
        }

        this.prtConnection = prtConnection;
        this.sessionID = sessionID;
        this.sessionType = sessionType;
    }

    public char[] estabilishSession() {
        String connectionString = "APL=" + sessionType + "\nPRTSES="+this.sessionID;

        try {
            char[] chars = prtConnection.sendMessage(connectionString);

            if (chars.length == 0) {
                System.out.println("Could not establish the session");
                isSessionEstablished = false;
            } else {
                System.out.println("Session Established");
                isSessionEstablished = true;
            }

            for (char element : chars) {
                this.sessionID += element;
                System.out.print(element);
            }
            System.out.println("\n");
            return chars;
        } catch (IOException | InterruptedException e) {
            isSessionEstablished = false;
            throw new RuntimeException(e);
        }
    }

    public void closeSession() {
        this.sessionID = "";
    }

    public byte[] sendMessage(AppMessageType messageType, AppRecuType recuType, int timeOutSec, String message) {
        if (!isSessionEstablished) {
            throw new RuntimeException("Session not established can't send a message");
        }
        MessageBuilder messageBuilder = new MessageBuilder();

        Message mensagem = messageBuilder
                .setMessageType(messageType)
                .setMessageType(recuType)
                .setSeqPed(this.seqPed)
                .setTimeout(timeOutSec)
                .setPayload(message.getBytes(StandardCharsets.UTF_8))
                .build();

        this.seqPed += 1;

        byte[] bytes = this.sendMessage(mensagem);

        return bytes;
    }

    public byte[] getMessage() {
        try {
            return this.prtConnection.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getMessage(int timeoutInMilliSeconds) {
        try {
            return this.prtConnection.getMessage(timeoutInMilliSeconds);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] sendMessage(byte[] message) {

        try {
            byte[] chars = prtConnection.sendMessage(message);


            for (byte element : chars) {
                this.sessionID += (char) element;
                System.out.print((char) element);
            }
            return chars;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private byte[] sendMessage(Message message) {
        try {
            byte[] chars = prtConnection.sendMessage(message.message());

            if (chars.length == 0) {
                System.out.println("Empty response");
            } else {
                System.out.println("Response: ");
            }

            for (byte element : chars) {
                System.out.print(element);
            }
            return chars;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
