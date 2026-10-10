package com.sfms.ui;

import com.sfms.dao.StudentDAO;
import com.sfms.model.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StudentProfileFrame extends JFrame {
    private final Student student;
    private final JTextField phoneField = new JTextField(20);
    private final JButton saveButton = new JButton("Save Phone");

    public StudentProfileFrame(Student student) {
        this.student = student;

        setTitle("Student Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addRow(form, "Student ID:",
                new JLabel(String.valueOf(student.getStudentId())));
        addRow(form, "Name:", new JLabel(student.getFullName()));
        addRow(form, "Email:", new JLabel(student.getEmail()));

        phoneField.setText(student.getPhone() == null ? "" : student.getPhone());
        addRow(form, "Phone (editable):", phoneField);

        addRow(form, "Course:", new JLabel(student.getCourse()));
        addRow(form, "Year:", new JLabel(student.getStudyYear()));

        saveButton.addActionListener(event -> savePhone());

        JPanel buttons = new JPanel();
        buttons.add(saveButton);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(400, 250));
        setLocationRelativeTo(null);
    }

    private void addRow(JPanel panel, String label, JComponent value) {
        panel.add(new JLabel(label));
        panel.add(value);
    }

    private void savePhone() {
        String phone = phoneField.getText().trim();

        if (!phone.isEmpty()
                && !phone.matches("\\+?[0-9() .-]{7,20}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a phone number using 7–20 digits, spaces, or + ( ) . -",
                    "Invalid Phone Number",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        saveButton.setEnabled(false);

        try {
            new StudentDAO().updatePhone(student.getStudentId(), phone);
            student.setPhone(phone.isEmpty() ? null : phone);

            JOptionPane.showMessageDialog(this, "Phone number saved.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not save the phone number: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            saveButton.setEnabled(true);
        }
    }
}