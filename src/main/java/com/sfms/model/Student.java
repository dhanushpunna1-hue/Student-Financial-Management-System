package com.sfms.model;

public class Student {

    private int studentId;
    private String rollNumber;
    private String fullName;
    private String email;
    private String department;
    private String academicYear;
    private String phoneNumber;

    // Default constructor
    public Student() {
    }

    // Constructor for adding a student
    public Student(String rollNumber, String fullName,
            String email, String department,
            String academicYear, String phoneNumber) {
        this.rollNumber = rollNumber;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.academicYear = academicYear;
        this.phoneNumber = phoneNumber;
    }

    // Constructor for retrieving a student with ID
    public Student(int studentId, String rollNumber,
            String fullName, String email,
            String department, String academicYear,
            String phoneNumber) {
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.academicYear = academicYear;
        this.phoneNumber = phoneNumber;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}