package com.sfms.ui;

import com.sfms.service.PaymentService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class PaymentMenu {

    private final Scanner scanner;
    private final PaymentService service;

    public PaymentMenu() {
        scanner = new Scanner(System.in);
        service = new PaymentService();
    }

    public void start() {

        while (true) {

            System.out.println("\n================================");
            System.out.println("          FEE PAYMENT");
            System.out.println("================================");
            System.out.println("1. Add Payment");
            System.out.println("2. View Payments");
            System.out.println("3. Delete Payment");
            System.out.println("4. Exit");
            System.out.println("================================");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    addPayment();
                    break;

                case 2:
                    service.viewAllPayments();
                    break;

                case 3:
                    deletePayment();
                    break;

                case 4:
                    System.out.println("Exiting Fee Payment module...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addPayment() {

        System.out.println("\n--- Add Payment ---");

        System.out.print("Enter fee ID (from the fees table): ");
        int feeStructureId = scanner.nextInt();

        System.out.print("Enter payment amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter payment date (YYYY-MM-DD): ");
        String dateInput = scanner.nextLine();

        LocalDate paymentDate;

        try {
            paymentDate = LocalDate.parse(dateInput);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
            System.out.println("Use YYYY-MM-DD.");
            return;
        }

        service.addPayment(
                feeStructureId,
                amount,
                paymentDate);
    }

    private void deletePayment() {

        System.out.println("\n--- Delete Payment ---");

        System.out.print("Enter payment ID: ");
        int paymentId = scanner.nextInt();
        scanner.nextLine();

        service.deletePayment(paymentId);
    }
}
