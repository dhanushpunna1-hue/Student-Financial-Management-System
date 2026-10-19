package com.sfms.dao;

import com.sfms.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class PaymentHistoryDAO {

    // Display all previous payments
    public void viewPaymentHistory() {

        String sql = "SELECT payment_id, fee_id, amount_paid, payment_date " +
                "FROM payments ORDER BY payment_date DESC";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            boolean found = false;

            System.out.println("\n========== PAYMENT HISTORY ==========");

            while (resultSet.next()) {

                found = true;

                System.out.println("Payment ID     : "
                        + resultSet.getInt("payment_id"));

                System.out.println("Fee ID         : "
                        + resultSet.getInt("fee_id"));

                System.out.println("Amount         : ₹"
                        + resultSet.getBigDecimal("amount_paid"));

                System.out.println("Payment Date   : "
                        + resultSet.getDate("payment_date"));

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
        System.out.println("\nTotal Payments Received: ₹" + getTotalPayments());
    }

    public BigDecimal getTotalPayments() {
        String sql = "SELECT COALESCE(SUM(amount_paid), 0) AS total FROM payments";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                BigDecimal total = resultSet.getBigDecimal("total");
                return total == null ? BigDecimal.ZERO : total;
            }
            return BigDecimal.ZERO;

        } catch (SQLException e) {
            throw new IllegalStateException("Could not retrieve total payments.", e);
        }
    }
}
