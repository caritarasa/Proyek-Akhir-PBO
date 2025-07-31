package src.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import model.Priority;
import src.model.Task;

public class DatabaseUtil {
    private static DatabaseUtil instance;
    private Connection connection;
    
    // Konfigurasi database MySQL - SESUAIKAN DENGAN PENGATURAN ANDA
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "philosofit_db";
    private static final String DB_USERNAME = "root"; // Ganti dengan username MySQL Anda
    private static final String DB_PASSWORD = ""; // Ganti dengan password MySQL Anda
    
    private DatabaseUtil() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DB] MySQL JDBC driver loaded.");
            
            // Build connection URL
            String dbUrl = String.format("jdbc:mysql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME);
            
            // Connection properties
            Properties props = new Properties();
            props.setProperty("user", DB_USERNAME);
            props.setProperty("password", DB_PASSWORD);
            props.setProperty("useSSL", "false");
            props.setProperty("allowPublicKeyRetrieval", "true");
            props.setProperty("serverTimezone", "Asia/Jakarta");
            props.setProperty("useUnicode", "true");
            props.setProperty("characterEncoding", "UTF-8");
            
            System.out.println("[DB] Connecting to: " + dbUrl);
            
            // Connect to database
            connection = DriverManager.getConnection(dbUrl, props);
            System.out.println("[DB] MySQL connection established!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] ERROR: MySQL JDBC driver not found!");
            System.err.println("Please add mysql-connector-java-8.0.33.jar to lib folder");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[DB] ERROR: Failed to connect to MySQL database!");
            System.err.println("Please check your database configuration in DatabaseUtil.java:");
            System.err.println("- Host: " + DB_HOST);
            System.err.println("- Port: " + DB_PORT);
            System.err.println("- Database: " + DB_NAME);
            System.err.println("- Username: " + DB_USERNAME);
            e.printStackTrace();
        }
    }
    
    public static DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }
    
    public void saveTask(Task task) throws SQLException {
        String sql = """
            INSERT INTO tasks (title, deadline, motivation, priority, completed)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            deadline = VALUES(deadline),
            motivation = VALUES(motivation),
            priority = VALUES(priority),
            completed = VALUES(completed),
            updated_at = CURRENT_TIMESTAMP
            """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setDate(2, Date.valueOf(task.getDeadline()));
            pstmt.setString(3, task.getMotivation());
            pstmt.setString(4, task.getPriority().name());
            pstmt.setBoolean(5, task.isCompleted());
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("[DB] Saved task: " + task.getTitle());
        }
    }
    
    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = """
            SELECT title, deadline, motivation, priority, completed 
            FROM tasks 
            WHERE completed = FALSE 
            ORDER BY 
                CASE priority
                    WHEN 'DO_FIRST' THEN 1
                    WHEN 'SCHEDULE' THEN 2
                    WHEN 'DELEGATE' THEN 3
                    WHEN 'ELIMINATE' THEN 4
                END,
                deadline ASC
            """;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Task task = new Task(
                    rs.getString("title"),
                    rs.getDate("deadline").toLocalDate(),
                    rs.getString("motivation"),
                    Priority.valueOf(rs.getString("priority"))
                );
                task.setCompleted(rs.getBoolean("completed"));
                tasks.add(task);
            }
        }
        
        return tasks;
    }
    
    public void deleteTask(String title) throws SQLException {
        String sql = "DELETE FROM tasks WHERE title = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
            System.out.println("[DB] Deleted task: " + title);
        }
    }
    
    public void markTaskCompleted(String title) throws SQLException {
        String sql = "UPDATE tasks SET completed = TRUE, updated_at = CURRENT_TIMESTAMP WHERE title = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
            System.out.println("[DB] Marked completed: " + title);
        }
    }
    
    public List<Task> searchTasks(String query) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = """
            SELECT title, deadline, motivation, priority, completed 
            FROM tasks 
            WHERE (title LIKE ? OR motivation LIKE ?) 
            AND completed = FALSE
            ORDER BY deadline ASC
            """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Task task = new Task(
                    rs.getString("title"),
                    rs.getDate("deadline").toLocalDate(),
                    rs.getString("motivation"),
                    Priority.valueOf(rs.getString("priority"))
                );
                task.setCompleted(rs.getBoolean("completed"));
                tasks.add(task);
            }
        }
        
        return tasks;
    }
}
