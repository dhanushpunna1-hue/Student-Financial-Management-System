package com.sfms;

import com.sfms.ui.FeeStructureMenu;
import com.sfms.ui.PaymentMenu;
import com.sfms.ui.ScholarshipMenu;
import com.sfms.ui.ExpenseMenu;
import com.sfms.ui.PaymentHistoryMenu;
import com.sfms.ui.FinancialReportMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        FeeStructureMenu feeStructureMenu = new FeeStructureMenu();
        PaymentMenu paymentMenu = new PaymentMenu();
        ScholarshipMenu scholarshipMenu = new ScholarshipMenu();
        ExpenseMenu expenseMenu = new ExpenseMenu();
        PaymentHistoryMenu paymentHistoryMenu = new PaymentHistoryMenu();
        FinancialReportMenu financialReportMenu = new FinancialReportMenu();

        while (true) {

            System.out.println("\n==============================================");
            System.out.println("       STUDENT FINANCIAL MANAGEMENT SYSTEM");
            System.out.println("==============================================");
            System.out.println("1. Fee Structure Management");
            System.out.println("2. Fee Payment");
            System.out.println("3. Scholarship Management");
            System.out.println("4. Expenses Management");
            System.out.println("5. Payment History");
            System.out.println("6. Financial Reports");
            System.out.println("7. Exit");
            System.out.println("==============================================");

            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {

                case 1:
                    feeStructureMenu.start();
                    break;

                case 2:
                    paymentMenu.start();
                    break;

                case 3:
                    scholarshipMenu.start();
                    break;

                case 4:
                    expenseMenu.start();
                    break;

                case 5:
                    paymentHistoryMenu.start();
                    break;

                case 6:
                    financialReportMenu.start();
                    break;

                case 7:
                    System.out.println(
                            "Thank you for using Student Financial Management System!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}