package com.msimplelogic.model;

public class OutstandingOverdueModel {

    String orderDate;
    String orderAmount;
    String outstanding;

    public OutstandingOverdueModel(String orderDate, String orderAmount, String outstanding) {
        this.orderDate = orderDate;
        this.orderAmount = orderAmount;
        this.outstanding = outstanding;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }
}
