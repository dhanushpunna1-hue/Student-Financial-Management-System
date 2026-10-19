package com.sfms.dao;

import com.sfms.database.DBConnection;
import com.sfms.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    // ADD PAYMENT
    public void addPayment(Payment payment) {

        String sql = "INSERT INTO payments (fee_id, amount_paid, payment_date) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, payment.getFeeStructureId());
            statement.setDouble(2, payment.getAmount());
            statement.setDate(3, Date.valueOf(payment.getPaymentDate()));

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

        String sql = "SELECT payment_id, fee_id, amount_paid, payment_date FROM payments";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Payment payment = new Payment(
                        resultSet.getInt("payment_id"),
                        resultSet.getInt("fee_id"),
                        resultSet.getDouble("amount_paid"),
                        resultSet.getDate("payment_date").toLocalDate(),
                        null,
                        null);

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

        String sql = "DELETE FROM payments WHERE payment_id = ?";

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
