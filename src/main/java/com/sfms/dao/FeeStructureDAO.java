package com.sfms.dao;

import com.sfms.database.DBConnection;
import com.sfms.model.FeeStructure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeeStructureDAO {

    public void addFeeStructure(FeeStructure fee) {
        String sql = "INSERT INTO fees (student_id, fee_name, amount, due_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fee.getStudentId());
            statement.setString(2, fee.getFeeName());
            statement.setDouble(3, fee.getAmount());
            statement.setDate(4, fee.getDueDate());
            statement.executeUpdate();
            System.out.println("Fee record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding fee record!");
            e.printStackTrace();
        }
    }

    public List<FeeStructure> getAllFeeStructures() {
        List<FeeStructure> fees = new ArrayList<>();
        String sql = "SELECT fee_id, student_id, fee_name, amount, due_date FROM fees ORDER BY fee_id";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                fees.add(new FeeStructure(
                        resultSet.getInt("fee_id"),
                        resultSet.getInt("student_id"),
                        resultSet.getString("fee_name"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("due_date")));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving fee records!");
            e.printStackTrace();
        }
        return fees;
    }

    public void updateFeeStructure(FeeStructure fee) {
        String sql = "UPDATE fees SET student_id = ?, fee_name = ?, amount = ?, due_date = ? WHERE fee_id = ?";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fee.getStudentId());
            statement.setString(2, fee.getFeeName());
            statement.setDouble(3, fee.getAmount());
            statement.setDate(4, fee.getDueDate());
            statement.setInt(5, fee.getFeeId());
            int rows = statement.executeUpdate();
            System.out.println(rows > 0 ? "Fee record updated successfully." : "Fee ID not found.");
        } catch (SQLException e) {
            System.out.println("Error updating fee record!");
            e.printStackTrace();
        }
    }

    public void deleteFeeStructure(int feeId) {
        String sql = "DELETE FROM fees WHERE fee_id = ?";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, feeId);
            int rows = statement.executeUpdate();
            System.out.println(rows > 0 ? "Fee record deleted successfully." : "Fee ID not found.");
        } catch (SQLException e) {
            System.out.println("Error deleting fee record!");
            e.printStackTrace();
        }
    }
}
