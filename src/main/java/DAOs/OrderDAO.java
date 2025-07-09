package DAOs;

import Models.Order;
import Ultis.DBContext;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Create
    public void create(Order order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO [Order] (cusId, orderDate, totalAmount, discountAmount, finalAmount, orderStatus, paymentMethod, shippingAddress, voucherId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, order.getCusId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setBigDecimal(4, order.getDiscountAmount());
            stmt.setBigDecimal(5, order.getFinalAmount());
            stmt.setString(6, order.getOrderStatus());
            stmt.setString(7, order.getPaymentMethod());
            stmt.setString(8, order.getShippingAddress());
            if (order.getVoucherId() != null) {
                stmt.setInt(9, order.getVoucherId());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                order.setOrderId(rs.getInt(1));
            }
        }
    }

    // Read by ID
    public Order getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order] WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("orderId"));
                order.setCusId(rs.getString("cusId"));
                order.setOrderDate(rs.getTimestamp("orderDate"));
                order.setTotalAmount(rs.getBigDecimal("totalAmount"));
                order.setDiscountAmount(rs.getBigDecimal("discountAmount"));
                order.setFinalAmount(rs.getBigDecimal("finalAmount"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setPaymentMethod(rs.getString("paymentMethod"));
                order.setShippingAddress(rs.getString("shippingAddress"));
                order.setVoucherId(rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null);
                return order;
            }
            return null;
        }
    }

    // Read all
    public List<Order> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order]";
        List<Order> orders = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("orderId"));
                order.setCusId(rs.getString("cusId"));
                order.setOrderDate(rs.getTimestamp("orderDate"));
                order.setTotalAmount(rs.getBigDecimal("totalAmount"));
                order.setDiscountAmount(rs.getBigDecimal("discountAmount"));
                order.setFinalAmount(rs.getBigDecimal("finalAmount"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setPaymentMethod(rs.getString("paymentMethod"));
                order.setShippingAddress(rs.getString("shippingAddress"));
                order.setVoucherId(rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null);
                orders.add(order);
            }
        }
        return orders;
    }

    // Read by user ID
    public List<Order> getByUserId(String userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order] WHERE cusId = ?";
        List<Order> orders = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("orderId"));
                order.setCusId(rs.getString("cusId"));
                order.setOrderDate(rs.getTimestamp("orderDate"));
                order.setTotalAmount(rs.getBigDecimal("totalAmount"));
                order.setDiscountAmount(rs.getBigDecimal("discountAmount"));
                order.setFinalAmount(rs.getBigDecimal("finalAmount"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setPaymentMethod(rs.getString("paymentMethod"));
                order.setShippingAddress(rs.getString("shippingAddress"));
                order.setVoucherId(rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null);
                orders.add(order);
            }
        }
        return orders;
    }

    // Update
    public void update(Order order) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET cusId = ?, orderDate = ?, totalAmount = ?, discountAmount = ?, finalAmount = ?, orderStatus = ?, paymentMethod = ?, shippingAddress = ?, voucherId = ? WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getCusId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setBigDecimal(4, order.getDiscountAmount());
            stmt.setBigDecimal(5, order.getFinalAmount());
            stmt.setString(6, order.getOrderStatus());
            stmt.setString(7, order.getPaymentMethod());
            stmt.setString(8, order.getShippingAddress());
            if (order.getVoucherId() != null) {
                stmt.setInt(9, order.getVoucherId());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }
            stmt.setInt(10, order.getOrderId());
            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM [Order] WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Update order status
    public void updateOrderStatus(int id, String status) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET orderStatus = ? WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    // Get orders filtered by status
    public List<Order> getOrdersByStatus(String status) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order] " + (status.equals("All") ? "" : "WHERE orderStatus = ?");
        List<Order> orders = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (!status.equals("All")) {
                stmt.setString(1, status);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("orderId"));
                order.setCusId(rs.getString("cusId"));
                order.setOrderDate(rs.getTimestamp("orderDate"));
                order.setTotalAmount(rs.getBigDecimal("totalAmount"));
                order.setDiscountAmount(rs.getBigDecimal("discountAmount"));
                order.setFinalAmount(rs.getBigDecimal("finalAmount"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setPaymentMethod(rs.getString("paymentMethod"));
                order.setShippingAddress(rs.getString("shippingAddress"));
                order.setVoucherId(rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null);
                orders.add(order);
            }
        }
        return orders;
    }
}
