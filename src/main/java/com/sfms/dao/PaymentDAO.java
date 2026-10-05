package com.sfms.dao;

import com.sfms.database.DBConnection;
import com.sfms.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    // ADD PAYMENT
    public void addPayment(Payment payment) {

        String sql = "INSERT INTO payment " +
                "(fee_structure_id, amount, payment_date, payment_method, transaction_reference) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, payment.getFeeStructureId());
            statement.setDouble(2, payment.getAmount());
            statement.setDate(3, Date.valueOf(payment.getPaymentDate()));
            statement.setString(4, payment.getPaymentMethod());
            statement.setString(5, payment.getTransactionReference());

            statement.executeUpdate();

            System.out.println("Payment added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding payment!");
            e.printStackTrace();
        }
    }

    // VIEW ALL PAYMENTS
    public List<Payment> getAllPayments() {

        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT * FROM payment";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Payment payment = new Payment(
                        resultSet.getInt("payment_id"),
                        resultSet.getInt("fee_structure_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("payment_date").toLocalDate(),
                        resultSet.getString("payment_method"),
                        resultSet.getString("transaction_reference"));

                payments.add(payment);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving payments!");
            e.printStackTrace();
        }

        return payments;
    }

    // DELETE PAYMENT
    public void deletePayment(int paymentId) {

        String sql = "DELETE FROM payment WHERE payment_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, paymentId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Payment deleted successfully!");
            } else {
                System.out.println("Payment not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting payment!");
            e.printStackTrace();
        }
    }
}