/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author SE18-CE180628-Nguyen Pham Doan Trang
 */
public class Voucher {

    private int voucherId;
    private String codeName;
    private String voucherDescription;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private Date startDate;
    private Date endDate;
    private boolean voucherActive;

    public Voucher() {
    }

    public Voucher(int voucherId, String codeName, String voucherDescription,
            String discountType, BigDecimal discountValue, BigDecimal minOrderAmount,
            Date startDate, Date endDate, boolean voucherActive) {
        this.voucherId = voucherId;
        this.codeName = codeName;
        this.voucherDescription = voucherDescription;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.voucherActive = voucherActive;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public void setVoucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isVoucherActive() {
        return voucherActive;
    }

    public void setVoucherActive(boolean voucherActive) {
        this.voucherActive = voucherActive;
    }

}
