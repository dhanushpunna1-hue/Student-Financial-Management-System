package com.sfms.service;

import com.sfms.dao.FinancialReportDAO;

import java.sql.Date;
import java.time.LocalDate;

public class FinancialReportService {

    private final FinancialReportDAO reportDAO;

    public FinancialReportService() {
        reportDAO = new FinancialReportDAO();
    }

    public void generateReport(Date startDate, Date endDate) {

        if (startDate == null || endDate == null) {
            System.out.println("Start date and end date are required.");
            return;
        }

        if (startDate.after(endDate)) {
            System.out.println("Start date cannot be after end date.");
            return;
        }

        double totalPayments = reportDAO.getTotalPayments(startDate, endDate);

        double totalExpenses = reportDAO.getTotalExpenses(startDate, endDate);

        double balance = totalPayments - totalExpenses;

        System.out.println("\n==========================================");
        System.out.println("          FINANCIAL REPORT");
        System.out.println("==========================================");
        System.out.println("Report Start Date : " + startDate);
        System.out.println("Report End Date   : " + endDate);
        System.out.println("------------------------------------------");
        System.out.printf("Total Payments    : ₹%.2f%n", totalPayments);
        System.out.printf("Total Expenses    : ₹%.2f%n", totalExpenses);
        System.out.println("------------------------------------------");
        System.out.printf("Net Difference    : ₹%.2f%n", balance);
        System.out.println("==========================================");

        if (balance > 0) {
            System.out.println("Payments exceed recorded expenses.");
        } else if (balance < 0) {
            System.out.println("Recorded expenses exceed payments.");
        } else {
            System.out.println("Payments and expenses are equal.");
        }
    }

    public void generateCurrentYearReport() {

        LocalDate today = LocalDate.now();

        Date startDate = Date.valueOf(
                LocalDate.of(today.getYear(), 1, 1));

        Date endDate = Date.valueOf(today);

        generateReport(startDate, endDate);
    }
}