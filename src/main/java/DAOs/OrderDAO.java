package DAOs;

import Models.Order;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    // UPDATED: Create method updated to match new Order fields
    public void create(Order order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO [Order] (cusId, order_date, totalAmount, discountAmount, orderStatus, paymentMethod, shippingAddress) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, order.getCusId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setDouble(3, order.getTotalAmount());
            stmt.setDouble(4, order.getDiscountAmount());
            stmt.setString(5, order.getStatus());
            stmt.setString(6, order.getPaymentMethod());
            stmt.setString(7, order.getShippingAddress());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                order.setId(rs.getInt(1));
            }
        }
    }

    // UPDATED: Read by ID updated to include new fields
    public Order getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order] WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getInt("orderId"),
                        rs.getString("cusId"),
                        rs.getTimestamp("order_date"),
                        rs.getString("orderStatus"),
                        rs.getDouble("totalAmount"),
                        rs.getDouble("discountAmount"),
                        rs.getDouble("finalAmount"),
                        rs.getString("paymentMethod"),
                        rs.getString("shippingAddress")
                );
            }
            return null;
        }
    }

    // UPDATED: Read all updated to include new fields
    public List<Order> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT o.*, c.cusFullName FROM [Order] o JOIN Customer c ON o.cusId = c.cusId";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("orderId"),
                        rs.getString("cusId"),
                        rs.getTimestamp("order_date"),
                        rs.getString("orderStatus"),
                        rs.getDouble("totalAmount"),
                        rs.getDouble("discountAmount"),
                        rs.getDouble("finalAmount"),
                        rs.getString("paymentMethod"),
                        rs.getString("shippingAddress")
                );
                orders.add(order);
            }
            return orders;
        }
    }

    // NEW: Filter orders by status
    public List<Order> getByStatus(String status) throws SQLException, ClassNotFoundException {
        String sql = "SELECT o.*, c.cusFullName FROM [Order] o JOIN Customer c ON o.cusId = c.cusId WHERE o.orderStatus = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("orderId"),
                        rs.getString("cusId"),
                        rs.getTimestamp("order_date"),
                        rs.getString("orderStatus"),
                        rs.getDouble("totalAmount"),
                        rs.getDouble("discountAmount"),
                        rs.getDouble("finalAmount"),
                        rs.getString("paymentMethod"),
                        rs.getString("shippingAddress")
                );
                orders.add(order);
            }
            return orders;
        }
    }

    // UPDATED: Read by cusId updated to include new fields
    public List<Order> getByCusId(String cusId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order] WHERE cusId = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cusId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("orderId"),
                        rs.getString("cusId"),
                        rs.getTimestamp("order_date"),
                        rs.getString("orderStatus"),
                        rs.getDouble("totalAmount"),
                        rs.getDouble("discountAmount"),
                        rs.getDouble("finalAmount"),
                        rs.getString("paymentMethod"),
                        rs.getString("shippingAddress")
                ));
            }
            return orders;
        }
    }

    // UPDATED: Update method updated to include new fields
    public void update(Order order) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET cusId = ?, order_date = ?, orderStatus = ?, totalAmount = ?, discountAmount = ?, paymentMethod = ?, shippingAddress = ? WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getCusId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setString(3, order.getStatus());
            stmt.setDouble(4, order.getTotalAmount());
            stmt.setDouble(5, order.getDiscountAmount());
            stmt.setString(6, order.getPaymentMethod());
            stmt.setString(7, order.getShippingAddress());
            stmt.setInt(8, order.getId());
            stmt.executeUpdate();
        }
    }

    // Delete (unchanged)
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM [Order] WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // UPDATED: Update order status with validation
    public void updateOrderStatus(int id, String status) throws SQLException, ClassNotFoundException {
        // Validate status (only allow Pending, Done, Cancel)
        if (!status.equals("Pending") && !status.equals("Done") && !status.equals("Cancel")) {
            throw new SQLException("Invalid status: " + status);
        }
        String sql = "UPDATE [Order] SET orderStatus = ? WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
}