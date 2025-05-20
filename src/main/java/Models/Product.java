package Models;

public class Product {
    private int proId;
    private String proName;
    private String proDescription;
    private double proPrice;
    private String proImage;
    private int proQuantity;
    private int proTypeId;

    public Product() {
    }

    public Product(int proId, String proName, String proDescription, double proPrice, String proImage, int proQuantity, int proTypeId) {
        this.proId = proId;
        this.proName = proName;
        this.proDescription = proDescription;
        this.proPrice = proPrice;
        this.proImage = proImage;
        this.proQuantity = proQuantity;
        this.proTypeId = proTypeId;
    }

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProDescription() {
        return proDescription;
    }

    public void setProDescription(String proDescription) {
        this.proDescription = proDescription;
    }

    public double getProPrice() {
        return proPrice;
    }

    public void setProPrice(double proPrice) {
        this.proPrice = proPrice;
    }

    public String getProImage() {
        return proImage;
    }

    public void setProImage(String proImage) {
        this.proImage = proImage;
    }

    public int getProQuantity() {
        return proQuantity;
    }

    public void setProQuantity(int proQuantity) {
        this.proQuantity = proQuantity;
    }

    public int getProTypeId() {
        return proTypeId;
    }

    public void setProTypeId(int proTypeId) {
        this.proTypeId = proTypeId;
    }
} 