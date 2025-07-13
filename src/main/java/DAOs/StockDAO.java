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
import java.util.List;

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

}
