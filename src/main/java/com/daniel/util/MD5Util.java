package com.daniel.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5Util {

    private static final String DEFAULT_SALT = "bookshop@2026";

    private MD5Util() {
        throw new IllegalStateException("Utility class");
    }

    public static String encrypt(String password) {
        return encrypt(password, DEFAULT_SALT);
    }

    public static String encrypt(String password, String salt) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("password can not be blank");
        }
        String actualSalt = salt == null ? "" : salt;
        return md5Hex(actualSalt + password);
    }

    public static boolean matches(String rawPassword, String salt, String encryptedPassword) {
        if (encryptedPassword == null || encryptedPassword.trim().isEmpty()) {
            return false;
        }
        return encrypt(rawPassword, salt).equalsIgnoreCase(encryptedPassword);
    }

    private static String md5Hex(String source) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(source.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(32);
            for (byte currentByte : bytes) {
                builder.append(String.format("%02x", currentByte & 0xff));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm is unavailable", e);
        }
    }
}
