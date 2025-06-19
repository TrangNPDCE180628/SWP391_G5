package DAOs;

import Models.Cart;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    // Create
    public void create(Cart cart) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Carts (user_id, status, productId, unitPrice, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cart.getUserId());
            stmt.setString(2, cart.getStatus());
            stmt.setInt(3, cart.getProductId());
            stmt.setDouble(4, cart.getUnitPrice());
            stmt.setInt(5, cart.getQuantity());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                cart.setId(rs.getInt(1));
            }
        }
    }

    // Read by ID
    public Cart getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Carts WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cart(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getInt("productId"),
                        rs.getDouble("unitPrice"),
                        rs.getInt("quantity")
                );
            }
            return null;
        }
    }

    // Read all
    public List<Cart> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Carts";
        List<Cart> carts = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                carts.add(new Cart(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getInt("productId"),
                        rs.getDouble("unitPrice"),
                        rs.getInt("quantity")
                ));
            }
            return carts;
        }
    }

    // Read by user ID
    public List<Cart> getByUserId(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Carts WHERE user_id = ?";
        List<Cart> carts = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                carts.add(new Cart(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getInt("productId"),
                        rs.getDouble("unitPrice"),
                        rs.getInt("quantity")
                ));
            }
            return carts;
        }
    }

    // Read active cart by user ID
    public List<Cart> getActiveCartByUserId(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Carts WHERE user_id = ? AND status = 'active'";
        List<Cart> carts = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                carts.add(new Cart(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getInt("productId"),
                        rs.getDouble("unitPrice"),
                        rs.getInt("quantity")
                ));
            }
            return carts;
        }
    }

    // Update
    public void update(Cart cart) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Carts SET user_id = ?, status = ?, productId = ?, unitPrice = ?, quantity = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cart.getUserId());
            stmt.setString(2, cart.getStatus());
            stmt.setInt(3, cart.getProductId());
            stmt.setDouble(4, cart.getUnitPrice());
            stmt.setInt(5, cart.getQuantity());
            stmt.setInt(6, cart.getId());
            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Carts WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Delete by user ID
    public void deleteByUserId(int userId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Carts WHERE user_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    // Update cart status
    public void updateCartStatus(int userId, String status) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Carts SET status = ? WHERE user_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
} 