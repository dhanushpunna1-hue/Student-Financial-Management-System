package com.sfms.service;

import com.sfms.dao.FinancialReportDAO;

import java.sql.Date;
import java.time.LocalDate;

public class FinancialReportService {
    private final FinancialReportDAO reportDAO = new FinancialReportDAO();

    public ReportSummary getReportSummary(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required.");
        }
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
        double payments = reportDAO.getTotalPayments(startDate, endDate);
        double expenses = reportDAO.getTotalExpenses(startDate, endDate);
        return new ReportSummary(startDate, endDate, payments, expenses);
    }

    public ReportSummary getCurrentYearReportSummary() {
        LocalDate today = LocalDate.now();
        Date start = Date.valueOf(LocalDate.of(today.getYear(), 1, 1));
        Date end = Date.valueOf(today);
        return getReportSummary(start, end);
    }

    public void generateReport(Date startDate, Date endDate) {
        printReport(getReportSummary(startDate, endDate));
    }

    public void generateCurrentYearReport() {
        printReport(getCurrentYearReportSummary());
    }

    private void printReport(ReportSummary report) {
        System.out.println("\n==========================================");
        System.out.println("          FINANCIAL REPORT");
        System.out.println("==========================================");
        System.out.println("Report Start Date : " + report.getStartDate());
        System.out.println("Report End Date   : " + report.getEndDate());
        System.out.println("------------------------------------------");
        System.out.printf("Total Payments    : ₹%,.2f%n", report.getTotalPayments());
        System.out.printf("Total Expenses    : ₹%,.2f%n", report.getTotalExpenses());
        System.out.println("------------------------------------------");
        System.out.printf("Net Difference    : ₹%,.2f%n", report.getNetDifference());
        System.out.println("==========================================");
        System.out.println(report.getSummaryMessage());
    }

    public static final class ReportSummary {
        private final Date startDate;
        private final Date endDate;
        private final double totalPayments;
        private final double totalExpenses;

        public ReportSummary(Date startDate, Date endDate, double totalPayments, double totalExpenses) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.totalPayments = totalPayments;
            this.totalExpenses = totalExpenses;
        }

        public Date getStartDate() { return startDate; }
        public Date getEndDate() { return endDate; }
        public double getTotalPayments() { return totalPayments; }
        public double getTotalExpenses() { return totalExpenses; }
        public double getNetDifference() { return totalPayments - totalExpenses; }

        public String getSummaryMessage() {
            if (getNetDifference() > 0) return "Payments exceed recorded expenses.";
            if (getNetDifference() < 0) return "Recorded expenses exceed payments.";
            return "Payments and expenses are equal.";
        }
    }
}
