package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import src.model.Task;
import model.Priority;
import controller.TaskManager;
import src.view.MainFrame;

public class TodoMatrixPanel extends JPanel {
    private JPanel doFirstPanel;
    private JPanel schedulePanel;
    private JPanel delegatePanel;
    private JPanel eliminatePanel;
    private TaskManager taskManager;
    private boolean isDarkMode = false;
    private MainFrame parentFrame;
    
    public TodoMatrixPanel(MainFrame parent, boolean darkMode) {
        this.parentFrame = parent;
        this.isDarkMode = darkMode;
        this.taskManager = new TaskManager();
        
        initializeComponents();
        setupLayout();
        applyTheme();
    }
    
    private void initializeComponents() {
        doFirstPanel = createQuadrantPanel("DO FIRST", Priority.DO_FIRST);
        schedulePanel = createQuadrantPanel("SCHEDULE", Priority.SCHEDULE);
        delegatePanel = createQuadrantPanel("DELEGATE", Priority.DELEGATE);
        eliminatePanel = createQuadrantPanel("ELIMINATE", Priority.ELIMINATE);
    }
    
    private JPanel createQuadrantPanel(String title, Priority priority) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.decode(priority.getColor()), 2),
            title,
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("San Francisco", Font.BOLD, 14),
            Color.decode(priority.getColor())
        ));
        
        return panel;
    }
    
    private void setupLayout() {
        setLayout(new GridLayout(2, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(new JScrollPane(doFirstPanel));
        add(new JScrollPane(schedulePanel));
        add(new JScrollPane(delegatePanel));
        add(new JScrollPane(eliminatePanel));
    }
    
    public void refreshTasks() {
        try {
            List<Task> tasks = taskManager.getAllTasks();
            clearAllPanels();
            
            for (Task task : tasks) {
                JPanel taskCard = createTaskCard(task);
                switch (task.getPriority()) {
                    case DO_FIRST:
                        doFirstPanel.add(taskCard);
                        break;
                    case SCHEDULE:
                        schedulePanel.add(taskCard);
                        break;
                    case DELEGATE:
                        delegatePanel.add(taskCard);
                        break;
                    case ELIMINATE:
                        eliminatePanel.add(taskCard);
                        break;
                }
            }
            
            revalidate();
            repaint();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearAllPanels() {
        doFirstPanel.removeAll();
        schedulePanel.removeAll();
        delegatePanel.removeAll();
        eliminatePanel.removeAll();
    }
    
    private JPanel createTaskCard(Task task) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode(task.getPriority().getColor()), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        // Title
        JLabel titleLabel = new JLabel(task.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Deadline
        JLabel deadlineLabel = new JLabel("📅 " + task.getFormattedDeadline());
        deadlineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        // Days until deadline
        long daysUntil = task.getDaysUntilDeadline();
        String urgencyText = daysUntil < 0 ? "TERLAMBAT!" : 
                            daysUntil == 0 ? "HARI INI!" :
                            daysUntil == 1 ? "BESOK" :
                            daysUntil + " hari lagi";
        JLabel urgencyLabel = new JLabel(urgencyText);
        urgencyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 9));
        urgencyLabel.setForeground(daysUntil <= 1 ? Color.RED : Color.GRAY);
        
        // Motivation (truncated)
        String motivation = task.getMotivation();
        if (motivation.length() > 100) {
            motivation = motivation.substring(0, 100) + "...";
        }
        JTextArea motivationArea = new JTextArea(motivation);
        motivationArea.setEditable(false);
        motivationArea.setWrapStyleWord(true);
        motivationArea.setLineWrap(true);
        motivationArea.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        motivationArea.setOpaque(false);
        motivationArea.setRows(2);
        
        // Top panel (title and urgency)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(urgencyLabel, BorderLayout.EAST);
        
        // Bottom panel (deadline)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(deadlineLabel, BorderLayout.WEST);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(motivationArea, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(Color.decode(task.getPriority().getColor()).brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(isDarkMode ? new Color(60, 60, 60) : Color.WHITE);
            }
        });
        
        // Right-click context menu
        setupContextMenu(card, task);
        
        card.setPreferredSize(new Dimension(280, 120));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        return card;
    }
    
    private void setupContextMenu(JPanel card, Task task) {
        JPopupMenu contextMenu = new JPopupMenu();
        
        JMenuItem deleteItem = new JMenuItem("Hapus");
        JMenuItem completeItem = new JMenuItem("Tandai Selesai");
        
        deleteItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus tugas '" + task.getTitle() + "'?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
            );
            
            if (result == JOptionPane.YES_OPTION) {
                try {
                    taskManager.deleteTask(task.getTitle());
                    refreshTasks();
                    JOptionPane.showMessageDialog(this, "Tugas berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus tugas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        completeItem.addActionListener(e -> {
            try {
                taskManager.markTaskCompleted(task.getTitle());
                refreshTasks();
                JOptionPane.showMessageDialog(this, "Tugas berhasil diselesaikan! 🎉", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menandai tugas selesai: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        contextMenu.add(deleteItem);
        contextMenu.add(completeItem);
        
        card.setComponentPopupMenu(contextMenu);
    }
    
    public void applyTheme() {
        Color bgColor = isDarkMode ? new Color(45, 45, 45) : new Color(250, 250, 250);
        Color panelBg = isDarkMode ? new Color(60, 60, 60) : Color.WHITE;
        
        setBackground(bgColor);
        
        applyThemeToPanel(doFirstPanel, panelBg);
        applyThemeToPanel(schedulePanel, panelBg);
        applyThemeToPanel(delegatePanel, panelBg);
        applyThemeToPanel(eliminatePanel, panelBg);
    }
    
    private void applyThemeToPanel(JPanel panel, Color bgColor) {
        panel.setBackground(bgColor);
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel taskCard = (JPanel) comp;
                taskCard.setBackground(bgColor);
                applyThemeToTaskCard(taskCard);
            }
        }
    }
    
    private void applyThemeToTaskCard(JPanel taskCard) {
        Color textColor = isDarkMode ? Color.WHITE : Color.BLACK;
        
        for (Component comp : taskCard.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel subPanel = (JPanel) comp;
                subPanel.setBackground(taskCard.getBackground());
                for (Component subComp : subPanel.getComponents()) {
                    if (subComp instanceof JLabel) {
                        subComp.setForeground(textColor);
                    }
                }
            } else if (comp instanceof JLabel) {
                comp.setForeground(textColor);
            } else if (comp instanceof JTextArea) {
                JTextArea textArea = (JTextArea) comp;
                textArea.setForeground(textColor);
                textArea.setBackground(taskCard.getBackground());
            }
        }
    }
    
    public void setDarkMode(boolean darkMode) {
        this.isDarkMode = darkMode;
        applyTheme();
    }
}
