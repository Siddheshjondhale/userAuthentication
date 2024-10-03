package com.example.userAuthentication.hashing;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;

public class HashUtil {

    // Method to hash and truncate the input value
    public static String hash(String value, int length) {
        try {
            // Create a SHA-256 digest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Compute the hash
            byte[] hash = digest.digest(value.getBytes());

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0'); // Append leading zero if needed
                hexString.append(hex);
            }

            // Return truncated hash
            return hexString.toString().substring(0, Math.min(length, hexString.length()));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

