package Models;

public class ProductAttribute {

    private String proId;
    private int attributeId;
    private String value;

    // Thêm thông tin để hiển thị
    private String attributeName;  // ví dụ: "RAM"
    private String productName;    // ví dụ: "iPhone 14"
    private String unit;
    private String productType;

    public ProductAttribute() {
    }

    public ProductAttribute(String proId, int attributeId, String value) {
        this.proId = proId;
        this.attributeId = attributeId;
        this.value = value;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
