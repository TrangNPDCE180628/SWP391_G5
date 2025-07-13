package Models;

public class OrderDetail {
    private int orderDetailId;     // ID của dòng chi tiết
    private int orderId;           // ID đơn hàng
    private String proId;          // ID sản phẩm (String theo DB)
    private int quantity;          // Số lượng
    private double unitPrice;      // Giá mỗi sản phẩm
    private Integer voucherId;     // Voucher (nullable)

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, int orderId, String proId, int quantity, double unitPrice, Integer voucherId) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.proId = proId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.voucherId = voucherId;
    }


    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    // ✅ Tính tổng giá (quantity * unitPrice)
    public double getTotalPrice() {
        return unitPrice * quantity;
    }
}
