/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Discount;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO {

    // Create a new discount
    public void create(Discount discount) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Discount (proId, discountType, discountValue, start_date, end_date, active, adminId) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, discount.getProId());
            stmt.setString(2, discount.getDiscountType());
            stmt.setDouble(3, discount.getDiscountValue());
            stmt.setDate(4, new java.sql.Date(discount.getStartDate().getTime()));
            stmt.setDate(5, new java.sql.Date(discount.getEndDate().getTime()));
            stmt.setBoolean(6, discount.isActive());
            stmt.setString(7, discount.getAdminId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                discount.setDiscountId(rs.getInt(1));
            }
        }
    }

    // Read discount by ID
    public Discount getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Discount WHERE discountId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Discount(
                    rs.getInt("discountId"),
                    rs.getString("proId"),
                    rs.getString("discountType"),
                    rs.getDouble("discountValue"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    rs.getBoolean("active"),
                    rs.getString("adminId")
                );
            }
            return null;
        }
    }

    // Read all discounts
    public List<Discount> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Discount";
        List<Discount> discounts = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                discounts.add(new Discount(
                    rs.getInt("discountId"),
                    rs.getString("proId"),
                    rs.getString("discountType"),
                    rs.getDouble("discountValue"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    rs.getBoolean("active"),
                    rs.getString("adminId")
                ));
            }
            return discounts;
        }
    }

    // Update a discount
    public void update(Discount discount) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Discount SET proId = ?, discountType = ?, discountValue = ?, start_date = ?, " +
                     "end_date = ?, active = ?, adminId = ? WHERE discountId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, discount.getProId());
            stmt.setString(2, discount.getDiscountType());
            stmt.setDouble(3, discount.getDiscountValue());
            stmt.setDate(4, new java.sql.Date(discount.getStartDate().getTime()));
            stmt.setDate(5, new java.sql.Date(discount.getEndDate().getTime()));
            stmt.setBoolean(6, discount.isActive());
            stmt.setString(7, discount.getAdminId());
            stmt.setInt(8, discount.getDiscountId());
            stmt.executeUpdate();
        }
    }

    // Delete a discount
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Discount WHERE discountId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Apply discount to product (calculate discounted price)
    public double applyDiscount(String proId, double originalPrice) throws SQLException, ClassNotFoundException {
        String sql = "SELECT discountType, discountValue FROM Discount WHERE proId = ? AND active = 1 " +
                     "AND start_date <= GETDATE() AND end_date >= GETDATE()";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String discountType = rs.getString("discountType");
                double discountValue = rs.getDouble("discountValue");
                if ("percentage".equals(discountType)) {
                    return originalPrice * (1 - discountValue / 100);
                } else if ("fixed".equals(discountType)) {
                    return Math.max(0, originalPrice - discountValue);
                }
            }
            return originalPrice;
        }
    }
}
