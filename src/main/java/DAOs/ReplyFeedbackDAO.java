/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

/**
 *
 * @author Thanh Duan
 */
import Models.ReplyFeedback;
import Ultis.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReplyFeedbackDAO {

    // Thêm phản hồi mới
    public void insertReplyFeedback(ReplyFeedback rf) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ReplyFeedback (feedbackId, cusId, staffId, contentReply) VALUES (?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rf.getFeedbackId());
            stmt.setString(2, rf.getCusId());
            stmt.setString(3, rf.getStaffId());
            stmt.setString(4, rf.getContentReply());

            stmt.executeUpdate();
        }
    }

    // Lấy danh sách tất cả phản hồi
    public List<ReplyFeedback> getAllReplies() throws SQLException, ClassNotFoundException {
        List<ReplyFeedback> list = new ArrayList<>();
        String sql = "SELECT * FROM ReplyFeedback ORDER BY createdAt DESC";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ReplyFeedback rf = new ReplyFeedback();
                rf.setReplyFeedbackId(rs.getInt("replyFeedbackId"));
                rf.setFeedbackId(rs.getInt("feedbackId"));
                rf.setCusId(rs.getString("cusId"));
                rf.setStaffId(rs.getString("staffId"));
                rf.setContentReply(rs.getString("contentReply"));
                rf.setCreatedAt(rs.getTimestamp("createdAt"));
                list.add(rf);
            }
        }
        return list;
    }

    // Lấy phản hồi theo feedbackId
    public List<ReplyFeedback> getRepliesByFeedbackId(int feedbackId) throws SQLException, ClassNotFoundException {
        List<ReplyFeedback> list = new ArrayList<>();
        String sql = "SELECT * FROM ReplyFeedback WHERE feedbackId = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedbackId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ReplyFeedback rf = new ReplyFeedback();
                rf.setReplyFeedbackId(rs.getInt("replyFeedbackId"));
                rf.setFeedbackId(rs.getInt("feedbackId"));
                rf.setCusId(rs.getString("cusId"));
                rf.setStaffId(rs.getString("staffId"));
                rf.setContentReply(rs.getString("contentReply"));
                rf.setCreatedAt(rs.getTimestamp("createdAt"));
                list.add(rf);
            }
        }
        return list;
    }

    // Xóa phản hồi theo ID
    public void deleteReply(int replyId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ReplyFeedback WHERE replyFeedbackId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, replyId);
            stmt.executeUpdate();
        }
    }
}
