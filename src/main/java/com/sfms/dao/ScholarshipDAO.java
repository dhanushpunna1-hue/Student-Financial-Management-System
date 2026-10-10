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

    // Add a scholarship
    public void addScholarship(Scholarship scholarship) {

        String sql = "INSERT INTO scholarship " +
                "(student_id, scholarship_name, amount, academic_year, status, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, scholarship.getStudentId());
            statement.setString(2, scholarship.getScholarshipName());
            statement.setDouble(3, scholarship.getAmount());
            statement.setString(4, scholarship.getAcademicYear());
            statement.setString(5, scholarship.getStatus());
            statement.setString(6, scholarship.getDescription());

            statement.executeUpdate();

            System.out.println("Scholarship added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding scholarship!");
            e.printStackTrace();
        }
    }

    // View all scholarships
    public List<Scholarship> getAllScholarships() {

        List<Scholarship> scholarships = new ArrayList<>();

        String sql = "SELECT * FROM scholarship";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Scholarship scholarship = new Scholarship(
                        resultSet.getInt("scholarship_id"),
                        resultSet.getInt("student_id"),
                        resultSet.getString("scholarship_name"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("academic_year"),
                        resultSet.getString("status"),
                        resultSet.getString("description"));

                scholarships.add(scholarship);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving scholarships!");
            e.printStackTrace();
        }

        return scholarships;
    }

    // Update scholarship details
    public void updateScholarship(Scholarship scholarship) {

        String sql = "UPDATE scholarship SET " +
                "student_id = ?, scholarship_name = ?, amount = ?, " +
                "academic_year = ?, status = ?, description = ? " +
                "WHERE scholarship_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, scholarship.getStudentId());
            statement.setString(2, scholarship.getScholarshipName());
            statement.setDouble(3, scholarship.getAmount());
            statement.setString(4, scholarship.getAcademicYear());
            statement.setString(5, scholarship.getStatus());
            statement.setString(6, scholarship.getDescription());
            statement.setInt(7, scholarship.getScholarshipId());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Scholarship updated successfully!");
            } else {
                System.out.println("Scholarship ID not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error updating scholarship!");
            e.printStackTrace();
        }
    }

    // Delete a scholarship
    public void deleteScholarship(int scholarshipId) {

        String sql = "DELETE FROM scholarship WHERE scholarship_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, scholarshipId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Scholarship deleted successfully!");
            } else {
                System.out.println("Scholarship ID not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting scholarship!");
            e.printStackTrace();
        }
    }
}