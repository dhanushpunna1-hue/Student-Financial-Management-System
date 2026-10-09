package com.sfms.ui;

import com.sfms.model.PendingFee;
import com.sfms.service.PendingFeeService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PendingFeeMenu {

    private final PendingFeeService pendingFeeService;
    private final Scanner scanner;

    public PendingFeeMenu() {
        pendingFeeService = new PendingFeeService();
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n==================================");
            System.out.println("       PENDING FEES MANAGEMENT");
            System.out.println("==================================");
            System.out.println("1. View All Student Fees");
            System.out.println("2. Search Fees by Student ID");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAllFees();
                    break;
                case "2":
                    searchByStudentId();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please select 1–3.");
            }
        }
    }

    private void viewAllFees() {
        try {
            List<PendingFee> fees = pendingFeeService.getAllPendingFees();

            displayFees(fees);
        } catch (SQLException e) {
            System.out.println("Unable to retrieve fees: " + e.getMessage());
        }
    }

    private void searchByStudentId() {
        try {
            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine().trim());

            List<PendingFee> fees = pendingFeeService.getPendingFeesByStudentId(studentId);

            displayFees(fees);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numeric Student ID.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Unable to retrieve fees: " + e.getMessage());
        }
    }

    private void displayFees(List<PendingFee> fees) {
        if (fees.isEmpty()) {
            System.out.println("No fee assignments found.");
            return;
        }

        System.out.println("\n================ PENDING FEES REPORT ================");
        System.out.printf(
                "%-8s %-15s %-22s %-15s %-12s %-12s %-12s%n",
                "ID", "Roll No.", "Student Name", "Fee Name",
                "Total", "Paid", "Pending");

        System.out.println(
                "--------------------------------------------------------------------------");

        BigDecimal totalFees = BigDecimal.ZERO;
        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalPending = BigDecimal.ZERO;

        for (PendingFee fee : fees) {
            BigDecimal total = fee.getTotalAmount();
            BigDecimal paid = fee.getPaidAmount();
            BigDecimal pending = fee.getPendingAmount();

            System.out.printf(
                    "%-8d %-15s %-22s %-15s %-12s %-12s %-12s%n",
                    fee.getStudentId(),
                    fee.getRollNumber(),
                    fee.getStudentName(),
                    fee.getFeeName(),
                    total,
                    paid,
                    pending);

            System.out.println("Status: " + fee.getStatus());

            totalFees = totalFees.add(total);
            totalPaid = totalPaid.add(paid);
            totalPending = totalPending.add(pending);
        }

        System.out.println(
                "=====================================================");

        System.out.println("Total Assigned Fees: " + totalFees);
        System.out.println("Total Paid:          " + totalPaid);
        System.out.println("Total Pending:       " + totalPending);
    }
}