package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MulticastServer {
    private static final String MULTICAST_GROUP_ADDRESS = "230.0.0.1";
    private static final int PORT = 9876;

    public void run() {
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_GROUP_ADDRESS);
            MulticastSocket socket = new MulticastSocket(PORT);

            socket.joinGroup(group);

            System.out.println("Servidor Multicast iniciado. Aguardando mensagens...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Mensagem recebida: " + message);

                if (message.contains("M-SEARCH")) {
                    System.out.println("Solicitação M-SEARCH detectada. Responder...");

                    String apiEndpoint = "http://127.0.0.1:8080/";
                    String responseMessage = "API Endpoint: " + apiEndpoint;

                    byte[] responseBuffer = responseMessage.getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length, group, PORT);
                    socket.send(responsePacket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

