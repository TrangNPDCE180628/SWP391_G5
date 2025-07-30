package Models;

public class OrderDetail {
  
    private int orderDetailId;
    private int orderId;
    private String proId;
    private int quantity;
    private double unitPrice;

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, int orderId, String proId, int quantity, double unitPrice) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.proId = proId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
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

    public double getTotalPrice() {
        return quantity * unitPrice;
    }
  
}
