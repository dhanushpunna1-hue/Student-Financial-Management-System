package com.sfms.ui;

import com.sfms.service.FeeStructureService;
import java.sql.Date;
import java.util.Scanner;

public class FeeStructureMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final FeeStructureService service = new FeeStructureService();

    public void start() {
        while (true) {
            System.out.println("\n===== FEES =====");
            System.out.println("1. Add Fee\n2. View Fees\n3. Update Fee\n4. Delete Fee\n5. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addFee();
                    case "2" -> service.viewAllFees();
                    case "3" -> updateFee();
                    case "4" -> deleteFee();
                    case "5" -> { return; }
                    default -> System.out.println("Choose a number from 1 to 5.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid number or date. Use whole numbers and YYYY-MM-DD.");
            }
        }
    }

    private void addFee() {
        System.out.print("Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Fee name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Due date (YYYY-MM-DD, blank if none): ");
        Date dueDate = readOptionalDate();
        service.addFee(studentId, name, amount, dueDate);
    }

    private void updateFee() {
        System.out.print("Fee ID: ");
        int feeId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Fee name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Due date (YYYY-MM-DD, blank if none): ");
        Date dueDate = readOptionalDate();
        service.updateFee(feeId, studentId, name, amount, dueDate);
    }

    private Date readOptionalDate() {
        String value = scanner.nextLine().trim();
        return value.isEmpty() ? null : Date.valueOf(value);
    }

    private void deleteFee() {
        System.out.print("Fee ID: ");
        service.deleteFee(Integer.parseInt(scanner.nextLine().trim()));
    }
}
