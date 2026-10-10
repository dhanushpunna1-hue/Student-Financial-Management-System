package com.sfms.model;

public class Scholarship {

    private int scholarshipId;
    private int studentId;
    private String scholarshipName;
    private double amount;
    private String academicYear;
    private String status;
    private String description;

    public Scholarship() {
    }

    public Scholarship(int studentId, String scholarshipName,
            double amount, String academicYear,
            String status, String description) {

        this.studentId = studentId;
        this.scholarshipName = scholarshipName;
        this.amount = amount;
        this.academicYear = academicYear;
        this.status = status;
        this.description = description;
    }

    public Scholarship(int scholarshipId, int studentId,
            String scholarshipName, double amount,
            String academicYear, String status,
            String description) {

        this.scholarshipId = scholarshipId;
        this.studentId = studentId;
        this.scholarshipName = scholarshipName;
        this.amount = amount;
        this.academicYear = academicYear;
        this.status = status;
        this.description = description;
    }

    public int getScholarshipId() {
        return scholarshipId;
    }

    public void setScholarshipId(int scholarshipId) {
        this.scholarshipId = scholarshipId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getScholarshipName() {
        return scholarshipName;
    }

    public void setScholarshipName(String scholarshipName) {
        this.scholarshipName = scholarshipName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}