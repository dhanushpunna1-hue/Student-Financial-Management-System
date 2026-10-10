package com.sfms.ui;

import com.sfms.dao.StudentDAO;
import com.sfms.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StudentSearchFrame extends JFrame {
    private final JTextField searchField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[] { "Student ID", "Name", "Email", "Course", "Year" }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable resultsTable = new JTable(tableModel);

    public StudentSearchFrame() {
        setTitle("Student Search");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        searchButton.addActionListener(event -> searchStudents());
        searchField.addActionListener(event -> searchStudents());

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search by student ID or name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JButton detailsButton = new JButton("View Details");
        detailsButton.addActionListener(event -> openSelectedStudent());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(detailsButton);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(800, 450);
        setLocationRelativeTo(null);
    }

    private void searchStudents() {
        String term = searchField.getText().trim();

        if (term.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a student ID or name.");
            return;
        }

        tableModel.setRowCount(0);
        searchButton.setEnabled(false);
        searchField.setEnabled(false);

        SwingWorker<List<Student>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Student> doInBackground() throws SQLException {
                return new StudentDAO().searchStudents(term);
            }

            @Override
            protected void done() {
                try {
                    List<Student> students = get();

                    for (Student student : students) {
                        tableModel.addRow(new Object[] {
                                student.getStudentId(),
                                student.getFullName(),
                                student.getEmail(),
                                student.getCourse(),
                                student.getStudyYear()
                        });
                    }

                    if (students.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                StudentSearchFrame.this,
                                "No matching students found.");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    showError(e);
                } catch (ExecutionException e) {
                    showError(e.getCause());
                } finally {
                    searchButton.setEnabled(true);
                    searchField.setEnabled(true);
                    searchField.requestFocusInWindow();
                }
            }
        };

        worker.execute();
    }

    private void openSelectedStudent() {
        int selectedRow = resultsTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a student first.");
            return;
        }

        int modelRow = resultsTable.convertRowIndexToModel(selectedRow);
        int studentId = (int) tableModel.getValueAt(modelRow, 0);

        try {
            Student student = new StudentDAO().findById(studentId);

            if (student == null) {
                JOptionPane.showMessageDialog(this, "That student could not be found.");
                return;
            }

            new StudentDetailsFrame(student).setVisible(true);
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void showError(Throwable error) {
        JOptionPane.showMessageDialog(
                this,
                "The operation failed: " + error.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new StudentSearchFrame().setVisible(true));
    }
}