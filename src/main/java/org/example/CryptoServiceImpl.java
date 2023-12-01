package org.example;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.Base64;

public class CryptoServiceImpl extends UnicastRemoteObject implements CryptoService {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    protected CryptoServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public CryptoResponse encrypt(String data) throws RemoteException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
            keyPairGenerator.initialize(2048, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA256AndMGF1Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedData = cipher.doFinal(data.getBytes());

            String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedData);

            return new CryptoResponse(true, encryptedBase64, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new CryptoResponse(false, null, "Erro ao criptografar os dados.");
        }
    }

    public void run() {
        try {
            CryptoService cryptoService = new CryptoServiceImpl();

            String serviceName = "CryptoService";

            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind(serviceName, cryptoService);

            System.out.println("Serviço RMI está ativo: " + serviceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


