/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Customer;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // CREATE
    public void insertCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Customer (cusId, username, cusPassword, cusFullName, cusGender, cusImage, cusGmail, cusPhone, cusAddress) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCusId());
            stmt.setString(2, customer.getUsername());
            stmt.setString(3, customer.getCusPassword());
            stmt.setString(4, customer.getCusFullName()); // Use getCusFullName() method
            stmt.setString(5, customer.getCusGender());
            stmt.setString(6, customer.getCusImage());
            stmt.setString(7, customer.getCusGmail());
            stmt.setString(8, customer.getCusPhone());
            stmt.setString(9, customer.getCusAddress());

            stmt.executeUpdate();
        }
    }

    // READ: get all
    public List<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCusId(rs.getString("cusId"));
                customer.setUsername(rs.getString("username"));
                customer.setCusPassword(rs.getString("cusPassword"));
                customer.setCusFullName(rs.getString("cusFullName"));
                customer.setCusGender(rs.getString("cusGender"));
                customer.setCusImage(rs.getString("cusImage"));
                customer.setCusGmail(rs.getString("cusGmail"));
                customer.setCusPhone(rs.getString("cusPhone"));
                customer.setCusAddress(rs.getString("cusAddress"));
                customers.add(customer);
            }
        }

        return customers;
    }

    // READ: get by ID
    public Customer getCustomerById(String cusId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE cusId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cusId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCusId(rs.getString("cusId"));
                    customer.setUsername(rs.getString("username"));
                    customer.setCusPassword(rs.getString("cusPassword"));
                    customer.setCusFullName(rs.getString("cusFullName"));
                    customer.setCusGender(rs.getString("cusGender"));
                    customer.setCusImage(rs.getString("cusImage"));
                    customer.setCusGmail(rs.getString("cusGmail"));
                    customer.setCusPhone(rs.getString("cusPhone"));
                    customer.setCusAddress(rs.getString("cusAddress"));
                    return customer;
                }
            }
        }
        return null;
    }

    // UPDATE
    public void updateCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Customer SET username=?, cusPassword=?, cusFullName=?, cusGender=?, cusImage=?, cusGmail=?, cusPhone=?, cusAddress=? WHERE cusId=?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getUsername());
            stmt.setString(2, customer.getCusPassword());
            stmt.setString(3, customer.getCusFullName()); // Use getCusFullName() method
            stmt.setString(4, customer.getCusGender());
            stmt.setString(5, customer.getCusImage());
            stmt.setString(6, customer.getCusGmail());
            stmt.setString(7, customer.getCusPhone());
            stmt.setString(8, customer.getCusAddress());
            stmt.setString(9, customer.getCusId());

            stmt.executeUpdate();
        }
    }

    //Update password customer
    public boolean updatePassword(String email, String newPassword) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Customer SET cusPassword = ? WHERE cusGmail = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE
    public void deleteCustomer(String cusId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Customer WHERE cusId=?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cusId);
            stmt.executeUpdate();
        }
    }

    //Check exist
    public boolean checkUsernameExists(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT 1 FROM Customer WHERE username = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try ( ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Trả về true nếu tìm thấy username
            }
        }
    }

    public String generateNextCusId() throws SQLException, ClassNotFoundException {
        // Sử dụng UUID để đảm bảo tính duy nhất
        String uuid = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String cusId = "C" + uuid.toUpperCase();
        
        // Kiểm tra xem ID đã tồn tại chưa (rất hiếm khi xảy ra với UUID)
        String checkSql = "SELECT COUNT(*) FROM Customer WHERE cusId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            
            stmt.setString(1, cusId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Nếu trùng (rất hiếm), tạo ID mới
                    uuid = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                    cusId = "C" + uuid.toUpperCase();
                }
            }
        }
        
        System.out.println("Generated Customer ID: " + cusId);
        return cusId;
    }

    public String getCusNameById(String cusId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT cusFullName FROM Customer WHERE cusId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cusId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cusFullName");
                }
            }
        }
        return null; // Not found
    }

    public boolean checkEmailExists(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT 1 FROM Customer WHERE cusGmail = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try ( ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Return true if email exists
            }
        }
    }
}
