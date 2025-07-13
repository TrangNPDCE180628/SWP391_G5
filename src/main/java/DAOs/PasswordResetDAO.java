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
            ps.executeUpdate();
        }
    }

    public boolean verifyOtp(String email, String otp) throws SQLException,ClassNotFoundException {
        String sql = "SELECT * FROM PasswordReset WHERE email = ? AND otp = ? AND expiry > GETDATE()";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, otp);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
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
