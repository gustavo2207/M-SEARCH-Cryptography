package org.example;
import java.io.*;
import java.net.*;

public class Client {
    private static final String MULTICAST_GROUP_ADDRESS = "230.0.0.1";
    private static final int PORT = 9876;

    public void run() {
        try {
            sendMulticastRequest();

            String responseMessage = receiveMulticastResponse();

            if (responseMessage != null && !responseMessage.isEmpty()) {
                System.out.println("Api Recebida: " + responseMessage);
                sendHttpPostRequest(responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMulticastRequest() throws IOException {
        InetAddress group = InetAddress.getByName(MULTICAST_GROUP_ADDRESS);
        MulticastSocket multicastSocket = new MulticastSocket();

        String searchMessage = "M-SEARCH * HTTP/1.1\r\n" +
                "Host: " + MULTICAST_GROUP_ADDRESS + ":" + PORT + "\r\n" +
                "Man: \"ssdp:discover\"\r\n" +
                "ST: example:service\r\n" +
                "MX: 1\r\n\r\n";

        byte[] searchData = searchMessage.getBytes();

        DatagramPacket searchPacket = new DatagramPacket(searchData, searchData.length, group, PORT);
        multicastSocket.send(searchPacket);

        System.out.println("Solicitação M-SEARCH enviada via Multicast.");

        multicastSocket.close();
    }

    private String receiveMulticastResponse() throws IOException {
        InetAddress group = InetAddress.getByName(MULTICAST_GROUP_ADDRESS);
        MulticastSocket multicastSocket = new MulticastSocket(PORT);
        multicastSocket.joinGroup(group);

        byte[] responseBuffer = new byte[1024];
        DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
        multicastSocket.receive(responsePacket);

        String responseMessage = new String(responsePacket.getData(), 0, responsePacket.getLength());
        System.out.println("Resposta recebida via Multicast: " + responseMessage);

        multicastSocket.leaveGroup(group);
        multicastSocket.close();

        return responseMessage;
    }

    private void sendHttpPostRequest(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String requestBody = "{\"endpoint\": \"" + endpoint + "\"}";

        try (OutputStream os = connection.getOutputStream()) {
            try (OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")) {
                osw.write(requestBody);
                osw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            System.out.println("Resposta da requisição POST: " + response.toString());
        } finally {
            connection.disconnect();
        }
    }
}
