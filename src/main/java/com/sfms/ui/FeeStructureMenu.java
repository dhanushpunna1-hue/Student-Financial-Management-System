package com.sfms.ui;

import com.sfms.service.FeeStructureService;

import java.util.Scanner;

public class FeeStructureMenu {

    private final Scanner scanner;
    private final FeeStructureService service;

    public FeeStructureMenu() {
        scanner = new Scanner(System.in);
        service = new FeeStructureService();
    }

    public void start() {

        while (true) {

            System.out.println("\n================================");
            System.out.println("        FEE STRUCTURE");
            System.out.println("================================");
            System.out.println("1. Add Fee Structure");
            System.out.println("2. View Fee Structures");
            System.out.println("3. Update Fee Structure");
            System.out.println("4. Delete Fee Structure");
            System.out.println("5. Exit");
            System.out.println("================================");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    addFee();
                    break;

                case 2:
                    service.viewAllFees();
                    break;

                case 3:
                    updateFee();
                    break;

                case 4:
                    deleteFee();
                    break;

                case 5:
                    System.out.println("Exiting Fee Structure module...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addFee() {

        System.out.println("\n--- Add Fee Structure ---");

        System.out.print("Enter fee name: ");
        String feeName = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter academic year: ");
        String academicYear = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        service.addFee(
                feeName,
                amount,
                academicYear,
                description);
    }

    private void updateFee() {

        System.out.println("\n--- Update Fee Structure ---");

        System.out.print("Enter fee ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new fee name: ");
        String feeName = scanner.nextLine();

        System.out.print("Enter new amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter new academic year: ");
        String academicYear = scanner.nextLine();

        System.out.print("Enter new description: ");
        String description = scanner.nextLine();

        service.updateFee(
                id,
                feeName,
                amount,
                academicYear,
                description);
    }

    private void deleteFee() {

        System.out.println("\n--- Delete Fee Structure ---");

        System.out.print("Enter fee ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        service.deleteFee(id);
    }
}