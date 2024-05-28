package com.uija.code;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;

public class MainReceiverSocket {
    public static int maxThreads = 3;
    public static int quantActualThreads = 0;

    public static void main(String[] args) {
        LinkedBlockingQueue<byte[]> messageQueue = new LinkedBlockingQueue<>();


        PrtConnection prtConnectionR = new PrtConnection("127.0.0.1", 4002);
        prtConnectionR.startSocket();

        PrtSession prtSession = new PrtSession(prtConnectionR, SessionType.RECEPTOR, "93400101");

        prtSession.estabilishSession();


        while (true) {
            byte[] message = prtSession.getMessage();

            if (quantActualThreads >= maxThreads && message.length != 0) {
                System.out.println("1");
                messageQueue.add(message);
                continue;
            }

            if (quantActualThreads < maxThreads && !messageQueue.isEmpty()) {
                System.out.println("2");
                messageQueue.add(message);
                message = messageQueue.remove();
                System.out.println("------------------------------");
                System.out.println(message);
            }

            if (message.length != 0) {
                ReceiverThread receiver = new ReceiverThread(prtSession, message);
                Thread thread = new Thread(receiver);

                thread.start();

            }

        }
    }
}
