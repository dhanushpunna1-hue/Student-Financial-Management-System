package com.sfms.dao;

import com.sfms.database.DBConnection;
import com.sfms.model.Scholarship;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScholarshipDAO {

    public void addScholarship(Scholarship scholarship) {
        String sql = "INSERT INTO scholarships "
                + "(student_id, scholarship_name, amount, status, academic_year, awarded_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scholarship.getStudentId());
            statement.setString(2, scholarship.getScholarshipName());
            statement.setDouble(3, scholarship.getAmount());
            statement.setString(4, scholarship.getStatus());
            statement.setString(5, scholarship.getAcademicYear());
            statement.setDate(6, scholarship.getAwardedDate());
            statement.executeUpdate();
            System.out.println("Scholarship added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding scholarship!");
            e.printStackTrace();
        }
    }

    public List<Scholarship> getAllScholarships() {
        List<Scholarship> scholarships = new ArrayList<>();
        String sql = "SELECT scholarship_id, student_id, scholarship_name, amount, status, "
                + "academic_year, awarded_date FROM scholarships ORDER BY scholarship_id";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                scholarships.add(new Scholarship(
                        resultSet.getInt("scholarship_id"),
                        resultSet.getInt("student_id"),
                        resultSet.getString("scholarship_name"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("academic_year"),
                        resultSet.getString("status"),
                        resultSet.getDate("awarded_date")));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving scholarships!");
            e.printStackTrace();
        }
        return scholarships;
    }

    public void updateScholarship(Scholarship scholarship) {
        String sql = "UPDATE scholarships SET student_id = ?, scholarship_name = ?, amount = ?, "
                + "status = ?, academic_year = ?, awarded_date = ? WHERE scholarship_id = ?";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scholarship.getStudentId());
            statement.setString(2, scholarship.getScholarshipName());
            statement.setDouble(3, scholarship.getAmount());
            statement.setString(4, scholarship.getStatus());
            statement.setString(5, scholarship.getAcademicYear());
            statement.setDate(6, scholarship.getAwardedDate());
            statement.setInt(7, scholarship.getScholarshipId());
            int rows = statement.executeUpdate();
            System.out.println(rows > 0 ? "Scholarship updated successfully." : "Scholarship ID not found.");
        } catch (SQLException e) {
            System.out.println("Error updating scholarship!");
            e.printStackTrace();
        }
    }

    public void deleteScholarship(int scholarshipId) {
        String sql = "DELETE FROM scholarships WHERE scholarship_id = ?";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scholarshipId);
            int rows = statement.executeUpdate();
            System.out.println(rows > 0 ? "Scholarship deleted successfully." : "Scholarship ID not found.");
        } catch (SQLException e) {
            System.out.println("Error deleting scholarship!");
            e.printStackTrace();
        }
    }
}
