package com.buaa.werwertrip.util;

import java.security.SecureRandom;

public class SaltGenerator {
    private static final int SALT_LENGTH = 16; // 盐值长度，以字节为单位

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
