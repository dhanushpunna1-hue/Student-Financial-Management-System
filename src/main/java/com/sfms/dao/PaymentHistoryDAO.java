package com.sfms.dao;

import com.sfms.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentHistoryDAO {

    // Display all previous payments
    public void viewPaymentHistory() {

        String sql = "SELECT * FROM payment ORDER BY payment_date DESC";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            boolean found = false;

            System.out.println("\n========== PAYMENT HISTORY ==========");

            while (resultSet.next()) {

                found = true;

                System.out.println("Payment ID     : "
                        + resultSet.getInt("payment_id"));

                System.out.println("Fee Structure ID: "
                        + resultSet.getInt("fee_structure_id"));

                System.out.println("Amount         : ₹"
                        + resultSet.getBigDecimal("amount"));

                System.out.println("Payment Date   : "
                        + resultSet.getDate("payment_date"));

                System.out.println("Payment Method : "
                        + resultSet.getString("payment_method"));

                System.out.println("Transaction Ref: "
                        + resultSet.getString("transaction_reference"));

                System.out.println("--------------------------------------");
            }

            if (!found) {
                System.out.println("No payment records found.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving payment history!");
            e.printStackTrace();
        }
    }

    // Calculate total amount paid
    public void viewTotalPayments() {

        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM payment";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                System.out.println("\nTotal Payments Received: ₹"
                        + resultSet.getBigDecimal("total"));
            }

        } catch (SQLException e) {
            System.out.println("Error calculating total payments!");
            e.printStackTrace();
        }
    }
}