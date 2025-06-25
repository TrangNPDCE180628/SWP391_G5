package DAOs;

import Models.Product;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProductDAO {

    // INSERT
    public void insertProduct(Product product) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Product (proId, proTypeId, proName, proDescription, proPrice, proImageUrl) VALUES (?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProId());
            stmt.setInt(2, product.getProTypeId());
            stmt.setString(3, product.getProName());
            stmt.setString(4, product.getProDescription());
            stmt.setBigDecimal(5, product.getProPrice());
            stmt.setString(6, product.getProImageMain());
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
                p.setProTypeId(rs.getInt("proTypeId"));
                p.setProName(rs.getString("proName"));
                p.setProDescription(rs.getString("proDescription"));
                p.setProPrice(rs.getBigDecimal("proPrice"));
                p.setProImageMain(rs.getString("proImageMain"));
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
                p.setProTypeId(rs.getInt("proTypeId"));
                p.setProName(rs.getString("proName"));
                p.setProDescription(rs.getString("proDescription"));
                p.setProPrice(rs.getBigDecimal("proPrice"));
                p.setProImageMain(rs.getString("proImageUrl"));
                return p;
            }
        }
        return null;
    }

    // UPDATE
    public void updateProduct(Product product) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET proTypeId=?, proName=?, proDescription=?, proPrice=?, proImageUrl=? WHERE proId=?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.getProTypeId());
            stmt.setString(2, product.getProName());
            stmt.setString(3, product.getProDescription());
            stmt.setBigDecimal(4, product.getProPrice());
            stmt.setString(5, product.getProImageMain());
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

    // PAGINATED GET
    public List<Product> getAll(int page, int productsPerPage) throws SQLException, ClassNotFoundException {
        int offset = (page - 1) * productsPerPage;
        String sql = "SELECT * FROM Product ORDER BY proId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Product> products = new ArrayList<>();

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, productsPerPage);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProId(rs.getString("proId"));
                product.setProTypeId(rs.getInt("proTypeId"));
                product.setProName(rs.getString("proName"));
                product.setProDescription(rs.getString("proDescription"));
                product.setProPrice(rs.getBigDecimal("proPrice"));
                product.setProImageMain(rs.getString("proImageUrl"));
                products.add(product);
            }
        }
        return products;
    }

    // COUNT ALL
    public int countAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Product";
        try ( Connection conn = DBContext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // SEARCH BY NAME
    public List<Product> searchByName(String searchTerm, int page, int productsPerPage) throws SQLException, ClassNotFoundException {
        int offset = (page - 1) * productsPerPage;
        String sql = "SELECT * FROM Product WHERE proName LIKE ? ORDER BY proId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Product> products = new ArrayList<>();

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setInt(2, offset);
            stmt.setInt(3, productsPerPage);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProId(rs.getString("proId"));
                product.setProTypeId(rs.getInt("proTypeId"));
                product.setProName(rs.getString("proName"));
                product.setProDescription(rs.getString("proDescription"));
                product.setProPrice(rs.getBigDecimal("proPrice"));
                product.setProImageMain(rs.getString("proImageUrl"));
                products.add(product);
            }
        }
        return products;
    }

    // COUNT SEARCH RESULTS
    public int countSearchResults(String searchTerm) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Product WHERE proName LIKE ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
