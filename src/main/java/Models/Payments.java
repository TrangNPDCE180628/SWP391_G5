package Models;



import java.time.LocalDateTime; // For DATETIME

public class Payments {
    private int id;
    private int orderId;
    private LocalDateTime paymentDate; // DATETIME mapped to LocalDateTime
    private double amount; // DECIMAL(10,2) mapped to double

    // Default constructor
    public Payments() {}

    // Parameterized constructor
    public Payments(int id, int orderId, LocalDateTime paymentDate, double amount) {
        this.id = id;
        this.orderId = orderId;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
