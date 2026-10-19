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
    public void addExpense(int studentId,
            String expenseName,
            double amount,
            Date expenseDate,
            String notes) {

        if (studentId <= 0) {
            System.out.println("Student ID must be greater than zero.");
            return;
        }

        if (expenseName == null || expenseName.trim().isEmpty()) {
            System.out.println("Expense name cannot be empty.");
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
                studentId,
                expenseName.trim(),
                amount,
                expenseDate,
                notes == null ? "" : notes.trim());

        expenseDAO.addExpense(expense);
    }

    // View all expenses
    public void viewAllExpenses() {
        List<Expense> expenses = getAllExpenses();

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("\n========== EXPENSE RECORDS ==========");

        for (Expense expense : expenses) {

            System.out.println("Expense ID  : " + expense.getExpenseId());
            System.out.println("Name        : " + expense.getExpenseName());
            System.out.println("Student ID  : " + expense.getStudentId());
            System.out.println("Amount      : ₹" + expense.getAmount());
            System.out.println("Date        : " + expense.getExpenseDate());
            System.out.println("Notes       : " + expense.getNotes());
            System.out.println("-------------------------------------");
        }

        System.out.println("Total Expenses: ₹" + expenseDAO.getTotalExpenses());
    }

    public List<Expense> getAllExpenses() {
        return expenseDAO.getAllExpenses();
    }

    // Update an expense
    public void updateExpense(int expenseId,
            int studentId,
            String expenseName,
            double amount,
            Date expenseDate,
            String notes) {

        if (expenseId <= 0) {
            System.out.println("Invalid expense ID.");
            return;
        }

        if (studentId <= 0) {
            System.out.println("Student ID must be greater than zero.");
            return;
        }

        if (expenseName == null || expenseName.trim().isEmpty()) {
            System.out.println("Expense name cannot be empty.");
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
                studentId,
                expenseName.trim(),
                amount,
                expenseDate,
                notes == null ? "" : notes.trim());

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
