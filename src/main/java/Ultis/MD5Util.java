package Ultis;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    
    /**
     * Hash a password using MD5
     * @param password The plain text password
     * @return The MD5 hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            
            // Convert byte array to hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
    
    /**
     * Verify a password against a hash
     * @param password The plain text password
     * @param hash The stored hash
     * @return true if password matches hash
     */
    public static boolean verifyPassword(String password, String hash) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(hash);
    }
}
