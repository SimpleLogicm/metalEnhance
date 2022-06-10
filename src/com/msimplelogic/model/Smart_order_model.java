package com.msimplelogic.model;

public class Smart_order_model {
    String Order_status;
    String amount;
    String Order_date;
    String fev;
    String id;
    String ordernumber;

    public Smart_order_model(String order_status, String amount, String order_date, String fev, String id, String ordernumber) {
        Order_status = order_status;
        this.amount = amount;
        Order_date = order_date;
        this.fev = fev;
        this.id = id;
        this.ordernumber = ordernumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getOrder_status() {
        return Order_status;
    }

    public void setOrder_status(String order_status) {
        Order_status = order_status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrder_date() {
        return Order_date;
    }

    public void setOrder_date(String order_date) {
        Order_date = order_date;
    }

    public String getFev() {
        return fev;
    }

    public void setFev(String fev) {
        this.fev = fev;
    }
}
