package DAOs;

import Models.OrderDetail;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {

    /* ───────────────────────────  CREATE  ─────────────────────────── */
    public boolean createOrderDetail(OrderDetail detail) throws SQLException, ClassNotFoundException {
        if (detail == null) {
            throw new IllegalArgumentException("OrderDetail cannot be null");
        }

        String sql = "INSERT INTO OrderDetail (orderId, proId, quantity, unitPrice, voucherId) "
                + "VALUES (?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, detail.getOrderId());
            stmt.setString(2, detail.getProId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setBigDecimal(4, java.math.BigDecimal.valueOf(detail.getUnitPrice())); // ✅
            if (detail.getVoucherId() != null) {
                stmt.setInt(5, detail.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;
        }
    }

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
                orderDetail.setOrderDetailId(rs.getInt(1));
            }
        }
    }

    /**
     * Thêm OrderDetail và trả về khóa chính sinh tự động, -1 nếu thất bại
     */
    public int createOrderDetailWithId(OrderDetail detail) throws SQLException, ClassNotFoundException {
        if (detail == null) {
            throw new IllegalArgumentException("OrderDetail cannot be null");
        }

        String sql
                = "INSERT INTO OrderDetail (orderId, proId, quantity, unitPrice, voucherId) "
                + "VALUES (?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, detail.getOrderId());
            stmt.setString(2, detail.getProId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setDouble(4, detail.getUnitPrice());

            if (detail.getVoucherId() != null) {
                stmt.setInt(5, detail.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            if (stmt.executeUpdate() > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return -1;
        }
    }

    /* ───────────────────────────  READ  ─────────────────────────── */
    public OrderDetail getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail WHERE orderDetailId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try ( ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? extract(rs) : null;
            }
        }
    }

    public List<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetail";
        List<OrderDetail> list = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extract(rs));
            }
        }
        return list;
    }

    public List<OrderDetail> getOrderDetailByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail WHERE orderId = ?";
        try ( Connection con = DBContext.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail od = new OrderDetail();
                    od.setOrderId(rs.getInt("orderId"));
                    od.setProId(rs.getString("proId"));
                    od.setQuantity(rs.getInt("quantity"));
                    od.setUnitPrice(rs.getDouble("unitPrice"));
                    list.add(od);
                }
            }
        }
        return list;
    }

    // Read by order ID
    public List<OrderDetail> getByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(extractOrderDetailFromResultSet(rs));
            }
        }
        return list;
    }

    /* ───────────────────────────  UPDATE  ─────────────────────────── */
    public boolean updateOrderDetail(OrderDetail detail) throws SQLException, ClassNotFoundException {
        if (detail == null) {
            throw new IllegalArgumentException("OrderDetail cannot be null");
        }

        String sql
                = "UPDATE OrderDetail SET orderId = ?, proId = ?, quantity = ?, unitPrice = ?, voucherId = ? "
                + "WHERE orderDetailId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, detail.getOrderId());
            stmt.setString(2, detail.getProId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setDouble(4, detail.getUnitPrice());

            if (detail.getVoucherId() != null) {
                stmt.setInt(5, detail.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setInt(6, detail.getOrderDetailId());
            return stmt.executeUpdate() > 0;
        }
        return list;
    }

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

            stmt.setInt(6, orderDetail.getOrderDetailId());

            stmt.executeUpdate();
        }
    }


    /* ───────────────────────────  DELETE  ─────────────────────────── */
    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderDetailId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetail WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    /* ───────────────────────────  UTILITIES  ─────────────────────────── */
    /**
     * Kiểm tra tồn tại OrderDetail theo ID
     */
    public boolean exists(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT 1 FROM OrderDetail WHERE orderDetailId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try ( ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Tổng tiền (quantity * unitPrice) của một Order
     */
    public double getTotalByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(quantity * unitPrice) FROM OrderDetail WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try ( ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        }
    }

    /* ───────────────────────────  PRIVATE  ─────────────────────────── */
    /**
     * Chuyển ResultSet → OrderDetail
     */
    private OrderDetail extract(ResultSet rs) throws SQLException {
        OrderDetail d = new OrderDetail();
        d.setOrderDetailId(rs.getInt("orderDetailId"));
        d.setOrderId(rs.getInt("orderId"));
        d.setProId(rs.getString("proId"));
        d.setQuantity(rs.getInt("quantity"));
        d.setUnitPrice(rs.getDouble("unitPrice"));

        int vId = rs.getInt("voucherId");
        d.setVoucherId(rs.wasNull() ? null : vId);
        return d;
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
