package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import model.WhyReason;

public class WhyReasonDialog extends JDialog {
    private WhyReason whyReason;
    private JTextArea currentQuestionArea;
    private JTextField answerField;
    private JButton nextButton;
    private JButton finishButton;
    private JTextArea motivationSummary;
    private List<String> questions;
    private int currentQuestionIndex = 0;
    private boolean isDarkMode = false;
    
    public WhyReasonDialog(Frame parent, String taskTitle, boolean darkMode) {
        super(parent, "Gali Alasan: " + taskTitle, true);
        this.isDarkMode = darkMode;
        this.whyReason = new WhyReason();
        initializeQuestions(taskTitle);
        initializeComponents();
        setupLayout();
        applyTheme();
        setLocationRelativeTo(parent);
    }
    
    private void initializeQuestions(String taskTitle) {
        questions = new ArrayList<>();
        questions.add("Kenapa kamu ingin menyelesaikan: '" + taskTitle + "'?");
        questions.add("Kenapa hal itu penting bagimu?");
        questions.add("Kenapa kamu merasa perlu melakukannya sekarang?");
        questions.add("Apa yang akan terjadi jika kamu tidak melakukannya?");
        questions.add("Bagaimana perasaanmu setelah menyelesaikannya nanti?");
    }
    
    private void initializeComponents() {
        currentQuestionArea = new JTextArea(3, 40);
        currentQuestionArea.setEditable(false);
        currentQuestionArea.setWrapStyleWord(true);
        currentQuestionArea.setLineWrap(true);
        currentQuestionArea.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        answerField = new JTextField(30);
        answerField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        nextButton = new JButton("Jawab & Lanjut");
        finishButton = new JButton("Selesai");
        finishButton.setEnabled(false);
        
        motivationSummary = new JTextArea(5, 40);
        motivationSummary.setEditable(false);
        motivationSummary.setWrapStyleWord(true);
        motivationSummary.setLineWrap(true);
        motivationSummary.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        
        setupEventListeners();
        displayCurrentQuestion();
    }
    
    private void setupEventListeners() {
        nextButton.addActionListener(e -> {
            String answer = answerField.getText().trim();
            if (!answer.isEmpty()) {
                whyReason.addWhy(answer);
                answerField.setText("");
                currentQuestionIndex++;
                
                if (currentQuestionIndex < questions.size()) {
                    displayCurrentQuestion();
                } else {
                    nextButton.setEnabled(false);
                    finishButton.setEnabled(true);
                    currentQuestionArea.setText("Bagus! Kamu telah menggali alasan yang cukup mendalam.");
                }
                updateMotivationSummary();
            }
        });
        
        finishButton.addActionListener(e -> {
            whyReason.setFinalMotivation(whyReason.getFinalMotivation());
            dispose();
        });
        
        answerField.addActionListener(e -> nextButton.doClick());
    }
    
    private void displayCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            currentQuestionArea.setText(questions.get(currentQuestionIndex));
        }
    }
    
    private void updateMotivationSummary() {
        StringBuilder summary = new StringBuilder("Alasan kamu:\n");
        for (int i = 0; i < whyReason.getWhyLayers().size(); i++) {
            summary.append((i + 1)).append(". ").append(whyReason.getWhyLayers().get(i)).append("\n");
        }
        motivationSummary.setText(summary.toString());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Question panel
        JPanel questionPanel = new JPanel(new BorderLayout(5, 5));
        questionPanel.add(new JLabel("Pertanyaan:"), BorderLayout.NORTH);
        questionPanel.add(new JScrollPane(currentQuestionArea), BorderLayout.CENTER);
        
        // Answer panel
        JPanel answerPanel = new JPanel(new BorderLayout(5, 5));
        answerPanel.add(new JLabel("Jawaban:"), BorderLayout.NORTH);
        answerPanel.add(answerField, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(nextButton);
        buttonPanel.add(finishButton);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout(5, 5));
        summaryPanel.add(new JLabel("Ringkasan Motivasi:"), BorderLayout.NORTH);
        summaryPanel.add(new JScrollPane(motivationSummary), BorderLayout.CENTER);
        
        mainPanel.add(questionPanel, BorderLayout.NORTH);
        mainPanel.add(answerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);
        
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void applyTheme() {
        Color bgColor = isDarkMode ? new Color(45, 45, 45) : Color.WHITE;
        Color textColor = isDarkMode ? Color.WHITE : Color.BLACK;
        Color fieldBg = isDarkMode ? new Color(60, 60, 60) : Color.WHITE;
        
        getContentPane().setBackground(bgColor);
        currentQuestionArea.setBackground(fieldBg);
        currentQuestionArea.setForeground(textColor);
        answerField.setBackground(fieldBg);
        answerField.setForeground(textColor);
        motivationSummary.setBackground(fieldBg);
        motivationSummary.setForeground(textColor);
        
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
    
    public WhyReason getWhyReason() {
        return whyReason;
    }
}
