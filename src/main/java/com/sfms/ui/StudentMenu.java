package com.sfms.ui;

import com.sfms.model.Student;
import com.sfms.service.StudentService;

import java.util.List;
import java.util.Scanner;

public class StudentMenu {

    private final Scanner scanner;
    private final StudentService studentService;

    public StudentMenu() {
        scanner = new Scanner(System.in);
        studentService = new StudentService();
    }

    public void start() {
        while (true) {
            System.out.println("\n================================");
            System.out.println("       STUDENT MANAGEMENT");
            System.out.println("================================");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Back to Main Menu");
            System.out.println("================================");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    viewAllStudents();
                    break;
                case "3":
                    searchStudent();
                    break;
                case "4":
                    updateStudent();
                    break;
                case "5":
                    deleteStudent();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1-6.");
            }
        }
    }

    private void addStudent() {
        System.out.println("\n--- Add Student ---");

        System.out.print("Roll Number: ");
        String rollNumber = scanner.nextLine().trim();

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("Email (optional, press Enter to skip): ");
        String email = scanner.nextLine().trim();

        System.out.print("Department: ");
        String department = scanner.nextLine().trim();

        System.out.print("Academic Year (e.g., 2nd Year): ");
        String academicYear = scanner.nextLine().trim();

        System.out.print("Phone Number (optional, press Enter to skip): ");
        String phoneNumber = scanner.nextLine().trim();

        Student student = new Student(
                rollNumber,
                fullName,
                email.isEmpty() ? null : email,
                department,
                academicYear,
                phoneNumber.isEmpty() ? null : phoneNumber);

        if (studentService.addStudent(student)) {
            System.out.println("Student added successfully!");
        } else {
            System.out.println(
                    "Student could not be added. Check the details "
                            + "and whether the roll number already exists.");
        }
    }

    private void viewAllStudents() {
        System.out.println("\n--- All Students ---");

        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.printf(
                "%-5s %-18s %-22s %-28s %-18s %-15s %-15s%n",
                "ID", "Roll Number", "Name", "Email",
                "Department", "Year", "Phone");

        System.out.println(
                "---------------------------------------------------------------------------------------------------------------");

        for (Student student : students) {
            System.out.printf(
                    "%-5d %-18s %-22s %-28s %-18s %-15s %-15s%n",
                    student.getStudentId(),
                    student.getRollNumber(),
                    student.getFullName(),
                    display(student.getEmail()),
                    student.getDepartment(),
                    student.getAcademicYear(),
                    display(student.getPhoneNumber()));
        }
    }

    private void searchStudent() {
        System.out.print("Enter Student ID: ");

        try {
            int studentId = Integer.parseInt(scanner.nextLine().trim());
            Student student = studentService.getStudentById(studentId);

            if (student == null) {
                System.out.println("Student not found.");
            } else {
                printStudent(student);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Enter a number.");
        }
    }

    private void updateStudent() {
        System.out.print("Enter the Student ID to update: ");

        try {
            int studentId = Integer.parseInt(scanner.nextLine().trim());

            Student existing = studentService.getStudentById(studentId);

            if (existing == null) {
                System.out.println("Student not found.");
                return;
            }

            System.out.println("Press Enter to keep an existing value.");

            System.out.print("Roll Number [" + existing.getRollNumber() + "]: ");
            String rollNumber = scanner.nextLine().trim();

            System.out.print("Full Name [" + existing.getFullName() + "]: ");
            String fullName = scanner.nextLine().trim();

            System.out.print("Email [" + display(existing.getEmail()) + "]: ");
            String email = scanner.nextLine().trim();

            System.out.print("Department [" + existing.getDepartment() + "]: ");
            String department = scanner.nextLine().trim();

            System.out.print("Academic Year [" + existing.getAcademicYear() + "]: ");
            String academicYear = scanner.nextLine().trim();

            System.out.print("Phone Number ["
                    + display(existing.getPhoneNumber()) + "]: ");
            String phoneNumber = scanner.nextLine().trim();

            if (!rollNumber.isEmpty()) {
                existing.setRollNumber(rollNumber);
            }
            if (!fullName.isEmpty()) {
                existing.setFullName(fullName);
            }
            if (!email.isEmpty()) {
                existing.setEmail(email);
            }
            if (!department.isEmpty()) {
                existing.setDepartment(department);
            }
            if (!academicYear.isEmpty()) {
                existing.setAcademicYear(academicYear);
            }
            if (!phoneNumber.isEmpty()) {
                existing.setPhoneNumber(phoneNumber);
            }

            if (studentService.updateStudent(existing)) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println(
                        "Update failed. Check the details and roll number.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Enter a number.");
        }
    }

    private void deleteStudent() {
        System.out.print("Enter the Student ID to delete: ");

        try {
            int studentId = Integer.parseInt(scanner.nextLine().trim());

            Student existing = studentService.getStudentById(studentId);

            if (existing == null) {
                System.out.println("Student not found.");
                return;
            }

            printStudent(existing);

            System.out.print(
                    "Are you sure you want to delete this student? (yes/no): ");

            String confirmation = scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            if (studentService.deleteStudent(studentId)) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Could not delete the student.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Enter a number.");
        }
    }

    private void printStudent(Student student) {
        System.out.println("\n--- Student Details ---");
        System.out.println("Student ID    : " + student.getStudentId());
        System.out.println("Roll Number   : " + student.getRollNumber());
        System.out.println("Full Name     : " + student.getFullName());
        System.out.println("Email         : " + display(student.getEmail()));
        System.out.println("Department    : " + student.getDepartment());
        System.out.println("Academic Year : " + student.getAcademicYear());
        System.out.println("Phone Number  : " + display(student.getPhoneNumber()));
    }

    private String display(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}