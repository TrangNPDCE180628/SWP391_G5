package Models;

import java.math.BigDecimal;
import java.sql.Date;


public class Product {
     private String proId;
    private int cateId;
    private int brandId;
    private String proName;
    private String proDescription;
    private BigDecimal proPrice;
    private int proStockQuantity;
    private int proWarrantyMonths;
    private String proModel;
    private String proColor;
    private BigDecimal proWeight;
    private String proDimensions;
    private String proOrigin;
    private String proMaterial;
    private String proConnectivity;
    private String proImageMain;
    private int status;

    public Product() {
    }

    public Product(String proId, int cateId, int brandId, String proName, String proDescription, BigDecimal proPrice, int proStockQuantity, int proWarrantyMonths, String proModel, String proColor, BigDecimal proWeight, String proDimensions, String proOrigin, String proMaterial, String proConnectivity, String proImageMain, int status) {
        this.proId = proId;
        this.cateId = cateId;
        this.brandId = brandId;
        this.proName = proName;
        this.proDescription = proDescription;
        this.proPrice = proPrice;
        this.proStockQuantity = proStockQuantity;
        this.proWarrantyMonths = proWarrantyMonths;
        this.proModel = proModel;
        this.proColor = proColor;
        this.proWeight = proWeight;
        this.proDimensions = proDimensions;
        this.proOrigin = proOrigin;
        this.proMaterial = proMaterial;
        this.proConnectivity = proConnectivity;
        this.proImageMain = proImageMain;
        this.status = status;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
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

    public int getProStockQuantity() {
        return proStockQuantity;
    }

    public void setProStockQuantity(int proStockQuantity) {
        this.proStockQuantity = proStockQuantity;
    }

    public int getProWarrantyMonths() {
        return proWarrantyMonths;
    }

    public void setProWarrantyMonths(int proWarrantyMonths) {
        this.proWarrantyMonths = proWarrantyMonths;
    }

    public String getProModel() {
        return proModel;
    }

    public void setProModel(String proModel) {
        this.proModel = proModel;
    }

    public String getProColor() {
        return proColor;
    }

    public void setProColor(String proColor) {
        this.proColor = proColor;
    }

    public BigDecimal getProWeight() {
        return proWeight;
    }

    public void setProWeight(BigDecimal proWeight) {
        this.proWeight = proWeight;
    }

    public String getProDimensions() {
        return proDimensions;
    }

    public void setProDimensions(String proDimensions) {
        this.proDimensions = proDimensions;
    }

    public String getProOrigin() {
        return proOrigin;
    }

    public void setProOrigin(String proOrigin) {
        this.proOrigin = proOrigin;
    }

    public String getProMaterial() {
        return proMaterial;
    }

    public void setProMaterial(String proMaterial) {
        this.proMaterial = proMaterial;
    }

    public String getProConnectivity() {
        return proConnectivity;
    }

    public void setProConnectivity(String proConnectivity) {
        this.proConnectivity = proConnectivity;
    }

    public String getProImageMain() {
        return proImageMain;
    }

    public void setProImageMain(String proImageMain) {
        this.proImageMain = proImageMain;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setProductId(int productId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setProductName(String productName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setDescription(String description) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setPrice(double price) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setQuantity(int quantity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setImage(String image) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setTypeId(int typeId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setUpdatedAt(Date valueOf) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setStatus(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
} 