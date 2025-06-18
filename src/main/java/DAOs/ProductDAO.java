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
        String sql = "INSERT INTO Product VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProId());
            stmt.setInt(2, product.getCateId());
            stmt.setInt(3, product.getBrandId());
            stmt.setString(4, product.getProName());
            stmt.setString(5, product.getProDescription());
            stmt.setBigDecimal(6, product.getProPrice());
            stmt.setInt(7, product.getProStockQuantity());
            stmt.setInt(8, product.getProWarrantyMonths());
            stmt.setString(9, product.getProModel());
            stmt.setString(10, product.getProColor());
            stmt.setBigDecimal(11, product.getProWeight());
            stmt.setString(12, product.getProDimensions());
            stmt.setString(13, product.getProOrigin());
            stmt.setString(14, product.getProMaterial());
            stmt.setString(15, product.getProConnectivity());
            stmt.setString(16, product.getProImageMain());
            stmt.setInt(17, product.getStatus());
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
                p.setCateId(rs.getInt("cateId"));
                p.setBrandId(rs.getInt("brandId"));
                p.setProName(rs.getString("proName"));
                p.setProDescription(rs.getString("proDescription"));
                p.setProPrice(rs.getBigDecimal("proPrice"));
                p.setProStockQuantity(rs.getInt("proStockQuantity"));
                p.setProWarrantyMonths(rs.getInt("proWarrantyMonths"));
                p.setProModel(rs.getString("proModel"));
                p.setProColor(rs.getString("proColor"));
                p.setProWeight(rs.getBigDecimal("proWeight"));
                p.setProDimensions(rs.getString("proDimensions"));
                p.setProOrigin(rs.getString("proOrigin"));
                p.setProMaterial(rs.getString("proMaterial"));
                p.setProConnectivity(rs.getString("proConnectivity"));
                p.setProImageMain(rs.getString("proImageMain"));
                p.setStatus(rs.getInt("status"));
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
                p.setCateId(rs.getInt("cateId"));
                p.setBrandId(rs.getInt("brandId"));
                p.setProName(rs.getString("proName"));
                p.setProDescription(rs.getString("proDescription"));
                p.setProPrice(rs.getBigDecimal("proPrice"));
                p.setProStockQuantity(rs.getInt("proStockQuantity"));
                p.setProWarrantyMonths(rs.getInt("proWarrantyMonths"));
                p.setProModel(rs.getString("proModel"));
                p.setProColor(rs.getString("proColor"));
                p.setProWeight(rs.getBigDecimal("proWeight"));
                p.setProDimensions(rs.getString("proDimensions"));
                p.setProOrigin(rs.getString("proOrigin"));
                p.setProMaterial(rs.getString("proMaterial"));
                p.setProConnectivity(rs.getString("proConnectivity"));
                p.setProImageMain(rs.getString("proImageMain"));
                p.setStatus(rs.getInt("status"));
                return p;
            }
        }
        return null;
    }

    // UPDATE
    public void updateProduct(Product product) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET cateId=?, brandId=?, proName=?, proDescription=?, proPrice=?, proStockQuantity=?, proWarrantyMonths=?, proModel=?, proColor=?, proWeight=?, proDimensions=?, proOrigin=?, proMaterial=?, proConnectivity=?, proImageMain=?, status=? WHERE proId=?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.getCateId());
            stmt.setInt(2, product.getBrandId());
            stmt.setString(3, product.getProName());
            stmt.setString(4, product.getProDescription());
            stmt.setBigDecimal(5, product.getProPrice());
            stmt.setInt(6, product.getProStockQuantity());
            stmt.setInt(7, product.getProWarrantyMonths());
            stmt.setString(8, product.getProModel());
            stmt.setString(9, product.getProColor());
            stmt.setBigDecimal(10, product.getProWeight());
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

    // DELETE
    public void deleteProduct(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Product WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    // Get product by category
    public List<Product> getByCategoryId(int cateId, int page, int productsPerPage) throws SQLException, ClassNotFoundException {
        int offset = (page - 1) * productsPerPage;
        String sql = "SELECT * FROM Product WHERE cateId = ? ORDER BY proId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Product> products = new ArrayList<>();

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cateId);
            stmt.setInt(2, offset);
            stmt.setInt(3, productsPerPage);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProId(rs.getString("proId"));
                product.setCateId(rs.getInt("cateId"));
                product.setBrandId(rs.getInt("brandId"));
                product.setProName(rs.getString("proName"));
                product.setProDescription(rs.getString("proDescription"));
                product.setProPrice(rs.getBigDecimal("proPrice"));
                product.setProStockQuantity(rs.getInt("proStockQuantity"));
                product.setProWarrantyMonths(rs.getInt("proWarrantyMonths"));
                product.setProModel(rs.getString("proModel"));
                product.setProColor(rs.getString("proColor"));
                product.setProWeight(rs.getBigDecimal("proWeight"));
                product.setProDimensions(rs.getString("proDimensions"));
                product.setProOrigin(rs.getString("proOrigin"));
                product.setProMaterial(rs.getString("proMaterial"));
                product.setProConnectivity(rs.getString("proConnectivity"));
                product.setProImageMain(rs.getString("proImageMain"));
                product.setStatus(rs.getInt("status"));

                products.add(product);
            }
        }

        return products;
    }

    //search by name
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
                product.setCateId(rs.getInt("cateId"));
                product.setBrandId(rs.getInt("brandId"));
                product.setProName(rs.getString("proName"));
                product.setProDescription(rs.getString("proDescription"));
                product.setProPrice(rs.getBigDecimal("proPrice"));
                product.setProStockQuantity(rs.getInt("proStockQuantity"));
                product.setProWarrantyMonths(rs.getInt("proWarrantyMonths"));
                product.setProModel(rs.getString("proModel"));
                product.setProColor(rs.getString("proColor"));
                product.setProWeight(rs.getBigDecimal("proWeight"));
                product.setProDimensions(rs.getString("proDimensions"));
                product.setProOrigin(rs.getString("proOrigin"));
                product.setProMaterial(rs.getString("proMaterial"));
                product.setProConnectivity(rs.getString("proConnectivity"));
                product.setProImageMain(rs.getString("proImageMain"));
                product.setStatus(rs.getInt("status"));

                products.add(product);
            }
        }

        return products;
    }

    //count search
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

    //count by typeid
    public int countByCategoryId(int cateId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Product WHERE cateId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cateId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

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
                product.setCateId(rs.getInt("cateId"));
                product.setBrandId(rs.getInt("brandId"));
                product.setProName(rs.getString("proName"));
                product.setProDescription(rs.getString("proDescription"));
                product.setProPrice(rs.getBigDecimal("proPrice"));
                product.setProStockQuantity(rs.getInt("proStockQuantity"));
                product.setProWarrantyMonths(rs.getInt("proWarrantyMonths"));
                product.setProModel(rs.getString("proModel"));
                product.setProColor(rs.getString("proColor"));
                product.setProWeight(rs.getBigDecimal("proWeight")); // BigDecimal
                product.setProDimensions(rs.getString("proDimensions"));
                product.setProOrigin(rs.getString("proOrigin"));
                product.setProMaterial(rs.getString("proMaterial"));
                product.setProConnectivity(rs.getString("proConnectivity"));
                product.setProImageMain(rs.getString("proImageMain"));
                product.setStatus(rs.getInt("status"));
                products.add(product);
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

    public List<Product> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Product getById(int productId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void update(Product product) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
