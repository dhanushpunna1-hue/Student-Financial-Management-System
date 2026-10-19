package com.sfms.model;

import java.sql.Date;

/** A student's fee record stored in the fees table. */
public class FeeStructure {
    private int feeId;
    private int studentId;
    private String feeName;
    private double amount;
    private Date dueDate;

    // Kept so older source files in the project still compile during replacement.
    private String academicYear;
    private String description;

    public FeeStructure() {
    }

    public FeeStructure(int studentId, String feeName, double amount, Date dueDate) {
        this.studentId = studentId;
        this.feeName = feeName;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    public FeeStructure(int feeId, int studentId, String feeName, double amount, Date dueDate) {
        this.feeId = feeId;
        this.studentId = studentId;
        this.feeName = feeName;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    // Legacy constructors retained for the project's existing manual test source.
    public FeeStructure(String feeName, double amount, String academicYear, String description) {
        this.feeName = feeName;
        this.amount = amount;
        this.academicYear = academicYear;
        this.description = description;
    }

    public FeeStructure(int feeId, String feeName, double amount,
            String academicYear, String description) {
        this.feeId = feeId;
        this.feeName = feeName;
        this.amount = amount;
        this.academicYear = academicYear;
        this.description = description;
    }

    public int getFeeId() { return feeId; }
    public void setFeeId(int feeId) { this.feeId = feeId; }
    public int getFeeStructureId() { return feeId; }
    public void setFeeStructureId(int id) { this.feeId = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getFeeName() { return feeName; }
    public void setFeeName(String feeName) { this.feeName = feeName; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
