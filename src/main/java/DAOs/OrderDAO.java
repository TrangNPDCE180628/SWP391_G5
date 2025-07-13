package DAOs;

import Models.Order;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int create(Order order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO [Order] (cusId, orderDate, totalAmount, discountAmount, finalAmount, orderStatus, paymentMethod, shippingAddress, voucherId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, order.getCusId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setBigDecimal(4, order.getDiscountAmount());

            // FinalAmount = total - discount
            order.setFinalAmount(order.getTotalAmount().subtract(order.getDiscountAmount()));
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
            try ( ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    order.setOrderId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public boolean update(Order order) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET paymentMethod = ?, shippingAddress = ?, orderStatus = ? WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getPaymentMethod());
            ps.setString(2, order.getShippingAddress());
            ps.setString(3, order.getOrderStatus());
            ps.setInt(4, order.getOrderId());
            return ps.executeUpdate() > 0;
        }
    }

    public Order getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order] WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("orderId"));
                    order.setCusId(rs.getString("cusId"));
                    order.setOrderDate(rs.getTimestamp("orderDate"));
                    order.setTotalAmount(rs.getBigDecimal("totalAmount"));
                    order.setDiscountAmount(rs.getBigDecimal("discountAmount"));
                    order.setFinalAmount(rs.getBigDecimal("finalAmount"));
                    order.setVoucherId(rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null);
                    order.setOrderStatus(rs.getString("orderStatus"));
                    order.setPaymentMethod(rs.getString("paymentMethod"));
                    order.setShippingAddress(rs.getString("shippingAddress"));
                    return order;
                }
            }
        }
        return null;
    }

    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM [Order] WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
