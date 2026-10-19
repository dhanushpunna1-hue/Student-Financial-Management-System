package com.sfms.model;

import java.sql.Date;

public class Expense {

    private int expenseId;
    private int studentId;
    private String expenseName;
    private double amount;
    private Date expenseDate;
    private String notes;

    public Expense() {
    }

    public Expense(int studentId, String expenseName, double amount,
            Date expenseDate, String notes) {
        this.studentId = studentId;
        this.expenseName = expenseName;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.notes = notes;
    }

    public Expense(int expenseId, int studentId, String expenseName,
            double amount, Date expenseDate, String notes) {
        this.expenseId = expenseId;
        this.studentId = studentId;
        this.expenseName = expenseName;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.notes = notes;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getStudentId() { return studentId; }

    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }
}
