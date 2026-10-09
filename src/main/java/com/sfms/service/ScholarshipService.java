package com.sfms.service;

import com.sfms.dao.ScholarshipDAO;
import com.sfms.model.Scholarship;

import java.util.List;

public class ScholarshipService {

    private final ScholarshipDAO scholarshipDAO;

    public ScholarshipService() {
        scholarshipDAO = new ScholarshipDAO();
    }

    // Add scholarship after validation
    public void addScholarship(int studentId,
            String scholarshipName,
            double amount,
            String academicYear,
            String status,
            String description) {

        if (studentId <= 0) {
            System.out.println("Student ID must be greater than 0.");
            return;
        }

        if (scholarshipName == null ||
                scholarshipName.trim().isEmpty()) {
            System.out.println("Scholarship name cannot be empty.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Scholarship amount must be greater than 0.");
            return;
        }

        if (academicYear == null ||
                academicYear.trim().isEmpty()) {
            System.out.println("Academic year cannot be empty.");
            return;
        }

        if (!isValidStatus(status)) {
            System.out.println("Status must be Pending, Approved, or Rejected.");
            return;
        }

        Scholarship scholarship = new Scholarship(
                studentId,
                scholarshipName.trim(),
                amount,
                academicYear.trim(),
                status,
                description);

        scholarshipDAO.addScholarship(scholarship);
    }

    // Display all scholarships
    public void viewAllScholarships() {

        List<Scholarship> scholarships = scholarshipDAO.getAllScholarships();

        if (scholarships.isEmpty()) {
            System.out.println("No scholarships found.");
            return;
        }

        System.out.println("\n========== SCHOLARSHIPS ==========");

        for (Scholarship scholarship : scholarships) {

            System.out.println("Scholarship ID : "
                    + scholarship.getScholarshipId());

            System.out.println("Student ID     : "
                    + scholarship.getStudentId());

            System.out.println("Name           : "
                    + scholarship.getScholarshipName());

            System.out.println("Amount         : ₹"
                    + scholarship.getAmount());

            System.out.println("Academic Year  : "
                    + scholarship.getAcademicYear());

            System.out.println("Status         : "
                    + scholarship.getStatus());

            System.out.println("Description    : "
                    + scholarship.getDescription());

            System.out.println("-----------------------------------");
        }
    }

    // Update scholarship
    public void updateScholarship(int scholarshipId,
            int studentId,
            String scholarshipName,
            double amount,
            String academicYear,
            String status,
            String description) {

        if (scholarshipId <= 0 || studentId <= 0) {
            System.out.println("Invalid scholarship ID or student ID.");
            return;
        }

        if (scholarshipName == null ||
                scholarshipName.trim().isEmpty()) {
            System.out.println("Scholarship name cannot be empty.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Scholarship amount must be greater than 0.");
            return;
        }

        if (academicYear == null ||
                academicYear.trim().isEmpty()) {
            System.out.println("Academic year cannot be empty.");
            return;
        }

        if (!isValidStatus(status)) {
            System.out.println("Invalid scholarship status.");
            return;
        }

        Scholarship scholarship = new Scholarship(
                scholarshipId,
                studentId,
                scholarshipName.trim(),
                amount,
                academicYear.trim(),
                status,
                description);

        scholarshipDAO.updateScholarship(scholarship);
    }

    // Delete scholarship
    public void deleteScholarship(int scholarshipId) {

        if (scholarshipId <= 0) {
            System.out.println("Invalid scholarship ID.");
            return;
        }

        scholarshipDAO.deleteScholarship(scholarshipId);
    }

    private boolean isValidStatus(String status) {

        return status != null &&
                (status.equalsIgnoreCase("Pending") ||
                        status.equalsIgnoreCase("Approved") ||
                        status.equalsIgnoreCase("Rejected"));
    }
}