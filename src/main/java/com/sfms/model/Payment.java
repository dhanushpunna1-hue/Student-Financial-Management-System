package com.sfms.model;

import java.time.LocalDate;

public class Payment {

    private int paymentId;
    private int feeStructureId;
    private double amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String transactionReference;

    public Payment() {
    }

    public Payment(int feeStructureId,
            double amount,
            LocalDate paymentDate,
            String paymentMethod,
            String transactionReference) {

        this.feeStructureId = feeStructureId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
    }

    public Payment(int paymentId,
            int feeStructureId,
            double amount,
            LocalDate paymentDate,
            String paymentMethod,
            String transactionReference) {

        this.paymentId = paymentId;
        this.feeStructureId = feeStructureId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getFeeStructureId() {
        return feeStructureId;
    }

    public void setFeeStructureId(int feeStructureId) {
        this.feeStructureId = feeStructureId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }
}