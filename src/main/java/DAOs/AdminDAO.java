package DAOs;

import Models.Admin;
import Ultis.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    // GET admin by ID
    public Admin getAdminById(String adminId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Admin WHERE adminId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, adminId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAdmin(rs);
                }
            }
        }
        return null;
    }

    // UPDATE admin info
    public void updateAdmin(Admin admin) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Admin SET adminName = ?, adminPassword = ?, adminGmail = ?, adminImage = ? WHERE adminId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getAdminFullName());
            stmt.setString(2, admin.getAdminPassword());
            stmt.setString(3, admin.getAdminGmail());
            stmt.setString(4, admin.getAdminImage());
            stmt.setString(5, admin.getAdminId());

            stmt.executeUpdate();
        }
    }

    public void updateProfileInfo(Admin admin) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Admin SET adminFullName = ?, adminGmail = ?, adminImage = ? WHERE adminId = ?";
        try (
                 Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, admin.getAdminFullName());
            stmt.setString(2, admin.getAdminGmail());
            stmt.setString(3, admin.getAdminImage());
            stmt.setString(4, admin.getAdminId());

            stmt.executeUpdate();
        }
    }

    //Get admin by name
    public Admin getAdminByName(String adminName) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Admin WHERE adminName = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, adminName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAdmin(rs);
                }
            }
        }
        return null;
    }

    // Helper method to map ResultSet to Admin object
    private Admin extractAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminId(rs.getString("adminId"));
        admin.setAdminFullName(rs.getString("adminName"));
        admin.setAdminPassword(rs.getString("adminPassword"));
        admin.setAdminGmail(rs.getString("adminGmail"));
        admin.setAdminImage(rs.getString("adminImage"));
        return admin;
    }
}
