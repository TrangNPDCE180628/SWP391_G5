/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Voucher;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SE18-CE180628-Nguyen Pham Doan Trang
 */
public class VoucherDAO {

    // Create
    public void create(Voucher voucher) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Voucher (codeName, voucherDescription, discountType, discountValue, minOrderAmount, start_date, end_date, voucherActive) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, voucher.getCodeName());
            stmt.setString(2, voucher.getVoucherDescription());
            stmt.setString(3, voucher.getDiscountType());
            stmt.setBigDecimal(4, voucher.getDiscountValue());
            stmt.setBigDecimal(5, voucher.getMinOrderAmount());
            stmt.setDate(6, voucher.getStartDate());
            stmt.setDate(7, voucher.getEndDate());
            stmt.setBoolean(8, voucher.isVoucherActive());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                voucher.setVoucherId(rs.getInt(1));
            }
        }
    }

    // Read by ID
    public Voucher getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Voucher WHERE voucherId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractVoucher(rs);
            }
        }
        return null;
    }

    // Read all
    public List<Voucher> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Voucher";
        List<Voucher> list = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractVoucher(rs));
            }
        }
        return list;
    }

    // Update
    public void update(Voucher voucher) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Voucher SET codeName = ?, voucherDescription = ?, discountType = ?, discountValue = ?, minOrderAmount = ?, start_date = ?, end_date = ?, voucherActive = ? "
                + "WHERE voucherId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voucher.getCodeName());
            stmt.setString(2, voucher.getVoucherDescription());
            stmt.setString(3, voucher.getDiscountType());
            stmt.setBigDecimal(4, voucher.getDiscountValue());
            stmt.setBigDecimal(5, voucher.getMinOrderAmount());
            stmt.setDate(6, voucher.getStartDate());
            stmt.setDate(7, voucher.getEndDate());
            stmt.setBoolean(8, voucher.isVoucherActive());
            stmt.setInt(9, voucher.getVoucherId());

            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Voucher WHERE voucherId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Pagination
    public List<Voucher> getAll(int page, int pageSize) throws SQLException, ClassNotFoundException {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM Voucher ORDER BY voucherId DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Voucher> list = new ArrayList<>();

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractVoucher(rs));
            }
        }
        return list;
    }

    // Count all
    public int countAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Voucher";
        try ( Connection conn = DBContext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Helper method: convert ResultSet to Voucher object
    private Voucher extractVoucher(ResultSet rs) throws SQLException {
        return new Voucher(
                rs.getInt("voucherId"),
                rs.getString("codeName"),
                rs.getString("voucherDescription"),
                rs.getString("discountType"),
                rs.getBigDecimal("discountValue"),
                rs.getBigDecimal("minOrderAmount"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getBoolean("voucherActive")
        );
    }
}
