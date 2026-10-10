package com.sfms.ui;

import com.sfms.dao.StudentDAO;
import com.sfms.model.Student;

import javax.swing.*;
import java.awt.*;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class StudentRegistrationFrame extends JFrame {
    private final JTextField studentIdField = new JTextField(20);
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JTextField courseField = new JTextField(20);
    private final JTextField yearField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public StudentRegistrationFrame() {
        setTitle("Student Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addRow(form, "Student ID:", studentIdField);
        addRow(form, "Full name:", nameField);
        addRow(form, "Email:", emailField);
        addRow(form, "Phone:", phoneField);
        addRow(form, "Course:", courseField);
        addRow(form, "Year:", yearField);
        addRow(form, "Password:", passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(event -> registerStudent());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(registerButton);

        add(form, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void addRow(JPanel panel, String label, JComponent field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    private void registerStudent() {
        try {
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            String fullName = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String course = courseField.getText().trim();
            String studyYear = yearField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (studentId <= 0 || fullName.isEmpty() || email.isEmpty()
                    || course.isEmpty() || studyYear.isEmpty()
                    || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Fill in all fields except Phone.");
                return;
            }

            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Enter a valid email address.");
                return;
            }

            if (password.length() < 8) {
                JOptionPane.showMessageDialog(
                        this, "Password must be at least 8 characters.");
                return;
            }

            Student student = new Student(
                    studentId, fullName, email, phone, course, studyYear);

            new StudentDAO().registerStudent(student, password);

            JOptionPane.showMessageDialog(this, "Student registered successfully.");
            clearForm();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Student ID must be a whole number.");
        } catch (SQLException | GeneralSecurityException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed. Check that the student ID and email "
                            + "are not already used, and check the Terminal.");
            e.printStackTrace();
        }
    }

    private void clearForm() {
        studentIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        courseField.setText("");
        yearField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new StudentRegistrationFrame().setVisible(true));
    }
}