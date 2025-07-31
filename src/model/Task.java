package src.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private String title;
    private LocalDate deadline;
    private String motivation;
    private model.Priority priority;
    private boolean completed;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    
    public Task(String title, LocalDate deadline, String motivation, model.Priority priority) {
        this.title = title;
        this.deadline = deadline;
        this.motivation = motivation;
        this.priority = priority;
        this.completed = false;
    }
    
    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    
    public String getMotivation() { return motivation; }
    public void setMotivation(String motivation) { this.motivation = motivation; }
    
    public model.Priority getPriority() { return priority; }
    public void setPriority(model.Priority priority) { this.priority = priority; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public String getFormattedDeadline() {
        return deadline.format(FORMATTER);
    }
    
    public long getDaysUntilDeadline() {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }
}
