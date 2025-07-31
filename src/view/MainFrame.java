package src.view;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;

import controller.TaskManager;
import src.model.Task;

public class MainFrame extends JFrame {
    private view.TodoMatrixPanel matrixPanel;
    private JTextField searchField;
    private JButton addTaskButton;
    private JButton searchButton;
    private JButton themeToggleButton;
    private TaskManager taskManager;
    private boolean isDarkMode = false;

    public MainFrame() {
        this.taskManager = new TaskManager();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        applyTheme();

        refreshMatrix();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Philosofit: Philosophy Found in To-Do List");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        matrixPanel = new view.TodoMatrixPanel(this, isDarkMode);

        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 12);

        searchField = new JTextField(20);
        searchField.setFont(emojiFont);
        searchField.setToolTipText("Cari tugas...");

        addTaskButton = new JButton("➕ Tambah Tugas");
        addTaskButton.setFont(new Font("Nunito Sans", Font.BOLD, 12));
        addTaskButton.setBackground(new Color(76, 175, 80));
        addTaskButton.setForeground(Color.BLACK);
        addTaskButton.setFocusPainted(false);

        searchButton = new JButton("Cari");
        searchButton.setFont(emojiFont);

        themeToggleButton = new JButton("🌙 Mode Gelap");
        themeToggleButton.setFont(emojiFont);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // HEADER
        JLabel headerLabel = new JLabel("EISENHOWER MATRIX MIX WITH FEYNMAN METHOD >.<", JLabel.CENTER);
        headerLabel.setFont(new Font("Lato", Font.BOLD, 20));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // MATRIX PANEL
        add(matrixPanel, BorderLayout.CENTER);

        // PANEL KANAN
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(new Color(250, 250, 250));

        // Tombol Tambah di atas
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRightPanel.setOpaque(false);
        topRightPanel.add(addTaskButton);

        // Cari & Mode Gelap di bawah
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomRightPanel.setOpaque(false);
        bottomRightPanel.add(new JLabel("🔍 Cari:"));
        bottomRightPanel.add(searchField);
        bottomRightPanel.add(searchButton);
        bottomRightPanel.add(themeToggleButton);

        rightPanel.add(topRightPanel, BorderLayout.NORTH);
        rightPanel.add(bottomRightPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        // STATUS
        JLabel statusLabel = new JLabel("📌 Gunakan klik kanan pada tugas untuk opsi lebih lanjut");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        addTaskButton.addActionListener(e -> {
            try {
                AddTaskUI addTaskDialog = new AddTaskUI(this, isDarkMode);
                addTaskDialog.setVisible(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error membuka dialog tambah tugas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());

        themeToggleButton.addActionListener(e -> toggleTheme());
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshMatrix();
            return;
        }

        try {
            List<Task> searchResults = taskManager.searchTasks(query);
            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tidak ada tugas yang ditemukan untuk: " + query, "Hasil Pencarian", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder results = new StringBuilder("Ditemukan " + searchResults.size() + " tugas:\n\n");
                for (Task task : searchResults) {
                    results.append("• ").append(task.getTitle())
                            .append(" (").append(task.getFormattedDeadline()).append(")")
                            .append(" - ").append(task.getPriority().getDisplayName())
                            .append("\n");
                }
                JOptionPane.showMessageDialog(this, results.toString(), "Hasil Pencarian", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        themeToggleButton.setText(isDarkMode ? "☀️ Mode Terang" : "🌙 Mode Gelap");
        matrixPanel.setDarkMode(isDarkMode);
        applyTheme();
    }

    private void applyTheme() {
        Color bgColor = isDarkMode ? new Color(45, 45, 45) : Color.WHITE;
        Color textColor = isDarkMode ? Color.WHITE : Color.BLACK;
        Color panelBg = isDarkMode ? new Color(60, 60, 60) : new Color(245, 245, 245);

        getContentPane().setBackground(bgColor);

        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                comp.setForeground(textColor);
            } else if (comp instanceof JPanel) {
                applyThemeToPanel((JPanel) comp, panelBg, textColor);
            }
        }

        searchField.setBackground(isDarkMode ? new Color(60, 60, 60) : Color.WHITE);
        searchField.setForeground(textColor);

        repaint();
    }

    private void applyThemeToPanel(JPanel panel, Color bgColor, Color textColor) {
        panel.setBackground(bgColor);
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setForeground(textColor);
            } else if (comp instanceof JPanel) {
                applyThemeToPanel((JPanel) comp, bgColor, textColor);
            }
        }
    }

    public void refreshMatrix() {
        matrixPanel.refreshTasks();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
