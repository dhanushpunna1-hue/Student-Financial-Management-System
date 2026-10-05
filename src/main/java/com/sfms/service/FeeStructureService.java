package com.sfms.service;

import com.sfms.dao.FeeStructureDAO;
import com.sfms.model.FeeStructure;

import java.util.List;

public class FeeStructureService {

    private final FeeStructureDAO feeStructureDAO;

    public FeeStructureService() {
        feeStructureDAO = new FeeStructureDAO();
    }

    // Add fee structure
    public void addFee(String feeName, double amount,
            String academicYear, String description) {

        if (feeName == null || feeName.trim().isEmpty()) {
            System.out.println("Fee name cannot be empty.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Fee amount must be greater than 0.");
            return;
        }

        if (academicYear == null || academicYear.trim().isEmpty()) {
            System.out.println("Academic year cannot be empty.");
            return;
        }

        FeeStructure fee = new FeeStructure(
                feeName,
                amount,
                academicYear,
                description);

        feeStructureDAO.addFeeStructure(fee);
    }

    // View all fee structures
    public void viewAllFees() {

        List<FeeStructure> fees = feeStructureDAO.getAllFeeStructures();

        if (fees.isEmpty()) {
            System.out.println("No fee structures found.");
            return;
        }

        System.out.println("\n========== FEE STRUCTURES ==========");

        for (FeeStructure fee : fees) {

            System.out.println("ID          : "
                    + fee.getFeeStructureId());

            System.out.println("Fee Name    : "
                    + fee.getFeeName());

            System.out.println("Amount      : ₹"
                    + fee.getAmount());

            System.out.println("Academic Year: "
                    + fee.getAcademicYear());

            System.out.println("Description : "
                    + fee.getDescription());

            System.out.println("------------------------------------");
        }
    }

    // Update fee structure
    public void updateFee(int id, String feeName,
            double amount, String academicYear,
            String description) {

        if (id <= 0) {
            System.out.println("Invalid fee ID.");
            return;
        }

        if (feeName == null || feeName.trim().isEmpty()) {
            System.out.println("Fee name cannot be empty.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Fee amount must be greater than 0.");
            return;
        }

        FeeStructure fee = new FeeStructure(
                id,
                feeName,
                amount,
                academicYear,
                description);

        feeStructureDAO.updateFeeStructure(fee);
    }

    // Delete fee structure
    public void deleteFee(int id) {

        if (id <= 0) {
            System.out.println("Invalid fee ID.");
            return;
        }

        feeStructureDAO.deleteFeeStructure(id);
    }
}