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
        String sql = "INSERT INTO Voucher (codeName, voucherDescription, discountType, discountValue, "
                + "maxDiscountValue, minOrderAmount, startDate, endDate, voucherActive, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, voucher.getCodeName());
            stmt.setString(2, voucher.getVoucherDescription());
            stmt.setString(3, voucher.getDiscountType());
            stmt.setBigDecimal(4, voucher.getDiscountValue());
            stmt.setBigDecimal(5, voucher.getMaxDiscountValue());
            stmt.setBigDecimal(6, voucher.getMinOrderAmount());
            stmt.setDate(7, voucher.getStartDate());
            stmt.setDate(8, voucher.getEndDate());
            stmt.setBoolean(9, voucher.isVoucherActive());
            stmt.setInt(10, voucher.getQuantity());

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

    public List<Voucher> getAllActive() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Voucher WHERE voucherActive = 1 AND startDate <= CAST(GETDATE() AS DATE) AND endDate >= CAST(GETDATE() AS DATE)";
        List<Voucher> list = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(extractVoucher(rs));
            }
        }
        return list;
    }

    // Update
    public void update(Voucher voucher) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Voucher SET codeName=?, voucherDescription=?, discountType=?, discountValue=?, "
                + "maxDiscountValue=?, minOrderAmount=?, startDate=?, endDate=?, voucherActive=?, quantity=? WHERE voucherId=?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voucher.getCodeName());
            stmt.setString(2, voucher.getVoucherDescription());
            stmt.setString(3, voucher.getDiscountType());
            stmt.setBigDecimal(4, voucher.getDiscountValue());
            stmt.setBigDecimal(5, voucher.getMaxDiscountValue());
            stmt.setBigDecimal(6, voucher.getMinOrderAmount());
            stmt.setDate(7, voucher.getStartDate());
            stmt.setDate(8, voucher.getEndDate());
            stmt.setBoolean(9, voucher.isVoucherActive());
            stmt.setInt(10, voucher.getQuantity());
            stmt.setInt(11, voucher.getVoucherId());

            stmt.executeUpdate();
        }
    }

    // Update quantity only
    public void updateQuantity(int voucherId, int newQuantity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Voucher SET quantity = ? WHERE voucherId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, voucherId);
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

    // Get by code
    public Voucher getByCode(String codeName) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Voucher WHERE codeName = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codeName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractVoucher(rs);
            }
        }
        return null;
    }

    public boolean isCodeNameExists(String codeName) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Voucher WHERE codeName = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codeName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean isCodeNameExistsForOtherId(String codeName, int voucherId) {
        String sql = "SELECT COUNT(*) FROM Voucher WHERE codeName = ? AND voucherId <> ?";
        try ( Connection con = DBContext.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codeName);
            ps.setInt(2, voucherId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper method: convert ResultSet to Voucher object
    private Voucher extractVoucher(ResultSet rs) throws SQLException {
        return new Voucher(
                rs.getInt("voucherId"),
                rs.getString("codeName"),
                rs.getString("voucherDescription"),
                rs.getString("discountType"),
                rs.getBigDecimal("discountValue"),
                rs.getBigDecimal("maxDiscountValue"),
                rs.getBigDecimal("minOrderAmount"),
                rs.getDate("startDate"),
                rs.getDate("endDate"),
                rs.getBoolean("voucherActive"),
                rs.getInt("quantity")
        );
    }
}
