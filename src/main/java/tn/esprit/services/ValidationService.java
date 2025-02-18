package tn.esprit.services;

import java.util.regex.Pattern;

public class ValidationService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@(.+)$"
    );
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Basic email validation regex
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    public static boolean isValidPassword(String password) {
        // Password must be at least 8 characters long
        return password != null && password.length() >= 8;
    }
    
    public static boolean isValidUsername(String username) {
        // Username must be at least 3 characters long and contain only letters, numbers, and underscores
        return username != null && username.matches("^[a-zA-Z0-9_]{3,}$");
    }
} 