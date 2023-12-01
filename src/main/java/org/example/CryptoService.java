package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CryptoService extends Remote {
    CryptoResponse encrypt(String name) throws RemoteException;
}
