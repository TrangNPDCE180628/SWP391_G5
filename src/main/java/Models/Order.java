package Models;

import java.math.BigDecimal;
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
    private BigDecimal totalAmount;           // Tổng tiền hàng trước giảm giá
    private BigDecimal discountAmount;        // Số tiền được giảm
    private BigDecimal finalAmount;           // Tổng tiền sau giảm giá
    private Integer voucherId;                // Mã voucher (nullable)
    private String orderStatus;               // Trạng thái đơn hàng
    private String paymentMethod;             // Phương thức thanh toán
    private String shippingAddress;           // Địa chỉ giao hàng
    private String receiverName;              // Tên người nhận
    private String receiverPhone;             // Số điện thoại người nhận
    private List<OrderDetail> orderDetails;   // Danh sách chi tiết đơn hàng

    public Order() {
    }

    public Order(int orderId, String cusId, Timestamp orderDate, BigDecimal totalAmount,
            BigDecimal discountAmount, BigDecimal finalAmount, Integer voucherId,
            String orderStatus, String paymentMethod, String shippingAddress,
            String receiverName, String receiverPhone, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.cusId = cusId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.voucherId = voucherId;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.orderDetails = orderDetails;
    }

    public Order(int orderId, String cusId, Timestamp orderDate, BigDecimal totalAmount,
            BigDecimal discountAmount, Integer voucherId,
            String orderStatus, String paymentMethod, String shippingAddress,
            String receiverName, String receiverPhone) {
        this.orderId = orderId;
        this.cusId = cusId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.voucherId = voucherId;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
    }

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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
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

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId + ", cusId=" + cusId + ", orderDate=" + orderDate + ", totalAmount=" + totalAmount + ", discountAmount=" + discountAmount + ", finalAmount=" + finalAmount + ", voucherId=" + voucherId + ", orderStatus=" + orderStatus + ", paymentMethod=" + paymentMethod + ", shippingAddress=" + shippingAddress + ", receiverName=" + receiverName + ", receiverPhone=" + receiverPhone + ", orderDetails=" + orderDetails + '}';
    }
    
    
}
