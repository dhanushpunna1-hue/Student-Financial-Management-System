package com.sfms;

import com.sfms.ui.FeeStructureMenu;
import com.sfms.ui.PaymentMenu;
import com.sfms.ui.ScholarshipMenu;
import com.sfms.ui.ExpenseMenu;
import com.sfms.ui.PaymentHistoryMenu;
import com.sfms.ui.FinancialReportMenu;
import com.sfms.ui.StudentMenu;
import com.sfms.ui.PendingFeeMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        StudentMenu studentMenu = new StudentMenu();
        FeeStructureMenu feeStructureMenu = new FeeStructureMenu();
        PaymentMenu paymentMenu = new PaymentMenu();
        ScholarshipMenu scholarshipMenu = new ScholarshipMenu();
        ExpenseMenu expenseMenu = new ExpenseMenu();
        PaymentHistoryMenu paymentHistoryMenu = new PaymentHistoryMenu();
        FinancialReportMenu financialReportMenu = new FinancialReportMenu();
        PendingFeeMenu pendingFeeMenu = new PendingFeeMenu();

        while (true) {
            System.out.println("\n==============================================");
            System.out.println("       STUDENT FINANCIAL MANAGEMENT SYSTEM");
            System.out.println("==============================================");
            System.out.println("1. Student Management");
            System.out.println("2. Fee Structure Management");
            System.out.println("3. Fee Payment");
            System.out.println("4. Scholarship Management");
            System.out.println("5. Expenses Management");
            System.out.println("6. Payment History");
            System.out.println("7. Financial Reports");
            System.out.println("8. Pending Fees Management");
            System.out.println("9. Exit");
            System.out.println("==============================================");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    studentMenu.start();
                    break;
                case "2":
                    feeStructureMenu.start();
                    break;
                case "3":
                    paymentMenu.start();
                    break;
                case "4":
                    scholarshipMenu.start();
                    break;
                case "5":
                    expenseMenu.start();
                    break;
                case "6":
                    paymentHistoryMenu.start();
                    break;
                case "7":
                    financialReportMenu.start();
                    break;
                case "8":
                    pendingFeeMenu.start();
                    break;
                case "9":
                    System.out.println(
                            "Thank you for using Student Financial Management System!");
                    scanner.close();
                    return;
                default:
                    System.out.println(
                            "Invalid choice. Please enter a number between 1 and 9.");
            }
        }
    }
}