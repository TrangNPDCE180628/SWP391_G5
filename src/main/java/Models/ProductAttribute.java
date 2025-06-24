package Models;

public class ProductAttribute {

    private String proId;
    private int attributeId;
    private String value;

    // Additional fields for display
    private String attributeName;
    private String productName;
    private String attributeValue;  // Duplicate of value for JSP use
    private String productId;       // Duplicate of proId for JSP use

    public ProductAttribute() {
    }

    public ProductAttribute(String proId, int attributeId, String value) {
        this.proId = proId;
        this.attributeId = attributeId;
        this.value = value;
        this.attributeValue = value;
        this.productId = proId;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
        this.productId = proId;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.attributeValue = value;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
