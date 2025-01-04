package com.example.userAuthentication.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {

    // Method to generate a random salt
    public static String generateSalt() {
        byte[] salt = new byte[16]; // Create a byte array for salt
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt); // Fill the byte array with random bytes
        return Base64.getEncoder().encodeToString(salt); // Return as a Base64 string
    }

    // Method to hash the input value with salt and truncate the result
    public static String hash(String value, int length) {
        try {
            // Combine the salt with the input value
            String saltedValue = value + generateSalt();

            // Create a SHA-256 digest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Compute the hash
            byte[] hash = digest.digest(saltedValue.getBytes());

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
