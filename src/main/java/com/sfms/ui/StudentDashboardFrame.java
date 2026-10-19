package com.sfms.ui;

import com.sfms.database.DBConnection;
import com.sfms.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class StudentDashboardFrame extends JFrame {
    private static final Color BACKGROUND = new Color(12, 12, 12);
    private static final Color PANEL = new Color(22, 22, 22);
    private static final Color FOREGROUND = new Color(241, 241, 241);
    private static final Color MUTED = new Color(201, 201, 201);
    private static final Color BORDER = new Color(64, 64, 64);
    private static final Color ACCENT = new Color(37, 95, 128);
    private static final Color GREEN = new Color(0, 180, 48);
    private static final Color RED = new Color(230, 230, 230);

    private final Student student;
    private final JLabel feesValue = valueLabel("Loading...");
    private final JLabel scholarshipValue = valueLabel("Loading...");
    private final JLabel paidValue = valueLabel("Loading...");
    private final JLabel balanceValue = valueLabel("Loading...");
    private final JLabel expensesValue = valueLabel("Loading...");
    private final JLabel dueDateValue = valueLabel("Loading...");
    private final JLabel dueStatusValue = new JLabel("Loading...");
    private final JLabel paymentProgressLabel = new JLabel("Loading...");
    private final JLabel paymentProgressPercentLabel = new JLabel("0%");
    private final JProgressBar paymentProgressBar = new JProgressBar(0, 10000);

public StudentDashboardFrame(Student student) {
        this.student = student;
        setTitle("Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(16, 28, 20, 28));

        JPanel appBar = new JPanel(new BorderLayout());
        appBar.setBackground(BACKGROUND);
        appBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER), new EmptyBorder(0, 0, 10, 0)));
        JLabel appTitle = new JLabel("Student Dashboard");
        appTitle.setForeground(FOREGROUND);
        appTitle.setFont(new Font("Arial", Font.BOLD, 15));
        appBar.add(appTitle, BorderLayout.WEST);
        JButton refreshButton = dashboardButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.addActionListener(event -> refreshFinancialSummary());
        appBar.add(refreshButton, BorderLayout.EAST);
        root.add(appBar, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setBackground(BACKGROUND);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JPanel welcomeRow = new JPanel(new BorderLayout(12, 0));
        welcomeRow.setBackground(BACKGROUND);
        JLabel welcome = new JLabel("Welcome, " + student.getFullName() + "!");
        welcome.setForeground(FOREGROUND);
        welcome.setFont(new Font("Arial", Font.BOLD, 48));
        welcomeRow.add(welcome, BorderLayout.WEST);
        body.add(welcomeRow);
        body.add(Box.createVerticalStrut(34));

        JPanel totals = new JPanel(new GridLayout(1, 3, 12, 0));
        totals.setBackground(BACKGROUND);
        totals.add(kpiCard("◉", "Total fees", feesValue, FOREGROUND));
        totals.add(kpiCard("▱", "Total paid", paidValue, FOREGROUND));
        totals.add(kpiCard("⚖", "Balance", balanceValue, RED));
        body.add(totals);
        body.add(Box.createVerticalStrut(42));

        JPanel progressHeader = new JPanel(new BorderLayout());
        progressHeader.setBackground(BACKGROUND);
        JLabel progressTitle = new JLabel("Payment progress");
        progressTitle.setForeground(MUTED);
        progressTitle.setFont(new Font("Arial", Font.PLAIN, 20));
        paymentProgressLabel.setForeground(MUTED);
        paymentProgressLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        progressHeader.add(progressTitle, BorderLayout.WEST);
        progressHeader.add(paymentProgressLabel, BorderLayout.EAST);
        body.add(progressHeader);
        paymentProgressBar.setStringPainted(false);
        paymentProgressBar.setForeground(GREEN);
        paymentProgressBar.setBackground(new Color(7, 7, 7));
        paymentProgressBar.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        paymentProgressBar.setPreferredSize(new Dimension(10, 18));
        paymentProgressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        body.add(Box.createVerticalStrut(12));
        body.add(paymentProgressBar);
        paymentProgressPercentLabel.setForeground(MUTED);
        paymentProgressPercentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        body.add(paymentProgressPercentLabel);
        body.add(Box.createVerticalStrut(18));

        JPanel details = new JPanel();
        details.setBackground(PANEL);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(8, 0, 8, 0)));
        addDetailRow(details, "♧", "Approved scholarship", scholarshipValue);
        addDetailRow(details, "₹", "Total expenses", expensesValue);
        body.add(details);
        body.add(Box.createVerticalGlue());

        JPanel buttons = new JPanel(new BorderLayout(14, 0));
        buttons.setBackground(BACKGROUND);
        JPanel accountButtons = new JPanel(new GridLayout(1, 2, 14, 0));
        accountButtons.setOpaque(false);
        JButton profile = dashboardButton("My Profile");
        profile.addActionListener(event -> new StudentProfileFrame(student).setVisible(true));
        JButton logout = dashboardButton("Log Out");
        logout.addActionListener(event -> {
            dispose();
            new StudentLoginFrame().setVisible(true);
        });
        JButton modules = dashboardButton("System Modules  →");
        modules.addActionListener(event -> {
            setVisible(false);
            new SystemModulesFrame(this).setVisible(true);
        });
        accountButtons.add(profile);
        accountButtons.add(logout);
        buttons.add(accountButtons, BorderLayout.WEST);
        buttons.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        buttons.add(modules, BorderLayout.EAST);

        root.add(body, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
        setMinimumSize(new Dimension(1100, 720));
        setSize(1300, 800);
        setLocationRelativeTo(null);
        loadFinancialSummary();
    }

    private JPanel kpiCard(String symbol, String title, JLabel value, Color valueColor) {
        JPanel card = new JPanel(new BorderLayout(18, 0));
        card.setBackground(PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(20, 18, 20, 18)));
        JLabel icon = new JLabel(symbol, SwingConstants.CENTER);
        icon.setForeground(MUTED);
        icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 34));
        icon.setPreferredSize(new Dimension(42, 58));
        JLabel heading = new JLabel(title);
        heading.setForeground(MUTED);
        heading.setFont(new Font("Arial", Font.PLAIN, 18));
        value.setForeground(valueColor);
        value.setFont(new Font("Arial", Font.BOLD, 28));
        JPanel labels = new JPanel();
        labels.setBackground(PANEL);
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
        labels.add(heading);
        labels.add(Box.createVerticalStrut(4));
        labels.add(value);
        card.add(icon, BorderLayout.WEST);
        card.add(labels, BorderLayout.CENTER);
        return card;
    }

    private void addDetailRow(JPanel panel, String icon, String title, JLabel value) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(PANEL);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        JLabel label = new JLabel(icon + "  " + title);
        label.setForeground(MUTED);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        value.setForeground(FOREGROUND);
        value.setFont(new Font("Arial", Font.BOLD, 20));
        row.add(label, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        panel.add(row);
    }

    private JButton dashboardButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(FOREGROUND);
        button.setBackground(PANEL);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(128, 132, 138), 1, true),
                new EmptyBorder(12, 16, 12, 16)));
        return button;
    }

    private void addSummaryRow(JPanel panel, String label, JLabel value) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        JLabel title = new JLabel(label);
        title.setForeground(MUTED);
        title.setFont(new Font("Arial", Font.BOLD, 21));
        row.add(title, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);
        panel.add(row);
    }

    private static JLabel valueLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setForeground(FOREGROUND);
        label.setFont(new Font("Arial", Font.BOLD, 21));
        return label;
    }

    static JButton styledButton(String text, boolean accent) {
        JButton button = new JButton(text);
        button.setForeground(accent ? new Color(105, 175, 255) : FOREGROUND);
        button.setBackground(PANEL);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent ? ACCENT : BORDER),
                new EmptyBorder(8, 16, 8, 16)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    static Color backgroundColor() { return BACKGROUND; }
    static Color panelColor() { return PANEL; }
    static Color foregroundColor() { return FOREGROUND; }
    static Color mutedColor() { return MUTED; }
    static Color borderColor() { return BORDER; }

    public void refreshFinancialSummary() {
        loadFinancialSummary();
    }

    private void loadFinancialSummary() {
        String feesSql = "SELECT COALESCE(SUM(amount), 0) FROM fees WHERE student_id = ?";
        String scholarshipSql = "SELECT COALESCE(SUM(amount), 0) FROM scholarships WHERE student_id = ? AND status = 'Approved'";
        String paymentsSql = "SELECT COALESCE(SUM(p.amount_paid), 0) FROM payments p JOIN fees f ON f.fee_id = p.fee_id WHERE f.student_id = ?";
        String expensesSql = "SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE student_id = ?";
        String dueDateSql = "SELECT MIN(due_date) FROM fees WHERE student_id = ? AND due_date >= CURRENT_DATE";

        SwingWorker<DashboardSummary, Void> worker = new SwingWorker<>() {
            @Override
            protected DashboardSummary doInBackground() throws SQLException {
                try (Connection connection = DBConnection.getConnection()) {
                    return new DashboardSummary(getAmount(connection, feesSql),
                            getAmount(connection, scholarshipSql), getAmount(connection, paymentsSql),
                            getAmount(connection, expensesSql), getNextDueDate(connection, dueDateSql));
                }
            }

            @Override
            protected void done() {
                try {
                    DashboardSummary summary = get();
                    feesValue.setText(formatCompactMoney(summary.fees));
                    scholarshipValue.setText(formatMoney(summary.scholarship));
                    paidValue.setText(formatCompactMoney(summary.paid));
                    BigDecimal balance = summary.fees.subtract(summary.scholarship).subtract(summary.paid);
                    balanceValue.setText(formatCompactMoney(balance));
                    dueStatusValue.setText(summary.nextDueDate == null ? "No dues upcoming" : "Due " + summary.nextDueDate);
                    BigDecimal paidPercent = summary.fees.signum() <= 0 ? BigDecimal.ZERO : summary.paid.multiply(BigDecimal.valueOf(100)).divide(summary.fees, 2, RoundingMode.HALF_UP);
                    paymentProgressLabel.setText(new DecimalFormat("0.00").format(paidPercent) + "% paid");
                    paymentProgressPercentLabel.setText(new DecimalFormat("0").format(paidPercent) + "%");
                    int progressUnits = summary.fees.signum() <= 0 ? 0 : summary.paid.multiply(BigDecimal.valueOf(10000)).divide(summary.fees, 0, RoundingMode.HALF_UP).intValue();
                    paymentProgressBar.setValue(Math.max(0, Math.min(10000, progressUnits)));
                    expensesValue.setText(formatMoney(summary.expenses));
                    dueDateValue.setText(summary.nextDueDate == null ? "No upcoming due date" : summary.nextDueDate.toString());
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

    private BigDecimal getAmount(Connection connection, String sql) throws SQLException {
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

    private Date getNextDueDate(Connection connection, String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, student.getStudentId());
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getDate(1) : null;
            }
        }
    }

    private String formatCompactMoney(BigDecimal amount) {
        return "₹ " + new DecimalFormat("#,##0.##").format(amount);
    }

    private String formatMoney(BigDecimal amount) {
        return "₹ " + new DecimalFormat("#,##0.00").format(amount);
    }

    private void showLoadError(Throwable error) {
        JOptionPane.showMessageDialog(this,
                "Could not load dashboard data. Check that the database table columns match the dashboard queries.\n\n"
                        + error.getMessage(), "Dashboard Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class GradientPanel extends JPanel {
        private GradientPanel() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setPaint(new GradientPaint(0, 0, new Color(5, 22, 48),
                    getWidth(), getHeight(), new Color(3, 11, 27)));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.dispose();
        }
    }

    private static class DashboardSummary {
        private final BigDecimal fees, scholarship, paid, expenses;
        private final Date nextDueDate;
        private DashboardSummary(BigDecimal fees, BigDecimal scholarship, BigDecimal paid,
                                 BigDecimal expenses, Date nextDueDate) {
            this.fees = fees;
            this.scholarship = scholarship;
            this.paid = paid;
            this.expenses = expenses;
            this.nextDueDate = nextDueDate;
        }
    }
}







