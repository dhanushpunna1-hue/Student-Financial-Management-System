package com.sfms.ui;

import com.sfms.dao.StudentDAO;
import com.sfms.model.Expense;
import com.sfms.model.FeeStructure;
import com.sfms.model.Payment;
import com.sfms.model.PendingFee;
import com.sfms.model.Scholarship;
import com.sfms.model.Student;
import com.sfms.service.ExpenseService;
import com.sfms.service.FeeStructureService;
import com.sfms.service.FinancialReportService;
import com.sfms.service.PaymentHistoryService;
import com.sfms.service.PaymentService;
import com.sfms.service.PendingFeeService;
import com.sfms.service.ScholarshipService;
import com.sfms.service.StudentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/** Dark, searchable sub-dashboard shared by all system modules. */
public class ManagementModulesFrame extends JFrame {
    private static final Color BG = new Color(20, 20, 20);
    private static final Color CARD = new Color(27, 27, 27);
    private static final Color TEXT = new Color(235, 235, 235);
    private static final Color MUTED = new Color(190, 190, 190);
    private static final Color LINE = new Color(55, 55, 55);
    private static final Color BLUE = new Color(33, 115, 210);

    private final String module;
    private final JPanel actions = new JPanel(new GridLayout(0, 2, 12, 12));
    private final JTextField searchField = new JTextField();
    private final JTable recordsTable = new JTable();
    private final JLabel[] metricValues = new JLabel[3];
    private final JLabel[] metricTitles = new JLabel[3];
    private DefaultTableModel currentModel;

    public ManagementModulesFrame(String module) {
        this.module = module;
        setTitle(module);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildActions();
        buildDashboard();
        loadDashboardData();
        setMinimumSize(new Dimension(820, 620));
        setSize(1020, 760);
        setLocationRelativeTo(null);
    }

