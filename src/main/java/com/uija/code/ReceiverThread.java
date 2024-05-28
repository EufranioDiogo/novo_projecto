package com.uija.code;

import java.nio.charset.StandardCharsets;

public class ReceiverThread implements Runnable{

    private PrtSession prtSession;
    private byte[] message;

    private int threadId = 0;
    private long timestamp = System.currentTimeMillis();

    public ReceiverThread(PrtSession prtSession, byte[] message) {
        this.prtSession = prtSession;
        this.message = message;
        MainReceiverSocket.quantActualThreads += 1;
        threadId = MainReceiverSocket.quantActualThreads;

        System.out.println("THREAD " + threadId + " Started!");
    }

    @Override
    public void run() {
        if (message.length != 0 && (message.length - 12) > 0) {
            int headerPRTSize = 12;
            byte[] payload = new byte[message.length - headerPRTSize];
            byte[] header = new byte[headerPRTSize];

            int headerIndex = 0;
            int payloadIndex = 12;

            System.arraycopy(message, 0, header, 0, header.length);

            System.arraycopy(message, payloadIndex, payload, 0, message.length - headerPRTSize);

            String responsePayload = "";

            for (byte element: payload) {
                char character = (char) element;

                responsePayload += character;
                System.out.print(character);
            }

            String upper = responsePayload.toUpperCase() + " RSP";

            int responseSize = upper.length() + headerPRTSize + 2;

            byte[] response = new byte[responseSize];

            response[0] = (byte) (responseSize / 256);
            response[1] = (byte) (responseSize % 256);


            System.arraycopy(header, 0, response, 2, header.length);

            System.arraycopy(upper.getBytes(StandardCharsets.US_ASCII), 0, response, header.length + 2, upper.length());

            byte[] bytes = prtSession.sendMessage(response);

            System.out.println("\n..SENDING MESSAGE..");
        } else {
            System.out.println("NO MESSAGE TO PROCESS");
        }

        System.out.println("THREAD " + threadId + " Finished!");
        System.out.println("Time: " + (System.currentTimeMillis() - timestamp) + " ms");
        MainReceiverSocket.quantActualThreads -= 1;

        Thread.currentThread().interrupt();
    }
}
