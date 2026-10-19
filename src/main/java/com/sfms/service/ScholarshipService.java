package com.sfms.service;

import com.sfms.dao.ScholarshipDAO;
import com.sfms.model.Scholarship;

import java.sql.Date;
import java.util.List;

public class ScholarshipService {
    private final ScholarshipDAO scholarshipDAO = new ScholarshipDAO();

    public void addScholarship(int studentId, String scholarshipName, double amount,
            String academicYear, String status, Date awardedDate) {
        if (!validFields(studentId, scholarshipName, amount, academicYear, status)) return;
        scholarshipDAO.addScholarship(new Scholarship(studentId, scholarshipName.trim(), amount,
                academicYear.trim(), normalizeStatus(status), awardedDate));
    }

    /** Compatibility overload for older callers; description is no longer stored. */
    public void addScholarship(int studentId, String scholarshipName, double amount,
            String academicYear, String status, String ignoredDescription) {
        addScholarship(studentId, scholarshipName, amount, academicYear, status, (Date) null);
    }

    public List<Scholarship> getAllScholarships() {
        return scholarshipDAO.getAllScholarships();
    }

    public void viewAllScholarships() {
        List<Scholarship> scholarships = getAllScholarships();
        if (scholarships.isEmpty()) {
            System.out.println("No scholarships found.");
            return;
        }
        for (Scholarship scholarship : scholarships) {
            System.out.printf("%d | %d | %s | ₹%.2f | %s | %s | %s%n",
                    scholarship.getScholarshipId(), scholarship.getStudentId(),
                    scholarship.getScholarshipName(), scholarship.getAmount(),
                    scholarship.getAcademicYear(), scholarship.getStatus(),
                    scholarship.getAwardedDate() == null ? "-" : scholarship.getAwardedDate());
        }
    }

    public void updateScholarship(int scholarshipId, int studentId, String scholarshipName,
            double amount, String academicYear, String status, Date awardedDate) {
        if (scholarshipId <= 0) {
            System.out.println("Invalid scholarship ID.");
            return;
        }
        if (!validFields(studentId, scholarshipName, amount, academicYear, status)) return;
        scholarshipDAO.updateScholarship(new Scholarship(scholarshipId, studentId,
                scholarshipName.trim(), amount, academicYear.trim(), normalizeStatus(status), awardedDate));
    }

    /** Compatibility overload for older callers; description is no longer stored. */
    public void updateScholarship(int scholarshipId, int studentId, String scholarshipName,
            double amount, String academicYear, String status, String ignoredDescription) {
        updateScholarship(scholarshipId, studentId, scholarshipName, amount,
                academicYear, status, (Date) null);
    }

    public void deleteScholarship(int scholarshipId) {
        if (scholarshipId <= 0) {
            System.out.println("Invalid scholarship ID.");
            return;
        }
        scholarshipDAO.deleteScholarship(scholarshipId);
    }

    private boolean validFields(int studentId, String name, double amount, String year, String status) {
        if (studentId <= 0) {
            System.out.println("Student ID must be greater than 0.");
            return false;
        }
        if (name == null || name.isBlank()) {
            System.out.println("Scholarship name cannot be empty.");
            return false;
        }
        if (amount <= 0 || !Double.isFinite(amount)) {
            System.out.println("Scholarship amount must be a valid positive number.");
            return false;
        }
        if (year == null || year.isBlank() || year.length() > 9) {
            System.out.println("Academic year is required and must be at most 9 characters.");
            return false;
        }
        if (normalizeStatus(status) == null) {
            System.out.println("Status must be Pending, Approved, or Rejected.");
            return false;
        }
        return true;
    }

    private String normalizeStatus(String status) {
        if (status == null) return null;
        if (status.equalsIgnoreCase("Pending")) return "Pending";
        if (status.equalsIgnoreCase("Approved")) return "Approved";
        if (status.equalsIgnoreCase("Rejected")) return "Rejected";
        return null;
    }
}
