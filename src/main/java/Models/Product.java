package Models;

import java.math.BigDecimal;

public class Product {

    private String proId;
    private String proName;
    private String proDescription;
    private BigDecimal proPrice;
    private String proImageUrl;
    private int proTypeId;

    public Product() {
    }

    public Product(String proId, String proName, String proDescription, BigDecimal proPrice, String proImageUrl, int proTypeId) {
        this.proId = proId;
        this.proName = proName;
        this.proDescription = proDescription;
        this.proPrice = proPrice;
        this.proImageUrl = proImageUrl;
        this.proTypeId = proTypeId;
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

    public String getProDescription() {
        return proDescription;
    }

    public void setProDescription(String proDescription) {
        this.proDescription = proDescription;
    }

    public BigDecimal getProPrice() {
        return proPrice;
    }

    public void setProPrice(BigDecimal proPrice) {
        this.proPrice = proPrice;
    }

    public String getProImageUrl() {
        return proImageUrl;
    }

    public void setProImageUrl(String proImageUrl) {
        this.proImageUrl = proImageUrl;
    }

    public int getProTypeId() {
        return proTypeId;
    }

    public void setProTypeId(int proTypeId) {
        this.proTypeId = proTypeId;
    }

}
