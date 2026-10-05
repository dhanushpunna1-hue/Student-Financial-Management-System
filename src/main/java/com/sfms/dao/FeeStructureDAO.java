package com.sfms.dao;

import com.sfms.database.DBConnection;
import com.sfms.model.FeeStructure;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeeStructureDAO {

    // ADD FEE STRUCTURE
    public void addFeeStructure(FeeStructure fee) {

        String sql = "INSERT INTO fee_structure " +
                "(fee_name, amount, academic_year, description) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, fee.getFeeName());
            statement.setDouble(2, fee.getAmount());
            statement.setString(3, fee.getAcademicYear());
            statement.setString(4, fee.getDescription());

            statement.executeUpdate();

            System.out.println("Fee structure added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding fee structure!");
            e.printStackTrace();
        }
    }

    // VIEW ALL FEE STRUCTURES
    public List<FeeStructure> getAllFeeStructures() {

        List<FeeStructure> feeList = new ArrayList<>();

        String sql = "SELECT * FROM fee_structure";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                FeeStructure fee = new FeeStructure(
                        resultSet.getInt("fee_structure_id"),
                        resultSet.getString("fee_name"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("academic_year"),
                        resultSet.getString("description"));

                feeList.add(fee);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving fee structures!");
            e.printStackTrace();
        }

        return feeList;
    }

    // UPDATE FEE STRUCTURE
    public void updateFeeStructure(FeeStructure fee) {

        String sql = "UPDATE fee_structure SET " +
                "fee_name = ?, amount = ?, academic_year = ?, description = ? " +
                "WHERE fee_structure_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, fee.getFeeName());
            statement.setDouble(2, fee.getAmount());
            statement.setString(3, fee.getAcademicYear());
            statement.setString(4, fee.getDescription());
            statement.setInt(5, fee.getFeeStructureId());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Fee structure updated successfully!");
            } else {
                System.out.println("Fee structure not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error updating fee structure!");
            e.printStackTrace();
        }
    }

    // DELETE FEE STRUCTURE
    public void deleteFeeStructure(int feeStructureId) {

        String sql = "DELETE FROM fee_structure " +
                "WHERE fee_structure_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, feeStructureId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Fee structure deleted successfully!");
            } else {
                System.out.println("Fee structure not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting fee structure!");
            e.printStackTrace();
        }
    }
}