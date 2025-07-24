/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Stock;
import Ultis.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 *
 * @author SE18-CE180628-Nguyen Pham Doan Trang
 */
public class StockDAO {

    public boolean doesProductExist(String proId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT COUNT(*) FROM Product WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<Stock> getAllStocks() throws SQLException, ClassNotFoundException {
        List<Stock> stockList = new ArrayList<>();
        String sql = "SELECT proId, stockQuantity, lastUpdated FROM Stock";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Stock stock = new Stock(
                        rs.getString("proId"),
                        rs.getInt("stockQuantity"),
                        rs.getTimestamp("lastUpdated")
                );
                stockList.add(stock);
            }
        }
        return stockList;
    }

    public void addStockQuantity(String proId, int quantity) throws SQLException, ClassNotFoundException {
        if (quantity <= 0) {
            return;
        }
        String sql = "UPDATE Stock SET stockQuantity = stockQuantity + ?, lastUpdated = GETDATE() WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, proId);
            stmt.executeUpdate();
        }
    }

    public void deleteStock(String proId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Stock WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proId);
            stmt.executeUpdate();
        }
    }

    public void createStock(String proId, int quantity) throws SQLException, ClassNotFoundException {
        if (quantity < 0) {
            throw new SQLException("Quantity must be non-negative");
        }
        if (!doesProductExist(proId)) {
            throw new SQLException("Product ID " + proId + " does not exist in the Product table");
        }
        String sql = "INSERT INTO Stock (proId, stockQuantity, lastUpdated) VALUES (?, ?, GETDATE())";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            stmt.setInt(2, quantity);
            stmt.executeUpdate();
        }
    }

    public void updateStockQuantity(String proId, int newQuantity) throws SQLException, ClassNotFoundException {
        if (newQuantity < 0) {
            return;
        }
        String sql = "UPDATE Stock SET stockQuantity = ?, lastUpdated = GETDATE() WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setString(2, proId);
            stmt.executeUpdate();
        }
    }

    public Stock getStockByProductId(String proId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT proId, stockQuantity, lastUpdated FROM Stock WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Stock(
                            rs.getString("proId"),
                            rs.getInt("stockQuantity"),
                            rs.getTimestamp("lastUpdated")
                    );
                }
            }
        }
        return null;
    }

    public int getStockProductByProductId(String proId) {
        int quantity = 0;
        try ( Connection con = DBContext.getConnection();  PreparedStatement ps = con.prepareStatement("SELECT stockQuantity FROM Stock WHERE proId = ?")) {
            ps.setString(1, proId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                quantity = rs.getInt("stockQuantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quantity;
    }

    public boolean updateStock(String proId, int newQuantity) {
        try ( Connection con = DBContext.getConnection();  PreparedStatement ps = con.prepareStatement("UPDATE Stock SET stockQuantity = ?, lastUpdated = GETDATE() WHERE proId = ?")) {
            ps.setInt(1, newQuantity);
            ps.setString(2, proId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // CHECK – lấy số lượng còn lại nhanh (dùng trong CartController)
    public int getQuantity(String proId) {
        String sql = "SELECT stockQuantity FROM Stock WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, proId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("stockQuantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean decreaseStockAfterOrder(String proId, int quantityOrdered) {
        String sql = "UPDATE Stock SET stockQuantity = stockQuantity - ? WHERE proId = ? AND stockQuantity >= ?";
        try ( Connection con = DBContext.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantityOrdered);
            ps.setString(2, proId);
            ps.setInt(3, quantityOrdered);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void increaseStockAfterCancel(String proId, int quantityOrdered) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Stock SET stockQuantity = stockQuantity + ? WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantityOrdered);
            stmt.setString(2, proId);
            stmt.executeUpdate();
        }
    }

    // Sort stock list
    public List<Stock> getStocksSorted(String sortBy, String order) throws SQLException {
        List<Stock> stocks = new ArrayList<>();

        // Danh sách các cột được cho phép sắp xếp
        List<String> validColumns = Arrays.asList("proId", "stockQuantity", "lastUpdated");
        List<String> validOrders = Arrays.asList("ASC", "DESC");

        // Kiểm tra tính hợp lệ của tham số đầu vào để tránh SQL Injection
        if (!validColumns.contains(sortBy)) {
            sortBy = "lastUpdated"; // mặc định
        }
        if (!validOrders.contains(order.toUpperCase())) {
            order = "DESC"; // mặc định
        }

        // Ghép câu query động
        String query = "SELECT * FROM Stock ORDER BY " + sortBy + " " + order;

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Stock s = new Stock();
                s.setProId(rs.getString("proId"));
                s.setStockQuantity(rs.getInt("stockQuantity"));
                s.setLastUpdated(rs.getTimestamp("lastUpdated"));
                stocks.add(s);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return stocks;
    }

// Reduce stock quantity after order is completed
    public boolean reduceStockQuantity(String proId, int quantity) throws SQLException {
        String sql = "UPDATE Stock SET stockQuantity = stockQuantity - ?, lastUpdated = CURRENT_TIMESTAMP WHERE proId = ? AND stockQuantity >= ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setString(2, proId);
            ps.setInt(3, quantity);

            return ps.executeUpdate() > 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all stock information with joined product and product type details.
     *
     * @return List of Map containing joined data from Product, Stock, and
     * ProductType
     */
    public List<Map<String, Object>> getAllProductStockData() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = "SELECT "
                + "p.proId, p.proName, p.proDescription, p.proPrice, p.proImageUrl, "
                + "pt.proTypeName, s.stockQuantity, s.lastUpdated "
                + "FROM Product p "
                + "JOIN Stock s ON p.proId = s.proId "
                + "JOIN ProductType pt ON p.proTypeId = pt.proTypeId";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("proId", rs.getString("proId"));
                row.put("proName", rs.getString("proName"));
                row.put("proDescription", rs.getString("proDescription"));
                row.put("proPrice", rs.getBigDecimal("proPrice"));
                row.put("proImageUrl", rs.getString("proImageUrl"));
                row.put("proTypeName", rs.getString("proTypeName"));
                row.put("stockQuantity", rs.getInt("stockQuantity"));
                row.put("lastUpdated", rs.getTimestamp("lastUpdated"));
                list.add(row);
            }
        }

        return list;
    }

    /**
     * Lấy danh sách sản phẩm chưa có record trong bảng Stock (null) hoặc có
     * stock = 0
     */
    public List<String> getProductsWithoutStock() throws SQLException, ClassNotFoundException {
        List<String> result = new ArrayList<>();

        String sql = "SELECT p.proId FROM Product p "
                + "LEFT JOIN Stock s ON p.proId = s.proId "
                + "WHERE s.proId IS NULL OR s.stockQuantity = 0";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(rs.getString("proId"));
            }

        }

        return result;
    }

    public void importStockQuantity(String proId, int quantity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Stock SET stockQuantity = stockQuantity + ?, lastUpdated = CURRENT_TIMESTAMP WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, proId);
            ps.executeUpdate();

        }
    }

    public void exportStockQuantity(String proId, int quantity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Stock SET stockQuantity = stockQuantity - ?, lastUpdated = CURRENT_TIMESTAMP WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, proId);
            ps.executeUpdate();
        }
    }
}
