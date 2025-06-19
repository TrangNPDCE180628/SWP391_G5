package DAOs;

import Models.OrderDetail;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    // UPDATED: Create method updated to match OrderDetail table
    public void create(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO OrderDetail (orderId, proId, quantity, unitPrice, voucherId) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, orderDetail.getOrderId());
            stmt.setString(2, orderDetail.getProductId()); // UPDATED: Changed to String for proId
            stmt.setInt(3, orderDetail.getQuantity());
            stmt.setDouble(4, orderDetail.getUnitPrice());
            if (orderDetail.getVoucherId() == 0) {
                stmt.setNull(5, Types.INTEGER); // Handle nullable voucherId
            } else {
                stmt.setInt(5, orderDetail.getVoucherId());
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                orderDetail.setId(rs.getInt(1));
            }
        }
    }

    // UPDATED: Read by ID updated
    public OrderDetail getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail WHERE orderDetailId = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                OrderDetail detail = new OrderDetail(
                        rs.getInt("orderDetailId"),
                        rs.getInt("orderId"),
                        rs.getString("proId"), // UPDATED: Changed to String
                        rs.getInt("quantity"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("unitPrice") * rs.getInt("quantity") // Calculate totalPrice
                );
                detail.setVoucherId(rs.getInt("voucherId"));
                return detail;

            }
            return null;
        }
    }

    // UPDATED: Read all updated
    public List<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail";

        List<OrderDetail> orderDetails = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                OrderDetail detail = new OrderDetail(
                        rs.getInt("orderDetailId"),
                        rs.getInt("orderId"),
                        rs.getString("proId"), // UPDATED: Changed to String
                        rs.getInt("quantity"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("unitPrice") * rs.getInt("quantity")
                );
                detail.setVoucherId(rs.getInt("voucherId"));
                orderDetails.add(detail);

            }
            return orderDetails;
        }
    }

    // UPDATED: Read by order ID updated
    public List<OrderDetail> getByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT od.*, p.proName FROM OrderDetail od JOIN Product p ON od.proId = p.proId WHERE od.orderId = ?";

        List<OrderDetail> orderDetails = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderDetail detail = new OrderDetail(
                        rs.getInt("orderDetailId"),
                        rs.getInt("orderId"),
                        rs.getString("proId"),
                        rs.getInt("quantity"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("unitPrice") * rs.getInt("quantity")
                );
                detail.setVoucherId(rs.getInt("voucherId"));
                orderDetails.add(detail);

            }
            return orderDetails;
        }
    }

    // UPDATED: Update method updated
    public void update(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE OrderDetail SET orderId = ?, proId = ?, quantity = ?, unitPrice = ?, voucherId = ? WHERE orderDetailId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderDetail.getOrderId());
            stmt.setString(2, orderDetail.getProductId());
            stmt.setInt(3, orderDetail.getQuantity());
            stmt.setDouble(4, orderDetail.getUnitPrice());
            if (orderDetail.getVoucherId() == 0) {
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setInt(5, orderDetail.getVoucherId());
            }

            stmt.setInt(6, orderDetail.getId());
            stmt.executeUpdate();
        }
    }


    // Delete (unchanged)
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderDetailId = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Delete by order ID (unchanged)
    public void deleteByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderId = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }

    // NEW: Add voucherId field to OrderDetail model
    public void setVoucherId(OrderDetail orderDetail, int voucherId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE OrderDetail SET voucherId = ? WHERE orderDetailId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (voucherId == 0) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, voucherId);
            }
            stmt.setInt(2, orderDetail.getId());
            stmt.executeUpdate();
        }
    }
}

