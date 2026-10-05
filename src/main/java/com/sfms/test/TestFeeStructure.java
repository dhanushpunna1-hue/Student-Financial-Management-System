package com.sfms.test;

import com.sfms.dao.FeeStructureDAO;
import com.sfms.model.FeeStructure;

import java.util.List;

public class TestFeeStructure {

    public static void main(String[] args) {

        FeeStructureDAO dao = new FeeStructureDAO();

        // Add a fee
        FeeStructure fee = new FeeStructure(
                "Tuition Fee",
                50000,
                "2026-27",
                "B.Tech Tuition Fee");

        dao.addFeeStructure(fee);

        // View all fees
        List<FeeStructure> fees = dao.getAllFeeStructures();

        System.out.println("\n--- Fee Structures ---");

        for (FeeStructure f : fees) {
            System.out.println(
                    f.getFeeStructureId() + " | " +
                            f.getFeeName() + " | ₹" +
                            f.getAmount() + " | " +
                            f.getAcademicYear() + " | " +
                            f.getDescription());
        }
    }
}