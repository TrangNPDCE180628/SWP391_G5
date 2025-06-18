/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Staff;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    // CREATE
    public void create(Staff staff) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Staff (staffId, staffName, staffFullName, staffPassword, staffGender, staffImage, staffGmail, staffPhone, staffPosition) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staff.getStaffId());
            stmt.setString(2, staff.getStaffName());
            stmt.setString(3, staff.getStaffFullName());
            stmt.setString(4, staff.getStaffPassword());
            stmt.setString(5, staff.getStaffGender());
            stmt.setString(6, staff.getStaffImage());
            stmt.setString(7, staff.getStaffGmail());
            stmt.setString(8, staff.getStaffPhone());
            stmt.setString(9, staff.getStaffPosition());
            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<Staff> getAll() throws SQLException, ClassNotFoundException {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM Staff";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffId(rs.getString("staffId"));
                staff.setStaffName(rs.getString("staffName"));
                staff.setStaffFullName(rs.getString("staffFullName"));
                staff.setStaffPassword(rs.getString("staffPassword"));
                staff.setStaffGender(rs.getString("staffGender"));
                staff.setStaffImage(rs.getString("staffImage"));
                staff.setStaffGmail(rs.getString("staffGmail"));
                staff.setStaffPhone(rs.getString("staffPhone"));
                staff.setStaffPosition(rs.getString("staffPosition"));
                list.add(staff);
            }
        }
        return list;
    }

    // READ BY ID
    public Staff getById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Staff WHERE staffId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Staff staff = new Staff();
                    staff.setStaffId(rs.getString("staffId"));
                    staff.setStaffName(rs.getString("staffName"));
                    staff.setStaffFullName(rs.getString("staffFullName"));
                    staff.setStaffPassword(rs.getString("staffPassword"));
                    staff.setStaffGender(rs.getString("staffGender"));
                    staff.setStaffImage(rs.getString("staffImage"));
                    staff.setStaffGmail(rs.getString("staffGmail"));
                    staff.setStaffPhone(rs.getString("staffPhone"));
                    staff.setStaffPosition(rs.getString("staffPosition"));
                    return staff;
                }
            }
        }
        return null;
    }

    // UPDATE
    public void update(Staff staff) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Staff SET staffName = ?, staffFullName = ?, staffPassword = ?, staffGender = ?, staffImage = ?, staffGmail = ?, staffPhone = ?, staffPosition = ? "
                + "WHERE staffId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staff.getStaffName());
            stmt.setString(2, staff.getStaffFullName());
            stmt.setString(3, staff.getStaffPassword());
            stmt.setString(4, staff.getStaffGender());
            stmt.setString(5, staff.getStaffImage());
            stmt.setString(6, staff.getStaffGmail());
            stmt.setString(7, staff.getStaffPhone());
            stmt.setString(8, staff.getStaffPosition());
            stmt.setString(9, staff.getStaffId());
            stmt.executeUpdate();
        }
    }
    
    // Update no pass
    public void updateProfileInfo(Staff staff) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Staff SET staffFullName = ?, staffGender = ?, staffImage = ?, staffGmail = ?, staffPhone = ?, staffPosition = ? "
                + "WHERE staffId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, staff.getStaffFullName());
            stmt.setString(2, staff.getStaffGender());
            stmt.setString(3, staff.getStaffImage());
            stmt.setString(4, staff.getStaffGmail());
            stmt.setString(5, staff.getStaffPhone());
            stmt.setString(6, staff.getStaffPosition());
            stmt.setString(7, staff.getStaffId());

            stmt.executeUpdate();
        }
    }

    // DELETE
    public void delete(String staffId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Staff WHERE staffId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staffId);
            stmt.executeUpdate();
        }
    }
}
