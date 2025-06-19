/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

/**
 *
 * @author Thanh Duan
 */
import Models.FeedbackReplyView;
import Ultis.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackReplyViewDAO {
    public List<FeedbackReplyView> getAllFeedbackReplies() throws Exception {
        List<FeedbackReplyView> list = new ArrayList<>();

        String sql = "SELECT * FROM ViewFeedbackReply";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FeedbackReplyView fb = new FeedbackReplyView();
                fb.setFeedbackId(rs.getInt("feedbackId"));
                fb.setCusId(rs.getString("cusId"));
                fb.setCusFullName(rs.getString("cusFullName"));
                fb.setProId(rs.getString("proId"));
                fb.setProName(rs.getString("proName"));
                fb.setFeedbackContent(rs.getString("feedbackContent"));
                fb.setRate(rs.getInt("rate"));
                fb.setReplyFeedbackId(rs.getObject("replyFeedbackId") != null ? rs.getInt("replyFeedbackId") : null);
                fb.setStaffId(rs.getString("staffId"));
                fb.setContentReply(rs.getString("contentReply"));
                fb.setCreatedAt(rs.getTimestamp("createdAt"));

                list.add(fb);
            }
        }

        return list;
    }
}

