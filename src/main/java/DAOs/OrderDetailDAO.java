package DAOs;

import Models.OrderDetail;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class OrderDetailDAO {

    // Create - trả về boolean để kiểm tra thành công
    public boolean create(OrderDetail detail) throws SQLException, ClassNotFoundException {
        if (detail == null) {
            throw new IllegalArgumentException("OrderDetail cannot be null");
        }

        String sql = "INSERT INTO OrderDetail (orderId, proId, quantity, unitPrice, voucherId) VALUES (?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, detail.getOrderId());
            stmt.setString(2, detail.getProductId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setBigDecimal(4, detail.getUnitPrice());

            if (detail.getVoucherId() != null) {
                stmt.setInt(5, detail.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Create với auto-generated ID
    public int createWithId(OrderDetail detail) throws SQLException, ClassNotFoundException {
        if (detail == null) {
            throw new IllegalArgumentException("OrderDetail cannot be null");
        }

        String sql = "INSERT INTO OrderDetail (orderId, proId, quantity, unitPrice, voucherId) VALUES (?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, detail.getOrderId());
            stmt.setString(2, detail.getProductId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setBigDecimal(4, detail.getUnitPrice());

            if (detail.getVoucherId() != null) {
                stmt.setInt(5, detail.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return -1;
        }
    }

    // Get by ID
    public OrderDetail getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail WHERE orderDetailId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractFromResultSet(rs);
                }
                return null;
            }
        }
    }

    // Get all
    public List<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail";
        List<OrderDetail> list = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        }
        return list;
    }

    // Get by Order ID
    public List<OrderDetail> getByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail WHERE orderId = ?";
        List<OrderDetail> list = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractFromResultSet(rs));
                }
            }
        }
        return list;
    }

    // Update
    public boolean update(OrderDetail detail) throws SQLException, ClassNotFoundException {
        if (detail == null) {
            throw new IllegalArgumentException("OrderDetail cannot be null");
        }

        String sql = "UPDATE OrderDetail SET orderId = ?, proId = ?, quantity = ?, unitPrice = ?, voucherId = ? WHERE orderDetailId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, detail.getOrderId());
            stmt.setString(2, detail.getProductId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setBigDecimal(4, detail.getUnitPrice());

            if (detail.getVoucherId() != null) {
                stmt.setInt(5, detail.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setInt(6, detail.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Delete by detail ID
    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderDetailId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Delete all by order ID
    public boolean deleteByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Check if exists
    public boolean exists(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM OrderDetail WHERE orderDetailId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Get total amount by order ID
    public BigDecimal getTotalByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(quantity * unitPrice) FROM OrderDetail WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal(1);
                    return total != null ? total : BigDecimal.ZERO;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    // Utility method to extract OrderDetail from ResultSet
    private OrderDetail extractFromResultSet(ResultSet rs) throws SQLException {
        OrderDetail detail = new OrderDetail();
        detail.setId(rs.getInt("orderDetailId"));
        detail.setOrderId(rs.getInt("orderId"));
        detail.setProductId(rs.getString("proId"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setUnitPrice(rs.getBigDecimal("unitPrice"));

        int vId = rs.getInt("voucherId");
        detail.setVoucherId(rs.wasNull() ? null : vId);

        return detail;
    }
}
