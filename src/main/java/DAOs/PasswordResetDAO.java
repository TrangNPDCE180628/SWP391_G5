package DAOs;

import Models.PasswordReset;
import Ultis.DBContext;

import java.sql.*;
import java.time.LocalDateTime;

public class PasswordResetDAO {

    public void insertOtp(PasswordReset pr) throws SQLException,ClassNotFoundException {
        String sql = "INSERT INTO PasswordReset (email, otp, expiry) VALUES (?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pr.getEmail());
            ps.setString(2, pr.getOtp());
            ps.setTimestamp(3, Timestamp.valueOf(pr.getExpiry()));
            
            // Debug logging
            System.out.println("=== INSERTING OTP ===");
            System.out.println("Email: " + pr.getEmail());
            System.out.println("OTP: " + pr.getOtp());
            System.out.println("Expiry: " + pr.getExpiry());
            
            int result = ps.executeUpdate();
            System.out.println("Insert result: " + result + " rows affected");
            System.out.println("=== END INSERT DEBUG ===");
        }
    }

    public boolean verifyOtp(String email, String otp) throws SQLException,ClassNotFoundException {
        String sql = "SELECT * FROM PasswordReset WHERE email = ? AND otp = ? AND expiry > GETDATE()";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, otp);
            
            // Debug logging
            System.out.println("=== OTP VERIFICATION DEBUG ===");
            System.out.println("Email: " + email);
            System.out.println("OTP: " + otp);
            System.out.println("SQL: " + sql);
            
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = rs.next();
                System.out.println("OTP found in database: " + found);
                
                if (!found) {
                    // Check if there's any OTP for this email
                    String checkSql = "SELECT * FROM PasswordReset WHERE email = ?";
                    try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                        checkPs.setString(1, email);
                        try (ResultSet checkRs = checkPs.executeQuery()) {
                            if (checkRs.next()) {
                                System.out.println("Found OTP for email: " + checkRs.getString("otp"));
                                System.out.println("Expiry: " + checkRs.getTimestamp("expiry"));
                                System.out.println("Current time: " + new java.sql.Timestamp(System.currentTimeMillis()));
                            } else {
                                System.out.println("No OTP found for email: " + email);
                            }
                        }
                    }
                }
                
                System.out.println("=== END DEBUG ===");
                return found;
            }
        }
    }

    public void deleteOtp(String email) throws SQLException,ClassNotFoundException {
        String sql = "DELETE FROM PasswordReset WHERE email = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }
}
