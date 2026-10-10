package com.sfms.model;

public class Student {
    private int studentId;
    private String fullName;
    private String email;
    private String phone;
    private String course;
    private String studyYear;

    public Student() {
    }

    public Student(
            int studentId,
            String fullName,
            String email,
            String phone,
            String course,
            String studyYear) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.course = course;
        this.studyYear = studyYear;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(String studyYear) {
        this.studyYear = studyYear;
    }
}