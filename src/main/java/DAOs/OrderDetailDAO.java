package DAOs;

import Models.OrderDetail;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {

    // Create
    public void create(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        double totalPrice = orderDetail.getQuantity() * orderDetail.getUnitPrice();
        orderDetail.setTotalPrice(totalPrice);

        String sql = "INSERT INTO OrderDetail (orderId, proId, quantity, unitPrice, voucherId) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, orderDetail.getOrderId());
            stmt.setString(2, orderDetail.getProductId());
            stmt.setInt(3, orderDetail.getQuantity());
            stmt.setDouble(4, orderDetail.getUnitPrice());
            stmt.setObject(5, null, Types.INTEGER);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                orderDetail.setId(rs.getString(1));
            }
        }
    }

    // Read by ID
    public OrderDetail getById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail WHERE orderDetailId = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                OrderDetail orderDetail = new OrderDetail(
                        rs.getString("orderDetailId"),
                        rs.getString("orderId"),
                        rs.getString("proId"),
                        rs.getInt("quantity"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("unitPrice") * rs.getInt("quantity")
                );
                orderDetail.setProductName(rs.getString("productName"));
                return orderDetail;
            }
            return null;
        }
    }

    // Read all
    public List<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail";
        List<OrderDetail> orderDetails = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail(
                        rs.getString("orderDetailId"),
                        rs.getString("orderId"),
                        rs.getString("proId"),
                        rs.getInt("quantity"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("unitPrice") * rs.getInt("quantity")
                );
                orderDetails.add(orderDetail);
            }
            return orderDetails;
        }
    }

    // Read by order ID
    public List<OrderDetail> getByOrderId(String orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail WHERE orderId = ?";
        List<OrderDetail> orderDetails = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail(
                        rs.getString("orderDetailId"),
                        rs.getString("orderId"),
                        rs.getString("proId"),
                        rs.getInt("quantity"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("unitPrice") * rs.getInt("quantity")
                );
                orderDetails.add(orderDetail);
            }
            return orderDetails;
        }
    }

    // Update
    public void update(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        orderDetail.setTotalPrice(orderDetail.getQuantity() * orderDetail.getUnitPrice());
        String sql = "UPDATE OrderDetail SET orderId = ?, proId = ?, quantity = ?, unitPrice = ?, voucherId = ? WHERE orderDetailId = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderDetail.getOrderId());
            stmt.setString(2, orderDetail.getProductId());
            stmt.setInt(3, orderDetail.getQuantity());
            stmt.setDouble(4, orderDetail.getUnitPrice());
            stmt.setObject(5, null, Types.INTEGER);
            stmt.setString(6, orderDetail.getId());
            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE—Elatin1 FROM OrderDetail WHERE orderDetailId = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    // Delete by order ID
    public void deleteByOrderId(String orderId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            stmt.executeUpdate();
        }
    }
}