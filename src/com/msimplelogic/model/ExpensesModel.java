package com.msimplelogic.model;

public class ExpensesModel {
    public String expenseId;
    public String expenseAmount;
    public String expenseApprovedAmount;
    public String expenseName;
    public String expensestatus;
    public String expenseDate;

    public ExpensesModel(String expenseId, String expenseAmount, String expenseApprovedAmount, String expenseName, String expensestatus, String expenseDate) {
        this.expenseId = expenseId;
        this.expenseAmount = expenseAmount;
        this.expenseApprovedAmount = expenseApprovedAmount;
        this.expenseName = expenseName;
        this.expensestatus = expensestatus;
        this.expenseDate = expenseDate;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseApprovedAmount() {
        return expenseApprovedAmount;
    }

    public void setExpenseApprovedAmount(String expenseApprovedAmount) {
        this.expenseApprovedAmount = expenseApprovedAmount;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getExpensestatus() {
        return expensestatus;
    }

    public void setExpensestatus(String expensestatus) {
        this.expensestatus = expensestatus;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }
}
