package com.daniel.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class MD5Util {

    private static final String SALT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(SALT_CHARS.charAt(random.nextInt(SALT_CHARS.length())));
        }
        return sb.toString();
    }

    public static String encrypt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((password + salt).getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String str = Integer.toHexString(b & 0xFF);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkPassword(String inputPassword, String salt, String encryptedPassword) {
        String encrypted = encrypt(inputPassword, salt);
        return encrypted.equals(encryptedPassword);
    }
}
