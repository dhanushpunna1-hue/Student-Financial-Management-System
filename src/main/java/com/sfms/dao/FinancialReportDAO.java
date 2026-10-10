package com.sfms.dao;

import com.sfms.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class FinancialReportDAO {

    // Get total payments within a date range
    public double getTotalPayments(Date startDate, Date endDate) {

        String sql = "SELECT COALESCE(SUM(amount), 0) AS total " +
                "FROM payment " +
                "WHERE payment_date BETWEEN ? AND ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, startDate);
            statement.setDate(2, endDate);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("total");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving payment report.");
            e.printStackTrace();
        }

        return 0.0;
    }

    // Get total expenses within a date range
    public double getTotalExpenses(Date startDate, Date endDate) {

        String sql = "SELECT COALESCE(SUM(amount), 0) AS total " +
                "FROM expenses " +
                "WHERE expense_date BETWEEN ? AND ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, startDate);
            statement.setDate(2, endDate);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("total");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving expense report.");
            e.printStackTrace();
        }

        return 0.0;
    }
}