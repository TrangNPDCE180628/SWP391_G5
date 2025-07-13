/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 * Stock - Represents the inventory (stock) information for a product.
 * This class stores the available quantity of a product and the last updated timestamp.
 * 
 * @author 
 */
public class Stock {
    
    /**
     * The unique product ID. This is both the primary key and a foreign key to Product.
     */
    private String proId;

    /**
     * The quantity of product currently in stock.
     * Must be a non-negative integer.
     */
    private int stockQuantity;

    /**
     * The timestamp of the last inventory update.
     */
    private java.sql.Timestamp lastUpdated;

    /**
     * Default constructor.
     */
    public Stock() {
    }

    /**
     * Parameterized constructor.
     * 
     * @param proId         the ID of the product
     * @param stockQuantity the available quantity
     * @param lastUpdated   the time when stock was last updated
     */
    public Stock(String proId, int stockQuantity, java.sql.Timestamp lastUpdated) {
        this.proId = proId;
        this.stockQuantity = stockQuantity;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Gets the product ID.
     * 
     * @return the product ID
     */
    public String getProId() {
        return proId;
    }

    /**
     * Sets the product ID.
     * 
     * @param proId the product ID
     */
    public void setProId(String proId) {
        this.proId = proId;
    }

    /**
     * Gets the stock quantity.
     * 
     * @return the stock quantity
     */
    public int getStockQuantity() {
        return stockQuantity;
    }

    /**
     * Sets the stock quantity.
     * 
     * @param stockQuantity the stock quantity (must be >= 0)
     */
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Gets the last updated timestamp.
     * 
     * @return the last updated time
     */
    public java.sql.Timestamp getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Sets the last updated timestamp.
     * 
     * @param lastUpdated the time the stock was last updated
     */
    public void setLastUpdated(java.sql.Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
