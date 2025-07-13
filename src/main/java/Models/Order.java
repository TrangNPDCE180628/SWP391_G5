package Models;

import Models.OrderDetail;
import java.sql.Timestamp;
import java.util.List;

/**
 * Order - Represents an order in the TechStore system.
 * Maps to the SQL table [Order].
 * 
 * @author <Your Name>
 */
public class Order {
    private int orderId;                      // Mã đơn hàng
    private String cusId;                     // Mã khách hàng
    private Timestamp orderDate;              // Ngày đặt hàng
    private double totalAmount;               // Tổng tiền hàng trước giảm giá
    private double discountAmount;            // Số tiền được giảm
    private double finalAmount;               // Tổng tiền sau giảm giá
    private Integer voucherId;                // Mã voucher (nullable)
    private String orderStatus;               // Trạng thái đơn hàng
    private String paymentMethod;             // Phương thức thanh toán
    private String shippingAddress;           // Địa chỉ giao hàng
    private List<OrderDetail> orderDetails;   // Danh sách chi tiết đơn hàng

    // Constructors
    public Order() {}

    public Order(int orderId, String cusId, Timestamp orderDate, double totalAmount, double discountAmount,
                 Integer voucherId, String orderStatus, String paymentMethod, String shippingAddress) {
        this.orderId = orderId;
        this.cusId = cusId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = totalAmount - discountAmount; // Tự động tính như trong SQL
        this.voucherId = voucherId;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        updateFinalAmount();
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
        updateFinalAmount();
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    private void updateFinalAmount() {
        this.finalAmount = this.totalAmount - this.discountAmount;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

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
