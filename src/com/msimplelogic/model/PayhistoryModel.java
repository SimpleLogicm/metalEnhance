package com.msimplelogic.model;

public class PayhistoryModel {

    String amount,date,time;

    public PayhistoryModel(String amount, String date, String time) {
        this.amount = amount;
        this.date = date;
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
