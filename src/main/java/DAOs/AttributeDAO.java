/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Attribute;
import Ultis.DBContext;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 *
 * @author Tram Ngoc Nguyen
 */
public class AttributeDAO {

    public void create(Attribute att) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Attribute (attributeName, unit, proTypeId) VALUES (?, ?, ?)";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, att.getAttributeName());
            stmt.setString(2, att.getUnit());
            stmt.setInt(3, att.getProTypeId());
            stmt.executeUpdate();
        }
    }

    public List<Attribute> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Attribute";
        List<Attribute> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Attribute(
                    rs.getInt("attributeId"),
                    rs.getString("attributeName"),
                    rs.getString("unit"),
                    rs.getInt("proTypeId")
                ));
            }
        }
        return list;
    }

    public void update(Attribute att) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Attribute SET attributeName = ?, unit = ?, proTypeId = ? WHERE attributeId = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, att.getAttributeName());
            stmt.setString(2, att.getUnit());
            stmt.setInt(3, att.getProTypeId());
            stmt.setInt(4, att.getAttributeId());
            stmt.executeUpdate();
        }
    }

    public void delete(int attributeId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Attribute WHERE attributeId = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attributeId);
            stmt.executeUpdate();
        }
    }
}

