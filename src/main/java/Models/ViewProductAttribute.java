/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

public class ViewProductAttribute {
    private String productId;
    private String productName;
    private String productType;
    private int attributeId;
    private String attributeName;
    private String value;
    private String unit;

    // Constructors
    public ViewProductAttribute() {}

    public ViewProductAttribute(String productId, String productName, String productType,
                                int attributeId, String attributeName, String value, String unit) {
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.attributeId = attributeId;
        this.attributeName = attributeName;
        this.value = value;
        this.unit = unit;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
