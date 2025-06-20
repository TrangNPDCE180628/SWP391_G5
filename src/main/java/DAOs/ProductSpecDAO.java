package DAOs;

import Models.ProductSpecification;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecDAO {

    // Create
    public void create(ProductSpecification spec) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ProductSpecification (proId, speCPU, speRAM, seStorage, speScreen, speOS, speBattery, speCamera, speGraphic) "
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

    // Read by specId
    public ProductSpecification getById(int specId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM ProductSpecification WHERE specId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, specId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductSpecification(
                        rs.getInt("specId"),
                        rs.getString("proId"),
                        rs.getString("speCPU"),
                        rs.getString("speRAM"),
                        rs.getString("seStorage"),
                        rs.getString("speScreen"),
                        rs.getString("speOS"),
                        rs.getString("speBattery"),
                        rs.getString("speCamera"),
                        rs.getString("speGraphic")
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
                        rs.getInt("specId"),
                        rs.getString("proId"),
                        rs.getString("speCPU"),
                        rs.getString("speRAM"),
                        rs.getString("seStorage"),
                        rs.getString("speScreen"),
                        rs.getString("speOS"),
                        rs.getString("speBattery"),
                        rs.getString("speCamera"),
                        rs.getString("speGraphic")
                ));
            }
            return specs;
        }
    }

    // Update
    public void update(ProductSpecification spec) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE ProductSpecification SET proId = ?, speCPU = ?, speRAM = ?, seStorage = ?, "
                + "speScreen = ?, speOS = ?, speBattery = ?, speCamera = ?, speGraphic = ? WHERE specId = ?";
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
        String sql = "DELETE FROM ProductSpecification WHERE specId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, specId);
            stmt.executeUpdate();
        }
    }

    // Get by Product ID
    public ProductSpecification getByProductId(String productId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM ProductSpecification WHERE proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductSpecification(
                        rs.getInt("specId"),
                        rs.getString("proId"),
                        rs.getString("speCPU"),
                        rs.getString("speRAM"),
                        rs.getString("seStorage"),
                        rs.getString("speScreen"),
                        rs.getString("speOS"),
                        rs.getString("speBattery"),
                        rs.getString("speCamera"),
                        rs.getString("speGraphic")
                );
            }
        }
        return null;
    }
}
