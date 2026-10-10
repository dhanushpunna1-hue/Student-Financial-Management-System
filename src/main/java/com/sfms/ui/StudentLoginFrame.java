package com.sfms.ui;

import com.sfms.dao.StudentDAO;
import com.sfms.model.Student;

import javax.swing.*;
import java.awt.*;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class StudentLoginFrame extends JFrame {
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public StudentLoginFrame() {
        setTitle("Student Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(event -> loginStudent());

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(event -> new StudentRegistrationFrame().setVisible(true));

        JPanel buttons = new JPanel();
        buttons.add(loginButton);
        buttons.add(registerButton);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void loginStudent() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Enter your email and password.");
            return;
        }

        try {
            Student student = new StudentDAO().authenticate(email, password);

            if (student == null) {
                JOptionPane.showMessageDialog(
                        this, "Email or password is incorrect.");
                return;
            }

            dispose();
            new StudentDashboardFrame(student).setVisible(true);

        } catch (SQLException | GeneralSecurityException e) {
            JOptionPane.showMessageDialog(
                    this, "Login failed. Check the Terminal for details.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new StudentLoginFrame().setVisible(true));
    }
}