/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Category;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // CREATE
    public void insertCategory(Category category) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Category (cateName) VALUES (?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCateName());
            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<Category> getAllCategories() throws SQLException, ClassNotFoundException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT cateId, cateName FROM Category";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Category c = new Category();
                c.setCateId(rs.getInt("cateId"));
                c.setCateName(rs.getString("cateName"));
                list.add(c);
            }
        }
        return list;
    }

    // READ BY ID
    public Category getCategoryById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT cateId, cateName FROM Category WHERE cateId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Category(rs.getInt("cateId"), rs.getString("cateName"));
            }
        }
        return null;
    }

    // UPDATE
    public void updateCategory(Category category) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Category SET cateName = ? WHERE cateId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCateName());
            stmt.setInt(2, category.getCateId());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void deleteCategory(int cateId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Category WHERE cateId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cateId);
            stmt.executeUpdate();
        }
    }
}

