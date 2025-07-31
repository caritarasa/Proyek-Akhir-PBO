package model;

public enum Priority {
    DO_FIRST("Do First", "#FF6B6B"),      // Red - Urgent & Important
    SCHEDULE("Schedule", "#4ECDC4"),       // Teal - Important not Urgent
    DELEGATE("Delegate", "#45B7D1"),       // Blue - Urgent not Important
    ELIMINATE("Eliminate", "#96CEB4");     // Green - Neither Urgent nor Important
    
    private final String displayName;
    private final String color;
    
    Priority(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColor() {
        return color;
    }
}
