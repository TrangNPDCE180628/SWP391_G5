package Models;

import java.sql.Timestamp;

/**
 *
 * @author SE18-CE180628-Nguyen Pham Doan Trang
 */
public class Stock {

    private String proId;
    private int stockQuantity;
    private Timestamp lastUpdated;

    public Stock() {
    }

    public Stock(String proId, int stockQuantity, Timestamp lastUpdated) {
        this.proId = proId;
        this.stockQuantity = stockQuantity;
        this.lastUpdated = lastUpdated;
    }
  
    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }
  
    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
