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
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
                orderDetail.setOrderDetailId(rs.getInt(1));
            }
        }
    }

    // Read by ID
    public OrderDetail getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail WHERE orderDetailId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractOrderDetailFromResultSet(rs);
            }
            return null;
        }
    }

    // Read all
    public List<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(extractOrderDetailFromResultSet(rs));
            }
        }
        return list;
    }

    // Read by order ID
    public List<OrderDetail> getByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(extractOrderDetailFromResultSet(rs));
            }
        }
        return list;
    }

    // Update
    public void update(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE OrderDetail SET orderId = ?, proId = ?, quantity = ?, unitPrice = ?, voucherId = ? WHERE orderDetailId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderDetail.getOrderId());
            stmt.setString(2, orderDetail.getProId());
            stmt.setInt(3, orderDetail.getQuantity());
            stmt.setDouble(4, orderDetail.getUnitPrice());

            if (orderDetail.getVoucherId() != null) {
                stmt.setInt(5, orderDetail.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setInt(6, orderDetail.getOrderDetailId());

            stmt.executeUpdate();
        }
    }

    // Delete by orderDetailId
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderDetailId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Delete by orderId (xóa tất cả dòng chi tiết theo orderId)
    public void deleteByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }

    // Helper method
    private OrderDetail extractOrderDetailFromResultSet(ResultSet rs) throws SQLException {
        return new OrderDetail(
                rs.getInt("orderDetailId"),
                rs.getInt("orderId"),
                rs.getString("proId"),
                rs.getInt("quantity"),
                rs.getDouble("unitPrice"),
                rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null
        );
    }
}
