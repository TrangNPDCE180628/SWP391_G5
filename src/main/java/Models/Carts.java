package Models;


public class Carts {
    private int id;
    private int userId;
    private String status;
    private int productId;
    private double unitPrice; // DECIMAL(10,2) mapped to double
    private int quantity;

    // Default constructor
    public Carts() {}

    // Parameterized constructor
    public Carts(int id, int userId, String status, int productId, double unitPrice, int quantity) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}