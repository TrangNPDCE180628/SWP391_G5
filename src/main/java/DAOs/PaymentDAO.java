package DAOs;

import Models.Payment;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    // Create
    public void create(Payment payment) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Payments (order_id, payment_date, amount) VALUES (?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payment.getOrderId());
            stmt.setTimestamp(2, payment.getPaymentDate());
            stmt.setDouble(3, payment.getAmount());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                payment.setId(rs.getInt(1));
            }
        }
    }

    // Read by ID
    public Payment getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Payments WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Payment(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getTimestamp("payment_date"),
                        rs.getDouble("amount")
                );
            }
            return null;
        }
    }

    // Read all
    public List<Payment> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Payments";
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getTimestamp("payment_date"),
                        rs.getDouble("amount")
                ));
            }
            return payments;
        }
    }

    // Read by order ID
    public List<Payment> getByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Payments WHERE order_id = ?";
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getTimestamp("payment_date"),
                        rs.getDouble("amount")
                ));
            }
            return payments;
        }
    }

    // Update
    public void update(Payment payment) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Payments SET order_id = ?, payment_date = ?, amount = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getOrderId());
            stmt.setTimestamp(2, payment.getPaymentDate());
            stmt.setDouble(3, payment.getAmount());
            stmt.setInt(4, payment.getId());
            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Payments WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Delete by order ID
    public void deleteByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Payments WHERE order_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }
} 