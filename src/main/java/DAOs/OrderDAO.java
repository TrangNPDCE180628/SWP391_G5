package DAOs;

import Models.Order;
import Ultis.DBContext;
import java.math.BigDecimal;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {
    // Create new order

    public void create(Order order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO [Order] (cusId, orderDate, totalAmount, discountAmount, voucherId, "
                + "orderStatus, paymentMethod, shippingAddress, finalAmount, receiverName, receiverPhone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            stmt.setBigDecimal(9, order.getFinalAmount());
            stmt.setString(10, order.getReceiverName());
            stmt.setString(11, order.getReceiverPhone());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                order.setOrderId(rs.getInt(1));
            }
        }
    }

    public int createOrder(Order order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO [Order] (cusId, orderDate, totalAmount, discountAmount, finalAmount, orderStatus, paymentMethod, shippingAddress, voucherId, receiverName, receiverPhone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                 Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, order.getCusId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setBigDecimal(4, order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO);

            // Tính finalAmount = totalAmount - discountAmount
            stmt.setBigDecimal(5, order.getFinalAmount());
            stmt.setString(6, order.getOrderStatus());
            stmt.setString(7, order.getPaymentMethod());
            stmt.setString(8, order.getShippingAddress());

            if (order.getVoucherId() != null) {
                stmt.setInt(9, order.getVoucherId());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            stmt.setString(10, order.getReceiverName());
            stmt.setString(11, order.getReceiverPhone());

            stmt.executeUpdate();

            try ( ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    order.setOrderId(generatedId);
                    return generatedId;
                }
            }
        }

        return -1;
    }

    public boolean updateOrder(Order order) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET paymentMethod = ?, shippingAddress = ?, orderStatus = ?, receiverName = ?, receiverPhone = ? WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, order.getPaymentMethod());
            ps.setString(2, order.getShippingAddress());
            ps.setString(3, order.getOrderStatus());
            ps.setString(4, order.getReceiverName());
            ps.setString(5, order.getReceiverPhone());
            ps.setInt(6, order.getOrderId());

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
                    order.setReceiverName(rs.getString("receiverName"));
                    order.setReceiverPhone(rs.getString("receiverPhone"));
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

    public void update(Order order) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET cusId = ?, orderDate = ?, totalAmount = ?, discountAmount = ?, "
                + "voucherId = ?, orderStatus = ?, paymentMethod = ?, shippingAddress = ?, "
                + "finalAmount = ?, receiverName = ?, receiverPhone = ? WHERE orderId = ?";

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
            stmt.setBigDecimal(9, order.getFinalAmount());
            stmt.setString(10, order.getReceiverName());
            stmt.setString(11, order.getReceiverPhone());
            stmt.setInt(12, order.getOrderId());

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
                            rs.getString("shippingAddress"),
                            rs.getString("receiverName"),
                            rs.getString("receiverPhone")
                    );
                    o.setFinalAmount(rs.getBigDecimal("finalAmount"));
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
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException, ClassNotFoundException {
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
        order.setReceiverName(rs.getString("receiverName"));
        order.setReceiverPhone(rs.getString("receiverPhone"));

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

    public Map<String, BigDecimal> getMonthlyRevenueInYear(int year) throws SQLException, ClassNotFoundException {
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

    public boolean checkOrderStatus(String cusId, String proId) throws Exception {
        String sql = "SELECT COUNT(*) "
                + "AS total FROM [Order] o "
                + "JOIN OrderDetail od ON o.orderId = od.orderId "
                + "WHERE o.cusId = ? AND od.proId = ? "
                + "AND o.orderStatus IN ('shipped') ";

        try (
                 Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cusId);
            stmt.setString(2, proId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        }
        return false;
    }

    /**
     * Retrieves order details filtered by order status.
     *
     * @param status The status to filter orders (e.g., "Pending", "Done",
     * "Cancel").
     * @return A list of maps containing order and order detail information.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<Map<String, Object>> getOrderDetailsByStatus(String status) throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> result = new ArrayList<>();

        String sql = "SELECT o.orderId, o.orderDate, o.totalAmount, o.discountAmount, o.finalAmount, "
                + "v.codeName, o.orderStatus, o.paymentMethod, o.shippingAddress, "
                + "o.receiverName, o.receiverPhone, "
                + "c.cusFullName, "
                + "od.orderDetailId, od.proId, p.proName, od.quantity, od.unitPrice, od.voucherId AS detailVoucherId "
                + "FROM [Order] o "
                + "JOIN Customer c ON o.cusId = c.cusId "
                + "JOIN OrderDetail od ON o.orderId = od.orderId "
                + "JOIN Product p ON od.proId = p.proId "
                + "LEFT JOIN Voucher v ON o.voucherId = v.voucherId "
                + "WHERE o.orderStatus = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("orderId", rs.getInt("orderId"));
                    row.put("orderDate", rs.getTimestamp("orderDate"));
                    row.put("totalAmount", rs.getBigDecimal("totalAmount"));
                    row.put("discountAmount", rs.getBigDecimal("discountAmount"));
                    row.put("finalAmount", rs.getBigDecimal("finalAmount"));
                    row.put("codeName", rs.getObject("codeName"));
                    row.put("orderStatus", rs.getString("orderStatus"));
                    row.put("paymentMethod", rs.getString("paymentMethod"));
                    row.put("shippingAddress", rs.getString("shippingAddress"));
                    row.put("receiverName", rs.getString("receiverName"));
                    row.put("receiverPhone", rs.getString("receiverPhone"));

                    row.put("cusFullName", rs.getString("cusFullName"));

                    row.put("orderDetailId", rs.getInt("orderDetailId"));
                    row.put("proId", rs.getString("proId"));
                    row.put("proName", rs.getString("proName"));
                    row.put("quantity", rs.getInt("quantity"));
                    row.put("unitPrice", rs.getBigDecimal("unitPrice"));
                    row.put("detailVoucherId", rs.getObject("detailVoucherId"));

                    BigDecimal totalPrice = rs.getBigDecimal("unitPrice").multiply(new BigDecimal(rs.getInt("quantity")));
                    row.put("totalPrice", totalPrice);

                    result.add(row);
                }
            }
        }

        return result;
    }

    public List<Map<String, Object>> getOrderDetailsAll() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> result = new ArrayList<>();

        String sql = "SELECT o.orderId, o.orderDate, o.totalAmount, o.discountAmount, o.finalAmount, "
                + "v.codeName, o.orderStatus, o.paymentMethod, o.shippingAddress, "
                + "o.receiverName, o.receiverPhone, "
                + "c.cusFullName, "
                + "od.orderDetailId, od.proId, p.proName, od.quantity, od.unitPrice, od.voucherId AS detailVoucherId "
                + "FROM [Order] o "
                + "JOIN Customer c ON o.cusId = c.cusId "
                + "JOIN OrderDetail od ON o.orderId = od.orderId "
                + "JOIN Product p ON od.proId = p.proId " + "LEFT JOIN Voucher v ON o.voucherId = v.voucherId";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("orderId", rs.getInt("orderId"));
                    row.put("orderDate", rs.getTimestamp("orderDate"));
                    row.put("totalAmount", rs.getBigDecimal("totalAmount"));
                    row.put("discountAmount", rs.getBigDecimal("discountAmount"));
                    row.put("finalAmount", rs.getBigDecimal("finalAmount"));
                    row.put("codeName", rs.getObject("codeName"));
                    row.put("orderStatus", rs.getString("orderStatus"));
                    row.put("paymentMethod", rs.getString("paymentMethod"));
                    row.put("shippingAddress", rs.getString("shippingAddress"));
                    row.put("receiverName", rs.getString("receiverName"));
                    row.put("receiverPhone", rs.getString("receiverPhone"));

                    row.put("cusFullName", rs.getString("cusFullName"));

                    row.put("orderDetailId", rs.getInt("orderDetailId"));
                    row.put("proId", rs.getString("proId"));
                    row.put("proName", rs.getString("proName"));
                    row.put("quantity", rs.getInt("quantity"));
                    row.put("unitPrice", rs.getBigDecimal("unitPrice"));
                    row.put("detailVoucherId", rs.getObject("detailVoucherId"));

                    BigDecimal totalPrice = rs.getBigDecimal("unitPrice").multiply(new BigDecimal(rs.getInt("quantity")));
                    row.put("totalPrice", totalPrice);

                    result.add(row);
                }
            }
        }

        return result;
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean updateOrderStatus(String orderId, String status) {
        String sql = "UPDATE [Order] SET orderStatus = ? WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, orderId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateOrderReceiverInfo(Order order) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [Order] SET receiverName = ?, receiverPhone = ?, shippingAddress = ?, orderStatus = ?, paymentMethod = ? WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getReceiverName());
            ps.setString(2, order.getReceiverPhone());
            ps.setString(3, order.getShippingAddress());
            ps.setString(4, order.getOrderStatus());
            ps.setString(5, order.getPaymentMethod());
            ps.setInt(6, order.getOrderId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(Order order) {
        String sql = "UPDATE [Order] SET orderStatus = ? WHERE orderId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getOrderStatus());
            stmt.setInt(2, order.getOrderId());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get orders by customer ID
    public List<Order> getOrdersByCustomerId(String customerId) throws SQLException, ClassNotFoundException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM [Order] WHERE cusId = ? ORDER BY orderDate DESC";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(extractOrderFromResultSet(rs));
                }
            }
        }
        return orders;
    }

    // Get orders by customer ID and status
    public List<Order> getOrdersByCustomerIdAndStatus(String customerId, String status) throws SQLException, ClassNotFoundException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM [Order] WHERE cusId = ? AND orderStatus = ? ORDER BY orderDate DESC";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);
            stmt.setString(2, status);

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(extractOrderFromResultSet(rs));
                }
            }
        }
        return orders;
    }

    // Get orders by customer ID with multiple statuses
    public List<Order> getOrdersByCustomerIdAndStatuses(String customerId, List<String> statuses) throws SQLException, ClassNotFoundException {
        if (statuses == null || statuses.isEmpty()) {
            return new ArrayList<>();
        }

        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM [Order] WHERE cusId = ? AND orderStatus IN (");

        for (int i = 0; i < statuses.size(); i++) {
            sql.append("?");
            if (i < statuses.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(") ORDER BY orderDate DESC");

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            stmt.setString(1, customerId);
            for (int i = 0; i < statuses.size(); i++) {
                stmt.setString(i + 2, statuses.get(i));
            }

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(extractOrderFromResultSet(rs));
                }
            }
        }
        return orders;
    }

    /**
     * Get revenue growth rate by month compared to previous month
     */
    public Map<String, Double> getMonthlyRevenueGrowthRate(int year) throws SQLException, ClassNotFoundException {
        Map<String, Double> growthRateMap = new LinkedHashMap<>();
        Map<String, BigDecimal> monthlyRevenue = getMonthlyRevenueInYear(year);

        BigDecimal previousRevenue = null;
        for (Map.Entry<String, BigDecimal> entry : monthlyRevenue.entrySet()) {
            String month = entry.getKey();
            BigDecimal currentRevenue = entry.getValue();

            if (previousRevenue != null && previousRevenue.compareTo(BigDecimal.ZERO) > 0) {
                // Calculate growth rate: (current - previous) / previous * 100
                BigDecimal growth = currentRevenue.subtract(previousRevenue)
                        .divide(previousRevenue, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal("100"));
                growthRateMap.put(month, growth.doubleValue());
            } else {
                // First month or previous month was 0, set growth to 0
                growthRateMap.put(month, 0.0);
            }
            previousRevenue = currentRevenue;
        }
        return growthRateMap;
    }

    /**
     * Get product revenue statistics from order details
     */
    public List<Map<String, Object>> getProductRevenue(int year) throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> productRevenueList = new ArrayList<>();

        String sql = "SELECT p.proName, "
                + "SUM(od.quantity * od.unitPrice) AS revenue, "
                + "SUM(od.quantity) AS totalQuantity "
                + "FROM [Order] o "
                + "JOIN OrderDetail od ON o.orderId = od.orderId "
                + "JOIN Product p ON od.proId = p.proId "
                + "WHERE o.orderStatus = 'Completed' AND YEAR(o.orderDate) = ? "
                + "GROUP BY p.proId, p.proName "
                + "ORDER BY revenue DESC";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> productData = new HashMap<>();
                    productData.put("productName", rs.getString("proName"));
                    productData.put("revenue", rs.getBigDecimal("revenue"));
                    productData.put("quantity", rs.getInt("totalQuantity"));
                    productRevenueList.add(productData);
                }
            }
        }
        return productRevenueList;
    }

    /**
     * Get yearly revenue statistics ordered from earliest to latest year
     */
    public Map<String, BigDecimal> getYearlyRevenue() throws SQLException, ClassNotFoundException {
        Map<String, BigDecimal> yearlyRevenueMap = new LinkedHashMap<>();

        String sql = "SELECT YEAR(orderDate) AS year, SUM(finalAmount) AS revenue "
                + "FROM [Order] "
                + "WHERE orderStatus = 'Completed' "
                + "GROUP BY YEAR(orderDate) "
                + "ORDER BY year ASC";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String year = String.valueOf(rs.getInt("year"));
                    BigDecimal revenue = rs.getBigDecimal("revenue");
                    yearlyRevenueMap.put(year, revenue);
                }
            }
        }
        return yearlyRevenueMap;
    }

    /**
     * Get revenue for specific year
     */
    public BigDecimal getRevenueByYear(int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(finalAmount) AS revenue "
                + "FROM [Order] "
                + "WHERE orderStatus = 'Completed' AND YEAR(orderDate) = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal revenue = rs.getBigDecimal("revenue");
                    return revenue != null ? revenue : BigDecimal.ZERO;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Get yearly growth rate compared to previous year
     * Returns data ordered from earliest year to latest year
     */
    public Map<String, Double> getYearlyGrowthRate() throws SQLException, ClassNotFoundException {
        Map<String, Double> growthRateMap = new LinkedHashMap<>();
        Map<String, BigDecimal> yearlyRevenue = getYearlyRevenue(); // Already ordered ASC by year

        BigDecimal previousRevenue = null;
        for (Map.Entry<String, BigDecimal> entry : yearlyRevenue.entrySet()) {
            String year = entry.getKey();
            BigDecimal currentRevenue = entry.getValue();

            if (previousRevenue != null && previousRevenue.compareTo(BigDecimal.ZERO) > 0) {
                // Calculate growth rate: (current year - previous year) / previous year * 100
                BigDecimal growth = currentRevenue.subtract(previousRevenue)
                        .divide(previousRevenue, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal("100"));
                growthRateMap.put(year, growth.doubleValue());
            } else {
                // First year or previous year was 0, set growth to 0
                growthRateMap.put(year, 0.0);
            }
            previousRevenue = currentRevenue;
        }
        return growthRateMap;
    }
}
