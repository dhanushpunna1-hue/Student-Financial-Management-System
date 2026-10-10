package com.sfms;

import com.sfms.ui.ExpenseMenu;
import com.sfms.ui.FeeStructureMenu;
import com.sfms.ui.FinancialReportMenu;
import com.sfms.ui.PaymentHistoryMenu;
import com.sfms.ui.PaymentMenu;
import com.sfms.ui.PendingFeeMenu;
import com.sfms.ui.ScholarshipMenu;
import com.sfms.ui.StudentLoginFrame;
import com.sfms.ui.StudentMenu;

import javax.swing.SwingUtilities;
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
            System.out.println("1. Student Login");
            System.out.println("2. Student Management");
            System.out.println("3. Fee Structure Management");
            System.out.println("4. Fee Payment");
            System.out.println("5. Scholarship Management");
            System.out.println("6. Expenses Management");
            System.out.println("7. Payment History");
            System.out.println("8. Financial Reports");
            System.out.println("9. Pending Fees Management");
            System.out.println("10. Exit");
            System.out.println("==============================================");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    SwingUtilities.invokeLater(
                            () -> new StudentLoginFrame().setVisible(true));
                    break;
                case "2":
                    studentMenu.start();
                    break;
                case "3":
                    feeStructureMenu.start();
                    break;
                case "4":
                    paymentMenu.start();
                    break;
                case "5":
                    scholarshipMenu.start();
                    break;
                case "6":
                    expenseMenu.start();
                    break;
                case "7":
                    paymentHistoryMenu.start();
                    break;
                case "8":
                    financialReportMenu.start();
                    break;
                case "9":
                    pendingFeeMenu.start();
                    break;
                case "10":
                    System.out.println(
                            "Thank you for using Student Financial Management System!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 10.");
            }
        }
    }
}