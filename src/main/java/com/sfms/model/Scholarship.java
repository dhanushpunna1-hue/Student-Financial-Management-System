package com.sfms.model;

import java.sql.Date;

public class Scholarship {
    private int scholarshipId;
    private int studentId;
    private String scholarshipName;
    private double amount;
    private String academicYear;
    private String status;
    private Date awardedDate;

    // Retained as an unused compatibility property; it is not stored in scholarships.
    private String description;

    public Scholarship() {
    }

    public Scholarship(int studentId, String scholarshipName, double amount,
            String academicYear, String status, Date awardedDate) {
        this.studentId = studentId;
        this.scholarshipName = scholarshipName;
        this.amount = amount;
        this.academicYear = academicYear;
        this.status = status;
        this.awardedDate = awardedDate;
    }

    public Scholarship(int scholarshipId, int studentId, String scholarshipName,
            double amount, String academicYear, String status, Date awardedDate) {
        this(studentId, scholarshipName, amount, academicYear, status, awardedDate);
        this.scholarshipId = scholarshipId;
    }

    // Older source files may still use the former description argument.
    public Scholarship(int studentId, String scholarshipName, double amount,
            String academicYear, String status, String description) {
        this(studentId, scholarshipName, amount, academicYear, status, (Date) null);
        this.description = description;
    }

    public Scholarship(int scholarshipId, int studentId, String scholarshipName,
            double amount, String academicYear, String status, String description) {
        this(studentId, scholarshipName, amount, academicYear, status, description);
        this.scholarshipId = scholarshipId;
    }

    public int getScholarshipId() { return scholarshipId; }
    public void setScholarshipId(int scholarshipId) { this.scholarshipId = scholarshipId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getScholarshipName() { return scholarshipName; }
    public void setScholarshipName(String scholarshipName) { this.scholarshipName = scholarshipName; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getAwardedDate() { return awardedDate; }
    public void setAwardedDate(Date awardedDate) { this.awardedDate = awardedDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
