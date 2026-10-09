package com.sfms.service;

import com.sfms.dao.StudentDAO;
import com.sfms.model.Student;

import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService() {
        studentDAO = new StudentDAO();
    }

    // 1. Add a student
    public boolean addStudent(Student student) {

        if (student == null) {
            System.out.println("Student details cannot be empty.");
            return false;
        }

        if (isBlank(student.getRollNumber())
                || isBlank(student.getFullName())
                || isBlank(student.getDepartment())
                || isBlank(student.getAcademicYear())) {

            System.out.println(
                    "Roll number, name, department and academic year are required.");
            return false;
        }

        if (!isBlank(student.getEmail())
                && !student.getEmail().matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            System.out.println("Please enter a valid email address.");
            return false;
        }

        if (!isBlank(student.getPhoneNumber())
                && !student.getPhoneNumber().matches("\\d{10,15}")) {

            System.out.println(
                    "Phone number must contain 10 to 15 digits.");
            return false;
        }

        return studentDAO.addStudent(student);
    }

    // 2. View all students
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    // 3. Search a student
    public Student getStudentById(int studentId) {

        if (studentId <= 0) {
            System.out.println("Student ID must be a positive number.");
            return null;
        }

        return studentDAO.getStudentById(studentId);
    }

    // 4. Update a student
    public boolean updateStudent(Student student) {

        if (student == null || student.getStudentId() <= 0) {
            System.out.println("Valid student details and ID are required.");
            return false;
        }

        if (isBlank(student.getRollNumber())
                || isBlank(student.getFullName())
                || isBlank(student.getDepartment())
                || isBlank(student.getAcademicYear())) {

            System.out.println(
                    "Roll number, name, department and academic year are required.");
            return false;
        }

        if (!isBlank(student.getEmail())
                && !student.getEmail().matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            System.out.println("Please enter a valid email address.");
            return false;
        }

        if (!isBlank(student.getPhoneNumber())
                && !student.getPhoneNumber().matches("\\d{10,15}")) {

            System.out.println(
                    "Phone number must contain 10 to 15 digits.");
            return false;
        }

        return studentDAO.updateStudent(student);
    }

    // 5. Delete a student
    public boolean deleteStudent(int studentId) {

        if (studentId <= 0) {
            System.out.println("Student ID must be a positive number.");
            return false;
        }

        return studentDAO.deleteStudent(studentId);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}