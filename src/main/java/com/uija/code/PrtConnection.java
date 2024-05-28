package com.uija.code;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class PrtConnection {
    private String host;
    private int port;

    private Socket socket;
    private boolean isPrtConnectionOn = false;

    public PrtConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startSocket() {
        if (isPrtConnectionOn) {
            System.out.println("PRT Connection already ON!");
            return;
        }

        try {
            this.socket = new Socket(host, port);

            this.isPrtConnectionOn = true;
            System.out.println("Prt Connection Started!\nHost: " + host + "\nPort: " + port);
        } catch (IOException e) {
            this.socket = null;

            this.isPrtConnectionOn = false;
        }
    }

    public boolean isPrtConnectionOn() {
        return isPrtConnectionOn;
    }

    public char[] sendMessage(String message) throws IOException, InterruptedException {
        if (!isPrtConnectionOn) {
            System.out.println("Must start PRT Connection!");
            return null;
        }

        byte[] encodedMesage = message.getBytes(StandardCharsets.UTF_8);
        int length = encodedMesage.length + 2;
        byte[] completeMessage = new byte[length];
        completeMessage[0] = (byte) (length / 256);
        completeMessage[1] = (byte) (length % 256);

        System.arraycopy(encodedMesage, 0, completeMessage, 2, length - 2);

        socket.getOutputStream().write(completeMessage, 0, length);

        InputStream inputStream = socket.getInputStream();

        int available = inputStream.available();

        if (available != 0) {
            byte firstByte = (byte) inputStream.read();
            byte secondByte = (byte) inputStream.read();

            int size = (firstByte << 8) + (secondByte << 0) - 2;

            if (size == 0) {
                return new char[]{};
            }
            char[] responseMessage = new char[size];

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            in.read(responseMessage, 0, size);

            return responseMessage;
        }
        return new char[]{};


    }

    public byte[] sendMessage(byte[] message) throws IOException, InterruptedException {
        if (!isPrtConnectionOn) {
            System.out.println("Must start PRT Connection!");
            return null;
        }


        socket.getOutputStream().write(message, 0, message.length);

        InputStream inputStream = socket.getInputStream();

        int available = inputStream.available();

        if (available != 0) {
            byte firstByte = (byte) inputStream.read();
            byte secondByte = (byte) inputStream.read();

            int size = (firstByte << 8) + (secondByte << 0) - 2;

            if (size == 0) {
                return new byte[]{};
            }
            byte[] responseMessage = inputStream.readNBytes(size);



            return responseMessage;
        }
        return new byte[]{};


    }

    public byte[] getMessage() throws IOException {
        InputStream inputStream = socket.getInputStream();

        int available = inputStream.available();

        if (available != 0) {
            byte firstByte = (byte) inputStream.read();
            byte secondByte = (byte) inputStream.read();

            int size = (firstByte << 8) + (secondByte << 0) - 2;


            if (size <= 0 ) {
                return new byte[]{};
            }
            byte[] responseMessage = socket.getInputStream().readNBytes(size);


            return responseMessage;
        }
        return new byte[]{};

    }

    public void close() throws IOException {
        this.socket.setSoTimeout(1000);
        this.socket.close();

        this.isPrtConnectionOn = false;
    }

    public String ip() {
        return this.host;
    }

    public Integer port() {
        return this.port;
    }

    public byte[] getMessage(int timeoutInMilliSeconds) throws IOException {
        InputStream inputStream = socket.getInputStream();

        int available = inputStream.available();

        if (available != 0) {
            byte firstByte = (byte) inputStream.read();
            byte secondByte = (byte) inputStream.read();

            int size = (firstByte << 8) + (secondByte << 0) - 2;


            if (size == 0) {
                return new byte[]{};
            }
            byte[] responseMessage = new byte[size];

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            for (int i = 0; i < size; i++) {
                responseMessage[i] = (byte) in.read();
            }

            return responseMessage;
        }
        return new byte[]{};
    }
}
