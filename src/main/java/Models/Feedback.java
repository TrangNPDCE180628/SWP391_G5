/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Thanh Duan
 */
public class Feedback {

    private int feedbackId;
    private String cusId;
    private String proId;
    private String content;
    private int rate;

    // Constructors
    public Feedback() {
    }

    public Feedback(int feedbackId, String cusId, String proId, String content, int rate) {
        this.feedbackId = feedbackId;
        this.cusId = cusId;
        this.proId = proId;
        this.content = content;
        this.rate = rate;
    }

    // Getters and Setters
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

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
