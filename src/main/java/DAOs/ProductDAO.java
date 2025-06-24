package DAOs;

import Models.Product;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProductDAO {

    // CREATE
    public void insertProduct(Product product) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Product (proId, proName, proDescription, proPrice, proImageUrl, proTypeId) VALUES (?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProId());
            stmt.setString(2, product.getProName());
            stmt.setString(3, product.getProDescription());
            stmt.setBigDecimal(4, product.getProPrice());
            stmt.setString(5, product.getProImageUrl());
            stmt.setInt(6, product.getProTypeId());

            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<Product> getAllProducts() throws SQLException, ClassNotFoundException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Product";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setProId(rs.getString("proId"));
                p.setProName(rs.getString("proName"));
                p.setProDescription(rs.getString("proDescription"));
                p.setProPrice(rs.getBigDecimal("proPrice"));
                p.setProImageUrl(rs.getString("proImageUrl"));
                p.setProTypeId(rs.getInt("proTypeId"));

                list.add(p);
            }
        }
        return list;
    }

    // READ BY ID
    public Product getProductById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Product WHERE proId = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Product p = new Product();
                p.setProId(rs.getString("proId"));
                p.setProName(rs.getString("proName"));
                p.setProDescription(rs.getString("proDescription"));
                p.setProPrice(rs.getBigDecimal("proPrice"));
                p.setProImageUrl(rs.getString("proImageUrl"));
                p.setProTypeId(rs.getInt("proTypeId"));
                return p;
            }
        }
        return null;
    }

    // UPDATE
    public void updateProduct(Product product) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET proName=?, proDescription=?, proPrice=?, proImageUrl=?, proTypeId=? WHERE proId=?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProName());
            stmt.setString(2, product.getProDescription());
            stmt.setBigDecimal(3, product.getProPrice());
            stmt.setString(4, product.getProImageUrl());
            stmt.setInt(5, product.getProTypeId());
            stmt.setString(6, product.getProId());

            stmt.executeUpdate();
        }
    }

    // DELETE
    public void deleteProduct(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Product WHERE proId = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    // Get products by category (proTypeId)
    public List<Product> getByTypeId(int typeId) throws SQLException, ClassNotFoundException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE proTypeId = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, typeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setProId(rs.getString("proId"));
                p.setProName(rs.getString("proName"));
                p.setProDescription(rs.getString("proDescription"));
                p.setProPrice(rs.getBigDecimal("proPrice"));
                p.setProImageUrl(rs.getString("proImageUrl"));
                p.setProTypeId(rs.getInt("proTypeId"));
                list.add(p);
            }
        }
        return list;
    }
}
