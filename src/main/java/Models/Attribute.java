/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Tram Ngoc Nguyen
 */
public class Attribute {

    private int attributeId;
    private String attributeName;
    private String unit;
    private int proTypeId;

    public Attribute() {
    }

    public Attribute(int attributeId, String attributeName, String unit, int proTypeId) {
        this.attributeId = attributeId;
        this.attributeName = attributeName;
        this.unit = unit;
        this.proTypeId = proTypeId;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getProTypeId() {
        return proTypeId;
    }

    public void setProTypeId(int proTypeId) {
        this.proTypeId = proTypeId;
    }
}
