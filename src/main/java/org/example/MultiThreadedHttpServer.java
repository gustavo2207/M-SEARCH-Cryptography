package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.concurrent.Executors;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MultiThreadedHttpServer {

    public void run() throws IOException {
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);

        server.createContext("/", new MyHandler());

        server.setExecutor(Executors.newFixedThreadPool(10));

        server.start();
        System.out.println("Servidor iniciado na porta 8080...");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();

            if (requestMethod.equalsIgnoreCase("POST")) {
                handlePostRequest(exchange);
            }
        }

        private void handlePostRequest(HttpExchange exchange) throws IOException {
            InputStream requestBody = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
            StringBuilder requestBodyStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBodyStringBuilder.append(line);
            }
            String postRequestBody = requestBodyStringBuilder.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(postRequestBody);

                String endpoint = jsonNode.get("endpoint").asText();

                System.out.println("Endpoint: " + endpoint);

                String result = performRMICryptography(endpoint);

                sendResponse(exchange, result);

            } catch (Exception e) {
                sendErrorResponse(exchange, "Erro ao processar JSON: " + e.getMessage());
            }
        }

        private String performRMICryptography(String name) {
            try {
                Registry registry = LocateRegistry.getRegistry("localhost", 1099);
                CryptoService cryptoService = (CryptoService) registry.lookup("CryptoService");

                CryptoResponse cryptoResponse = cryptoService.encrypt(name);
                if (cryptoResponse.isSuccess())
                    return cryptoResponse.getEncryptedName();
                else
                    return  cryptoResponse.getErrorMessage();
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
                return "Erro na criptografia: " + e.getMessage();
            }
        }

        private void sendResponse(HttpExchange exchange, String response) throws IOException {
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private void sendErrorResponse(HttpExchange exchange, String errorMessage) throws IOException {
            exchange.sendResponseHeaders(500, errorMessage.length());
            OutputStream os = exchange.getResponseBody();
            os.write(errorMessage.getBytes());
            os.close();
        }
    }
}

