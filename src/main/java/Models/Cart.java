package Models;

public class Cart {

    private int cartId;      // cartId INT
    private String cusId;    // cusId VARCHAR(10)
    private String proId;    // proId VARCHAR(10)
    private int quantity;    // quantity INT

    public Cart() {
    }

    public Cart(int cartId, String cusId, String proId, int quantity) {
        this.cartId = cartId;
        this.cusId = cusId;
        this.proId = proId;
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
