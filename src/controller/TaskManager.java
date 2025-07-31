package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import src.database.DatabaseUtil;
import src.model.Task;
import model.Priority;
import model.WhyReason;

public class TaskManager {
    private DatabaseUtil database;
    
    public TaskManager() {
        this.database = DatabaseUtil.getInstance();
    }
    
    public Priority calculatePriority(Task task, WhyReason whyReason) {
        int score = 0;
        
        // Urgency scoring based on deadline
        long daysUntilDeadline = task.getDaysUntilDeadline();
        if (daysUntilDeadline <= 2) {
            score += 50; // Very urgent
        } else if (daysUntilDeadline <= 7) {
            score += 30; // Urgent
        } else if (daysUntilDeadline <= 30) {
            score += 15; // Somewhat urgent
        }
        
        // Importance scoring based on motivation depth
        int whyLayerCount = whyReason.getWhyLayers().size();
        if (whyLayerCount >= 3) {
            score += 40; // Deep motivation = important
        } else if (whyLayerCount >= 2) {
            score += 25;
        } else if (whyLayerCount >= 1) {
            score += 10;
        }
        
        // Additional scoring based on motivation content
        String motivation = whyReason.getFinalMotivation().toLowerCase();
        if (motivation.contains("career") || motivation.contains("karir") ||
            motivation.contains("health") || motivation.contains("kesehatan") ||
            motivation.contains("family") || motivation.contains("keluarga") ||
            motivation.contains("goal") || motivation.contains("tujuan")) {
            score += 20;
        }
        
        // Determine priority based on score
        if (score >= 70) {
            return Priority.DO_FIRST;
        } else if (score >= 40) {
            return Priority.SCHEDULE;
        } else if (score >= 20) {
            return Priority.DELEGATE;
        } else {
            return Priority.ELIMINATE;
        }
    }
    
    public void saveTask(Task task) throws SQLException {
        database.saveTask(task);
    }
    
    public List<Task> getAllTasks() throws SQLException {
        return database.getAllTasks();
    }

    public void deleteTask(String title) throws SQLException {
        database.deleteTask(title);
    }

    public void markTaskCompleted(String title) throws SQLException {
        database.markTaskCompleted(title);
    }

    public List<Task> searchTasks(String query) throws SQLException {
        return database.searchTasks(query);
    }
    
    public boolean isValidTask(String title, LocalDate deadline) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        if (deadline == null) {
            return false;
        }
        return true;
    }
}
