package com.aecb.util;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;

public class AESEncryption {

    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static String decryptedString;
    private static String encryptedString;

    public static void setKeyOld(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            Timber.d("Exception : " + e.toString());
        }
    }

    public static void setKeyNew(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            Timber.d("Exception : " + e.toString());
        }
    }

    public static String getDecryptedString() {
        return decryptedString;
    }

    private static void setDecryptedString(String decryptedString) {
        AESEncryption.decryptedString = decryptedString;
    }

    public static String getEncryptedString() {
        return encryptedString;
    }

    private static void setEncryptedString(String encryptedString) {
        AESEncryption.encryptedString = encryptedString;
    }

    public static String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            setEncryptedString(Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)), 0));
        } catch (Exception e) {
            Timber.d("Error :" + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            setDecryptedString(new String(cipher.doFinal(Base64.decode(strToDecrypt, 0))));
        } catch (Exception e) {
            Timber.d("Error :" + e.toString());
        }
        return null;
    }

}