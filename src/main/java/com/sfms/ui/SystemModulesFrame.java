package com.sfms.ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Separate, categorized screen containing the system module shortcuts. */
public class SystemModulesFrame extends JFrame {
    private static final Color BG = new Color(18, 18, 18);
    private static final Color CARD = new Color(27, 27, 27);
    private static final Color TEXT = new Color(238, 238, 238);
    private static final Color MUTED = new Color(190, 190, 190);
    private static final Color LINE = new Color(55, 55, 55);
    private final StudentDashboardFrame dashboard;

    public SystemModulesFrame(StudentDashboardFrame dashboard) {
        this.dashboard = dashboard;
        setTitle("System Modules");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent event) { returnToDashboard(); }
        });

        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(28, 30, 22, 30));

        JPanel header = new JPanel();
        header.setBackground(BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("System Modules");
        title.setForeground(TEXT);
        title.setFont(new Font("Arial", Font.BOLD, 38));
        JLabel subtitle = new JLabel("Choose a module to manage system records.");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 23));
        header.add(title);
        header.add(Box.createVerticalStrut(3));
        header.add(subtitle);
        content.add(header, BorderLayout.NORTH);

        JPanel sections = new JPanel();
        sections.setBackground(BG);
        sections.setLayout(new BoxLayout(sections, BoxLayout.Y_AXIS));
        sections.add(section("S", "Student records", new Module[] {
                new Module("S", "Student Management", "Add, edit, remove", "Student Management", new Color(50, 145, 230)),
                new Module("?", "Search Students", "Find by name or ID", "Student Search", new Color(50, 145, 230))
        }));
        sections.add(Box.createVerticalStrut(18));
        sections.add(section("F", "Fee operations", new Module[] {
                new Module("F", "Fee Structures", "Course-wise fee setup", "Fee Structure Management", new Color(0, 205, 45)),
                new Module("$", "Fee Payments", "Record new payments", "Fee Payment", new Color(0, 205, 45)),
                new Module("!", "Pending Fees", "Dues and balances", "Pending Fees Management", new Color(0, 205, 45)),
                new Module("H", "Payment History", "Past transactions", "Payment History", new Color(0, 205, 45))
        }));
        sections.add(Box.createVerticalStrut(18));
        JPanel finance = section("$", "Finance", new Module[] {
                new Module("S", "Scholarships", "Approvals and amounts", "Scholarship Management", new Color(245, 166, 0)),
                new Module("E", "Expenses", "Track spending", "Expenses Management", new Color(245, 166, 0))
        });
        sections.add(finance);
        sections.add(Box.createVerticalStrut(10));
        Module report = new Module("R", "Financial Reports", "Summaries and exports", "Financial Reports", new Color(245, 166, 0));
        sections.add(card(report, true));
        sections.add(Box.createVerticalStrut(6));

        JScrollPane scroll = new JScrollPane(sections);
        scroll.setBorder(null);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        content.add(scroll, BorderLayout.CENTER);

        JButton back = new JButton("Back to Dashboard");
        back.setForeground(TEXT);
        back.setBackground(CARD);
        back.setOpaque(true);
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(LINE, 12),
                new EmptyBorder(10, 16, 10, 16)));
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(event -> returnToDashboard());
        JPanel footer = new JPanel();
        footer.setBackground(BG);
        footer.add(back);
        content.add(footer, BorderLayout.SOUTH);

        setContentPane(content);
        setMinimumSize(new Dimension(1000, 700));
        setSize(1300, 800);
        setLocationRelativeTo(dashboard);
    }

    private JPanel section(String symbol, String title, Module[] modules) {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setBackground(BG);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel heading = new JPanel(new BorderLayout(10, 0));
        heading.setBackground(BG);
        JLabel icon = new JLabel(symbol);
        icon.setForeground(MUTED);
        icon.setFont(new Font("Arial", Font.BOLD, 17));
        JLabel name = new JLabel(title);
        name.setForeground(TEXT);
        name.setFont(new Font("Arial", Font.BOLD, 21));
        heading.add(icon, BorderLayout.WEST);
        heading.add(name, BorderLayout.CENTER);
        JPanel rule = new JPanel();
        rule.setBackground(LINE);
        rule.setPreferredSize(new Dimension(10, 1));
        heading.add(rule, BorderLayout.SOUTH);
        section.add(heading, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 10));
        grid.setBackground(BG);
        for (Module module : modules) grid.add(card(module, false));
        section.add(grid, BorderLayout.CENTER);
        return section;
    }

    private JButton card(Module module, boolean fullWidth) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(14, 0));
        button.setBackground(CARD);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(LINE, 18), new EmptyBorder(17, 18, 17, 18)));
        button.setPreferredSize(new Dimension(520, 118));
        button.setMinimumSize(new Dimension(260, 110));
        if (fullWidth) {
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 118));
        }

        JLabel icon = new BadgeIcon(module.symbol, module.accent);
        icon.setForeground(module.accent);
        icon.setFont(new Font("Arial", Font.BOLD, 27));
        icon.setPreferredSize(new Dimension(68, 68));
        button.add(icon, BorderLayout.WEST);

        JPanel labels = new JPanel();
        labels.setBackground(CARD);
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(module.label);
        name.setForeground(TEXT);
        name.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel description = new JLabel(module.description);
        description.setForeground(MUTED);
        description.setFont(new Font("Arial", Font.PLAIN, 19));
        labels.add(name);
        labels.add(Box.createVerticalStrut(2));
        labels.add(description);
        button.add(labels, BorderLayout.CENTER);

        button.addActionListener(event -> openModule(module.moduleName));
        MouseAdapter click = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent event) { button.doClick(); }
        };
        icon.addMouseListener(click);
        labels.addMouseListener(click);
        name.addMouseListener(click);
        description.addMouseListener(click);
        return button;
    }

    private void openModule(String moduleName) {
        if ("Student Search".equals(moduleName)) {
            new StudentSearchFrame().setVisible(true);
        } else {
            new ManagementModulesFrame(moduleName).setVisible(true);
        }
    }

    private void returnToDashboard() {
        dispose();
        dashboard.refreshFinancialSummary();
        dashboard.setVisible(true);
    }

    private static class BadgeIcon extends JLabel {
        private final Color accent;

        BadgeIcon(String symbol, Color accent) {
            super(symbol, SwingConstants.CENTER);
            this.accent = accent;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(accent.getRed() / 8,
                    accent.getGreen() / 8, accent.getBlue() / 8));
            g.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 18, 18);
            g.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(color);
            g.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g.dispose();
        }

        @Override
        public Insets getBorderInsets(Component component) {
            return new Insets(1, 1, 1, 1);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    private static class Module {
        private final String symbol;
        private final String label;
        private final String description;
        private final String moduleName;
        private final Color accent;

        private Module(String symbol, String label, String description, String moduleName, Color accent) {
            this.symbol = symbol;
            this.label = label;
            this.description = description;
            this.moduleName = moduleName;
            this.accent = accent;
        }
    }
}
