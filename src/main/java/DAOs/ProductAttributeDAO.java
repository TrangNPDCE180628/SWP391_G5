package DAOs;

import Models.ProductAttribute;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductAttributeDAO {

    private Connection conn;

    // Constructor mặc định (dùng trong servlet)
    public ProductAttributeDAO() throws SQLException, ClassNotFoundException {
        this.conn = DBContext.getConnection();
    }

    // Constructor nhận Connection bên ngoài (tuỳ chọn)
    public ProductAttributeDAO(Connection conn) {
        this.conn = conn;
    }

    // CREATE
    public void create(ProductAttribute pa) throws SQLException {
        String sql = "INSERT INTO ProductAttribute (proId, attributeId, value) VALUES (?, ?, ?)";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pa.getProId());
            stmt.setInt(2, pa.getAttributeId());
            stmt.setString(3, pa.getValue());
            stmt.executeUpdate();
        }
    }

    // READ - get all
    public List<ProductAttribute> getAll() throws SQLException {
        List<ProductAttribute> list = new ArrayList<>();
        String sql = "SELECT pa.*, a.attributeName, p.proName "
                + "FROM ProductAttribute pa "
                + "JOIN Attribute a ON pa.attributeId = a.attributeId "
                + "JOIN Product p ON pa.proId = p.proId";
        try ( PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ProductAttribute pa = new ProductAttribute();
                pa.setProId(rs.getString("proId"));
                pa.setAttributeId(rs.getInt("attributeId"));
                pa.setValue(rs.getString("value"));
                pa.setAttributeName(rs.getString("attributeName"));
                pa.setProductName(rs.getString("proName"));
                list.add(pa);
            }
        }
        return list;
    }

    // READ - get by ID pair
    public ProductAttribute getByProductIdAndAttributeId(String proId, int attributeId) throws SQLException {
        String sql = "SELECT pa.*, a.attributeName, p.proName "
                + "FROM ProductAttribute pa "
                + "JOIN Attribute a ON pa.attributeId = a.attributeId "
                + "JOIN Product p ON pa.proId = p.proId "
                + "WHERE pa.proId = ? AND pa.attributeId = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            stmt.setInt(2, attributeId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ProductAttribute pa = new ProductAttribute();
                    pa.setProId(rs.getString("proId"));
                    pa.setAttributeId(rs.getInt("attributeId"));
                    pa.setValue(rs.getString("value"));
                    pa.setAttributeName(rs.getString("attributeName"));
                    pa.setProductName(rs.getString("proName"));
                    return pa;
                }
            }
        }
        return null;
    }

    // UPDATE
    public void update(ProductAttribute pa) throws SQLException {
        String sql = "UPDATE ProductAttribute SET value = ? WHERE proId = ? AND attributeId = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pa.getValue());
            stmt.setString(2, pa.getProId());
            stmt.setInt(3, pa.getAttributeId());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void delete(String proId, int attributeId) throws SQLException {
        String sql = "DELETE FROM ProductAttribute WHERE proId = ? AND attributeId = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            stmt.setInt(2, attributeId);
            stmt.executeUpdate();
        }
    }

    public List<ProductAttribute> filterAndSort(String proId, String attributeName, String value, String sortField, String sortOrder) throws SQLException {
        List<ProductAttribute> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT pa.*, a.attributeName, a.unit, a.proTypeId, p.proName, pt.proTypeName "
                + "FROM ProductAttribute pa "
                + "JOIN Attribute a ON pa.attributeId = a.attributeId "
                + "JOIN Product p ON pa.proId = p.proId "
                + "JOIN ProductType pt ON p.proTypeId = pt.proTypeId "
                + "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (proId != null && !proId.trim().isEmpty()) {
            sql.append(" AND pa.proId LIKE ?");
            params.add("%" + proId + "%");
        }
        if (attributeName != null && !attributeName.trim().isEmpty()) {
            sql.append(" AND a.attributeName LIKE ?");
            params.add("%" + attributeName + "%");
        }
        if (value != null && !value.trim().isEmpty()) {
            sql.append(" AND pa.value LIKE ?");
            params.add("%" + value + "%");
        }

        // Validate sortField
        if (sortField != null && (sortField.equals("proId") || sortField.equals("attributeName") || sortField.equals("value"))) {
            sql.append(" ORDER BY ");
            switch (sortField) {
                case "proId":
                    sql.append("pa.proId");
                    break;
                case "attributeName":
                    sql.append("a.attributeName");
                    break;
                case "value":
                    sql.append("pa.value");
                    break;
            }
            if ("desc".equalsIgnoreCase(sortOrder)) {
                sql.append(" DESC");
            } else {
                sql.append(" ASC");
            }
        }

        try ( PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProductAttribute pa = new ProductAttribute();
                    pa.setProId(rs.getString("proId"));
                    pa.setProductName(rs.getString("proName"));
                    pa.setAttributeId(rs.getInt("attributeId"));
                    pa.setAttributeName(rs.getString("attributeName"));
                    pa.setValue(rs.getString("value"));
                    pa.setUnit(rs.getString("unit"));
                    pa.setProductType(rs.getString("proTypeName"));
                    list.add(pa);
                }
            }
        }

        return list;

    }
// Kiểm tra xem cặp (proId, attributeId) đã tồn tại chưa

    public boolean exists(String proId, int attributeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ProductAttribute WHERE proId = ? AND attributeId = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            stmt.setInt(2, attributeId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
