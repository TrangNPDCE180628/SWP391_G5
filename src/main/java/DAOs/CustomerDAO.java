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
        String sql = "INSERT INTO Customer (cusId, username, cusPassword, cusFullName, cusGender, cusImage, cusGmail, cusPhone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCusId());
            stmt.setString(2, customer.getUsername());
            stmt.setString(3, customer.getCusPassword());
            stmt.setString(4, customer.getCusFullName());
            stmt.setString(5, customer.getCusGender());
            stmt.setString(6, customer.getCusImage());
            stmt.setString(7, customer.getCusGmail());
            stmt.setString(8, customer.getCusPhone());

            stmt.executeUpdate();
        }
    }

    // READ: get all
    public List<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
                customers.add(customer);
            }
        }

        return customers;
    }

    // READ: get by ID
    public Customer getCustomerById(String cusId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE cusId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cusId);
            try (ResultSet rs = stmt.executeQuery()) {
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
                    return customer;
                }
            }
        }
        return null;
    }

    // UPDATE
    public void updateCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Customer SET username=?, cusPassword=?, cusFullName=?, cusGender=?, cusImage=?, cusGmail=?, cusPhone=? WHERE cusId=?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getUsername());
            stmt.setString(2, customer.getCusPassword());
            stmt.setString(3, customer.getCusFullName());
            stmt.setString(4, customer.getCusGender());
            stmt.setString(5, customer.getCusImage());
            stmt.setString(6, customer.getCusGmail());
            stmt.setString(7, customer.getCusPhone());
            stmt.setString(8, customer.getCusId());

            stmt.executeUpdate();
        }
    }

    // DELETE
    public void deleteCustomer(String cusId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Customer WHERE cusId=?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cusId);
            stmt.executeUpdate();
        }
    }
    //Check exist
    public boolean checkUsernameExists(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT 1 FROM Customer WHERE username = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Trả về true nếu tìm thấy username
            }
        }
    }
    
    public String generateNextCusId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT TOP 1 cusId FROM Customer ORDER BY cusId DESC";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("cusId"); // VD: "C0012"
                try {
                    int num = Integer.parseInt(lastId.substring(1)); // Bỏ ký tự 'C' và parse số
                    return String.format("C%04d", num + 1); // VD: "C0013"
                } catch (NumberFormatException e) {
                    // Trường hợp chuỗi không đúng định dạng
                    throw new SQLException("Invalid cusId format: " + lastId);
                }
            } else {
                return "C0001"; // Nếu chưa có dữ liệu nào
            }
        }
    
}
}
