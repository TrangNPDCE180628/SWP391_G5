package DAOs;

import Models.Product;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public void create(Product product) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Product (proId, cateId, brandId, proName, proDescription, proPrice, proStockQuantity, "
                + "proWarrantyMonths, proModel, proColor, proWeight, proDimensions, proOrigin, proMaterial, "
                + "proConnectivity, proImageMain, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProId());
            stmt.setInt(2, product.getCateId());
            stmt.setInt(3, product.getBrandId());
            stmt.setString(4, product.getProName());
            stmt.setString(5, product.getProDescription());
            stmt.setDouble(6, product.getProPrice());
            stmt.setInt(7, product.getProStockQuantity());
            stmt.setInt(8, product.getProWarrantyMonths());
            stmt.setString(9, product.getProModel());
            stmt.setString(10, product.getProColor());
            stmt.setDouble(11, product.getProWeight());
            stmt.setString(12, product.getProDimensions());
            stmt.setString(13, product.getProOrigin());
            stmt.setString(14, product.getProMaterial());
            stmt.setString(15, product.getProConnectivity());
            stmt.setString(16, product.getProImageMain());
            stmt.setInt(17, product.getStatus());
            stmt.executeUpdate();
        }
    }

    public Product getById(String proId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Product WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        }
        return null;
    }

    public List<Product> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Product";
        List<Product> products = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    public void update(Product product) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET cateId=?, brandId=?, proName=?, proDescription=?, proPrice=?, proStockQuantity=?, "
                + "proWarrantyMonths=?, proModel=?, proColor=?, proWeight=?, proDimensions=?, proOrigin=?, proMaterial=?, "
                + "proConnectivity=?, proImageMain=?, status=? WHERE proId=?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.getCateId());
            stmt.setInt(2, product.getBrandId());
            stmt.setString(3, product.getProName());
            stmt.setString(4, product.getProDescription());
            stmt.setDouble(5, product.getProPrice());
            stmt.setInt(6, product.getProStockQuantity());
            stmt.setInt(7, product.getProWarrantyMonths());
            stmt.setString(8, product.getProModel());
            stmt.setString(9, product.getProColor());
            stmt.setDouble(10, product.getProWeight());
            stmt.setString(11, product.getProDimensions());
            stmt.setString(12, product.getProOrigin());
            stmt.setString(13, product.getProMaterial());
            stmt.setString(14, product.getProConnectivity());
            stmt.setString(15, product.getProImageMain());
            stmt.setInt(16, product.getStatus());
            stmt.setString(17, product.getProId());
            stmt.executeUpdate();
        }
    }

    public void delete(String proId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Product WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            stmt.executeUpdate();
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getString("proId"),
                rs.getInt("cateId"),
                rs.getInt("brandId"),
                rs.getString("proName"),
                rs.getString("proDescription"),
                rs.getDouble("proPrice"),
                rs.getInt("proStockQuantity"),
                rs.getInt("proWarrantyMonths"),
                rs.getString("proModel"),
                rs.getString("proColor"),
                rs.getDouble("proWeight"),
                rs.getString("proDimensions"),
                rs.getString("proOrigin"),
                rs.getString("proMaterial"),
                rs.getString("proConnectivity"),
                rs.getString("proImageMain"),
                rs.getInt("status")
        );
    }

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
                products.add(mapResultSetToProduct(rs));
            }
        }

        return products;
    }

    public int countAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Product";
        try ( Connection conn = DBContext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

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

    public List<Product> getByTypeId(int typeId, int page, int productsPerPage) throws SQLException, ClassNotFoundException {
        int offset = (page - 1) * productsPerPage;
        String sql = "SELECT * FROM Product WHERE cateId = ? ORDER BY proId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Product> products = new ArrayList<>();

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeId);
            stmt.setInt(2, offset);
            stmt.setInt(3, productsPerPage);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    // Count products by type
    public int countByTypeId(int typeId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Products WHERE proTypeId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    // Get products with pagination

    public List<Product> getAll(int page, int productsPerPage) throws SQLException, ClassNotFoundException {
        int offset = (page - 1) * productsPerPage;
        String sql = "SELECT * FROM Product ORDER BY proId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Product> products = new ArrayList<>();

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, offset);
            stmt.setInt(2, productsPerPage);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getString("proId"),
                        rs.getInt("cateId"),
                        rs.getInt("brandId"),
                        rs.getString("proName"),
                        rs.getString("proDescription"),
                        rs.getDouble("proPrice"),
                        rs.getInt("proStockQuantity"),
                        rs.getInt("proWarrantyMonths"),
                        rs.getString("proModel"),
                        rs.getString("proColor"),
                        rs.getDouble("proWeight"),
                        rs.getString("proDimensions"),
                        rs.getString("proOrigin"),
                        rs.getString("proMaterial"),
                        rs.getString("proConnectivity"),
                        rs.getString("proImageMain"),
                        rs.getInt("status")
                );
                products.add(product);
            }
        }
        return products;
    }

}
