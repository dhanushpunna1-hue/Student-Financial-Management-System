package com.sfms.service;

import com.sfms.dao.FeeStructureDAO;
import com.sfms.model.FeeStructure;

import java.sql.Date;
import java.util.List;

public class FeeStructureService {
    private final FeeStructureDAO feeStructureDAO = new FeeStructureDAO();

    public void addFee(int studentId, String feeName, double amount, Date dueDate) {
        if (studentId <= 0) {
            System.out.println("Enter a valid student ID.");
            return;
        }
        if (feeName == null || feeName.isBlank()) {
            System.out.println("Fee name cannot be empty.");
            return;
        }
        if (amount <= 0 || !Double.isFinite(amount)) {
            System.out.println("Fee amount must be a valid positive number.");
            return;
        }
        feeStructureDAO.addFeeStructure(new FeeStructure(studentId, feeName.trim(), amount, dueDate));
    }

    public List<FeeStructure> getAllFees() {
        return feeStructureDAO.getAllFeeStructures();
    }

    public void viewAllFees() {
        List<FeeStructure> fees = getAllFees();
        if (fees.isEmpty()) {
            System.out.println("No fee records found.");
            return;
        }
        System.out.println("FEE ID | STUDENT ID | FEE NAME | AMOUNT | DUE DATE");
        for (FeeStructure fee : fees) {
            System.out.printf("%d | %d | %s | ₹%.2f | %s%n",
                    fee.getFeeId(), fee.getStudentId(), fee.getFeeName(),
                    fee.getAmount(), fee.getDueDate() == null ? "-" : fee.getDueDate());
        }
    }

    public void updateFee(int feeId, int studentId, String feeName, double amount, Date dueDate) {
        if (feeId <= 0 || studentId <= 0) {
            System.out.println("Enter valid fee and student IDs.");
            return;
        }
        if (feeName == null || feeName.isBlank()) {
            System.out.println("Fee name cannot be empty.");
            return;
        }
        if (amount <= 0 || !Double.isFinite(amount)) {
            System.out.println("Fee amount must be a valid positive number.");
            return;
        }
        feeStructureDAO.updateFeeStructure(
                new FeeStructure(feeId, studentId, feeName.trim(), amount, dueDate));
    }

    public void deleteFee(int feeId) {
        if (feeId <= 0) {
            System.out.println("Invalid fee ID.");
            return;
        }
        feeStructureDAO.deleteFeeStructure(feeId);
    }
}
