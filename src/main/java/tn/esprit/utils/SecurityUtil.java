package tn.esprit.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    public static boolean verifyPassword(String inputPassword, String storedHashedPassword) {
        try {
            String hashedInput = hashPassword(inputPassword);
            return MessageDigest.isEqual(
                hashedInput.getBytes("UTF-8"),
                storedHashedPassword.getBytes("UTF-8")
            );
        } catch (Exception e) {
            throw new RuntimeException("Error verifying password", e);
        }
    }
} 