package org.example;

import java.io.IOException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) {
        MulticastServer multicastServer = new MulticastServer();

        Thread multicastThread = new Thread(multicastServer::run);
        multicastThread.start();

        Thread cryptoServiceThread = new Thread(() -> {
            try {
                CryptoServiceImpl cryptoServiceImpl = new CryptoServiceImpl();
                cryptoServiceImpl.run();
            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            }
        });
        cryptoServiceThread.start();

        MultiThreadedHttpServer multiThreadedHttpServer = new MultiThreadedHttpServer();

        Thread httpServerThread = new Thread(() -> {
            try {
                multiThreadedHttpServer.run();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
        httpServerThread.start();


        Client client = new Client();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread clientThread = new Thread(client::run);
        clientThread.start();

    }
}
