package src.view;

import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.sql.SQLException;
import java.util.Properties;
import java.util.Date;
import java.time.ZoneId;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.TaskManager;
import model.Priority;
import src.model.Task;
import model.WhyReason;

public class AddTaskUI extends JDialog {
    private JTextField titleField; 
    private JDatePickerImpl datePicker;
    private JTextArea motivationArea;
    private JLabel priorityLabel;
    private JButton reasonButton;
    private JButton calculateButton;
    private JButton saveButton;
    private JButton cancelButton;
    
    private TaskManager taskManager;
    private WhyReason whyReason;
    private Priority calculatedPriority;
    private boolean isDarkMode = false;
    private src.view.MainFrame parentFrame;
    
    public AddTaskUI(src.view.MainFrame parent, boolean darkMode) throws SQLException {
        super(parent, "Tambah Tugas Baru", true);
        this.parentFrame = parent;
        this.isDarkMode = darkMode;
        this.taskManager = new TaskManager();
        this.whyReason = new WhyReason();
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        applyTheme();
        setLocationRelativeTo(parent);
    }
    
    private void initializeComponents() {
        titleField = new JTextField(30);
        titleField.setFont(new Font("Inter", Font.PLAIN, 12));

        // DatePicker inisialisasi
        UtilDateModel model = new UtilDateModel();
        LocalDate defaultDate = LocalDate.now().plusDays(7);
        model.setDate(defaultDate.getYear(), defaultDate.getMonthValue() - 1, defaultDate.getDayOfMonth());
        model.setSelected(true);

        Properties p = new Properties();
        p.put("text.today", "Hari Ini");
        p.put("text.month", "Bulan");
        p.put("text.year", "Tahun");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        motivationArea = new JTextArea(4, 30);
        motivationArea.setEditable(false);
        motivationArea.setWrapStyleWord(true);
        motivationArea.setLineWrap(true);
        motivationArea.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        motivationArea.setText("Klik 'Gali Alasan' untuk menggali motivasi...");

        priorityLabel = new JLabel("Prioritas: (Belum dihitung)");
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        reasonButton = new JButton("Gali Alasan ➔");
        calculateButton = new JButton("Hitung Prioritas");
        calculateButton.setEnabled(false);
        saveButton = new JButton("Simpan");
        saveButton.setEnabled(false);
        cancelButton = new JButton("Batal");

        reasonButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        calculateButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title row
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(new JLabel("Judul Tugas:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(titleField, gbc);
        
        // Deadline row
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Deadline:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(datePicker, gbc);
        
        // Reason button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        mainPanel.add(reasonButton, gbc);
        
        // Motivation area
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(new JScrollPane(motivationArea), gbc);
        
        // Priority label
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        mainPanel.add(priorityLabel, gbc);
        
        // Calculate button
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 5, 5, 5);
        mainPanel.add(calculateButton, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setSize(450, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void setupEventListeners() {
        reasonButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi judul tugas terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            view.WhyReasonDialog dialog = new view.WhyReasonDialog(parentFrame, title, isDarkMode);
            dialog.setVisible(true);
            
            whyReason = dialog.getWhyReason();
            motivationArea.setText(whyReason.getFinalMotivation());
            calculateButton.setEnabled(true);
        });
        
        calculateButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                Date selectedDate = (Date) datePicker.getModel().getValue();
                if (selectedDate == null) {
                    JOptionPane.showMessageDialog(this, "Pilih tanggal deadline!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                LocalDate deadline = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (!taskManager.isValidTask(title, deadline)) {
                    JOptionPane.showMessageDialog(this, "Judul dan deadline harus diisi dengan benar!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Task tempTask = new Task(title, deadline, whyReason.getFinalMotivation(), Priority.ELIMINATE);
                calculatedPriority = taskManager.calculatePriority(tempTask, whyReason);
                
                priorityLabel.setText("Prioritas: " + calculatedPriority.getDisplayName());
                priorityLabel.setForeground(Color.decode(calculatedPriority.getColor()));
                saveButton.setEnabled(true);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error menghitung prioritas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        saveButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                Date selectedDate = (Date) datePicker.getModel().getValue();
                if (selectedDate == null) {
                    JOptionPane.showMessageDialog(this, "Pilih tanggal deadline!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                LocalDate deadline = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                
                Task task = new Task(title, deadline, whyReason.getFinalMotivation(), calculatedPriority);
                taskManager.saveTask(task);
                
                JOptionPane.showMessageDialog(this, "Tugas berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.refreshMatrix();
                dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan tugas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void applyTheme() {
        Color bgColor = isDarkMode ? new Color(45, 45, 45) : Color.WHITE;
        Color textColor = isDarkMode ? Color.WHITE : Color.BLACK;
        Color fieldBg = isDarkMode ? new Color(60, 60, 60) : Color.WHITE;
        
        getContentPane().setBackground(bgColor);
        titleField.setBackground(fieldBg);
        titleField.setForeground(textColor);
        motivationArea.setBackground(fieldBg);
        motivationArea.setForeground(textColor);
        
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                setComponentTheme((JPanel) comp, bgColor, textColor);
            }
        }
    }
    
    private void setComponentTheme(JPanel panel, Color bgColor, Color textColor) {
        panel.setBackground(bgColor);
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setForeground(textColor);
            } else if (comp instanceof JPanel) {
                setComponentTheme((JPanel) comp, bgColor, textColor);
            }
        }
    }
}
