package com.sfms.dao;

import com.sfms.database.DBConnection;
import com.sfms.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // 1. Add a student
    public boolean addStudent(Student student) {

        String sql = "INSERT INTO student "
                + "(roll_number, full_name, email, department, "
                + "academic_year, phone_number) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getRollNumber());
            statement.setString(2, student.getFullName());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getDepartment());
            statement.setString(5, student.getAcademicYear());
            statement.setString(6, student.getPhoneNumber());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    // 2. View all students
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM student ORDER BY student_id";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                students.add(mapStudent(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving students: "
                    + e.getMessage());
        }

        return students;
    }

    // 3. Search student by ID
    public Student getStudentById(int studentId) {

        String sql = "SELECT * FROM student WHERE student_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapStudent(resultSet);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching student: "
                    + e.getMessage());
        }

        return null;
    }

    // 4. Update a student
    public boolean updateStudent(Student student) {

        String sql = "UPDATE student SET roll_number = ?, "
                + "full_name = ?, email = ?, department = ?, "
                + "academic_year = ?, phone_number = ? "
                + "WHERE student_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getRollNumber());
            statement.setString(2, student.getFullName());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getDepartment());
            statement.setString(5, student.getAcademicYear());
            statement.setString(6, student.getPhoneNumber());
            statement.setInt(7, student.getStudentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating student: "
                    + e.getMessage());
            return false;
        }
    }

    // 5. Delete a student
    public boolean deleteStudent(int studentId) {

        String sql = "DELETE FROM student WHERE student_id = ?";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting student: "
                    + e.getMessage());
            return false;
        }
    }

    // Convert a database row into a Student object
    private Student mapStudent(ResultSet resultSet)
            throws SQLException {

        return new Student(
                resultSet.getInt("student_id"),
                resultSet.getString("roll_number"),
                resultSet.getString("full_name"),
                resultSet.getString("email"),
                resultSet.getString("department"),
                resultSet.getString("academic_year"),
                resultSet.getString("phone_number"));
    }
}