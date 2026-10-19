package com.sfms.ui;

import com.sfms.service.ScholarshipService;
import java.sql.Date;
import java.util.Scanner;

public class ScholarshipMenu {

    private final Scanner scanner;
    private final ScholarshipService service;

    public ScholarshipMenu() {
        scanner = new Scanner(System.in);
        service = new ScholarshipService();
    }

    public void start() {

        while (true) {

            System.out.println("\n================================");
            System.out.println("     SCHOLARSHIP MANAGEMENT");
            System.out.println("================================");
            System.out.println("1. Add Scholarship");
            System.out.println("2. View Scholarships");
            System.out.println("3. Update Scholarship");
            System.out.println("4. Delete Scholarship");
            System.out.println("5. Back to Main Menu");
            System.out.println("================================");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addScholarship();
                    break;

                case 2:
                    service.viewAllScholarships();
                    break;

                case 3:
                    updateScholarship();
                    break;

                case 4:
                    deleteScholarship();
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addScholarship() {

        try {
            System.out.println("\n--- Add Scholarship ---");

            System.out.print("Enter student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter scholarship name: ");
            String name = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter academic year (e.g. 2026-27): ");
            String year = scanner.nextLine();

            System.out.print("Enter status (Pending/Approved/Rejected): ");
            String status = scanner.nextLine();

            System.out.print("Awarded date (YYYY-MM-DD, blank if none): ");
            Date awardedDate = readOptionalDate();

            service.addScholarship(
                    studentId, name, amount, year, status, awardedDate);

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid number or date. Use YYYY-MM-DD for awarded date.");
        }
    }

    private void updateScholarship() {

        try {
            System.out.println("\n--- Update Scholarship ---");

            System.out.print("Enter scholarship ID: ");
            int scholarshipId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter scholarship name: ");
            String name = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter academic year: ");
            String year = scanner.nextLine();

            System.out.print("Enter status (Pending/Approved/Rejected): ");
            String status = scanner.nextLine();

            System.out.print("Awarded date (YYYY-MM-DD, blank if none): ");
            Date awardedDate = readOptionalDate();

            service.updateScholarship(
                    scholarshipId, studentId, name, amount,
                    year, status, awardedDate);

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid number or date. Use YYYY-MM-DD for awarded date.");
        }
    }

    private void deleteScholarship() {

        try {
            System.out.print("Enter scholarship ID to delete: ");
            int scholarshipId = Integer.parseInt(scanner.nextLine());

            service.deleteScholarship(scholarshipId);

        } catch (NumberFormatException e) {
            System.out.println("Invalid scholarship ID.");
        }
    }

    private Date readOptionalDate() {
        String value = scanner.nextLine().trim();
        return value.isEmpty() ? null : Date.valueOf(value);
    }
}
