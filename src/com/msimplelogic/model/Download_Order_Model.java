package com.msimplelogic.model;

public class Download_Order_Model {
    private String Order_Number;
    private String Order_Date;
    private String Division;
    private String Brand;
    private String Item_Code;
    private String Qty;
    private String Amt;

    public Download_Order_Model(String order_Number, String order_Date, String division, String brand, String item_Code, String qty, String amt) {
        Order_Number = order_Number;
        Order_Date = order_Date;
        Division = division;
        Brand = brand;
        Item_Code = item_Code;
        Qty = qty;
        Amt = amt;
    }

    public String getOrder_Number() {
        return Order_Number;
    }

    public void setOrder_Number(String order_Number) {
        Order_Number = order_Number;
    }

    public String getOrder_Date() {
        return Order_Date;
    }

    public void setOrder_Date(String order_Date) {
        Order_Date = order_Date;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getItem_Code() {
        return Item_Code;
    }

    public void setItem_Code(String item_Code) {
        Item_Code = item_Code;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
    }
}
