package Models;

public class OrderDetail {
    private int id;
    private int orderId;
    private String proId; // UPDATED: Changed to String to match DB schema
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private int voucherId; // NEW: Added to match DB schema
    private String productName; // NEW: For display purposes in order details

    public OrderDetail() {
    }

    // UPDATED: Constructor updated to include new fields
    public OrderDetail(int id, int orderId, String proId, int quantity, double unitPrice, double totalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.proId = proId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
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

    // UPDATED: Changed to getProId
    public String getProductId() {
        return proId;
    }

    // UPDATED: Changed to setProId
    public void setProductId(String proId) {
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
    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    // NEW: Getter and setter for productName
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}