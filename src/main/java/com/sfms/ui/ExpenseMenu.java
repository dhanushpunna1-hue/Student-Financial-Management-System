package com.sfms.ui;

import com.sfms.service.ExpenseService;

import java.sql.Date;
import java.util.Scanner;

public class ExpenseMenu {

    private final Scanner scanner;
    private final ExpenseService service;

    public ExpenseMenu() {
        scanner = new Scanner(System.in);
        service = new ExpenseService();
    }

    public void start() {

        while (true) {

            System.out.println("\n================================");
            System.out.println("       EXPENSE MANAGEMENT");
            System.out.println("================================");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Update Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. View Total Expenses");
            System.out.println("6. Back to Main Menu");
            System.out.println("================================");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addExpense();
                    break;

                case 2:
                    service.viewAllExpenses();
                    break;

                case 3:
                    updateExpense();
                    break;

                case 4:
                    deleteExpense();
                    break;

                case 5:
                    service.viewTotalExpenses();
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addExpense() {

        try {
            System.out.println("\n--- Add Expense ---");

            System.out.print("Enter expense name: ");
            String name = scanner.nextLine();

            System.out.print("Enter category (Academic/Event/Other): ");
            String category = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter date (YYYY-MM-DD): ");
            Date date = Date.valueOf(scanner.nextLine());

            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            service.addExpense(name, category, amount, date, description);

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD format.");
        }
    }

    private void updateExpense() {

        try {
            System.out.println("\n--- Update Expense ---");

            System.out.print("Enter expense ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter new expense name: ");
            String name = scanner.nextLine();

            System.out.print("Enter new category: ");
            String category = scanner.nextLine();

            System.out.print("Enter new amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter new date (YYYY-MM-DD): ");
            Date date = Date.valueOf(scanner.nextLine());

            System.out.print("Enter new description: ");
            String description = scanner.nextLine();

            service.updateExpense(
                    id, name, category, amount, date, description);

        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD format.");
        }
    }

    private void deleteExpense() {

        try {
            System.out.print("Enter expense ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());

            service.deleteExpense(id);

        } catch (NumberFormatException e) {
            System.out.println("Invalid expense ID.");
        }
    }
}