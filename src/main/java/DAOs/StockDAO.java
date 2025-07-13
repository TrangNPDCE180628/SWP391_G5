/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Stock;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StockDAO - Handles database operations related to stock inventory.
 * Provides methods to view, add, edit, create, and delete inventory quantities.
 */
public class StockDAO {

    /**
     * Checks if a product ID exists in the Product table.
     * 
     * @param proId the product ID to check
     * @return true if the product ID exists, false otherwise
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean doesProductExist(String proId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Product WHERE proId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Retrieves the list of all stock entries in the inventory.
     * 
     * @return list of Stock objects
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<Stock> getAllStocks() throws SQLException, ClassNotFoundException {
        List<Stock> stockList = new ArrayList<>();
        String sql = "SELECT proId, stockQuantity, lastUpdated FROM Stock";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
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

    /**
     * Adds inventory quantity to a product's stock.
     * 
     * @param proId   the product ID
     * @param quantity the quantity to add (must be > 0)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void addStockQuantity(String proId, int quantity) throws SQLException, ClassNotFoundException {
        if (quantity <= 0) return;
        String sql = "UPDATE Stock SET stockQuantity = stockQuantity + ?, lastUpdated = GETDATE() WHERE proId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, proId);
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a product's stock record from the inventory.
     * 
     * @param proId the product ID
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void deleteStock(String proId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Stock WHERE proId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            stmt.executeUpdate();
        }
    }

    /**
     * Creates a new stock record for a product.
     * 
     * @param proId    the product ID
     * @param quantity the initial stock quantity (must be >= 0)
     * @throws SQLException if the product ID doesn't exist or a database error occurs
     * @throws ClassNotFoundException
     */
    public void createStock(String proId, int quantity) throws SQLException, ClassNotFoundException {
        if (quantity < 0) {
            throw new SQLException("Quantity must be non-negative");
        }
        if (!doesProductExist(proId)) {
            throw new SQLException("Product ID " + proId + " does not exist in the Product table");
        }
        String sql = "INSERT INTO Stock (proId, stockQuantity, lastUpdated) VALUES (?, ?, GETDATE())";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            stmt.setInt(2, quantity);
            stmt.executeUpdate();
        }
    }

    /**
     * Edits the stock quantity of a product to a new value.
     * 
     * @param proId     the product ID
     * @param newQuantity the new stock quantity (must be >= 0)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void updateStockQuantity(String proId, int newQuantity) throws SQLException, ClassNotFoundException {
        if (newQuantity < 0) return;
        String sql = "UPDATE Stock SET stockQuantity = ?, lastUpdated = GETDATE() WHERE proId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setString(2, proId);
            stmt.executeUpdate();
        }
    }

    /**
     * Gets the stock information for a specific product.
     * 
     * @param proId the product ID
     * @return Stock object or null if not found
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Stock getStockByProductId(String proId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT proId, stockQuantity, lastUpdated FROM Stock WHERE proId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            try (ResultSet rs = stmt.executeQuery()) {
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
}