package DAOs;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Ultis.DBContext;

public class RevenueDAO {
    private Connection conn;

    public RevenueDAO() throws SQLException {
        try {
            conn = DBContext.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Failed to load JDBC driver", e);
        }
    }

    /**
     * Calculate total revenue from completed orders within an optional date range.
     * @param startDate Optional start date for filtering (nullable)
     * @param endDate Optional end date for filtering (nullable)
     * @return Total revenue as BigDecimal
     */
    public BigDecimal getTotalRevenue(Timestamp startDate, Timestamp endDate) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT SUM(finalAmount) as totalRevenue FROM [Order] WHERE orderStatus = 'Completed'"
        );
        if (startDate != null && endDate != null) {
            sql.append(" AND orderDate BETWEEN ? AND ?");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            if (startDate != null && endDate != null) {
                stmt.setTimestamp(1, startDate);
                stmt.setTimestamp(2, endDate);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("totalRevenue");
                return total != null ? total : BigDecimal.ZERO;
            }
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get monthly revenue grouped by year-month from completed orders within optional date range.
     * @param startDate Optional start date
     * @param endDate Optional end date
     * @return List of Object arrays: [month, revenue]
     */
    public List<Object[]> getMonthlyRevenue(Timestamp startDate, Timestamp endDate) throws SQLException {
        List<Object[]> revenueList = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT FORMAT(orderDate, 'yyyy-MM') AS month, SUM(finalAmount) AS revenue " +
            "FROM [Order] WHERE orderStatus = 'Completed'"
        );
        if (startDate != null && endDate != null) {
            sql.append(" AND orderDate BETWEEN ? AND ?");
        }
        sql.append(" GROUP BY FORMAT(orderDate, 'yyyy-MM') ORDER BY month");

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            if (startDate != null && endDate != null) {
                stmt.setTimestamp(1, startDate);
                stmt.setTimestamp(2, endDate);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String month = rs.getString("month");
                BigDecimal revenue = rs.getBigDecimal("revenue");
                revenueList.add(new Object[]{month, revenue});
            }
        }
        return revenueList;
    }

    /**
     * Get daily revenue grouped by date from completed orders within optional date range.
     * @param startDate Optional start date
     * @param endDate Optional end date
     * @return List of Object arrays: [date, revenue]
     */
    public List<Object[]> getDailyRevenue(Timestamp startDate, Timestamp endDate) throws SQLException {
        List<Object[]> revenueList = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT CONVERT(date, orderDate) AS date, SUM(finalAmount) AS revenue " +
            "FROM [Order] WHERE orderStatus = 'Completed'"
        );
        if (startDate != null && endDate != null) {
            sql.append(" AND orderDate BETWEEN ? AND ?");
        }
        sql.append(" GROUP BY CONVERT(date, orderDate) ORDER BY date");

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            if (startDate != null && endDate != null) {
                stmt.setTimestamp(1, startDate);
                stmt.setTimestamp(2, endDate);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String date = rs.getString("date");
                BigDecimal revenue = rs.getBigDecimal("revenue");
                revenueList.add(new Object[]{date, revenue});
            }
        }
        return revenueList;
    }
}