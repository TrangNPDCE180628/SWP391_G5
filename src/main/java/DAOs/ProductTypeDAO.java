package DAOs;

import Models.ProductTypes;
import Ultis.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductTypeDAO {

    // Create a new product type
    public void addProductType(ProductTypes productType) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ProductType (proTypeName) VALUES (?)";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productType.getName());
            stmt.executeUpdate();
        }
    }

    // Update an existing product type
    public void updateProductType(ProductTypes productType) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE ProductType SET proTypeName = ? WHERE proTypeId = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productType.getName());
            stmt.setInt(2, productType.getId());
            stmt.executeUpdate();
        }
    }

    // Delete a product type
    public void deleteProductType(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ProductType WHERE proTypeId = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Get all product types
    public List<ProductTypes> getAllProductTypes() throws SQLException, ClassNotFoundException {
        List<ProductTypes> types = new ArrayList<>();
        String sql = "SELECT * FROM ProductType";
        try (Connection conn = DBContext.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ProductTypes type = new ProductTypes();
                type.setId(rs.getInt("proTypeId"));
                type.setName(rs.getString("proTypeName"));
                types.add(type);
            }
        }
        return types;
    }

    // Get product type by ID
    public ProductTypes getProductTypeById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM ProductType WHERE proTypeId = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ProductTypes type = new ProductTypes();
                type.setId(rs.getInt("proTypeId"));
                type.setName(rs.getString("proTypeName"));
                return type;
            }
        }
        return null;
    }
}