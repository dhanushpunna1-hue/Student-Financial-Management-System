package com.sfms.dao;

import com.sfms.database.DBConnection;
import com.sfms.model.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    // Add a new expense
    public void addExpense(Expense expense) {

        String sql = "INSERT INTO expenses " +
                "(student_id, expense_name, amount, expense_date, notes) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, expense.getStudentId());
            statement.setString(2, expense.getExpenseName());
            statement.setDouble(3, expense.getAmount());
            statement.setDate(4, expense.getExpenseDate());
            statement.setString(5, expense.getNotes());

            statement.executeUpdate();

            System.out.println("Expense added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding expense!");
            e.printStackTrace();
        }
    }

    // View all expenses
    public List<Expense> getAllExpenses() {

        List<Expense> expenses = new ArrayList<>();

        String sql = "SELECT * FROM expenses ORDER BY expense_date DESC";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Expense expense = new Expense(
                        resultSet.getInt("expense_id"),
                        resultSet.getInt("student_id"),
                        resultSet.getString("expense_name"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("expense_date"),
                        resultSet.getString("notes"));

                expenses.add(expense);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving expenses!");
            e.printStackTrace();
        }

        return expenses;
    }

    // Update an existing expense
    public void updateExpense(Expense expense) {

        String sql = "UPDATE expenses SET " +
                "student_id = ?, expense_name = ?, amount = ?, " +
                "expense_date = ?, notes = ? " +
                "WHERE expense_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, expense.getStudentId());
            statement.setString(2, expense.getExpenseName());
            statement.setDouble(3, expense.getAmount());
            statement.setDate(4, expense.getExpenseDate());
            statement.setString(5, expense.getNotes());
            statement.setInt(6, expense.getExpenseId());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Expense updated successfully!");
            } else {
                System.out.println("Expense ID not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error updating expense!");
            e.printStackTrace();
        }
    }

    // Delete an expense
    public void deleteExpense(int expenseId) {

        String sql = "DELETE FROM expenses WHERE expense_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, expenseId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Expense deleted successfully!");
            } else {
                System.out.println("Expense ID not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting expense!");
            e.printStackTrace();
        }
    }

    // Calculate total expenses
    public double getTotalExpenses() {

        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM expenses";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }

        } catch (SQLException e) {
            System.out.println("Error calculating total expenses!");
            e.printStackTrace();
        }

        return 0.0;
    }
}
