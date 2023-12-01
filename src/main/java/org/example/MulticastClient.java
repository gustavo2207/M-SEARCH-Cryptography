package org.example;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastClient {
    private static final String MULTICAST_GROUP_ADDRESS = "230.0.0.1";
    private static final int PORT = 9876;

    public void run() {
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_GROUP_ADDRESS);
            DatagramSocket socket = new DatagramSocket();

            String searchMessage = "M-SEARCH * HTTP/1.1\r\n" +
                    "Host: " + MULTICAST_GROUP_ADDRESS + ":" + PORT + "\r\n" +
                    "Man: \"ssdp:discover\"\r\n" +
                    "ST: example:service\r\n" +
                    "MX: 1\r\n\r\n";

            byte[] searchData = searchMessage.getBytes();

            DatagramPacket searchPacket = new DatagramPacket(searchData, searchData.length, group, PORT);
            socket.send(searchPacket);

            System.out.println("Solicitação M-SEARCH enviada.");

            byte[] responseBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);

            String responseMessage = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Resposta recebida: " + responseMessage);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
