package DAOs;

import Models.OrderDetail;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {

    // Create
    public void create(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO OrderDetail (orderId, proId, quantity, unitPrice, voucherId) VALUES (?, ?, ?, ?, ?)";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, orderDetail.getOrderId());
            stmt.setString(2, orderDetail.getProId());
            stmt.setInt(3, orderDetail.getQuantity());
            stmt.setDouble(4, orderDetail.getUnitPrice());
            if (orderDetail.getVoucherId() != null) {
                stmt.setInt(5, orderDetail.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                orderDetail.setId(rs.getInt(1));
            }
        }
    }

    // Read by ID
    public OrderDetail getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail WHERE orderDetailId = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new OrderDetail(
                        rs.getInt("orderDetailId"),
                        rs.getInt("orderId"),
                        rs.getString("proId"),
                        rs.getInt("quantity"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("unitPrice") * rs.getInt("quantity"), // Calculate totalPrice
                        rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null
                );
            }
            return null;
        }
    }

    // Read all
    public List<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail";
        List<OrderDetail> orderDetails = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orderDetails.add(new OrderDetail(
                        rs.getInt("orderDetailId"),
                        rs.getInt("orderId"),
                        rs.getString("proId"),
                        rs.getInt("quantity"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("unitPrice") * rs.getInt("quantity"),
                        rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null
                ));
            }
            return orderDetails;
        }
    }

    // Read by order ID
    public List<OrderDetail> getByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail WHERE orderId = ?";
        List<OrderDetail> orderDetails = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orderDetails.add(new OrderDetail(
                        rs.getInt("orderDetailId"),
                        rs.getInt("orderId"),
                        rs.getString("proId"),
                        rs.getInt("quantity"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("unitPrice") * rs.getInt("quantity"),
                        rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null
                ));
            }
            return orderDetails;
        }
    }

    // Update
    public void update(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE OrderDetail SET orderId = ?, proId = ?, quantity = ?, unitPrice = ?, voucherId = ? WHERE orderDetailId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderDetail.getOrderId());
            stmt.setString(2, orderDetail.getProId());
            stmt.setInt(3, orderDetail.getQuantity());
            stmt.setDouble(4, orderDetail.getUnitPrice());
            if (orderDetail.getVoucherId() != null) {
                stmt.setInt(5, orderDetail.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, orderDetail.getId());
            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderDetailId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Delete by order ID
    public void deleteByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }
}

