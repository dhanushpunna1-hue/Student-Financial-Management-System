package com.sfms.ui;

import com.sfms.database.DBConnection;
import com.sfms.model.Student;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class StudentDashboardFrame extends JFrame {
    private final Student student;

    private final JLabel feesValue = new JLabel("Loading...");
    private final JLabel scholarshipValue = new JLabel("Loading...");
    private final JLabel paidValue = new JLabel("Loading...");
    private final JLabel balanceValue = new JLabel("Loading...");
    private final JLabel expensesValue = new JLabel("Loading...");
    private final JLabel dueDateValue = new JLabel("Loading...");

    public StudentDashboardFrame(Student student) {
        this.student = student;

        setTitle("Student Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel welcomeLabel = new JLabel("Welcome, " + student.getFullName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel summary = new JPanel(new GridLayout(6, 2, 12, 12));
        summary.add(new JLabel("Total fees:"));
        summary.add(feesValue);
        summary.add(new JLabel("Approved scholarship:"));
        summary.add(scholarshipValue);
        summary.add(new JLabel("Total paid:"));
        summary.add(paidValue);
        summary.add(new JLabel("Balance:"));
        summary.add(balanceValue);
        summary.add(new JLabel("Total expenses:"));
        summary.add(expensesValue);
        summary.add(new JLabel("Next fee due date:"));
        summary.add(dueDateValue);

        JButton profileButton = new JButton("My Profile");
        profileButton.addActionListener(
                event -> new StudentProfileFrame(student).setVisible(true));

        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(event -> {
            dispose();
            new StudentLoginFrame().setVisible(true);
        });

        JPanel buttons = new JPanel();
        buttons.add(profileButton);
        buttons.add(logoutButton);

        JPanel content = new JPanel(new BorderLayout(15, 20));
        content.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        content.add(welcomeLabel, BorderLayout.NORTH);
        content.add(summary, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        add(content);
        pack();
        setMinimumSize(new Dimension(450, 350));
        setLocationRelativeTo(null);

        loadFinancialSummary();
    }

    private void loadFinancialSummary() {
        String feesSql = "SELECT COALESCE(SUM(amount), 0) FROM fees WHERE student_id = ?";

        String scholarshipSql = """
                SELECT COALESCE(SUM(amount), 0)
                FROM scholarships
                WHERE student_id = ? AND status = 'Approved'
                """;

        String paymentsSql = """
                SELECT COALESCE(SUM(p.amount_paid), 0)
                FROM payments p
                JOIN fees f ON f.fee_id = p.fee_id
                WHERE f.student_id = ?
                """;

        String expensesSql = "SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE student_id = ?";

        String dueDateSql = """
                SELECT MIN(due_date)
                FROM fees
                WHERE student_id = ? AND due_date >= CURRENT_DATE
                """;

        SwingWorker<DashboardSummary, Void> worker = new SwingWorker<>() {
            @Override
            protected DashboardSummary doInBackground() throws SQLException {
                try (Connection connection = DBConnection.getConnection()) {
                    BigDecimal fees = getAmount(connection, feesSql);
                    BigDecimal scholarship = getAmount(connection, scholarshipSql);
                    BigDecimal paid = getAmount(connection, paymentsSql);
                    BigDecimal expenses = getAmount(connection, expensesSql);
                    Date nextDueDate = getNextDueDate(connection, dueDateSql);

                    return new DashboardSummary(
                            fees, scholarship, paid, expenses, nextDueDate);
                }
            }

            @Override
            protected void done() {
                try {
                    DashboardSummary summary = get();

                    feesValue.setText(formatMoney(summary.fees));
                    scholarshipValue.setText(formatMoney(summary.scholarship));
                    paidValue.setText(formatMoney(summary.paid));

                    BigDecimal balance = summary.fees
                            .subtract(summary.scholarship)
                            .subtract(summary.paid);
                    balanceValue.setText(formatMoney(balance));

                    expensesValue.setText(formatMoney(summary.expenses));
                    dueDateValue.setText(summary.nextDueDate == null
                            ? "No upcoming due date"
                            : summary.nextDueDate.toString());

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    showLoadError(e);
                } catch (ExecutionException e) {
                    showLoadError(e.getCause());
                }
            }
        };

        worker.execute();
    }

    private BigDecimal getAmount(Connection connection, String sql)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, student.getStudentId());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    BigDecimal amount = result.getBigDecimal(1);
                    return amount == null ? BigDecimal.ZERO : amount;
                }
                return BigDecimal.ZERO;
            }
        }
    }

    private Date getNextDueDate(Connection connection, String sql)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, student.getStudentId());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getDate(1);
                }
                return null;
            }
        }
    }

    private String formatMoney(BigDecimal amount) {
        return "₹ " + new DecimalFormat("#,##0.00").format(amount);
    }

    private void showLoadError(Throwable error) {
        JOptionPane.showMessageDialog(
                this,
                "Could not load dashboard data. Check that the database table "
                        + "columns match the dashboard queries.\n\n"
                        + error.getMessage(),
                "Dashboard Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private static class DashboardSummary {
        private final BigDecimal fees;
        private final BigDecimal scholarship;
        private final BigDecimal paid;
        private final BigDecimal expenses;
        private final Date nextDueDate;

        private DashboardSummary(
                BigDecimal fees,
                BigDecimal scholarship,
                BigDecimal paid,
                BigDecimal expenses,
                Date nextDueDate) {
            this.fees = fees;
            this.scholarship = scholarship;
            this.paid = paid;
            this.expenses = expenses;
            this.nextDueDate = nextDueDate;
        }
    }
}