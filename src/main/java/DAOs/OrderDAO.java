package DAOs;

import Models.Order;
import Ultis.DBContext;
import java.math.BigDecimal;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {
    // Create new order

    public void create(Order order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO [Order] (cusId, orderDate, totalAmount, discountAmount, voucherId, orderStatus, paymentMethod, shippingAddress) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, order.getCusId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setBigDecimal(4, order.getDiscountAmount());

            if (order.getVoucherId() != null) {
                stmt.setInt(5, order.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setString(6, order.getOrderStatus());
            stmt.setString(7, order.getPaymentMethod());
            stmt.setString(8, order.getShippingAddress());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                order.setOrderId(rs.getInt(1));
            }
        }
    }

    public int createOrder(Order order) throws SQLException, ClassNotFoundException {
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

    public boolean updateOrder(Order order) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET paymentMethod = ?, shippingAddress = ?, orderStatus = ? WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getPaymentMethod());
            ps.setString(2, order.getShippingAddress());
            ps.setString(3, order.getOrderStatus());
            ps.setInt(4, order.getOrderId());
            return ps.executeUpdate() > 0;
        }
    }

    public Order getOrderById(int id) throws SQLException, ClassNotFoundException {
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
    // Read - Get order by ID

    public Order getById(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [Order] WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractOrderFromResultSet(rs);
            }
        }
        return null;
    }

    // Read - Get all orders
    public List<Order> getAll() throws SQLException, ClassNotFoundException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM [Order]";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        }
        return orders;
    }

    // Update
    public void update(Order order) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET cusId = ?, orderDate = ?, totalAmount = ?, discountAmount = ?, "
                + "voucherId = ?, orderStatus = ?, paymentMethod = ?, shippingAddress = ? WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getCusId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setBigDecimal(4, order.getDiscountAmount());

            if (order.getVoucherId() != null) {
                stmt.setInt(5, order.getVoucherId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setString(6, order.getOrderStatus());
            stmt.setString(7, order.getPaymentMethod());
            stmt.setString(8, order.getShippingAddress());
            stmt.setInt(9, order.getOrderId());

            stmt.executeUpdate();
        }
    }

    public void updateStatus(int orderId, String status) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET orderStatus = ? WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }

    public List<Order> getByStatus(String status) throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM [Order] WHERE orderStatus = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order(
                            rs.getInt("orderId"),
                            rs.getString("cusId"),
                            rs.getTimestamp("orderDate"),
                            rs.getBigDecimal("totalAmount"),
                            rs.getBigDecimal("discountAmount"),
                            rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null,
                            rs.getString("orderStatus"),
                            rs.getString("paymentMethod"),
                            rs.getString("shippingAddress")
                    );
                    list.add(o);
                }
            }
        }
        return list;
    }

    public void deleteOrder(int orderId) throws SQLException, ClassNotFoundException {
        String deleteDetailsSql = "DELETE FROM [OrderDetail] WHERE orderId = ?";
        String deleteOrderSql = "DELETE FROM [Order] WHERE orderId = ?";

        try ( Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false);

            try (
                     PreparedStatement stmtDetails = conn.prepareStatement(deleteDetailsSql);  PreparedStatement stmtOrder = conn.prepareStatement(deleteOrderSql)) {
                stmtDetails.setInt(1, orderId);
                stmtDetails.executeUpdate();

                stmtOrder.setInt(1, orderId);
                stmtOrder.executeUpdate();

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    // Helper method to map ResultSet to Order object
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("orderId"));
        order.setCusId(rs.getString("cusId"));
        order.setOrderDate(rs.getTimestamp("orderDate"));
        order.setTotalAmount(rs.getBigDecimal("totalAmount"));
        order.setDiscountAmount(rs.getBigDecimal("discountAmount"));
        order.setVoucherId(rs.getObject("voucherId") != null ? rs.getInt("voucherId") : null);
        order.setOrderStatus(rs.getString("orderStatus"));
        order.setPaymentMethod(rs.getString("paymentMethod"));
        order.setShippingAddress(rs.getString("shippingAddress"));

        // finalAmount được tự động tính theo logic trong model (không cần set trực tiếp)
        return order;
    }

    public BigDecimal getRevenueByMonth(int year, int month) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(finalAmount) AS revenue "
                + "FROM [Order] "
                + "WHERE orderStatus = 'Completed' "
                + "AND YEAR(orderDate) = ? AND MONTH(orderDate) = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, year);
            stmt.setInt(2, month);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("revenue") != null ? rs.getBigDecimal("revenue") : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalRevenue() throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(finalAmount) AS total FROM [Order] WHERE orderStatus = 'Completed'";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("total") != null ? rs.getBigDecimal("total") : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public Map<String, BigDecimal> getMonthlyRevenueInYear(int year) throws SQLException,ClassNotFoundException {
        Map<String, BigDecimal> revenueMap = new LinkedHashMap<>();

        String sql = "SELECT MONTH(orderDate) AS month, SUM(finalAmount) AS revenue "
                + "FROM [Order] "
                + "WHERE orderStatus = 'Completed' AND YEAR(orderDate) = ? "
                + "GROUP BY MONTH(orderDate) ORDER BY month";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int month = rs.getInt("month");
                BigDecimal revenue = rs.getBigDecimal("revenue");
                revenueMap.put(String.format("%02d", month), revenue);
            }
        }
        return revenueMap;
    }

}
