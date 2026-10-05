package com.sfms.model;

public class FeeStructure {

    private int feeStructureId;
    private String feeName;
    private double amount;
    private String academicYear;
    private String description;

    public FeeStructure() {
    }

    public FeeStructure(String feeName, double amount,
            String academicYear, String description) {
        this.feeName = feeName;
        this.amount = amount;
        this.academicYear = academicYear;
        this.description = description;
    }

    public FeeStructure(int feeStructureId, String feeName,
            double amount, String academicYear,
            String description) {
        this.feeStructureId = feeStructureId;
        this.feeName = feeName;
        this.amount = amount;
        this.academicYear = academicYear;
        this.description = description;
    }

    public int getFeeStructureId() {
        return feeStructureId;
    }

    public void setFeeStructureId(int feeStructureId) {
        this.feeStructureId = feeStructureId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}