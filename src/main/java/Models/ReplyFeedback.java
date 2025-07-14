/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Thanh Duan
 */
import java.sql.Timestamp;

public class ReplyFeedback {

    private int replyFeedbackId;
    private int feedbackId;
    private String cusId;
    private String staffId;
    private String contentReply;
    private Timestamp createdAt;

    // Constructors
    public ReplyFeedback() {
    }

    public ReplyFeedback(int replyFeedbackId, int feedbackId, String cusId, String staffId, String contentReply, Timestamp createdAt) {
        this.replyFeedbackId = replyFeedbackId;
        this.feedbackId = feedbackId;
        this.cusId = cusId;
        this.staffId = staffId;
        this.contentReply = contentReply;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getReplyFeedbackId() {
        return replyFeedbackId;
    }

    public void setReplyFeedbackId(int replyFeedbackId) {
        this.replyFeedbackId = replyFeedbackId;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getContentReply() {
        return contentReply;
    }

    public void setContentReply(String contentReply) {
        this.contentReply = contentReply;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
