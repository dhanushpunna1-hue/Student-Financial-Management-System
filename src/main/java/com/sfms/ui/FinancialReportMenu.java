package com.sfms.ui;

import com.sfms.service.FinancialReportService;

import java.sql.Date;
import java.util.Scanner;

public class FinancialReportMenu {

    private final Scanner scanner;
    private final FinancialReportService reportService;

    public FinancialReportMenu() {
        scanner = new Scanner(System.in);
        reportService = new FinancialReportService();
    }

    public void start() {

        while (true) {

            System.out.println("\n================================");
            System.out.println("        FINANCIAL REPORTS");
            System.out.println("================================");
            System.out.println("1. Generate Date-Range Report");
            System.out.println("2. Generate Current-Year Report");
            System.out.println("3. Back to Main Menu");
            System.out.println("================================");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    generateDateRangeReport();
                    break;

                case "2":
                    reportService.generateCurrentYearReport();
                    break;

                case "3":
                    return;

                default:
                    System.out.println("Invalid choice. Enter 1, 2, or 3.");
            }
        }
    }

    private void generateDateRangeReport() {

        try {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            Date startDate = Date.valueOf(scanner.nextLine().trim());

            System.out.print("Enter end date (YYYY-MM-DD): ");
            Date endDate = Date.valueOf(scanner.nextLine().trim());

            reportService.generateReport(startDate, endDate);

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Invalid date. Please use YYYY-MM-DD format.");
        }
    }
}