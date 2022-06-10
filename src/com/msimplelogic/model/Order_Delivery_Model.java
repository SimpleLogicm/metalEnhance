package com.msimplelogic.model;

import java.io.Serializable;

public class Order_Delivery_Model implements Serializable {

    private String ditem_id1;
    private String product_named;
    private String order_name_value;
    private String balance_stock_value;
    private String order_qty_value;
    private String delivered_qty_value;


    public String getDitem_id1() {
        return ditem_id1;
    }

    public void setDitem_id1(String ditem_id1) {
        this.ditem_id1 = ditem_id1;
    }

    public String getProduct_named() {
        return product_named;
    }

    public void setProduct_named(String product_named) {
        this.product_named = product_named;
    }

    public String getOrder_name_value() {
        return order_name_value;
    }

    public void setOrder_name_value(String order_name_value) {
        this.order_name_value = order_name_value;
    }

    public String getBalance_stock_value() {
        return balance_stock_value;
    }

    public void setBalance_stock_value(String balance_stock_value) {
        this.balance_stock_value = balance_stock_value;
    }

    public String getOrder_qty_value() {
        return order_qty_value;
    }

    public void setOrder_qty_value(String order_qty_value) {
        this.order_qty_value = order_qty_value;
    }

    public String getDelivered_qty_value() {
        return delivered_qty_value;
    }

    public void setDelivered_qty_value(String delivered_qty_value) {
        this.delivered_qty_value = delivered_qty_value;
    }
}
