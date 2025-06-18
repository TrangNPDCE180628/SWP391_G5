package Models;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int id;
    private String cusId; // UPDATED: Changed from userId to cusId to match DB schema
    private Timestamp orderDate;
    private String status;
    private double totalAmount; // UPDATED: Renamed totalPrice to totalAmount
    private double discountAmount; // NEW: Added to match DB schema
    private double finalAmount; // NEW: Added to match DB schema
    private String paymentMethod; // NEW: Added to match DB schema
    private String shippingAddress; // NEW: Added to match DB schema
    private List<OrderDetail> orderDetails;

    public Order() {
    }

    // UPDATED: Constructor updated to include new fields
    public Order(int id, String cusId, Timestamp orderDate, String status, double totalAmount, 
                 double discountAmount, double finalAmount, String paymentMethod, String shippingAddress) {
        this.id = id;
        this.cusId = cusId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // UPDATED: Changed from getUserId to getCusId
    public String getCusId() {
        return cusId;
    }

    // UPDATED: Changed from setUserId to setCusId
    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // UPDATED: Renamed to getTotalAmount
    public double getTotalAmount() {
        return totalAmount;
    }

    // UPDATED: Renamed to setTotalAmount
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // NEW: Getter and setter for discountAmount
    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    // NEW: Getter and setter for finalAmount
    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    // NEW: Getter and setter for paymentMethod
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // NEW: Getter and setter for shippingAddress
    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}