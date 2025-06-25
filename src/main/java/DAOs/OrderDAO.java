package DAOs;

import Models.Order;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    // Create
    public void create(Order order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO [Order] (cusId, orderDate, orderStatus, totalAmount, discountAmount, voucherId, paymentMethod, shippingAddress) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, order.getUserId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setString(3, order.getStatus());
            stmt.setDouble(4, order.getTotalPrice() + order.getDiscountAmount());
            stmt.setDouble(5, order.getDiscountAmount());
            stmt.setObject(6, order.getVoucherId(), Types.INTEGER);
            stmt.setString(7, order.getPaymentMethod());
            stmt.setString(8, order.getShippingAddress());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                order.setId(rs.getString(1));
            }
        }
    }

    // Read by ID
    public Order getById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order] WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getString("orderId"),
                        rs.getString("cusId"),
                        rs.getTimestamp("orderDate"),
                        rs.getString("orderStatus"),
                        rs.getDouble("finalAmount"),
                        rs.getString("shippingAddress"),
                        rs.getString("paymentMethod"),
                        rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null,
                        rs.getDouble("discountAmount")
                );
            }
            return null;
        }
    }

    // Read all
    public List<Order> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order]";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(new Order(
                        rs.getString("orderId"),
                        rs.getString("cusId"),
                        rs.getTimestamp("orderDate"),
                        rs.getString("orderStatus"),
                        rs.getDouble("finalAmount"),
                        rs.getString("shippingAddress"),
                        rs.getString("paymentMethod"),
                        rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null,
                        rs.getDouble("discountAmount")
                ));
            }
            return orders;
        }
    }

    // Read by user ID
    public List<Order> getByUserId(String userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order] WHERE cusId = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getString("orderId"),
                        rs.getString("cusId"),
                        rs.getTimestamp("orderDate"),
                        rs.getString("orderStatus"),
                        rs.getDouble("finalAmount"),
                        rs.getString("shippingAddress"),
                        rs.getString("paymentMethod"),
                        rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null,
                        rs.getDouble("discountAmount")
                ));
            }
            return orders;
        }
    }

    // Read by status
    public List<Order> getByStatus(String status) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order] WHERE orderStatus = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getString("orderId"),
                        rs.getString("cusId"),
                        rs.getTimestamp("orderDate"),
                        rs.getString("orderStatus"),
                        rs.getDouble("finalAmount"),
                        rs.getString("shippingAddress"),
                        rs.getString("paymentMethod"),
                        rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null,
                        rs.getDouble("discountAmount")
                ));
            }
            return orders;
        }
    }

    // Update
    public void update(Order order) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET cusId = ?, orderDate = ?, orderStatus = ?, totalAmount = ?, discountAmount = ?, voucherId = ?, paymentMethod = ?, shippingAddress = ? WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getUserId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setString(3, order.getStatus());
            stmt.setDouble(4, order.getTotalPrice() + order.getDiscountAmount());
            stmt.setDouble(5, order.getDiscountAmount());
            stmt.setObject(6, order.getVoucherId(), Types.INTEGER);
            stmt.setString(7, order.getPaymentMethod());
            stmt.setString(8, order.getShippingAddress());
            stmt.setString(9, order.getId());
            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM [Order] WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    // Update order status
    public void updateOrderStatus(String id, String status) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET orderStatus = ? WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, id);
            stmt.executeUpdate();
        }
    }
}