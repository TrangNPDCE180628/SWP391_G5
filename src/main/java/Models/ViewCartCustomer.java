package Models;

public class ViewCartCustomer {

    private int cartId;
    private String cusId;
    private String proId;
    private String proName;
    private double proPrice;
    private String proImageUrl;
    private int quantity;

    public ViewCartCustomer() {
    }

    public ViewCartCustomer(int cartId, String cusId, String proId, String proName, double proPrice, String proImageUrl, int quantity) {
        this.cartId = cartId;
        this.cusId = cusId;
        this.proId = proId;
        this.proName = proName;
        this.proPrice = proPrice;
        this.proImageUrl = proImageUrl;
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

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public double getProPrice() {
        return proPrice;
    }

    public void setProPrice(double proPrice) {
        this.proPrice = proPrice;
    }

    public String getProImageUrl() {
        return proImageUrl;
    }

    public void setProImageUrl(String proImageUrl) {
        this.proImageUrl = proImageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return proPrice * quantity;
    }
}
