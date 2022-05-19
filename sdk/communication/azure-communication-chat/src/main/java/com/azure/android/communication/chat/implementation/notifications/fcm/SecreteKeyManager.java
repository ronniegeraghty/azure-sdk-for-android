package com.azure.android.communication.chat.implementation.notifications.fcm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import lombok.Getter;
import lombok.Setter;

public class SecreteKeyManager {
    private static SecreteKeyManager secreteKeyManager;

    private KeyGenerator keyGenerator;

    private KeyStore keyStore;

    @Getter
    @Setter
    private boolean executionFail;

    @Getter
    @Setter
    private long keyRotationTime;

    public final static String curCryptoKeyAlias = "CUR_CRYPTO_KEY";

    public final static String curAuthKeyAlias = "CUR_AUTH_KEY";

    public final static String preCryptoKeyAlias = "PRE_CRYPTO_KEY";

    public final static String preAuthKeyAlias = "PRE_AUTH_KEY";

    public SecreteKeyManager()  {
        try {
            this.keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("KeyGenerator failed: " + e.getMessage());
        }
        this.keyGenerator.init(256);
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to initialize key store", e);
        }
        executionFail = false;
    }

    public static SecreteKeyManager instance() {
        if (secreteKeyManager == null) {
            secreteKeyManager = new SecreteKeyManager();
        }
        return secreteKeyManager;
    }

    private char[] getPassword() {
        return "com.azure.android.communication.chat.android".toCharArray();
    }

    private void loading(String path) {
        //Loading keys if key store file exists
        char[] password = getPassword();

        try (FileInputStream fis = new File(path).exists() ? new FileInputStream(path) : null) {
            keyStore.load(fis, password);
        } catch (IOException | CertificateException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public SecretKey getSecreteKey(String alias) {
        KeyStore.ProtectionParameter protParam =
            new KeyStore.PasswordProtection(getPassword());

        // get my private key
        KeyStore.SecretKeyEntry pkEntry = null;
        try {
            pkEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, protParam);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get secrete key", e);
        }
        if (pkEntry == null) {
            return null;
        }
        SecretKey secretKey = pkEntry.getSecretKey();
        return secretKey;
    }

    private void storingKeyWithAlias(SecretKey secretKey, String alias, String path) {
        if (secretKey == null) {
            return;
        }
        KeyStore.ProtectionParameter protParam =
            new KeyStore.PasswordProtection(getPassword());
        KeyStore.SecretKeyEntry skEntry =
            new KeyStore.SecretKeyEntry(secretKey);
        try {
            keyStore.setEntry(alias, skEntry, protParam);
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to set entry for key store", e);
        }

        // store away the keystore
        File outputFile = new File(path);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                new RuntimeException("Failed to create key store file");
            }
        }
        try (FileOutputStream fos = new FileOutputStream(outputFile, true)) {
            keyStore.store(fos, getPassword());
        } catch (Exception e) {
            throw new RuntimeException("Failed to save secrete key", e);
        }
    }

    public void refreshCredentials(String path) {
        loading(path);
        SecretKey curCryptoKey = getSecreteKey(curCryptoKeyAlias);
        SecretKey curAuthKey = getSecreteKey(curAuthKeyAlias);
        storingKeyWithAlias(curCryptoKey, preCryptoKeyAlias, path);
        storingKeyWithAlias(curAuthKey, preAuthKeyAlias, path);

        SecretKey newCryptoKey = keyGenerator.generateKey();
        SecretKey newAuthKey = keyGenerator.generateKey();
        storingKeyWithAlias(newCryptoKey, curCryptoKeyAlias, path);
        storingKeyWithAlias(newAuthKey, curAuthKeyAlias, path);
        keyRotationTime = System.currentTimeMillis();
    }
}
