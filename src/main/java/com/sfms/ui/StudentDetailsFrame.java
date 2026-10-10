package com.sfms.ui;

import com.sfms.model.Student;

import javax.swing.*;
import java.awt.*;

public class StudentDetailsFrame extends JFrame {
    public StudentDetailsFrame(Student student) {
        setTitle("Student Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel details = new JPanel(new GridLayout(6, 2, 12, 12));
        details.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addRow(details, "Student ID:", String.valueOf(student.getStudentId()));
        addRow(details, "Name:", student.getFullName());
        addRow(details, "Email:", student.getEmail());
        addRow(details, "Phone:", valueOrMissing(student.getPhone()));
        addRow(details, "Course:", valueOrMissing(student.getCourse()));
        addRow(details, "Year:", valueOrMissing(student.getStudyYear()));

        add(details, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void addRow(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value));
    }

    private String valueOrMissing(String value) {
        return value == null || value.isBlank() ? "Not provided" : value;
    }
}