package com.sfms.service;

import com.sfms.dao.ExpenseDAO;
import com.sfms.model.Expense;

import java.sql.Date;
import java.util.List;

public class ExpenseService {

    private final ExpenseDAO expenseDAO;

    public ExpenseService() {
        expenseDAO = new ExpenseDAO();
    }

    // Add an expense
    public void addExpense(String expenseName,
            String category,
            double amount,
            Date expenseDate,
            String description) {

        if (expenseName == null || expenseName.trim().isEmpty()) {
            System.out.println("Expense name cannot be empty.");
            return;
        }

        if (category == null || category.trim().isEmpty()) {
            System.out.println("Category cannot be empty.");
            return;
        }

        if (amount <= 0 || !Double.isFinite(amount)) {
            System.out.println("Amount must be a valid positive number.");
            return;
        }

        if (expenseDate == null) {
            System.out.println("Expense date cannot be empty.");
            return;
        }

        Expense expense = new Expense(
                expenseName.trim(),
                category.trim(),
                amount,
                expenseDate,
                description == null ? "" : description.trim());

        expenseDAO.addExpense(expense);
    }

    // View all expenses
    public void viewAllExpenses() {

        List<Expense> expenses = expenseDAO.getAllExpenses();

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("\n========== EXPENSE RECORDS ==========");

        for (Expense expense : expenses) {

            System.out.println("Expense ID  : " + expense.getExpenseId());
            System.out.println("Name        : " + expense.getExpenseName());
            System.out.println("Category    : " + expense.getCategory());
            System.out.println("Amount      : ₹" + expense.getAmount());
            System.out.println("Date        : " + expense.getExpenseDate());
            System.out.println("Description : " + expense.getDescription());
            System.out.println("-------------------------------------");
        }

        System.out.println("Total Expenses: ₹" + expenseDAO.getTotalExpenses());
    }

    // Update an expense
    public void updateExpense(int expenseId,
            String expenseName,
            String category,
            double amount,
            Date expenseDate,
            String description) {

        if (expenseId <= 0) {
            System.out.println("Invalid expense ID.");
            return;
        }

        if (expenseName == null || expenseName.trim().isEmpty()) {
            System.out.println("Expense name cannot be empty.");
            return;
        }

        if (category == null || category.trim().isEmpty()) {
            System.out.println("Category cannot be empty.");
            return;
        }

        if (amount <= 0 || !Double.isFinite(amount)) {
            System.out.println("Amount must be a valid positive number.");
            return;
        }

        if (expenseDate == null) {
            System.out.println("Expense date cannot be empty.");
            return;
        }

        Expense expense = new Expense(
                expenseId,
                expenseName.trim(),
                category.trim(),
                amount,
                expenseDate,
                description == null ? "" : description.trim());

        expenseDAO.updateExpense(expense);
    }

    // Delete an expense
    public void deleteExpense(int expenseId) {

        if (expenseId <= 0) {
            System.out.println("Expense ID must be greater than zero.");
            return;
        }

        expenseDAO.deleteExpense(expenseId);
    }

    // Display total expenses
    public void viewTotalExpenses() {

        double total = expenseDAO.getTotalExpenses();

        System.out.println("\nTotal Expenses: ₹" + total);
    }
}