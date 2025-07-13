package DAOs;

import Models.ProductAttribute;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductAttributeDAO {

    public void create(ProductAttribute pa) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ProductAttribute (proId, attributeId, value) VALUES (?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pa.getProId());
            stmt.setInt(2, pa.getAttributeId());
            stmt.setString(3, pa.getValue());
            stmt.executeUpdate();
        }
    }

    public List<ProductAttribute> getAll() throws SQLException, ClassNotFoundException {
        List<ProductAttribute> list = new ArrayList<>();
        String sql = "SELECT pa.proId, pa.attributeId, pa.value, "
                + "a.attributeName, p.proName AS productName "
                + "FROM ProductAttribute pa "
                + "JOIN Attribute a ON pa.attributeId = a.attributeId "
                + "JOIN Product p ON pa.proId = p.proId";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProductAttribute pa = new ProductAttribute();
                pa.setProId(rs.getString("proId"));
                pa.setAttributeId(rs.getInt("attributeId"));
                pa.setValue(rs.getString("value"));
                pa.setAttributeName(rs.getString("attributeName"));
                pa.setProductName(rs.getString("productName"));
                list.add(pa);
            }
        }

        return list;
    }

    public List<ProductAttribute> getByProductId(String proId) throws SQLException, ClassNotFoundException {
        List<ProductAttribute> list = new ArrayList<>();
        String sql = "SELECT pa.proId, pa.attributeId, pa.value, "
                + "a.attributeName, p.proName AS productName "
                + "FROM ProductAttribute pa "
                + "JOIN Attribute a ON pa.attributeId = a.attributeId "
                + "JOIN Product p ON pa.proId = p.proId "
                + "WHERE pa.proId = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductAttribute pa = new ProductAttribute();
                pa.setProId(rs.getString("proId"));
                pa.setAttributeId(rs.getInt("attributeId"));
                pa.setValue(rs.getString("value"));
                pa.setAttributeName(rs.getString("attributeName"));
                pa.setProductName(rs.getString("productName"));
                list.add(pa);
            }
        }

        return list;
    }

    public List<ProductAttribute> filter(String productId, String attrName, String value) throws SQLException, ClassNotFoundException {
        List<ProductAttribute> list = new ArrayList<>();
        String sql = "SELECT pa.proId, pa.attributeId, pa.value, "
                + "a.attributeName, p.proName AS productName "
                + "FROM ProductAttribute pa "
                + "JOIN Attribute a ON pa.attributeId = a.attributeId "
                + "JOIN Product p ON pa.proId = p.proId "
                + "WHERE (? IS NULL OR pa.proId = ?) "
                + "AND (? IS NULL OR a.attributeName LIKE ?) "
                + "AND (? IS NULL OR pa.value LIKE ?)";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.setString(2, productId);
            stmt.setString(3, attrName);
            stmt.setString(4, "%" + attrName + "%");
            stmt.setString(5, value);
            stmt.setString(6, "%" + value + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductAttribute pa = new ProductAttribute();
                pa.setProId(rs.getString("proId"));
                pa.setAttributeId(rs.getInt("attributeId"));
                pa.setValue(rs.getString("value"));
                pa.setAttributeName(rs.getString("attributeName"));
                pa.setProductName(rs.getString("productName"));
                list.add(pa);
            }
        }
        return list;
    }

    public ProductAttribute getByProductIdAndAttributeId(String proId, int attributeId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT pa.proId, pa.attributeId, pa.value, "
                + "a.attributeName, p.proName AS productName "
                + "FROM ProductAttribute pa "
                + "JOIN Attribute a ON pa.attributeId = a.attributeId "
                + "JOIN Product p ON pa.proId = p.proId "
                + "WHERE pa.proId = ? AND pa.attributeId = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proId);
            stmt.setInt(2, attributeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ProductAttribute pa = new ProductAttribute();
                pa.setProId(rs.getString("proId"));
                pa.setAttributeId(rs.getInt("attributeId"));
                pa.setValue(rs.getString("value"));
                pa.setAttributeName(rs.getString("attributeName"));
                pa.setProductName(rs.getString("productName"));
                return pa;
            }
        }

        return null;
    }

    public void update(ProductAttribute pa) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE ProductAttribute SET value = ? WHERE proId = ? AND attributeId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pa.getValue());
            stmt.setString(2, pa.getProId());
            stmt.setInt(3, pa.getAttributeId());
            stmt.executeUpdate();
        }
    }

    public void delete(String proId, int attributeId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ProductAttribute WHERE proId = ? AND attributeId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proId);
            stmt.setInt(2, attributeId);
            stmt.executeUpdate();
        }
    }
}
