package Models;

public class OrderDetail {

    private int id;
    private int orderId;
    private String proId; // UPDATED: Changed from int productId to String proId to match DB schema
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private Integer voucherId; // NEW: Added to match DB schema (nullable)

    public OrderDetail() {
    }

    // UPDATED: Constructor updated to include new fields
    public OrderDetail(int id, int orderId, String proId, int quantity, double unitPrice, double totalPrice, Integer voucherId) {
        this.id = id;
        this.orderId = orderId;
        this.proId = proId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.voucherId = voucherId;
    }

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

    // UPDATED: Getter and setter for proId
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
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // NEW: Getter and setter for voucherId
    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }
}

