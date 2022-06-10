package com.msimplelogic.model;

public class TargetOrderListModel {

    private String orderId;
    private String productName;
    private String totalQty;
    private String retPrice;
    private String mrp;
    private String amount;
    private String custShopName;

    public TargetOrderListModel(String orderId, String productName, String totalQty, String retPrice, String mrp, String amount, String custShopName) {
        this.orderId = orderId;
        this.productName = productName;
        this.totalQty = totalQty;
        this.retPrice = retPrice;
        this.mrp = mrp;
        this.amount = amount;
        this.custShopName = custShopName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getRetPrice() {
        return retPrice;
    }

    public void setRetPrice(String retPrice) {
        this.retPrice = retPrice;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCustShopName() {
        return custShopName;
    }

    public void setCustShopName(String custShopName) {
        this.custShopName = custShopName;
    }
}
