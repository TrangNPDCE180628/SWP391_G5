/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Brand;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrandDAO {

    // CREATE
    public void insertBrand(Brand brand) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Brand (brandName) VALUES (?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, brand.getBrandName());
            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<Brand> getAllBrands() throws SQLException, ClassNotFoundException {
        List<Brand> list = new ArrayList<>();
        String sql = "SELECT brandId, brandName FROM Brand";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Brand brand = new Brand();
                brand.setBrandId(rs.getInt("brandId"));
                brand.setBrandName(rs.getString("brandName"));
                list.add(brand);
            }
        }
        return list;
    }

    // READ BY ID
    public Brand getBrandById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT brandId, brandName FROM Brand WHERE brandId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Brand(rs.getInt("brandId"), rs.getString("brandName"));
            }
        }
        return null;
    }

    // UPDATE
    public void updateBrand(Brand brand) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Brand SET brandName = ? WHERE brandId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, brand.getBrandName());
            stmt.setInt(2, brand.getBrandId());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void deleteBrand(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Brand WHERE brandId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