    private void buildDashboard() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(20, 28, 18, 28));

        JPanel top = new JPanel();
        top.setBackground(BG);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        JPanel appBar = new JPanel(new BorderLayout());
        appBar.setBackground(BG);
        JLabel appName = new JLabel("▣  " + module);
        appName.setForeground(MUTED);
        appName.setFont(new Font("Arial", Font.BOLD, 15));
        appBar.add(appName, BorderLayout.WEST);
        JLabel moreDots = new JLabel("•••");
        moreDots.setForeground(MUTED);
        appBar.add(moreDots, BorderLayout.EAST);
        appBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, LINE));
        top.add(appBar);
        top.add(Box.createVerticalStrut(16));
        JLabel breadcrumb = new JLabel("System Modules / " + module);
        breadcrumb.setForeground(MUTED);
        breadcrumb.setFont(new Font("Arial", Font.BOLD, 14));
        top.add(breadcrumb);
        JLabel heading = new JLabel(module);
        heading.setForeground(TEXT);
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        top.add(Box.createVerticalStrut(4));
        top.add(heading);

        JPanel metricStrip = new JPanel(new GridLayout(1, 3, 12, 0));
        metricStrip.setBackground(BG);
        for (int i = 0; i < 3; i++) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(CARD);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(LINE), new EmptyBorder(10, 14, 10, 14)));
            metricTitles[i] = new JLabel();
            metricTitles[i].setForeground(MUTED);
            metricTitles[i].setFont(new Font("Arial", Font.BOLD, 14));
            metricValues[i] = new JLabel("—");
            metricValues[i].setForeground(TEXT);
            metricValues[i].setFont(new Font("Arial", Font.BOLD, 20));
            card.add(metricTitles[i]);
            card.add(Box.createVerticalStrut(4));
            card.add(metricValues[i]);
            metricStrip.add(card);
        }
        top.add(Box.createVerticalStrut(16));
        top.add(metricStrip);
        root.add(top, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setBackground(BG);
        searchField.setFont(new Font("Arial", Font.PLAIN, 17));
        searchField.setForeground(TEXT);
        searchField.setBackground(CARD);
        searchField.setCaretColor(TEXT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE), new EmptyBorder(9, 12, 9, 12)));
        searchField.setToolTipText("Search records");
        toolbar.add(searchField, BorderLayout.CENTER);

        JPanel toolbarButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        toolbarButtons.setBackground(BG);
        JButton primary = findPrimaryAction();
        toolbarButtons.add(primary);
        JButton export = actionButton("⇩  Export");
        export.addActionListener(event -> exportTable());
        toolbarButtons.add(export);
        JButton more = actionButton("•••");
        more.addActionListener(event -> showActionsMenu(more));
        toolbarButtons.add(more);
        toolbar.add(toolbarButtons, BorderLayout.EAST);

        configureTable();
        JScrollPane tableScroll = new JScrollPane(recordsTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(LINE));
        tableScroll.getViewport().setBackground(BG);
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(BG);
        center.add(toolbar, BorderLayout.NORTH);
        center.add(tableScroll, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        JButton back = actionButton("← Back to System Modules");
        back.addActionListener(event -> dispose());
        JLabel count = new JLabel("Showing 0 records");
        count.setForeground(MUTED);
        count.setFont(new Font("Arial", Font.BOLD, 13));
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG);
        footer.add(back, BorderLayout.WEST);
        footer.add(count, BorderLayout.EAST);
        root.add(footer, BorderLayout.SOUTH);
        recordsTable.putClientProperty("countLabel", count);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent event) { filterRows(); }
            public void removeUpdate(DocumentEvent event) { filterRows(); }
            public void changedUpdate(DocumentEvent event) { filterRows(); }
        });
        setContentPane(root);
    }

    private void configureTable() {
        recordsTable.setBackground(BG);
        recordsTable.setForeground(TEXT);
        recordsTable.setGridColor(LINE);
        recordsTable.setSelectionBackground(new Color(25, 56, 88));
        recordsTable.setSelectionForeground(TEXT);
        recordsTable.setRowHeight(42);
        recordsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        recordsTable.setFillsViewportHeight(true);
        recordsTable.setAutoCreateRowSorter(true);
        recordsTable.getTableHeader().setBackground(CARD);
        recordsTable.getTableHeader().setForeground(MUTED);
        recordsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        recordsTable.getTableHeader().setPreferredSize(new Dimension(10, 40));
        recordsTable.setShowGrid(true);
        recordsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, selected, focused, row, column);
                if (value instanceof BigDecimal amount) {
                    setText(money(amount));
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                setBackground(selected ? table.getSelectionBackground() : BG);
                setForeground(selected ? table.getSelectionForeground() : TEXT);
                return cell;
            }
        });
    }

    private JButton actionButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(TEXT);
        button.setBackground(CARD);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE), new EmptyBorder(9, 14, 9, 14)));
        return button;
    }

    private JButton findPrimaryAction() {
        for (Component component : actions.getComponents()) {
            if (component instanceof JButton button && button.getText().startsWith("Add ")) {
                JButton primary = actionButton("＋  " + button.getText().replaceFirst("^Add ", "Add "));
                primary.setForeground(new Color(170, 205, 245));
                primary.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BLUE), new EmptyBorder(9, 14, 9, 14)));
                primary.addActionListener(event -> button.doClick());
                return primary;
            }
        }
        JButton refresh = actionButton("↻  Refresh");
        refresh.addActionListener(event -> loadDashboardData());
        return refresh;
    }

    private void showActionsMenu(Component anchor) {
        JPopupMenu menu = new JPopupMenu();
        for (Component component : actions.getComponents()) {
            if (component instanceof JButton button) {
                JMenuItem item = new JMenuItem(button.getText());
                item.addActionListener(event -> button.doClick());
                menu.add(item);
            }
        }
        menu.show(anchor, 0, anchor.getHeight());
    }

    private void loadDashboardData() {
        try {
            currentModel = createModuleTable();
            recordsTable.setModel(currentModel);
            recordsTable.setAutoCreateRowSorter(true);
            updateMetrics();
            updateRecordCount();
            filterRows();
        } catch (RuntimeException error) {
            currentModel = new DefaultTableModel(new Object[] { "Information" }, 0);
            currentModel.addRow(new Object[] { "Could not load records: " + error.getMessage() });
            recordsTable.setModel(currentModel);
            updateMetrics();
            updateRecordCount();
        }
    }

    private DefaultTableModel createModuleTable() {
        DefaultTableModel model;
        switch (module) {
            case "Student Management" -> {
                model = tableModel("Student ID", "Student", "Email", "Course", "Study Year", "Phone");
                for (Student s : new StudentDAO().getAllStudents()) model.addRow(new Object[] {
                        s.getStudentId(), s.getFullName(), display(s.getEmail()), display(s.getCourse()),
                        display(s.getStudyYear()), display(s.getPhone()) });
            }
            case "Fee Structure Management" -> {
                model = tableModel("Fee ID", "Student ID", "Fee Name", "Amount", "Due Date");
                for (FeeStructure f : new FeeStructureService().getAllFees()) model.addRow(new Object[] {
                        f.getFeeId(), f.getStudentId(), f.getFeeName(), BigDecimal.valueOf(f.getAmount()),
                        f.getDueDate() == null ? "—" : f.getDueDate() });
            }
            case "Fee Payment", "Payment History" -> {
                model = tableModel("Receipt", "Student", "Date", "Amount", "Status");
                Map<Integer, String> namesByStudent = new HashMap<>();
                for (Student student : new StudentDAO().getAllStudents()) namesByStudent.put(student.getStudentId(), student.getFullName());
                Map<Integer, String> namesByFee = new HashMap<>();
                for (FeeStructure fee : new FeeStructureService().getAllFees()) namesByFee.put(fee.getFeeId(), namesByStudent.getOrDefault(fee.getStudentId(), "Student " + fee.getStudentId()));
                for (Payment p : new PaymentService().getAllPayments()) model.addRow(new Object[] {
                        String.format("R-%04d", p.getPaymentId()), namesByFee.getOrDefault(p.getFeeStructureId(), "Fee " + p.getFeeStructureId()), p.getPaymentDate(),
                        BigDecimal.valueOf(p.getAmount()), "Paid" });
            }
            case "Pending Fees Management" -> {
                model = tableModel("Fee ID", "Student ID", "Student", "Fee Name", "Total", "Paid", "Remaining", "Due Date", "Status");
                for (PendingFee f : getPendingFeesSafely()) model.addRow(new Object[] {
                        f.getFeeStructureId(), f.getStudentId(), f.getStudentName(), f.getFeeName(),
                        f.getTotalAmount(), f.getPaidAmount(),
                        f.getPendingAmount(), f.getDueDate() == null ? "—" : f.getDueDate(), f.getStatus() });
            }
            case "Scholarship Management" -> {
                model = tableModel("Scholarship ID", "Student ID", "Name", "Amount", "Status", "Academic Year", "Awarded Date");
                for (Scholarship s : new ScholarshipService().getAllScholarships()) model.addRow(new Object[] {
                        s.getScholarshipId(), s.getStudentId(), s.getScholarshipName(), BigDecimal.valueOf(s.getAmount()),
                        s.getStatus(), s.getAcademicYear(), s.getAwardedDate() == null ? "—" : s.getAwardedDate() });
            }
            case "Expenses Management" -> {
                model = tableModel("Expense ID", "Student ID", "Expense", "Amount", "Date", "Notes");
                for (Expense e : new ExpenseService().getAllExpenses()) model.addRow(new Object[] {
                        e.getExpenseId(), e.getStudentId(), e.getExpenseName(), BigDecimal.valueOf(e.getAmount()),
                        e.getExpenseDate(), display(e.getNotes()) });
            }
            case "Financial Reports" -> {
                model = tableModel("Report", "Description");
                model.addRow(new Object[] { "Current-year report", "Use More actions to generate" });
                model.addRow(new Object[] { "Date-range report", "Use More actions to generate" });
            }
            default -> model = tableModel("Information");
        }
        return model;
    }

    private List<PendingFee> getPendingFeesSafely() {
        try {
            return new PendingFeeService().getAllPendingFees();
        } catch (SQLException error) {
            throw new IllegalStateException("Could not load pending fee records.", error);
        }
    }
    private DefaultTableModel tableModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private void updateMetrics() {
        int rowCount = currentModel == null ? 0 : currentModel.getRowCount();
        BigDecimal total = BigDecimal.ZERO;
        if (currentModel != null) {
            int amountColumn = -1;
            String preferred = "Pending Fees Management".equals(module) ? "Remaining" : "Amount";
            for (int col = 0; col < currentModel.getColumnCount(); col++) {
                if (preferred.equalsIgnoreCase(currentModel.getColumnName(col))) { amountColumn = col; break; }
            }
            if (amountColumn >= 0) {
                for (int row = 0; row < currentModel.getRowCount(); row++) {
                    Object value = currentModel.getValueAt(row, amountColumn);
                    if (value instanceof BigDecimal amount) total = total.add(amount);
                }
            }
        }
        if ("Fee Payment".equals(module) || "Payment History".equals(module)) {
            metricTitles[0].setText("Total collected");
            metricValues[0].setText(money(total));
            BigDecimal pending = BigDecimal.ZERO;
            if ("Fee Payment".equals(module)) {
                try { for (PendingFee fee : getPendingFeesSafely()) pending = pending.add(fee.getPendingAmount()); }
                catch (RuntimeException ignored) { }
            }
            metricTitles[1].setText("Pending");
            metricValues[1].setText(money(pending));
            metricTitles[2].setText("Payments");
            metricValues[2].setText(String.valueOf(rowCount));
        } else {
            metricTitles[0].setText("Records");
            metricValues[0].setText(String.valueOf(rowCount));
            metricTitles[1].setText("Total amount");
            metricValues[1].setText(money(total));
            metricTitles[2].setText("Module");
            metricValues[2].setText("Ready");
        }
    }

    private String money(BigDecimal amount) { return "₹ " + String.format("%,.2f", amount); }

