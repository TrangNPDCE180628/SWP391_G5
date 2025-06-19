/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

/**
 *
 * @author Thanh Duan
 */

import Ultis.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Feedback;

public class FeedbackDAO {

    public List<Feedback> getAllFeedbacks() throws SQLException, ClassNotFoundException {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM Feedback";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Feedback fb = new Feedback();
                fb.setFeedbackId(rs.getInt("feedbackId"));
                fb.setCusId(rs.getString("cusId"));
                fb.setProId(rs.getString("proId"));
                fb.setContent(rs.getString("content"));
                fb.setRate(rs.getInt("rate"));
                list.add(fb);
            }
        }
        return list;
    }

    public List<Feedback> getFeedbacksByProduct(String proId) throws SQLException, ClassNotFoundException {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM Feedback WHERE proId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Feedback fb = new Feedback();
                    fb.setFeedbackId(rs.getInt("feedbackId"));
                    fb.setCusId(rs.getString("cusId"));
                    fb.setProId(rs.getString("proId"));
                    fb.setContent(rs.getString("content"));
                    fb.setRate(rs.getInt("rate"));
                    list.add(fb);
                }
            }
        }
        return list;
    }

    public Feedback getFeedbackById(int feedbackId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Feedback WHERE feedbackId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedbackId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Feedback fb = new Feedback();
                    fb.setFeedbackId(rs.getInt("feedbackId"));
                    fb.setCusId(rs.getString("cusId"));
                    fb.setProId(rs.getString("proId"));
                    fb.setContent(rs.getString("content"));
                    fb.setRate(rs.getInt("rate"));
                    return fb;
                }
            }
        }
        return null;
    }

    public void insertFeedback(Feedback fb) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Feedback (cusId, proId, content, rate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fb.getCusId());
            stmt.setString(2, fb.getProId());
            stmt.setString(3, fb.getContent());
            stmt.setInt(4, fb.getRate());
            stmt.executeUpdate();
        }
    }

    public void deleteFeedback(int feedbackId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Feedback WHERE feedbackId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedbackId);
            stmt.executeUpdate();
        }
    }
}

