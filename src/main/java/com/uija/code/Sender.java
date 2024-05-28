package com.uija.code;

import com.uija.code.enums.AppMessageType;
import com.uija.code.enums.AppRecuType;

import java.util.Random;
import java.util.Scanner;

public class Sender {
    public static Scanner scanner = new Scanner(System.in);



    public static void main(String[] args) {
        PrtConnection prtConnectionE;

        while(true) {
            System.out.println("WELCOME TO PRT EMISSOR");
            System.out.print("IP: ");
            String ip = scanner.next();

            System.out.print("PORT: ");
            Integer port = scanner.nextInt();

            prtConnectionE = new PrtConnection(ip, port);
            prtConnectionE.startSocket();

            if (prtConnectionE.isPrtConnectionOn()) {
                System.out.println("PRT SOCKET CONNECTION ESTABLISHED!");
                break;
            }
        }

        String prtSession;
        PrtSession prtSessionObject;

        while (true) {
            System.out.print("PRT SESSION: ");
            prtSession = scanner.next();

            short sessionType = 3;
            SessionType prtSessionType;

            while (sessionType != 1 && sessionType != 2) {
                System.out.print("PRT SESSION TYPE\n1- RECEPTOR\n2- EMISSOR\n:");
                sessionType = scanner.nextShort();
            }

            switch (sessionType) {
                case 1:
                    prtSessionType = SessionType.RECEPTOR;
                    break;
                case 2:
                    prtSessionType = SessionType.EMISSOR;
                    break;
                default:
                    throw new RuntimeException("Not valid Session Type");
            }

            prtSessionObject = new PrtSession(prtConnectionE, prtSessionType,prtSession);

            prtSessionObject.estabilishSession();

            if (prtSessionObject.isSessionEstablished) {
                break;
            }
        }

        while (true) {
            System.out.println("WELCOME YOU'RE READY TO SEND ANY MESSAGE FROM PRT");
            System.out.print("Message (Host: "+prtConnectionE.ip()+" Port: " + prtConnectionE.port() +",Session: "+prtSession+"): ");

            String mensagem = "";
            mensagem = (new Random()).nextInt(100000) + "dsmddmkds" + ((new Random()).nextInt(100000) % 2 == 0 ? "DSJJDSN" : "MMMMMM");


            byte[] messageResponse = prtSessionObject.sendMessage(AppMessageType.PED_MESSAGE, AppRecuType.NO_RECOVER_ACK, 15, mensagem);

            if (messageResponse.length != 0) {
                byte[] ackPedResp = new byte[messageResponse.length];

                System.arraycopy(messageResponse, 0, ackPedResp, 0, ackPedResp.length);


                byte[] message = prtSessionObject.getMessage();

                System.out.println(message);
            }

        }

    }
}
