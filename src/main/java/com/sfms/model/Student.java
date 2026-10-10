package com.sfms.model;

public class Student {
    private int studentId;
    private String rollNumber;
    private String fullName;
    private String email;
    private String phone;
    private String course;
    private String studyYear;

    public Student() {
    }

    // Constructor used by your existing student features
    public Student(int studentId, String fullName, String email,
                   String phone, String course, String studyYear) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.course = course;
        this.studyYear = studyYear;
    }

    // Constructor used by the incoming student management features
    public Student(String rollNumber, String fullName, String email,
                   String department, String academicYear, String phoneNumber) {
        this.rollNumber = rollNumber;
        this.fullName = fullName;
        this.email = email;
        this.course = department;
        this.studyYear = academicYear;
        this.phone = phoneNumber;
    }

    // Constructor used when loading a student with an ID
    public Student(int studentId, String rollNumber, String fullName,
                   String email, String department, String academicYear,
                   String phoneNumber) {
        this(rollNumber, fullName, email, department, academicYear, phoneNumber);
        this.studentId = studentId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone = phoneNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDepartment() {
        return course;
    }

    public void setDepartment(String department) {
        this.course = department;
    }

    public String getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(String studyYear) {
        this.studyYear = studyYear;
    }

    public String getAcademicYear() {
        return studyYear;
    }

    public void setAcademicYear(String academicYear) {
        this.studyYear = academicYear;
    }
}