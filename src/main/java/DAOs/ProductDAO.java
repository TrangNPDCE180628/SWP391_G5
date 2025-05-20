package DAOs;

import Models.Product;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    // Create
    public void create(Product product) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Products (proName, proDescription, proPrice, proImage, proQuantity, proTypeId) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getProName());
            stmt.setString(2, product.getProDescription());
            stmt.setDouble(3, product.getProPrice());
            stmt.setString(4, product.getProImage());
            stmt.setInt(5, product.getProQuantity());
            stmt.setInt(6, product.getProTypeId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                product.setProId(rs.getInt(1));
            }
        }
    }

    // Read by ID
    public Product getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Products WHERE proId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("proId"),
                        rs.getString("proName"),
                        rs.getString("proDescription"),
                        rs.getDouble("proPrice"),
                        rs.getString("proImage"),
                        rs.getInt("proQuantity"),
                        rs.getInt("proTypeId")
                );
            }
            return null;
        }
    }

    // Read all
    public List<Product> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Products";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("proId"),
                        rs.getString("proName"),
                        rs.getString("proDescription"),
                        rs.getDouble("proPrice"),
                        rs.getString("proImage"),
                        rs.getInt("proQuantity"),
                        rs.getInt("proTypeId")
                ));
            }
            return products;
        }
    }

    // Read by type ID
    public List<Product> getByTypeId(int typeId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Products WHERE proTypeId = ?";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("proId"),
                        rs.getString("proName"),
                        rs.getString("proDescription"),
                        rs.getDouble("proPrice"),
                        rs.getString("proImage"),
                        rs.getInt("proQuantity"),
                        rs.getInt("proTypeId")
                ));
            }
            return products;
        }
    }

    // Update
    public void update(Product product) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Products SET proName = ?, proDescription = ?, proPrice = ?, proImage = ?, proQuantity = ?, proTypeId = ? WHERE proId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProName());
            stmt.setString(2, product.getProDescription());
            stmt.setDouble(3, product.getProPrice());
            stmt.setString(4, product.getProImage());
            stmt.setInt(5, product.getProQuantity());
            stmt.setInt(6, product.getProTypeId());
            stmt.setInt(7, product.getProId());
            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Products WHERE proId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Get products with pagination
    public List<Product> getAll(int page, int productsPerPage) throws SQLException, ClassNotFoundException {
        int offset = (page - 1) * productsPerPage;
        String sql = "SELECT * FROM Products ORDER BY proId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, productsPerPage);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("proId"),
                    rs.getString("proName"),
                    rs.getString("proDescription"),
                    rs.getDouble("proPrice"),
                    rs.getString("proImage"),
                    rs.getInt("proQuantity"),
                    rs.getInt("proTypeId")
                ));
            }
        }
        return products;
    }

    // Get products by type with pagination
    public List<Product> getByTypeId(int typeId, int page, int productsPerPage) throws SQLException, ClassNotFoundException {
        int offset = (page - 1) * productsPerPage;
        String sql = "SELECT * FROM Products WHERE proTypeId = ? ORDER BY proId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeId);
            stmt.setInt(2, offset);
            stmt.setInt(3, productsPerPage);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("proId"),
                    rs.getString("proName"),
                    rs.getString("proDescription"),
                    rs.getDouble("proPrice"),
                    rs.getString("proImage"),
                    rs.getInt("proQuantity"),
                    rs.getInt("proTypeId")
                ));
            }
        }
        return products;
    }

    // Search products by name with pagination
    public List<Product> searchByName(String searchTerm, int page, int productsPerPage) throws SQLException, ClassNotFoundException {
        int offset = (page - 1) * productsPerPage;
        String sql = "SELECT * FROM Products WHERE proName LIKE ? ORDER BY proId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setInt(2, offset);
            stmt.setInt(3, productsPerPage);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("proId"),
                    rs.getString("proName"),
                    rs.getString("proDescription"),
                    rs.getDouble("proPrice"),
                    rs.getString("proImage"),
                    rs.getInt("proQuantity"),
                    rs.getInt("proTypeId")
                ));
            }
        }
        return products;
    }

    // Count total products
    public int countAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Products";
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Count products by type
    public int countByTypeId(int typeId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Products WHERE proTypeId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Count search results
    public int countSearchResults(String searchTerm) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Products WHERE proName LIKE ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
} 