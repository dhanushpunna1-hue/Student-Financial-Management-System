package com.sfms.dao;

import com.sfms.database.DBConnection;
import com.sfms.model.Student;
import com.sfms.service.PasswordUtil;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public void registerStudent(Student student, String password)
            throws SQLException, GeneralSecurityException {

        String passwordHash = PasswordUtil.hashPassword(password);

        String studentSql = """
                INSERT INTO students
                    (student_id, full_name, email, phone, course, study_year)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String userSql = """
                INSERT INTO users (student_id, password_hash)
                VALUES (?, ?)
                """;

        try (Connection connection = DBConnection.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement statement =
                             connection.prepareStatement(studentSql)) {
                    statement.setInt(1, student.getStudentId());
                    statement.setString(2, student.getFullName());
                    statement.setString(3, student.getEmail());
                    statement.setString(4, student.getPhone());
                    statement.setString(5, student.getCourse());
                    statement.setString(6, student.getStudyYear());
                    statement.executeUpdate();
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(userSql)) {
                    statement.setInt(1, student.getStudentId());
                    statement.setString(2, passwordHash);
                    statement.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    public Student authenticate(String email, String password)
            throws SQLException, GeneralSecurityException {

        String sql = """
                SELECT s.student_id, s.full_name, s.email, s.phone,
                       s.course, s.study_year, u.password_hash
                FROM students s
                JOIN users u ON u.student_id = s.student_id
                WHERE LOWER(s.email) = LOWER(?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email.trim());

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                if (!PasswordUtil.verifyPassword(
                        password, result.getString("password_hash"))) {
                    return null;
                }

                return mapStudent(result);
            }
        }
    }

    public void updatePhone(int studentId, String phone) throws SQLException {
        String sql = "UPDATE students SET phone = ? WHERE student_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (phone == null || phone.isBlank()) {
                statement.setNull(1, java.sql.Types.VARCHAR);
            } else {
                statement.setString(1, phone.trim());
            }

            statement.setInt(2, studentId);

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Student ID was not found.");
            }
        }
    }

    public List<Student> searchStudents(String term) throws SQLException {
        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT student_id, full_name, email, phone, course, study_year
                FROM students
                WHERE CAST(student_id AS CHAR) LIKE ?
                   OR full_name LIKE ?
                ORDER BY full_name
                """;

        String pattern = "%" + term.trim() + "%";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, pattern);
            statement.setString(2, pattern);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    students.add(mapStudent(result));
                }
            }
        }

        return students;
    }

    public Student findById(int studentId) throws SQLException {
        String sql = """
                SELECT student_id, full_name, email, phone, course, study_year
                FROM students
                WHERE student_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? mapStudent(result) : null;
            }
        }
    }

    // Methods used by the student management menu

    public boolean addStudent(Student student) {
        final int id;

        try {
            id = Integer.parseInt(student.getRollNumber().trim());
        } catch (Exception e) {
            System.out.println("Roll number must be a number.");
            return false;
        }

        String sql = """
                INSERT INTO students
                    (student_id, full_name, email, phone, course, study_year)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setString(2, student.getFullName());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getPhoneNumber());
            statement.setString(5, student.getDepartment());
            statement.setString(6, student.getAcademicYear());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = """
                SELECT student_id, full_name, email, phone, course, study_year
                FROM students
                ORDER BY student_id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                students.add(mapStudent(result));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }

        return students;
    }

    public Student getStudentById(int studentId) {
        try {
            return findById(studentId);
        } catch (SQLException e) {
            System.out.println("Error searching student: " + e.getMessage());
            return null;
        }
    }

    public boolean updateStudent(Student student) {
        final int id;

        try {
            id = Integer.parseInt(student.getRollNumber().trim());
        } catch (Exception e) {
            System.out.println("Roll number must be a number.");
            return false;
        }

        String sql = """
                UPDATE students
                SET full_name = ?, email = ?, phone = ?, course = ?, study_year = ?
                WHERE student_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getFullName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getPhoneNumber());
            statement.setString(4, student.getDepartment());
            statement.setString(5, student.getAcademicYear());
            statement.setInt(6, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    private Student mapStudent(ResultSet result) throws SQLException {
        int id = result.getInt("student_id");

        return new Student(
                id,
                String.valueOf(id),
                result.getString("full_name"),
                result.getString("email"),
                result.getString("course"),
                result.getString("study_year"),
                result.getString("phone"));
    }
}