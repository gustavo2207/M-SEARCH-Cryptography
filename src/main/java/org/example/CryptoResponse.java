package org.example;

import java.io.Serializable;

public class CryptoResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String encryptedName;
    private String errorMessage;

    public CryptoResponse(boolean success, String encryptedName, String errorMessage) {
        this.success = success;
        this.encryptedName = encryptedName;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getEncryptedName() {
        return encryptedName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