@SuppressWarnings({"rawtypes", "unchecked"})
    private void filterRows() {
        if (!(recordsTable.getRowSorter() instanceof TableRowSorter sorter)) return;
        String query = searchField.getText().trim();
        sorter.setRowFilter(query.isEmpty() ? null : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(query)));
        updateRecordCount();
    }

    private void updateRecordCount() {
        Object value = recordsTable.getClientProperty("countLabel");
        if (value instanceof JLabel label) {
            int shown = recordsTable.getRowCount();
            int total = currentModel == null ? 0 : currentModel.getRowCount();
            label.setText("Showing " + shown + " of " + total);
        }
    }

    private void exportTable() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export " + module + " records");
        chooser.setSelectedFile(new java.io.File(module.toLowerCase().replace(' ', '-') + ".csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (var writer = Files.newBufferedWriter(chooser.getSelectedFile().toPath(), StandardCharsets.UTF_8)) {
            for (int col = 0; col < currentModel.getColumnCount(); col++) {
                if (col > 0) writer.write(',');
                writer.write(csv(currentModel.getColumnName(col)));
            }
            writer.newLine();
            for (int row = 0; row < currentModel.getRowCount(); row++) {
                for (int col = 0; col < currentModel.getColumnCount(); col++) {
                    if (col > 0) writer.write(',');
                    writer.write(csv(String.valueOf(currentModel.getValueAt(row, col))));
                }
                writer.newLine();
            }
            showText("Export", "Records exported successfully.");
        } catch (IOException error) { showError(error); }
    }

    private String csv(String value) { return "\"" + value.replace("\"", "\"\"") + "\""; }
    private void buildActions() {
        switch (module) {
            case "Student Management" -> {
                button("Add Student", this::addStudent);
                button("View All Students", this::viewStudents);
                button("Search Student by ID", this::searchStudent);
                button("Update Student", this::updateStudent);
                button("Delete Student", this::deleteStudent);
            }
            case "Fee Structure Management" -> {
                button("Add Fee Structure", this::addFee);
                button("View Fee Structures", this::viewFees);
                button("Update Fee Structure", this::updateFee);
                button("Delete Fee Structure", this::deleteFee);
            }
            case "Fee Payment" -> {
                button("Add Payment", this::addPayment);
                button("View Payments", this::viewPayments);
                button("Delete Payment", this::deletePayment);
            }
            case "Scholarship Management" -> {
                button("Add Scholarship", this::addScholarship);
                button("View Scholarships", this::viewScholarships);
                button("Update Scholarship", this::updateScholarship);
                button("Delete Scholarship", this::deleteScholarship);
            }
            case "Expenses Management" -> {
                button("Add Expense", this::addExpense);
                button("View Expenses", this::viewExpenses);
                button("Update Expense", this::updateExpense);
                button("Delete Expense", this::deleteExpense);
                button("View Total Expenses", () -> capture("Total Expenses",
                        () -> new ExpenseService().viewTotalExpenses()));
            }
            case "Payment History" -> {
                button("View All Payments", this::viewPayments);
                button("View Total Payments", this::showTotalPayments);
            }
            case "Financial Reports" -> {
                button("Current-Year Report", this::currentYearReport);
                button("Date-Range Report", this::dateRangeReport);
            }
            case "Pending Fees Management" -> {
                button("View All Pending Fees", this::viewPendingFees);
                button("Search by Student ID", this::pendingFeesByStudent);
            }
            default -> button("Close", this::dispose);
        }
    }

    private void button(String label, Runnable action) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(205, 42));
        button.addActionListener(event -> { action.run(); loadDashboardData(); });
        actions.add(button);
    }

    private void addStudent() {
        JTextField studentIdField = new JTextField(18);
        JTextField nameField = new JTextField(18);
        JTextField emailField = new JTextField(18);
        JTextField courseField = new JTextField(18);
        JTextField yearField = new JTextField(18);
        JTextField phoneField = new JTextField(18);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Student ID (number):")); form.add(studentIdField);
        form.add(new JLabel("Full name:")); form.add(nameField);
        form.add(new JLabel("Email (optional):")); form.add(emailField);
        form.add(new JLabel("Course:")); form.add(courseField);
        form.add(new JLabel("Study year (number):")); form.add(yearField);
        form.add(new JLabel("Phone (optional):")); form.add(phoneField);

        int choice = JOptionPane.showConfirmDialog(this, form, "Add Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) return;

        String studentId;
        String year;
        try {
            studentId = String.valueOf(Integer.parseInt(studentIdField.getText().trim()));
            year = String.valueOf(Integer.parseInt(yearField.getText().trim()));
        } catch (NumberFormatException e) {
            showText("Invalid value", "Student ID and study year must be whole numbers.");
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showText("Missing name", "Enter the student's full name.");
            return;
        }

        Student student = new Student(studentId, name,
                emptyToNull(emailField.getText()), courseField.getText().trim(),
                year, emptyToNull(phoneField.getText()));
        capture("Add Student", () -> System.out.println(
                new StudentService().addStudent(student)
                        ? "Student added successfully."
                        : "Could not add the student. Check the fields and database."));
    }

    private void viewStudents() {
        try {
            List<Student> students = new StudentDAO().getAllStudents();
            if (students.isEmpty()) {
                showText("Students", "No students found.");
                return;
            }

            DefaultTableModel model = new DefaultTableModel(
                    new Object[] { "Student ID", "Full Name", "Email", "Course", "Study Year", "Phone" }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (Student s : students) {
                model.addRow(new Object[] {
                        s.getStudentId(), s.getFullName(), display(s.getEmail()),
                        display(s.getCourse()), display(s.getStudyYear()), display(s.getPhone())
                });
            }

            JTable table = new JTable(model);
            table.setAutoCreateRowSorter(true);
            table.setRowHeight(24);
            table.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(820, 320));
            JOptionPane.showMessageDialog(this, scrollPane, "Students",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) { showError(e); }
    }

    private void searchStudent() {
        Integer id = promptInt("Student ID:"); if (id == null) return;
        Student s = new StudentService().getStudentById(id);
        if (s == null) {
            showText("Student", "Student not found.");
            return;
        }

        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Field", "Student Details" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addRow(new Object[] { "Student ID", s.getStudentId() });
        model.addRow(new Object[] { "Full Name", s.getFullName() });
        model.addRow(new Object[] { "Email", display(s.getEmail()) });
        model.addRow(new Object[] { "Course", display(s.getCourse()) });
        model.addRow(new Object[] { "Study Year", display(s.getStudyYear()) });
        model.addRow(new Object[] { "Phone", display(s.getPhone()) });

        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(140);
        table.getColumnModel().getColumn(1).setPreferredWidth(360);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(540, 220));
        JOptionPane.showMessageDialog(this, scrollPane, "Student",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateStudent() {
        Integer id = promptInt("Student ID to update:"); if (id == null) return;
        Student student = new StudentService().getStudentById(id);
        if (student == null) { showText("Student", "Student not found."); return; }

        JTextField nameField = new JTextField(student.getFullName(), 18);
        JTextField emailField = new JTextField(student.getEmail() == null ? "" : student.getEmail(), 18);
        JTextField courseField = new JTextField(student.getCourse() == null ? "" : student.getCourse(), 18);
        JTextField yearField = new JTextField(student.getStudyYear() == null ? "" : student.getStudyYear(), 18);
        JTextField phoneField = new JTextField(student.getPhone() == null ? "" : student.getPhone(), 18);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Full name:")); form.add(nameField);
        form.add(new JLabel("Email (optional):")); form.add(emailField);
        form.add(new JLabel("Course:")); form.add(courseField);
        form.add(new JLabel("Study year (number):")); form.add(yearField);
        form.add(new JLabel("Phone (optional):")); form.add(phoneField);

        int choice = JOptionPane.showConfirmDialog(this, form, "Update Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) return;

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showText("Missing name", "Enter the student's full name.");
            return;
        }
        try {
            student.setFullName(name);
            student.setEmail(emptyToNull(emailField.getText()));
            student.setDepartment(courseField.getText().trim());
            student.setAcademicYear(String.valueOf(Integer.parseInt(yearField.getText().trim())));
            student.setPhoneNumber(emptyToNull(phoneField.getText()));
        } catch (NumberFormatException e) {
            showText("Invalid value", "Study year must be a whole number.");
            return;
        }
        capture("Update Student", () -> System.out.println(
                new StudentService().updateStudent(student)
                        ? "Student updated successfully."
                        : "Could not update the student."));
    }

    private void deleteStudent() {
        Integer id = promptInt("Student ID to delete:"); if (id == null) return;
        if (confirm("Delete student " + id + "?"))
            capture("Delete Student", () -> {
                if (new StudentService().deleteStudent(id)) {
                    System.out.println("Student deleted successfully.");
                }
            });
    }

    private void addFee() {
        JTextField studentIdField = new JTextField(16);
        JTextField nameField = new JTextField(16);
        JTextField amountField = new JTextField(16);
        JTextField dueDateField = new JTextField(16);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Student ID:")); form.add(studentIdField);
        form.add(new JLabel("Fee name:")); form.add(nameField);
        form.add(new JLabel("Amount:")); form.add(amountField);
        form.add(new JLabel("Due date (YYYY-MM-DD, optional):")); form.add(dueDateField);
        if (JOptionPane.showConfirmDialog(this, form, "Add Fee",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            double amount = Double.parseDouble(amountField.getText().trim());
            Date dueDate = parseOptionalDate(dueDateField.getText());
            capture("Add Fee", () -> new FeeStructureService().addFee(
                    studentId, nameField.getText().trim(), amount, dueDate));
        } catch (RuntimeException e) { showError(e); }
    }

    private void updateFee() {
        JTextField feeIdField = new JTextField(16);
        JTextField studentIdField = new JTextField(16);
        JTextField nameField = new JTextField(16);
        JTextField amountField = new JTextField(16);
        JTextField dueDateField = new JTextField(16);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Fee ID:")); form.add(feeIdField);
        form.add(new JLabel("Student ID:")); form.add(studentIdField);
        form.add(new JLabel("Fee name:")); form.add(nameField);
        form.add(new JLabel("Amount:")); form.add(amountField);
        form.add(new JLabel("Due date (YYYY-MM-DD, optional):")); form.add(dueDateField);
        if (JOptionPane.showConfirmDialog(this, form, "Update Fee",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            int feeId = Integer.parseInt(feeIdField.getText().trim());
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            double amount = Double.parseDouble(amountField.getText().trim());
            Date dueDate = parseOptionalDate(dueDateField.getText());
            capture("Update Fee", () -> new FeeStructureService().updateFee(
                    feeId, studentId, nameField.getText().trim(), amount, dueDate));
        } catch (RuntimeException e) { showError(e); }
    }

    private void deleteFee() {
        Integer id = promptInt("Fee ID:"); if (id == null) return;
        capture("Delete Fee", () -> new FeeStructureService().deleteFee(id));
    }

    private void viewFees() {
        List<FeeStructure> fees = new FeeStructureService().getAllFees();
        if (fees.isEmpty()) { showText("Fees", "No fee records found."); return; }
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Fee ID", "Student ID", "Fee Name", "Amount", "Due Date" }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        for (FeeStructure fee : fees) {
            model.addRow(new Object[] { fee.getFeeId(), fee.getStudentId(), fee.getFeeName(),
                    String.format("₹%.2f", fee.getAmount()),
                    fee.getDueDate() == null ? "-" : fee.getDueDate() });
        }
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(720, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Fee Records", JOptionPane.INFORMATION_MESSAGE);
    }

    private Date parseOptionalDate(String value) {
        String trimmed = value == null ? "" : value.trim();
        return trimmed.isEmpty() ? null : Date.valueOf(trimmed);
    }

    private void addPayment() {
        List<FeeStructure> fees = new FeeStructureService().getAllFees();
        if (fees.isEmpty()) {
            showText("Add Payment", "There are no fee records. Add a fee first.");
            return;
        }

        JComboBox<FeeStructure> feeChoice = new JComboBox<>(fees.toArray(new FeeStructure[0]));
        feeChoice.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean selected, boolean focused) {
                super.getListCellRendererComponent(list, value, index, selected, focused);
                if (value instanceof FeeStructure fee) {
                    setText("Fee #" + fee.getFeeId() + " — " + fee.getFeeName()
                            + " (Student " + fee.getStudentId() + ", "
                            + String.format("₹%.2f", fee.getAmount()) + ")");
                }
                return this;
            }
        });
        JTextField amountField = new JTextField(14);
        JTextField dateField = new JTextField(LocalDate.now().toString(), 14);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Select fee:")); form.add(feeChoice);
        form.add(new JLabel("Payment amount:")); form.add(amountField);
        form.add(new JLabel("Payment date (YYYY-MM-DD):")); form.add(dateField);
        if (JOptionPane.showConfirmDialog(this, form, "Add Payment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        try {
            FeeStructure selectedFee = (FeeStructure) feeChoice.getSelectedItem();
            if (selectedFee == null) {
                showText("Add Payment", "Select a fee record first.");
                return;
            }
            double amount = Double.parseDouble(amountField.getText().trim());
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            capture("Add Payment", () -> new PaymentService().addPayment(
                    selectedFee.getFeeId(), amount, date));
        } catch (RuntimeException e) { showError(e); }
    }

    private void deletePayment() {
        Integer id = promptInt("Payment ID:"); if (id == null) return;
        capture("Delete Payment", () -> new PaymentService().deletePayment(id));
    }

    private void viewPayments() {
        List<Payment> payments = new PaymentService().getAllPayments();
        if (payments.isEmpty()) { showText("Payments", "No payment records found."); return; }
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Payment ID", "Fee ID", "Amount Paid", "Payment Date" }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        for (Payment payment : payments) {
            model.addRow(new Object[] { payment.getPaymentId(), payment.getFeeStructureId(),
                    String.format("₹%.2f", payment.getAmount()), payment.getPaymentDate() });
        }
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(620, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Payment Records", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showTotalPayments() {
        try {
            BigDecimal total = new PaymentHistoryService().getTotalPayments();
            JLabel amount = new JLabel(String.format(
                    "<html><div style='text-align:center;padding:14px;'>"
                            + "<span style='font-size:14px;color:#555;'>Total payments received</span><br>"
                            + "<span style='font-size:28px;font-weight:bold;'>₹%,.2f</span></div></html>",
                    total));
            amount.setHorizontalAlignment(SwingConstants.CENTER);
            amount.setPreferredSize(new Dimension(340, 100));
            JOptionPane.showMessageDialog(this, amount, "Payment Summary", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) {
            showError(e);
        }
    }

    private void addScholarship() {
        JTextField studentIdField = new JTextField(16);
        JTextField nameField = new JTextField(16);
        JTextField amountField = new JTextField(16);
        JTextField yearField = new JTextField(16);
        JComboBox<String> statusField = new JComboBox<>(new String[] { "Pending", "Approved", "Rejected" });
        JTextField awardedDateField = new JTextField(16);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Student ID:")); form.add(studentIdField);
        form.add(new JLabel("Scholarship name:")); form.add(nameField);
        form.add(new JLabel("Amount:")); form.add(amountField);
        form.add(new JLabel("Academic year:")); form.add(yearField);
        form.add(new JLabel("Status:")); form.add(statusField);
        form.add(new JLabel("Awarded date (YYYY-MM-DD, optional):")); form.add(awardedDateField);
        if (JOptionPane.showConfirmDialog(this, form, "Add Scholarship",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            double amount = Double.parseDouble(amountField.getText().trim());
            Date awardedDate = parseOptionalDate(awardedDateField.getText());
            capture("Add Scholarship", () -> new ScholarshipService().addScholarship(
                    studentId, nameField.getText().trim(), amount, yearField.getText().trim(),
                    (String) statusField.getSelectedItem(), awardedDate));
        } catch (RuntimeException e) { showError(e); }
    }

    private void updateScholarship() {
        JTextField idField = new JTextField(16);
        JTextField studentIdField = new JTextField(16);
        JTextField nameField = new JTextField(16);
        JTextField amountField = new JTextField(16);
        JTextField yearField = new JTextField(16);
        JComboBox<String> statusField = new JComboBox<>(new String[] { "Pending", "Approved", "Rejected" });
        JTextField awardedDateField = new JTextField(16);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Scholarship ID:")); form.add(idField);
        form.add(new JLabel("Student ID:")); form.add(studentIdField);
        form.add(new JLabel("Scholarship name:")); form.add(nameField);
        form.add(new JLabel("Amount:")); form.add(amountField);
        form.add(new JLabel("Academic year:")); form.add(yearField);
        form.add(new JLabel("Status:")); form.add(statusField);
        form.add(new JLabel("Awarded date (YYYY-MM-DD, optional):")); form.add(awardedDateField);
        if (JOptionPane.showConfirmDialog(this, form, "Update Scholarship",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            double amount = Double.parseDouble(amountField.getText().trim());
            Date awardedDate = parseOptionalDate(awardedDateField.getText());
            capture("Update Scholarship", () -> new ScholarshipService().updateScholarship(
                    id, studentId, nameField.getText().trim(), amount, yearField.getText().trim(),
                    (String) statusField.getSelectedItem(), awardedDate));
        } catch (RuntimeException e) { showError(e); }
    }

    private void viewScholarships() {
        List<Scholarship> scholarships = new ScholarshipService().getAllScholarships();
        if (scholarships.isEmpty()) { showText("Scholarships", "No scholarship records found."); return; }
        DefaultTableModel model = new DefaultTableModel(new Object[] {
                "Scholarship ID", "Student ID", "Name", "Amount", "Status", "Academic Year", "Awarded Date"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        for (Scholarship scholarship : scholarships) {
            model.addRow(new Object[] { scholarship.getScholarshipId(), scholarship.getStudentId(),
                    scholarship.getScholarshipName(), String.format("₹%.2f", scholarship.getAmount()),
                    scholarship.getStatus(), scholarship.getAcademicYear(),
                    scholarship.getAwardedDate() == null ? "-" : scholarship.getAwardedDate() });
        }
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(820, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Scholarship Records", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteScholarship() {
        Integer id = promptInt("Scholarship ID:"); if (id == null) return;
        capture("Delete Scholarship", () -> new ScholarshipService().deleteScholarship(id));
    }

    private void addExpense() {
        Integer studentId = promptInt("Student ID:"); if (studentId == null) return;
        String name = prompt("Expense name:"); if (name == null) return;
        Double amount = promptDouble("Amount:"); if (amount == null) return;
        String date = prompt("Date (YYYY-MM-DD):"); if (date == null) return;
        String notes = prompt("Notes:"); if (notes == null) return;
        try {
            capture("Add Expense", () -> new ExpenseService().addExpense(
                    studentId, name, amount, Date.valueOf(date), notes));
        } catch (RuntimeException e) { showError(e); }
    }

    private void updateExpense() {
        Integer id = promptInt("Expense ID:"); if (id == null) return;
        Integer studentId = promptInt("Student ID:"); if (studentId == null) return;
        String name = prompt("Expense name:"); if (name == null) return;
        Double amount = promptDouble("Amount:"); if (amount == null) return;
        String date = prompt("Date (YYYY-MM-DD):"); if (date == null) return;
        String notes = prompt("Notes:"); if (notes == null) return;
        try {
            capture("Update Expense", () -> new ExpenseService().updateExpense(
                    id, studentId, name, amount, Date.valueOf(date), notes));
        } catch (RuntimeException e) { showError(e); }
    }

    private void deleteExpense() {
        Integer id = promptInt("Expense ID:"); if (id == null) return;
        capture("Delete Expense", () -> new ExpenseService().deleteExpense(id));
    }

    private void viewExpenses() {
        List<Expense> expenses = new ExpenseService().getAllExpenses();
        if (expenses.isEmpty()) { showText("Expenses", "No expense records found."); return; }
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Expense ID", "Student ID", "Expense Name", "Amount", "Date", "Notes" }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        for (Expense expense : expenses) {
            model.addRow(new Object[] { expense.getExpenseId(), expense.getStudentId(),
                    expense.getExpenseName(), String.format("₹%.2f", expense.getAmount()),
                    expense.getExpenseDate(), display(expense.getNotes()) });
        }
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(760, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Expense Records", JOptionPane.INFORMATION_MESSAGE);
    }

    private void dateRangeReport() {
        String start = prompt("Start date (YYYY-MM-DD):"); if (start == null) return;
        String end = prompt("End date (YYYY-MM-DD):"); if (end == null) return;
        try {
            FinancialReportService.ReportSummary report = new FinancialReportService()
                    .getReportSummary(Date.valueOf(start), Date.valueOf(end));
            showFinancialReport(report);
        } catch (RuntimeException e) { showError(e); }
    }

    private void currentYearReport() {
        try {
            showFinancialReport(new FinancialReportService().getCurrentYearReportSummary());
        } catch (RuntimeException e) { showError(e); }
    }

    private void showFinancialReport(FinancialReportService.ReportSummary report) {
        DefaultTableModel model = new DefaultTableModel(new Object[] { "Report Item", "Amount" }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        model.addRow(new Object[] { "Total Payments", String.format("₹%,.2f", report.getTotalPayments()) });
        model.addRow(new Object[] { "Total Expenses", String.format("₹%,.2f", report.getTotalExpenses()) });
        model.addRow(new Object[] { "Net Difference", String.format("₹%,.2f", report.getNetDifference()) });

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(190);
        table.getColumnModel().getColumn(1).setPreferredWidth(170);

        JLabel period = new JLabel("Report period: " + report.getStartDate() + " to " + report.getEndDate());
        period.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        JLabel summary = new JLabel(report.getSummaryMessage(), SwingConstants.CENTER);
        summary.setFont(summary.getFont().deriveFont(Font.BOLD, 14f));

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.add(period, BorderLayout.NORTH);
        content.add(new JScrollPane(table), BorderLayout.CENTER);
        content.add(summary, BorderLayout.SOUTH);
        content.setPreferredSize(new Dimension(500, 260));
        JOptionPane.showMessageDialog(this, content, "Financial Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewPendingFees() {
        try { showPendingFees(getPendingFeesSafely()); }
        catch (Exception e) { showError(e); }
    }

    private void pendingFeesByStudent() {
        Integer id = promptInt("Student ID:"); if (id == null) return;
        try { showPendingFees(new PendingFeeService().getPendingFeesByStudentId(id)); }
        catch (Exception e) { showError(e); }
    }

    private void showPendingFees(List<PendingFee> fees) {
        if (fees.isEmpty()) { showText("Pending Fees", "No fee records found."); return; }
        DefaultTableModel model = new DefaultTableModel(new Object[] {
                "Fee ID", "Student ID", "Student", "Fee Name", "Total", "Paid", "Remaining", "Due Date", "Status"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        for (PendingFee fee : fees) {
            model.addRow(new Object[] {
                    fee.getFeeStructureId(), fee.getStudentId(), fee.getStudentName(), fee.getFeeName(),
                    String.format("₹%,.2f", fee.getTotalAmount()),
                    String.format("₹%,.2f", fee.getPaidAmount()),
                    String.format("₹%,.2f", fee.getPendingAmount()),
                    fee.getDueDate() == null ? "-" : fee.getDueDate(), fee.getStatus()
            });
        }
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 320));
        JOptionPane.showMessageDialog(this, scrollPane, "Pending Fee Records", JOptionPane.INFORMATION_MESSAGE);
    }

    private String prompt(String label) {
        JTextField input = new JTextField(22);
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(input, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, panel, module,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.OK_OPTION ? input.getText() : null;
    }

    private Integer promptInt(String label) {
        String value = prompt(label);
        if (value == null) return null;
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { showText("Invalid value", "Enter a whole number."); return null; }
    }

    private Double promptDouble(String label) {
        String value = prompt(label);
        if (value == null) return null;
        try { return Double.parseDouble(value.trim()); }
        catch (NumberFormatException e) { showText("Invalid value", "Enter a valid amount."); return null; }
    }

    private boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void capture(String title, Runnable action) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream previousOut = System.out;
        PrintStream previousErr = System.err;
        try (PrintStream captured = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(captured);
            System.setErr(captured);
            action.run();
        } catch (RuntimeException e) {
            bytes.writeBytes((e.getMessage() == null ? e.toString() : e.getMessage())
                    .getBytes(StandardCharsets.UTF_8));
        } finally {
            System.setOut(previousOut);
            System.setErr(previousErr);
        }
        String output = bytes.toString(StandardCharsets.UTF_8).trim();
        showText(title, output.isEmpty() ? "Action completed." : output);
    }

    private void showText(String title, String text) {
        JTextArea area = new JTextArea(text, 6, 48);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JOptionPane.showMessageDialog(this, new JScrollPane(area), title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(Throwable error) {
        JOptionPane.showMessageDialog(this,
                error.getMessage() == null ? error.toString() : error.getMessage(),
                "Could not complete action", JOptionPane.ERROR_MESSAGE);
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String display(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}







