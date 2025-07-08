/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Ultis.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author SE18-CE180628-Nguyen Pham Doan Trang
 */
public class StockDAO {

    public int getStockByProductId(String proId) {
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
