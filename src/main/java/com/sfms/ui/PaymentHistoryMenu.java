package com.sfms.ui;

import com.sfms.service.PaymentHistoryService;
import java.util.Scanner;

public class PaymentHistoryMenu {

    private final Scanner scanner;
    private final PaymentHistoryService service;

    public PaymentHistoryMenu() {
        scanner = new Scanner(System.in);
        service = new PaymentHistoryService();
    }

    public void start() {

        while (true) {

            System.out.println("\n================================");
            System.out.println("        PAYMENT HISTORY");
            System.out.println("================================");
            System.out.println("1. View All Payments");
            System.out.println("2. View Total Payments");
            System.out.println("3. Back to Main Menu");
            System.out.println("================================");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    service.viewPaymentHistory();
                    break;

                case "2":
                    service.viewTotalPayments();
                    break;

                case "3":
                    return;

                default:
                    System.out.println("Invalid choice. Enter 1, 2, or 3.");
            }
        }
    }
}
