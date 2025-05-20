package DAOs;

import Models.ProductTypes;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductTypeDAO {
    // Create
    public void create(ProductTypes productType) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ProductTypes (name) VALUES (?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, productType.getName());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                productType.setId(rs.getInt(1));
            }
        }
    }

    // Read by ID
    public ProductTypes getById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM ProductTypes WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductTypes(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }
            return null;
        }
    }

    // Read all
    public List<ProductTypes> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM ProductTypes";
        List<ProductTypes> productTypes = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productTypes.add(new ProductTypes(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
            return productTypes;
        }
    }

    // Update
    public void update(ProductTypes productType) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE ProductTypes SET name = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productType.getName());
            stmt.setInt(2, productType.getId());
            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ProductTypes WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
} 