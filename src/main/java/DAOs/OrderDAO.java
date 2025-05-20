package DAOs;

import Models.Order;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    // Create
    public void create(Order order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Orders (userId, orderDate, status, totalPrice) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getUserId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setString(3, order.getStatus());
            stmt.setDouble(4, order.getTotalPrice());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                order.setId(rs.getInt(1));
            }
        }
    }

    // Read by ID
    public Order getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Orders WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getTimestamp("orderDate"),
                        rs.getString("status"),
                        rs.getDouble("totalPrice")
                );
            }
            return null;
        }
    }

    // Read all
    public List<Order> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Orders";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getTimestamp("orderDate"),
                        rs.getString("status"),
                        rs.getDouble("totalPrice")
                ));
            }
            return orders;
        }
    }

    // Read by user ID
    public List<Order> getByUserId(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Orders WHERE userId = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getTimestamp("orderDate"),
                        rs.getString("status"),
                        rs.getDouble("totalPrice")
                ));
            }
            return orders;
        }
    }

    // Update
    public void update(Order order) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Orders SET userId = ?, orderDate = ?, status = ?, totalPrice = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getUserId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setString(3, order.getStatus());
            stmt.setDouble(4, order.getTotalPrice());
            stmt.setInt(5, order.getId());
            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Orders WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Update order status
    public void updateOrderStatus(int id, String status) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Orders SET status = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
} 