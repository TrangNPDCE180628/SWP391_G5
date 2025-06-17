package DAOs;

import Models.ProductSpecification;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecDAO {

    // Create
    public void create(ProductSpecification spec) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ProductSpecification (product_id, cpu, ram, storage, screen, os, battery, camera, graphic) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, spec.getProductId());
            stmt.setString(2, spec.getCpu());
            stmt.setString(3, spec.getRam());
            stmt.setString(4, spec.getStorage());
            stmt.setString(5, spec.getScreen());
            stmt.setString(6, spec.getOs());
            stmt.setString(7, spec.getBattery());
            stmt.setString(8, spec.getCamera());
            stmt.setString(9, spec.getGraphic());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                spec.setSpecId(rs.getInt(1));
            }
        }
    }

    // Read by ID
    public ProductSpecification getById(int specId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM ProductSpecification WHERE spec_id = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, specId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductSpecification(
                        rs.getInt("spec_id"),
                        rs.getString("product_id"),
                        rs.getString("cpu"),
                        rs.getString("ram"),
                        rs.getString("storage"),
                        rs.getString("screen"),
                        rs.getString("os"),
                        rs.getString("battery"),
                        rs.getString("camera"),
                        rs.getString("graphic")
                );
            }
            return null;
        }
    }

    // Read all
    public List<ProductSpecification> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM ProductSpecification";
        List<ProductSpecification> specs = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                specs.add(new ProductSpecification(
                        rs.getInt("spec_id"),
                        rs.getString("product_id"),
                        rs.getString("cpu"),
                        rs.getString("ram"),
                        rs.getString("storage"),
                        rs.getString("screen"),
                        rs.getString("os"),
                        rs.getString("battery"),
                        rs.getString("camera"),
                        rs.getString("graphic")
                ));
            }
            return specs;
        }
    }

    // Update
    public void update(ProductSpecification spec) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE ProductSpecification SET product_id = ?, cpu = ?, ram = ?, storage = ?, screen = ?, os = ?, battery = ?, camera = ?, graphic = ? "
                + "WHERE spec_id = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, spec.getProductId());
            stmt.setString(2, spec.getCpu());
            stmt.setString(3, spec.getRam());
            stmt.setString(4, spec.getStorage());
            stmt.setString(5, spec.getScreen());
            stmt.setString(6, spec.getOs());
            stmt.setString(7, spec.getBattery());
            stmt.setString(8, spec.getCamera());
            stmt.setString(9, spec.getGraphic());
            stmt.setInt(10, spec.getSpecId());

            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(int specId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ProductSpecification WHERE spec_id = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, specId);
            stmt.executeUpdate();
        }
    }

    public ProductSpecification getByProductId(String productId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM ProductSpecification WHERE product_id = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductSpecification(
                        rs.getInt("spec_id"),
                        rs.getString("product_id"),
                        rs.getString("cpu"),
                        rs.getString("ram"),
                        rs.getString("storage"),
                        rs.getString("screen"),
                        rs.getString("os"),
                        rs.getString("battery"),
                        rs.getString("camera"),
                        rs.getString("graphic")
                );
            }
        }
        return null;
    }

}
