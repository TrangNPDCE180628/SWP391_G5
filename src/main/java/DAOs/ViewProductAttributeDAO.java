package DAOs;

import Models.ViewProductAttribute;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewProductAttributeDAO {

    private Connection connection;

    public ViewProductAttributeDAO(Connection connection) {
        this.connection = connection;
    }

    public List<ViewProductAttribute> getAll() throws SQLException {
        List<ViewProductAttribute> list = new ArrayList<>();
        String sql = "SELECT productId, productName, productType, attributeId, attributeName, value, unit FROM ViewProductAttribute";

        try ( PreparedStatement ps = connection.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ViewProductAttribute vpa = new ViewProductAttribute();
                vpa.setProductId(rs.getString("productId"));
                vpa.setProductName(rs.getString("productName"));
                vpa.setProductType(rs.getString("productType"));
                vpa.setAttributeId(rs.getInt("attributeId"));
                vpa.setAttributeName(rs.getString("attributeName"));
                vpa.setValue(rs.getString("value"));
                vpa.setUnit(rs.getString("unit"));
                list.add(vpa);
            }
        }

        return list;
    }

    public List<ViewProductAttribute> filterAndSort(
            String proId,
            String attributeName,
            String value,
            String sortField,
            String sortOrder) throws SQLException {

        List<ViewProductAttribute> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT productId, productName, productType, attributeId, attributeName, value, unit FROM ViewProductAttribute WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (proId != null && !proId.trim().isEmpty()) {
            sql.append(" AND productId LIKE ?");
            params.add("%" + proId.trim() + "%");
        }

        if (attributeName != null && !attributeName.trim().isEmpty()) {
            sql.append(" AND attributeName LIKE ?");
            params.add("%" + attributeName.trim() + "%");
        }

        if (value != null && !value.trim().isEmpty()) {
            sql.append(" AND value LIKE ?");
            params.add("%" + value.trim() + "%");
        }

        List<String> allowedSortFields = Arrays.asList("attributeName", "productId", "value");
        List<String> allowedSortOrders = Arrays.asList("asc", "desc");

        if (allowedSortFields.contains(sortField)) {
            sql.append(" ORDER BY ").append(sortField);
            if (allowedSortOrders.contains(sortOrder)) {
                sql.append(" ").append(sortOrder.toUpperCase());
            } else {
                sql.append(" ASC");
            }
        }

        try ( PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ViewProductAttribute vpa = new ViewProductAttribute();
                    vpa.setProductId(rs.getString("productId"));
                    vpa.setProductName(rs.getString("productName"));
                    vpa.setProductType(rs.getString("productType"));
                    vpa.setAttributeId(rs.getInt("attributeId"));
                    vpa.setAttributeName(rs.getString("attributeName"));
                    vpa.setValue(rs.getString("value"));
                    vpa.setUnit(rs.getString("unit"));
                    list.add(vpa);
                }
            }
        }

        return list;
    }
}
